package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.save.Save;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import it.polimi.ingsw.server.servercontroller.controllerstates.*;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {

    private static GameController instance;
    private List<Player> playerList = new ArrayList<>(4);
    private int maxPlayerNumber = 0;
    private boolean lastTurn = false;
    private boolean onlyOneCommonCard = false;
    private Player activePlayer = null; //acts as a kind of turn
    private BoardFactory board;
    private EndGameToken endGameToken= EndGameToken.getEndGameToken(); //todo add in UML (added because it's singleton, it needs to be reset after end of the game)
    private GameState state;
    private List<CommonTargetCard> commonTargetCardsList;
    private Map<String, TCPMessageController> nickToTCPMessageControllerMapping = new ConcurrentHashMap<>(4);
    private Map<String, Integer> nickToUnansweredCheck = new HashMap<>(4);
    private final Server server;
    private boolean gameOver;
    private static final int THREAD_SLEEP_MILLISECONDS = 1000;
    private static final int TIMER_DURATION_MILLISECONDS = 30000;
    private Timer timer;
    private boolean timerIsRunning = false;
    private boolean lastConnectedUserMadeHisMove = false;

    private GameController(Server s) {
        this.state = new ServerInitState();
        this.server = s;
        this.gameOver=false;
        startCheckThread();
    }

    public static GameController getGameController(Server s) {
        if (instance == null) {
            instance = new GameController(s);
        }
        return instance;
    }

    public void putNickToSocketMapping(String nickname, TCPMessageController controller) {
        nickToTCPMessageControllerMapping.put(nickname, controller);
    }

    public Map<String, TCPMessageController> getNickToTCPMessageControllerMapping() {
        return nickToTCPMessageControllerMapping;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public boolean checkMove(Body body) {  //ok serve
        if (!body.getPlayerNickname().equals(activePlayer.getNickname())) { //forse questo non va bene?
            return false;
        }
        if (!board.checkMove(body.getPositionsPicked())) {
            return false;
        }
        if(!activePlayer.checkColumnChosen(body.getPositionsPicked().size(),body.getColumn())){
            return false;
        }
        return true;
    }

    public void executeMove(Body body) throws InvalidMoveException {
        if (checkMove(body)) {
            List<Item> items = getListItems(body);
            try { //inutile qui simo, ti convincerò
                activePlayer.getShelf().insertItems(body.getColumn(), items);
            } catch (Exception NotEnoughSpaceInColumnException) {
                System.err.println("Not enough space in the column provided!");
            }
            System.err.println("POST INSERT");
            if (checkBoardNeedForRefill()) {
                board.refillBoard();
            }
            System.err.println("POST REFILL");
            if (!lastTurn && checkLastTurn()) {
                activePlayer.setEndGameToken(EndGameToken.getEndGameToken());
                lastTurn = true;
            }
            System.err.println("POST CHECK LAST TURN");
            activePlayer.updateScore();

            System.err.println("POST UPDATE SCORE");
            //Setting the next active player
            changeActivePlayer();

        }
        else throw new InvalidMoveException();
    }

    public void changeActivePlayer() {
        int currentPlayerIndex=playerList.indexOf(activePlayer);
        int nextIndex=nextIndexCalc(playerList.indexOf(activePlayer));
        while(nextIndex!=-1 && !playerList.get(nextIndex).isConnected())
            nextIndex=nextIndexCalc(nextIndex);
        if(nextIndex!=-1) {
            if(nextIndex==currentPlayerIndex)
                lastConnectedUserMadeHisMove = true;
            activePlayer = playerList.get(nextIndex);
        }
        else {
            activePlayer = null;
            gameOver=true;
        }
    }

    public NewView getNewView(){
        NewView newView = new NewView();
        if (gameOver) {
            newView.setGameOver(true);
        }
        else {
            newView.setActivePlayer(getActivePlayer().getNickname());
            newView.setGameOver(false);
        }
        newView.setYouAreTheLastUserAndYouAlreadyMadeYourMove(lastConnectedUserMadeHisMove);
        newView.setBoardItems(board.getBoardMatrix());
        newView.setBoardBitMask(board.getBitMask());
        newView.setEndGameToken(EndGameToken.getEndGameToken());
        for(Player p : playerList) {
            newView.getPlayerList().add(p.getNickname());
            newView.getNicknameToPointsMap().put(p.getNickname(), p.getPlayerScore());
            newView.getNicknameToShelfMap().put(p.getNickname(), p.getShelf().getShelfMatrix());
            newView.getPlayersToTokens().put(p.getNickname(), p.getScoringTokenList());
        }
        for(CommonTargetCard c : commonTargetCardsList) {
            newView.getCommonsToTokens().put(c.getName(), c.getScoringTokensList());
        }
        saveGameState();
        return newView;
    }

    public void saveGameState(){
        Save save=new Save();
        if(activePlayer!=null)
            save.setActivePlayerNickname(activePlayer.getNickname());
        Map<String, Item[][]> nicknameToShelfMap = new LinkedHashMap<>(4);
        Map<String, Integer> nicknameToPointsMap = new LinkedHashMap<>(4);
        Map<String, List<ScoringToken>> nicknameToScoringTokensMap = new LinkedHashMap<>(4);
        Map<String, PersonalTargetCard> nicknameToPersonalTargetCard=new LinkedHashMap<>(4);
        for(Player player:playerList){
            nicknameToShelfMap.put(player.getNickname(), player.getShelf().getShelfMatrix());
            nicknameToPointsMap.put(player.getNickname(),player.getPlayerScore());
            nicknameToScoringTokensMap.put(player.getNickname(), player.getScoringTokenList());
            nicknameToPersonalTargetCard.put(player.getNickname(), player.getPersonalTargetCard());
            if(player.hasEndGameToken())
                save.setEndGameTokenAssignedToWhom(player.getNickname());
        }
        save.setNicknameToShelfMap(nicknameToShelfMap);
        save.setNicknameToPointsMap(nicknameToPointsMap);
        save.setNicknameToScoringTokensMap(nicknameToScoringTokensMap);
        save.setNicknameToPersonalTargetCard(nicknameToPersonalTargetCard);
        save.setLastTurn(lastTurn);
        save.setGameOver(gameOver);
        for(CommonTargetCard commonTargetCard:commonTargetCardsList){
            save.getCommonTargetCardMap().put(commonTargetCard.getName(),commonTargetCard.getScoringTokensList());
        }
        save.setBoardItems(board.getBoardMatrix());
        save.setBoardBitMask(board.getBitMask());
        save.setMaxPlayerPlayer(maxPlayerNumber);
        Gson gson= new Gson();
        try (FileWriter writer = new FileWriter("src/main/java/it/polimi/ingsw/save/OldGame.json")) {
            gson.toJson(save, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int nextIndexCalc(int currIndex){
        if(playerList.size()-1==currIndex) {
            if(lastTurn)
                return -1;
            return 0;
        }
        else
            return currIndex+1;
    }

    public List<Item> getListItems(Body body) {
        List<Item> items = new ArrayList<>();
        for (int[] picks : body.getPositionsPicked()) {
            items.add(new Item(board.getBoardMatrixElement(picks[0], picks[1]).getColor()));
            board.setBoardMatrixElement(null,picks[0], picks[1]);
        }
        return items;

    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public boolean checkBoardNeedForRefill() {

        for (int i = 0; i < board.getBoardNumberOfRows(); i++) {
            for (int j = 0; j < board.getBoardNumberOfColumns(); j++) {
                if (board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i, j) != null) {
                    if(!board.itemHasAllFreeSide(i, j))
                        return false; //There is at least one non-null element that has at least one item on one of its side
                }
            }
        }
        return true; //Needs to be refilled

    }

    public void changeState(GameState state) {
        if(state != this.state) {
            this.state = state;
        }
    }

    public int getAvailableSlot() {
        return state.getAvailableSlot(maxPlayerNumber, playerList);
    }

    public void addPlayer(Player player) {
        state.addPlayer(player, playerList);
    }

    public void setupGame(boolean onlyOneCommonCard) {
        this.commonTargetCardsList = state.setupCommonList(onlyOneCommonCard, maxPlayerNumber);
        this.board = state.setupBoard(maxPlayerNumber);
        state.boardNeedsRefill(this.board);
        state.setupPlayers(playerList, commonTargetCardsList, board, this);
    }

    public boolean checkLastTurn() {
        for (Player p : playerList) {
            if (p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }

    public void prepareForNewGame() {
        playerList = new ArrayList<>();
        lastTurn = false;
        activePlayer = null;
        maxPlayerNumber=0;
        changeState(new ServerInitState());
        commonTargetCardsList = new ArrayList<>();
        nickToTCPMessageControllerMapping = new ConcurrentHashMap<>(4);
        nickToUnansweredCheck = new HashMap<>(4);
        gameOver=false;
        lastConnectedUserMadeHisMove =false;
        endGameToken.resetEndGameToken();
        server.prepareServerForNewGame();
    }

    public int checkNicknameAvailability(String nickname) {
        return (state.checkNicknameAvailability(nickname, playerList));
    }

    public boolean isGameReady() {
        return state.isGameReady(playerList, maxPlayerNumber);
    }

    public void changePlayerConnectionStatus(String nickname) {
        for (Player player : playerList) {
            if (player.getNickname().equals(nickname)) {
                player.setConnected(!player.isConnected());
                break;
            }
        }
    }

    public boolean createLobby(int maxPlayerNumber, boolean onlyOneCommonCard) {
        if (maxPlayerNumber <= 4 && maxPlayerNumber >= 2) {
            setupLobbyParameters(maxPlayerNumber, onlyOneCommonCard);
            return true;
        }
        return false;
    }

    public void setupLobbyParameters(int maxPlayerNumber, boolean onlyOneCommonCard) {
        this.maxPlayerNumber = maxPlayerNumber;
        this.onlyOneCommonCard = onlyOneCommonCard;
    }

    public boolean checkSavedGame(String nickname) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/it/polimi/ingsw/save/OldGame.json")));
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonContent, JsonObject.class);
            if (!jsonObject.get("gameOver").getAsBoolean()) {
                JsonObject nicknameToShelfMap = jsonObject.getAsJsonObject("nicknameToShelfMap");
                for (String nicknamesInJson : nicknameToShelfMap.keySet()) {
                    if(nicknamesInJson.equals(nickname)) {
                        return true;
                    }
                }
            }
        } catch(IOException e){
            return false;
        }
        return false;
    }

    public synchronized int presentation(String nickname) throws FirstPlayerException, CancelGameException, GameStartException, FullLobbyException, WaitForLobbyParametersException, RejoinRequestException { //response to presentation
        int availableSlots = getAvailableSlot();
        if (availableSlots == -1) { //handling the first player
            if (checkSavedGame(nickname)) { //the first player is present in the saved game
                setupGame(onlyOneCommonCard);
                changeState(new WaitingForSavedGameState());
                for (Player player : playerList) {
                    if (player.getNickname().equals(nickname)) {
                        player.setConnected(true);
                    }
                }
                return 2;
            } else {
                changeState(new WaitingForPlayerState()); //the first player is new
                addPlayer(new Player(nickname));
                throw new FirstPlayerException();
            }
        }
        else if (availableSlots == -2) {
            throw new WaitForLobbyParametersException();
        }
        else if (availableSlots == 0) {  //No place for a new player
            // check if the connection is from a disconnected user
            if(checkForDisconnectedPlayer(nickname)) {
                throw new RejoinRequestException();
            }
            throw new FullLobbyException();
        }

        switch (checkNicknameAvailability(nickname)) {
            case -1 -> //it wasn't possible to restore a saved game, goodbye to everyone
                    throw new CancelGameException();
            case 0 -> {     //unavailable nickname
                return 0;
            }
            default -> {  //you're in! (case: 1)
                addPlayer(new Player(nickname));
                if (isGameReady()) {
                    throw new GameStartException();
                }
                if(state.getClass().equals(WaitingForPlayerState.class))
                    return 1;
                return 3; //We are in waitingForSavedGameState
            }
        }
    }

    public void startGame(){
        if(!state.getClass().equals(WaitingForSavedGameState.class)) {
            setupGame(onlyOneCommonCard);
            activePlayer = playerList.get(0);
        }
        changeState(new RunningGameState());
    }

    public void disconnectAllUsers() throws RemoteException {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body body = new Body();
            body.setGoodbyeType(0);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("Goodbye", body);
            nickToTCPMessageControllerMapping.get(s).closeConnection();
        }
        server.disconnectAllRMIUsers();
    }

    public void yourTarget() throws RemoteException {
        for(String s : getNickToTCPMessageControllerMapping().keySet()) {
            Body body = new Body();
            for(Player p : playerList) {
                if(Objects.equals(p.getNickname(), s)) {
                    body.setPersonalCardNumber(p.getPersonalTargetCard().getPersonalNumber());
                    break;
                }
            }
            for(CommonTargetCard c : commonTargetCardsList) { //here is not null
                body.getCommonTargetCardsName().add(c.getName());
            }
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Your Target", body);
        }
        server.sendCardsRMI();
    }

    public void updateView() throws RemoteException {
        Body body = new Body();
        NewView newView = getNewView();
        body.setNewView(newView);
        for(String s : getNickToTCPMessageControllerMapping().keySet()) {
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Update View", body);
        }
        server.updateViewRMI(newView);

        if(newView.isGameOver()){
            prepareForNewGame();
        }
    }

    public void peerToPeerMsg(String sender, String receiver, String text, String localDateTime) throws InvalidNicknameException, RemoteException {
        if(!nickToTCPMessageControllerMapping.containsKey(receiver) && !server.checkReceiver(receiver)) {
            throw new InvalidNicknameException();
        }

        // send the message to receiver
        if(nickToTCPMessageControllerMapping.containsKey(receiver)) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            body.setLocalDateTime(localDateTime);
            nickToTCPMessageControllerMapping.get(receiver).printTCPMessage("New Msg", body);
        }
        else {
            server.peerToPeerMsg(sender, receiver, text,localDateTime);
        }

        // send the message to the sender
        if(nickToTCPMessageControllerMapping.containsKey(sender)) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            body.setLocalDateTime(localDateTime);
            nickToTCPMessageControllerMapping.get(sender).printTCPMessage("New Msg", body);
        }
        else {
            server.peerToPeerMsg(sender, sender, text,localDateTime);
        }
    }

    public void broadcastMsg(String sender, String text, String localDateTime) throws RemoteException {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            body.setLocalDateTime(localDateTime);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("New Msg", body);
        }
        server.broadcastMsg(sender, text,localDateTime);
    }


    public void intentionalDisconnectionUserTCP(TCPMessageController tcpMessageController) throws RemoteException {
        String nickname = null;
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(nickToTCPMessageControllerMapping.get(s) == tcpMessageController) {
                nickname = s;
                break;
            }
        }
        changePlayerConnectionStatus(nickname);
        Body b = new Body();
        b.setGoodbyeType(2);
        tcpMessageController.printTCPMessage("Goodbye", b);
        if(activePlayer.getNickname().equals(nickname)) {
            changeActivePlayer();
            updateView();
        }
    }

    public void setBoard(BoardFactory b){
        this.board = b;
    }

    public void setActivePlayer(Player p){
        this.activePlayer = p;
    }

    public void setLastTurn(boolean bool){
        this.lastTurn = bool;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void setMaxPlayerNumber(int maxPlayerNumber) {
        this.maxPlayerNumber = maxPlayerNumber;
    }

    public void setOnlyOneCommonCard(boolean onlyOneCommonCard) {
        this.onlyOneCommonCard = onlyOneCommonCard;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public List<CommonTargetCard> getCommonTargetCardsList() {
        return commonTargetCardsList;
    }
    public BoardFactory getBoard() {
        return board;
    }

    public void startCheckThread() {
        server.startCheckThreadRMI(); //RMI thread is different, located in server
        new Thread(() -> {
            while(true) {
                /*for(String nickname : nickToTCPMessageControllerMapping.keySet()) {
                    nickToTCPMessageControllerMapping.get(nickname).printTCPMessage("Check", null);
                    if(nickToUnansweredCheck.containsKey(nickname)) {
                        nickToUnansweredCheck.put(nickname, nickToUnansweredCheck.get(nickname) + 1);
                    }
                    else {
                        nickToUnansweredCheck.put(nickname, 1);
                    }
                    if(nickToUnansweredCheck.get(nickname) == 5) {
                        changePlayerConnectionStatus(nickname);
                        if(state.getClass().equals(RunningGameState.class) && activePlayer.getNickname().equals(nickname)) {
                            changeActivePlayer();
                            try {
                                updateView();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(getPlayerList().size() == 1 && getMaxPlayerNumber() == 0) {
                            changeState(new ServerInitState());
                            getPlayerList().clear();
                        }
                    }
                }*/
                if(state.getClass().equals(RunningGameState.class)
                    && countConnectedUsers() <= 1 && !timerIsRunning){
                    startTimer();
                }
                try {
                    Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startTimer(){
        this.timerIsRunning = true;
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (countConnectedUsers() <= 1) {
                    endGameForLackOfPlayers();
                } else {
                    System.out.println("Timer annullato");
                    cancelTimer();
                }
            }
        }, TIMER_DURATION_MILLISECONDS);
    }

    public void endGameForLackOfPlayers() {
        deleteSavedGame();
        if(countConnectedUsers()==1) {
            for (Player player : playerList) {
                if (player.isConnected()) {
                    if (nickToTCPMessageControllerMapping.containsKey(player.getNickname())) { //the last user is tcp
                        Body body = new Body();
                        body.setGoodbyeType(1);
                        nickToTCPMessageControllerMapping.get(player.getNickname()).printTCPMessage("Goodbye", body);
                        nickToTCPMessageControllerMapping.get(player.getNickname()).closeConnection();
                    }
                    server.disconnectLastRMIUser();
                }
            }
        }
        //there are no users remaining
        prepareForNewGame();
    }

    public void cancelTimer(){
        timerIsRunning=false;
        timer.cancel();
    }
    public int countConnectedUsers(){
        int connectedUsers=0;
        for(Player player:playerList){
            if(player.isConnected()){
                connectedUsers++;
            }
        }
        return connectedUsers;
    }

    public void resetUnansweredCheckCounter(TCPMessageController tcpMessageController) {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(nickToTCPMessageControllerMapping.get(s) == tcpMessageController) {
                nickToUnansweredCheck.put(s, 0);
                break;
            }
        }
    }

    public int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }

    public GameState getState() {
        return state;
    }

    public boolean didLastUserMadeHisMove() {
        return lastConnectedUserMadeHisMove;
    }
    public void setLastConnectedUserMadeHisMove(boolean bool){
        this.lastConnectedUserMadeHisMove = bool;
    }

    public boolean checkForDisconnectedPlayer(String nickname) {
        for(Player p : playerList) {
            if(!p.isConnected() && Objects.equals(p.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }

    public void deleteSavedGame() {
        try {
            File file = new File("src/main/java/it/polimi/ingsw/save/OldGame.json");
            file.delete();
        }
        catch (SecurityException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}


