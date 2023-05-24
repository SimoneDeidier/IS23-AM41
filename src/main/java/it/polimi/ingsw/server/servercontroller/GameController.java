package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Save.Save;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.servercontroller.controllerstates.*;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

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
    private GameState state;
    private List<CommonTargetCard> commonTargetCardsList;
    private Map<String, TCPMessageController> nickToTCPMessageControllerMapping = new ConcurrentHashMap<>(4);
    private final Server server;
    private Thread checkThread;
    private boolean gameOver;

    private GameController(Server s) {
        this.state = new ServerInitState();
        this.server = s;
        this.gameOver=false;
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
        System.err.println("CALLED EXECUTE MOVE");
        if (checkMove(body)) {
            List<Item> items = getListItems(body);
            try { //inutile qui simo, ti convincerò
                activePlayer.getShelf().insertItems(body.getColumn(), items);
            } catch (Exception NotEnoughSpaceInColumnException) {
                System.err.println("Not enough space in the column provided!");
            }
            if (checkBoardNeedForRefill()) {
                board.refillBoard();
            }
            if (!lastTurn && checkLastTurn()) {
                activePlayer.setEndGameToken(EndGameToken.getEndGameToken());
                lastTurn = true;
            }
            activePlayer.updateScore();

            //Setting the next active player
            int nextIndex=nextIndexCalc(playerList.indexOf(activePlayer));
            while(nextIndex!=-1 && !playerList.get(nextIndex).isConnected())
                nextIndex=nextIndexCalc(nextIndex);
            if(nextIndex!=-1)
                activePlayer=playerList.get(nextIndex);
            else {
                activePlayer = null;
                gameOver=true;
            }

        }
        else throw new InvalidMoveException();
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
        newView.setBoardItems(board.getBoardMatrix());
        newView.setBoardBitMask(board.getBitMask());
        for(Player p : playerList) {
            newView.getNicknameToPointsMap().put(p.getNickname(), p.getPlayerScore());
            newView.getNicknameToShelfMap().put(p.getNickname(), p.getShelf().getShelfMatrix());
        }
        saveGameState();
        return newView;
    }

    public void saveGameState(){
        Save save=new Save();
        save.setActivePlayer(activePlayer);
        save.setLastTurn(lastTurn);
        save.setGameOver(gameOver);
        save.setState(state);
        save.setCommonTargetCardList(commonTargetCardsList);
        save.setBoard(board);
        save.setPlayerList(playerList);
        Gson gson= new Gson();
        try (FileWriter writer = new FileWriter("src/main/java/it/polimi/ingsw/Save/OldGame.json")) {
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
        state.setupPlayers(playerList, commonTargetCardsList, board,this );
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
        changeState(new ServerInitState());
        commonTargetCardsList = new ArrayList<>();
        nickToTCPMessageControllerMapping = new ConcurrentHashMap<>(4);
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
        //todo togliere il commento, funziona al 100%, c'è il commento solo per testare caso in cui non trova il nickname ma funziona!!
        /*try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/it/polimi/ingsw/Save/OldGame.json")));
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonContent, JsonObject.class);
            if (!jsonObject.get("gameOver").getAsBoolean()) {
                JsonArray playerList = jsonObject.getAsJsonArray("playerList");
                for (JsonElement jsonElement : playerList) {
                    JsonObject playerObject = jsonElement.getAsJsonObject();
                    if(playerObject.get("nickname").getAsString().equals(nickname)) {
                        return true;
                    }
                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }*/
        return false;
    }

    public synchronized int presentation(String nickname) throws FirstPlayerException, CancelGameException, GameStartException, FullLobbyException, WaitForLobbyParametersException { //response to presentation
        int availableSlots = getAvailableSlot();
        if (availableSlots == -1) { //handling the first player
            if (checkSavedGame(nickname)) { //the first player is present in the saved game
                setupGame(onlyOneCommonCard);
                changeState(new WaitingForSavedGameState());
                for (Player player : playerList) {
                    if (player.getNickname().equals(nickname)) {
                        player.setConnected(true);
                        //todo starts ping towards that user
                    }
                    //todo aggiungere giocatore alla lista di player tcp o rmi?
                    return 2;
                }
            } else {
                changeState(new WaitingForPlayerState()); //the first player is new
                System.err.println(state);
                addPlayer(new Player(nickname));
                System.err.println(playerList);
                //todo Start ping towards first player
                throw new FirstPlayerException();
            }
        }
        else if (availableSlots == -2) {
            throw new WaitForLobbyParametersException();
        }
        else if (availableSlots == 0) {  //No place for a new player
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
                //todo starts ping towards that user
                if (isGameReady()) {
                    throw new GameStartException();
                }
                return 1;
            }
        }
    }

    public void startGame(){
        setupGame(onlyOneCommonCard);
        activePlayer=playerList.get(0);
        changeState(new RunningGameState());
        //todo da spostare in presentation
        /*checkThread = new Thread(() -> {
            System.err.println("THREAD PING STARTED!");
            while(true) {
                try {
                    Thread.sleep(1000);
                    // mando check a tutti i giocatori
                   //System.out.printf("Check");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        checkThread.start();*/
    }

    public void disconnectAllUsers() throws RemoteException {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("Goodbye", null);
            nickToTCPMessageControllerMapping.get(s).closeConnection();
        }
        server.disconnectAllRMIUsers();
    }

    public void yourTarget() throws RemoteException {
        System.err.println("YOUR TARGET");
        System.err.println("UTENTI TCP: " + getNickToTCPMessageControllerMapping().keySet());
        for(String s : getNickToTCPMessageControllerMapping().keySet()) {
            Body body = new Body();
            System.err.println("PLAYER LIST: " + playerList);
            for(Player p : playerList) {
                if(Objects.equals(p.getNickname(), s)) {
                    body.setPersonalCardNumber(p.getPersonalTargetCard().getPersonalNumber());
                    System.err.println("MANDO A: " + p.getNickname());
                    break;
                }
            }
            for(CommonTargetCard c : commonTargetCardsList) {
                body.getCommonTargetCardsName().add(c.getName());
            }
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Your Target", body);
        }
        List<String> commonList=new ArrayList<>();
        for(CommonTargetCard commonTargetCard: commonTargetCardsList){
            commonList.add(commonTargetCard.getName());
        }
        server.sendCardsRMI(commonList);
    }

    public void updateView() throws RemoteException {
        System.err.println("CALLED UPDATE VIEW");
        Body body = new Body();
        NewView newView = getNewView();
        body.setNewView(newView);
        for(String s : getNickToTCPMessageControllerMapping().keySet()) {
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Update View", body);
        }
        //todo eliminare sotto, era per test
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(newView.getBoardItems()[i][j]!=null)
                    System.out.printf(newView.getBoardItems()[i][j].getColor() + " ");
                else
                    System.out.printf("null ");
            }
            System.out.println();
        }
        //todo eliminare sopra
        server.updateViewRMI(newView);
    }

    public void peerToPeerMsg(String sender, String receiver, String text, String localDateTime) throws InvalidNicknameException, RemoteException {
        if(!nickToTCPMessageControllerMapping.containsKey(receiver) && !server.checkReceiver(receiver)) {
            throw new InvalidNicknameException();
        }

        // send the message to receiver
        if(nickToTCPMessageControllerMapping.containsKey(receiver)) {
            System.err.println("Mando PTP MSG a " + receiver);
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
            System.err.println("Mando PTP MSG a " + sender);
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
            System.err.println("MANDO BRD MSG A " + s);
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            body.setLocalDateTime(localDateTime);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("New Msg", body);
        }
        server.broadcastMsg(sender, text,localDateTime);
    }


    public void intentionalDisconnectionUserTCP(TCPMessageController tcpMessageController) {
        String nickname = null;
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(nickToTCPMessageControllerMapping.get(s) == tcpMessageController) {
                nickname = s;
                break;
            }
        }
        for(Player p : playerList) {
            if(Objects.equals(p.getNickname(), nickname)) {
                p.setConnected(false);
                tcpMessageController.printTCPMessage("Goodbye", null);
                return;
            }
        }
    }

    public boolean checkReJoinRequest(String nickname) {
        for(Player p : playerList) {
            if(Objects.equals(p.getNickname(), nickname) && !p.isConnected()) {
                p.setConnected(true);
                return true;
            }
        }
        return false;
        // todo da fare in RMI
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

    public void setCommonTargetCardsList(List<CommonTargetCard> commonTargetCardsList) {
        this.commonTargetCardsList = commonTargetCardsList;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}


