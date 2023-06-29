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

/**
 * The GameController class is responsible for handling the lobby, that includes all game dynamics, player's moves and connections.
 */
public class GameController {

    private static GameController instance;
    private List<Player> playerList = new ArrayList<>(4);
    private int maxPlayerNumber = 0;
    private boolean lastTurn = false;
    private boolean onlyOneCommonCard = false;
    private Player activePlayer = null; //acts as a kind of turn
    private BoardFactory board;
    private final EndGameToken endGameToken= EndGameToken.getEndGameToken(); //todo add in UML (added because it's singleton, it needs to be reset after end of the game)
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
    /**
     * Returns the instance of GameController or creates a new one if it doesn't exist.
     *
     * @param s the Server instance
     * @return the GameController instance
     */
    public static GameController getGameController(Server s) {
        if (instance == null) {
            instance = new GameController(s);
        }
        return instance;
    }
    /**
     * Puts the mapping of nickname to TCPMessageController in the map.
     *
     * @param nickname the nickname of the player
     * @param controller the TCPMessageController instance
     */
    public void putNickToSocketMapping(String nickname, TCPMessageController controller) {
        nickToTCPMessageControllerMapping.put(nickname, controller);
    }
    /**
     * Returns the mapping of nickname to TCPMessageController.
     *
     * @return the mapping of nickname to TCPMessageController
     */
    public Map<String, TCPMessageController> getNickToTCPMessageControllerMapping() {
        return nickToTCPMessageControllerMapping;
    }
    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayerList() {
        return playerList;
    }
    /**
     * Checks if the move specified in the body is valid by the game rules.
     *
     * @param body the Body instance containing the move details
     * @return true if the move is valid, false otherwise
     */
    public boolean checkMove(Body body) {
        if (!body.getPlayerNickname().equals(activePlayer.getNickname())) {
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
    /**
     * Executes the move specified in the body.
     *
     * @param body the Body instance containing the move details
     * @throws InvalidMoveException if the move is invalid
     */
    public void executeMove(Body body) throws InvalidMoveException {
        if (checkMove(body)) {
            List<Item> items = getListItems(body);
            try {
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
            changeActivePlayer();

        }
        else throw new InvalidMoveException();
    }
    /**
     * Changes the active player to the next player.
     */
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
    /**
     * Returns a new view of the game state.
     *
     * @return the NewView instance representing the current game state
     */
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
    /**
     * Saves the current game state to a json file.
     */
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
        try (FileWriter writer = new FileWriter(new File(System.getProperty("user.dir"), "savings.json"))) {
            gson.toJson(save, writer);
        } catch (IOException e) {
            System.err.println("Writing to the JSON save file was not possible, the desired resource could not be accessed!");
        }
    }
    /**
     * Calculates the index of the next player.
     *
     * @param currIndex the index of the current player
     * @return the index of the next player
     */
    public int nextIndexCalc(int currIndex){
        if(playerList.size()-1==currIndex) {
            if(lastTurn)
                return -1;
            return 0;
        }
        else
            return currIndex+1;
    }
    /**
     * Returns a list of items based on the positions picked in the body.
     *
     * @param body the Body instance containing the positions picked
     * @return the list of items
     */
    public List<Item> getListItems(Body body) {
        List<Item> items = new ArrayList<>();
        for (int[] picks : body.getPositionsPicked()) {
            items.add(new Item(board.getBoardMatrixElement(picks[0], picks[1]).getColor()));
            board.setBoardMatrixElement(null,picks[0], picks[1]);
        }
        return items;

    }
    /**
     * Returns the active player.
     *
     * @return the active player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }
    /**
     * Checks if the board needs to be refilled.
     *
     * @return true if the board needs to be refilled, false otherwise
     */
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
    /**
     * Changes the state of the game controller.
     *
     * @param state the new state to set
     */
    public void changeState(GameState state) {
        if(state != this.state) {
            this.state = state;
        }
    }
    /**
     * Returns the number of available slots for new players.
     *
     * @return the number of available slots
     */
    public int getAvailableSlot() {
        return state.getAvailableSlot(maxPlayerNumber, playerList);
    }
    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        state.addPlayer(player, playerList);
    }
    /**
     * Sets up the game with the specified parameters.
     *
     * @param onlyOneCommonCard true if there is only one common card, false if there are two common cards
     */
    public void setupGame(boolean onlyOneCommonCard) {
        this.commonTargetCardsList = state.setupCommonList(onlyOneCommonCard, maxPlayerNumber);
        this.board = state.setupBoard(maxPlayerNumber);
        state.boardNeedsRefill(this.board);
        state.setupPlayers(playerList, commonTargetCardsList, board, this);
    }
    /**
     * Checks if it is the last turn.
     *
     * @return true if it is the last turn, false otherwise
     */
    public boolean checkLastTurn() {
        for (Player p : playerList) {
            if (p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Prepares for a new game by resetting all the game parameters.
     */
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
        timerIsRunning=false;
    }
    /**
     * Checks the availability of a nickname.
     *
     * @param nickname the nickname to check
     * @return The return value of checkNicknameAvailability: according to the GameState and the nicknames it may be -1, 0 or 1
     */
    public int checkNicknameAvailability(String nickname) {
        return (state.checkNicknameAvailability(nickname, playerList));
    }
    /**
     * Checks if the game is ready to start.
     *
     * @return true if the game is ready.
     */
    public boolean isGameReady() {
        return state.isGameReady(playerList, maxPlayerNumber);
    }

    /**
     * Changes the player connection status.
     *
     * @param nickname the nickname of the player
     */
    public void changePlayerConnectionStatus(String nickname) {
        for (Player player : playerList) {
            if (player.getNickname().equals(nickname)) {
                player.setConnected(!player.isConnected());
                break;
            }
        }
    }

    /**
     * Creates the lobby of the game and sets the lobby parameters.
     *
     * @param maxPlayerNumber The maximum number of players
     * @param onlyOneCommonCard A boolean flat that indicates if there will be one common card or two
     * @return True if the maxPlayerNumber is between 2 and 4, false otherwise
     */
    public boolean createLobby(int maxPlayerNumber, boolean onlyOneCommonCard) {
        if (maxPlayerNumber <= 4 && maxPlayerNumber >= 2) {
            setupLobbyParameters(maxPlayerNumber, onlyOneCommonCard);
            return true;
        }
        return false;
    }

    /**
     * Sets lobby parameters.
     *
     * @param maxPlayerNumber The maximum number of players
     * @param onlyOneCommonCard A boolean flat that indicates if there will be one common card or two
     */
    public void setupLobbyParameters(int maxPlayerNumber, boolean onlyOneCommonCard) {
        this.maxPlayerNumber = maxPlayerNumber;
        this.onlyOneCommonCard = onlyOneCommonCard;
    }
/**
 * Checks if a saved game exists for the given nickname.
 *
 * @param nickname the nickname of the player
 * @return true if a saved game exists for the player, false otherwise
 */
    /**
     * Checks if an unfinished saved game exists and the given nickname of the player was in that game.
     *
     * @param nickname the nickname of the player
     * @return true if a saved game exists for the player, false otherwise
     */
    public boolean checkSavedGame(String nickname) {
        try {
            File save = new File(System.getProperty("user.dir"), "savings.json");
            String jsonContent = new String(Files.readAllBytes(save.toPath()));
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
    /**
     * Handles the presentation of a player.
     *
     * @param nickname the nickname of the player
     * @return an integer representing the result of the presentation
     * @throws FirstPlayerException if the player is the first player
     * @throws CancelGameException if the game is canceled
     * @throws GameStartException if the game starts
     * @throws FullLobbyException if the lobby is full
     * @throws WaitForLobbyParametersException if waiting for lobby parameters
     * @throws RejoinRequestException if a rejoin request is made
     */
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
        else if(checkForDisconnectedPlayer(nickname)) {
            // check if the connection is from a disconnected user
            throw new RejoinRequestException();
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
                if (isGameReady()) {
                    throw new GameStartException();
                }
                if(state.getClass().equals(WaitingForPlayerState.class))
                    return 1;
                return 3; //We are in waitingForSavedGameState
            }
        }
    }
    /**
     * Starts the game.
     */
    public void startGame(){
        if(!state.getClass().equals(WaitingForSavedGameState.class)) {
            setupGame(onlyOneCommonCard);
            activePlayer = playerList.get(0);
        }
        changeState(new RunningGameState());
    }

    /**
     * Disconnects all users from the game.
     *
     * @throws IOException if an I/O error occurs
     */
    public void disconnectAllUsers() throws IOException {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body body = new Body();
            body.setGoodbyeType(0);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("Goodbye", body);
            nickToTCPMessageControllerMapping.get(s).closeConnection();
        }
        server.disconnectAllRMIUsers();
    }
    /**
     * Gives to the players their target cards.
     */
    public void yourTarget() {
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
    /**
     * Updates the view for all players.
     *
     * @throws RemoteException if a remote error occurs
     */
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
    /**
     * Sends a peer-to-peer message (TCP or RMI) from the sender to the receiver.
     *
     * @param sender the nickname of the sender
     * @param receiver the nickname of the receiver
     * @param text the text of the message
     * @param localDateTime the timestamp of the message
     * @throws InvalidNicknameException if an invalid nickname is given
     */
    public void peerToPeerMsg(String sender, String receiver, String text, String localDateTime) throws InvalidNicknameException {
        if(!nickToTCPMessageControllerMapping.containsKey(receiver) && !server.checkReceiverInRMI(receiver)) {
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
    /**
     * Broadcasts a message from the sender to all players.
     *
     * @param sender the nickname of the sender
     * @param text  the text of the message
     * @param localDateTime the timestamp of the message
     */
    public void broadcastMsg(String sender, String text, String localDateTime) {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            body.setLocalDateTime(localDateTime);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("New Msg", body);
        }
        server.broadcastMsg(sender, text,localDateTime);
    }

    /**
     * Handles intentional disconnection of a TCP user.
     *
     * @param tcpMessageController the TCPMessageController of the user
     * @throws RemoteException if a remote error occurs
     */
    public void intentionalDisconnectionUserTCP(TCPMessageController tcpMessageController) throws RemoteException {
        String nickname = null;
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(nickToTCPMessageControllerMapping.get(s) == tcpMessageController) {
                nickname = s;
                break;
            }
        }
        nickToTCPMessageControllerMapping.remove(nickname);
        changePlayerConnectionStatus(nickname);
        Body b = new Body();
        b.setGoodbyeType(2);
        tcpMessageController.printTCPMessage("Goodbye", b);
        if(activePlayer.getNickname().equals(nickname)) {
            changeActivePlayer();
            updateView();
        }
        notifyOfDisconnectionAllUsers(nickname);
    }
    /**
     * Sets the board of the game.
     *
     * @param b the board factory
     */
    public void setBoard(BoardFactory b){
        this.board = b;
    }
    /**
     * Sets the active player.
     *
     * @param p the active player
     */
    public void setActivePlayer(Player p){
        this.activePlayer = p;
    }
    /**
     * Sets the last turn indicator.
     *
     * @param bool the value of the indicator
     */
    public void setLastTurn(boolean bool){
        this.lastTurn = bool;
    }
    /**
     * Sets the player list.
     *
     * @param playerList the player list
     */
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
    /**
     * Sets the maximum player number.
     *
     * @param maxPlayerNumber the maximum player number
     */
    public void setMaxPlayerNumber(int maxPlayerNumber) {
        this.maxPlayerNumber = maxPlayerNumber;
    }
    /**
     * Sets the flag for having only one common card or two.
     *
     * @param onlyOneCommonCard the flag value: true for one, false for two
     */
    public void setOnlyOneCommonCard(boolean onlyOneCommonCard) {
        this.onlyOneCommonCard = onlyOneCommonCard;
    }
    /**
     * Sets the game state.
     *
     * @param state the game state
     */
    public void setState(GameState state) {
        this.state = state;
    }
    /**
     * Sets the game over flag.
     *
     * @param gameOver the game over flag
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    /**
     * Returns the list of common target cards.
     *
     * @return The list of common target cards.
     */
    public List<CommonTargetCard> getCommonTargetCardsList() {
        return commonTargetCardsList;
    }
    /**
     * Returns the game board.
     *
     * @return The game board.
     */
    public BoardFactory getBoard() {
        return board;
    }
    /**
     * Starts the check thread to monitor player connections and handle disconnections.
     */
    public void startCheckThread() {
        server.startCheckThreadRMI(); //RMI thread is different, located in server
        new Thread(() -> {
            while(true) {
                synchronized (nickToTCPMessageControllerMapping) {
                    for (String nickname : nickToTCPMessageControllerMapping.keySet()) {
                        nickToTCPMessageControllerMapping.get(nickname).printTCPMessage("Check", null);
                        if (nickToUnansweredCheck.containsKey(nickname)) {
                            nickToUnansweredCheck.put(nickname, nickToUnansweredCheck.get(nickname) + 1);
                        } else {
                            nickToUnansweredCheck.put(nickname, 1);
                        }
                        if (nickToUnansweredCheck.get(nickname) == 5) {
                            try {
                                nickToTCPMessageControllerMapping.get(nickname).closeConnection();
                            } catch (IOException e) {
                                System.err.println("Socket connection could not be closed with the inactive" +  nickname + " user!");
                            }
                            nickToTCPMessageControllerMapping.remove(nickname);
                            changePlayerConnectionStatus(nickname);
                            if (state.getClass().equals(RunningGameState.class)) {
                                notifyOfDisconnectionAllUsers(nickname);
                            }
                            if (state.getClass().equals(RunningGameState.class) && activePlayer.getNickname().equals(nickname)) {
                                changeActivePlayer();
                                try {
                                    updateView();
                                } catch (RemoteException e) {
                                    System.err.println("A RemoteException was thrown, the UpdateView message could not be sent to one or more users after the user on duty had logged off due to inactivity!");
                                }
                            } else if (getPlayerList().size() == 1 && getMaxPlayerNumber() == 0) {
                                changeState(new ServerInitState());
                                getPlayerList().clear();
                            }
                            else if(state.getClass().equals(WaitingForPlayerState.class)) {
                                notifyOfDisconnectionFromLobby(nickname);
                            }
                        }
                    }
                }
                if(state.getClass().equals(RunningGameState.class)
                    && countConnectedUsers() <= 1 && !timerIsRunning){
                    startTimer();
                }
                if(timerIsRunning && countConnectedUsers()>1){
                    cancelTimer();
                    timerIsRunning=false;
                }
                try {
                    Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                }
                catch (InterruptedException e) {
                    System.err.println("Il thread di ping è stato interrotto improvvisamente!");
                }
            }
        }).start();
    }
    /**
     * Starts the game timer.
     */
    public void startTimer(){
        this.timerIsRunning = true;
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (countConnectedUsers() <= 1) {
                    try {
                        endGameForLackOfPlayers();
                    } catch (IOException e) {
                        System.err.println("The game could not be ended due to a lack of players!");
                    }
                } else {
                    cancelTimer();
                }
            }
        }, TIMER_DURATION_MILLISECONDS);
    }
    /**
     * Ends the game due to a lack of players.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void endGameForLackOfPlayers() throws IOException {
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
    /**
     * Cancels the game timer.
     */
    public void cancelTimer(){
        timerIsRunning=false;
        timer.cancel();
    }
    /**
     * Counts the number of connected users.
     *
     * @return The number of connected users.
     */
    public int countConnectedUsers(){
        int connectedUsers=0;
        for(Player player:playerList){
            if(player.isConnected()){
                connectedUsers++;
            }
        }
        return connectedUsers;
    }
    /**
     * Resets the unanswered check counter for a given TCP message controller.
     *
     * @param tcpMessageController The TCP message controller to reset the counter for.
     */
    public void resetUnansweredCheckCounter(TCPMessageController tcpMessageController) {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(nickToTCPMessageControllerMapping.get(s) == tcpMessageController) {
                nickToUnansweredCheck.put(s, 0);
                break;
            }
        }
    }
    /**
     * Returns the maximum number of players.
     *
     * @return The maximum number of players.
     */
    public int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }
    /**
     * Returns the current game state.
     *
     * @return The current game state.
     */
    public GameState getState() {
        return state;
    }
    /**
     * Checks if the last connected user made their move.
     *
     * @return True if the last connected user made their move, false otherwise.
     */
    public boolean didLastUserMadeHisMove() {
        return lastConnectedUserMadeHisMove;
    }
    /**
     * Sets whether the last connected user made their move.
     *
     * @param bool The value indicating whether the last connected user made their move.
     */
    public void setLastConnectedUserMadeHisMove(boolean bool){
        this.lastConnectedUserMadeHisMove = bool;
    }
    /**
     * Checks if a player with the given nickname is disconnected.
     *
     * @param nickname The nickname of the player to check.
     * @return True if the player is disconnected, false otherwise.
     */
    public boolean checkForDisconnectedPlayer(String nickname) {
        for(Player p : playerList) {
            if(!p.isConnected() && Objects.equals(p.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Deletes the saved game file.
     */
    public void deleteSavedGame() {
        try {
            File save = new File(System.getProperty("user.dir"), "savings.json");
            save.delete();
        }
        catch (SecurityException | NullPointerException e) {
            System.err.println("The old JSON saving could not be deleted!");
        }
    }
    /**
     * Notifies all users of a player disconnection.
     *
     * @param nickname The nickname of the disconnected player.
     */
    public void notifyOfDisconnectionAllUsers(String nickname) {
        for(String user : nickToTCPMessageControllerMapping.keySet()) {
            if(!Objects.equals(nickname, user)) {
                Body b = new Body();
                b.setPlayerNickname(nickname);
                nickToTCPMessageControllerMapping.get(user).printTCPMessage("Player Disconnected", b);
            }
        }
        server.notifyOfDisconnectionAllRMIUsers(nickname);
    }
    /**
     * Notifies all users of a player reconnection.
     *
     * @param nickname The nickname of the reconnected player.
     */
    public void notifyOfReconnectionAllUsers(String nickname) {
        for(String user : nickToTCPMessageControllerMapping.keySet()) {
            if(!Objects.equals(nickname, user)) {
                Body b = new Body();
                b.setPlayerNickname(nickname);
                nickToTCPMessageControllerMapping.get(user).printTCPMessage("Player Reconnected", b);
            }
        }
        server.notifyOfReconnectionAllRMIUsers(nickname);
    }

    public void notifyOfConnectedUser(String nickname) {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(!Objects.equals(s, nickname)) {
                Body b = new Body();
                b.setPlayerNickname(nickname);
                nickToTCPMessageControllerMapping.get(s).printTCPMessage("User Connected", b);
            }
        }
        server.notifyOfConnectedUser(nickname);
    }

    public void notifyOfDisconnectionFromLobby(String nickname) {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body b = new Body();
            b.setPlayerNickname(nickname);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("Disconnected From Lobby", b);
        }
        server.notifyOfDisconnectionFromLobby(nickname);
    }

    public void notifyOfReconnectionInLobby(String nickname) {

        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            if(Objects.equals(s, nickname)) {
                Body b = new Body();
                Map<String, Boolean> lobby = new HashMap<>();
                for(Player player : playerList) {
                    lobby.put(player.getNickname(), player.isConnected());
                }
                b.setLobby(lobby);
                b.setNumberOfPlayers(getMaxPlayerNumber());
                nickToTCPMessageControllerMapping.get(s).printTCPMessage("Rejoined In Lobby", b);
            }
            else {
                Body b = new Body();
                b.setPlayerNickname(nickname);
                nickToTCPMessageControllerMapping.get(s).printTCPMessage("User Rejoined", b);
            }
        }
        server.notifyOfReconnectionInLobby(nickname);
    }

}


