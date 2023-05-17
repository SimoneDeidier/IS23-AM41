package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.EmptyItemListToInsert;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.servercontroller.controllerstates.*;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

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

    public GameController(Server s) {
        this.state = new ServerInitState();
        this.server = s;
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
        //da rifare
        if (!body.getPlayerNickname().equals(activePlayer.getNickname())) {
            return false;
        }
        if (!board.checkMove(body.getPositionsPicked())) {
            return false;
        }
        //tutti gli if per checkare
        try {
            executeMove(body);
        }
        catch (EmptyItemListToInsert e) {
            return false;
        }
        return true;
    }

    public void executeMove(Body body) throws EmptyItemListToInsert {  //ok supporto a checkMove
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
            if (!lastTurn & checkLastTurn()) {
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
            else
                activePlayer=null; //signals to the server the game is over!

            //todo add salvataggio nel json
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

    public List<Item> getListItems(Body body) { //ok supporto a checkMove

        List<Item> items = new ArrayList<>();
        for (int[] picks : body.getPositionsPicked()) {
            items.add(board.getBoardMatrixElement(picks[0], picks[1]));
        }
        return items;

    }

    public boolean checkBoardNeedForRefill() { //ok supporto a checkMove

        for (int i = 0; i < board.getBoardNumberOfRows(); i++) {
            for (int j = 0; j < board.getBoardNumberOfColumns(); j++) {
                if (board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i, j) != null && !board.itemHasAllFreeSide(i, j)) {
                    return false; //There is at least one non-null element that has at least one other item on one of its side
                }
            }
        }
        return true; //Needs to be refilled

    }

    public void changeState(GameState state) {  //ok
        if(state != this.state) {
            this.state = state;
        }
    }

    public int getAvailableSlot() {  //ok
        return state.getAvailableSlot(maxPlayerNumber, playerList);
    }

    public void addPlayer(Player player) { //ok
        state.addPlayer(player, playerList);
    }

    public void setupGame(boolean onlyOneCommonCard) { //ok supporto a isGameReady
        state.setupGame(maxPlayerNumber, commonTargetCardsList, board, onlyOneCommonCard,playerList,this);
    }

    public boolean checkLastTurn() {  //ok supporto a checkMove
        for (Player p : playerList) {
            if (p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }

    public void prepareForNewGame() { //ok
        playerList = new ArrayList<>();
        lastTurn = false;
        activePlayer = null;
        board.resetBoard();
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

    public void changePlayerConnectionStatus(Player player) {
        player.setConnected(!player.isConnected());
    }

    public boolean createLobby(int maxPlayerNumber, boolean onlyOneCommonCard) { //response to create lobby
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
        //todo look into json and look if the nickname is present there
        return false;
    }

    public synchronized int presentation(String nickname) throws FirstPlayerException, CancelGameException, GameStartException, FullLobbyException, WaitForLobbyParametersException { //response to presentation
        int availableSlots = getAvailableSlot();
        if (availableSlots == -1) { //handling the first player
            System.err.println("FIRST PLAYER");
            if (checkSavedGame(nickname)) { //the first player is present in the saved game
                setupGame(onlyOneCommonCard);
                changeState(new WaitingForSavedGameState());
                for (Player player : playerList) {
                    if (player.getNickname().equals(nickname))
                        player.setConnected(true);
                    return 2;
                }
            } else {
                System.err.println("NO SAVED PLAYER");
                changeState(new WaitingForPlayerState()); //the first player is new
                System.err.println("UPDATED SERVER STATE");
                System.err.println(state);
                addPlayer(new Player(nickname));
                System.err.println("ADDED PLAYER TO PLAYERLIST");
                System.err.println(playerList);
                throw new FirstPlayerException();
            }
        }
        else if (availableSlots == -2) {
            throw new WaitForLobbyParametersException();
        }
        else if (availableSlots == 0) {  //No place for a new player
            System.err.println("FULL LOBBY!");
            throw new FullLobbyException();
        }

        switch (checkNicknameAvailability(nickname)) {
            case -1 -> { //it wasn't possible to restore a saved game, goodbye to everyone
                throw new CancelGameException();
            }
            case 0 -> {     //unavailable nickname
                System.err.println("NICKNAME UNAVAILABLE");
                return 0;
            }
            default -> {  //you're in! (case: 1)
                System.err.println("NICKNAME OK");
                addPlayer(new Player(nickname));
                System.err.println("ADDED PLAYER");
                if (isGameReady()) {
                    throw new GameStartException();
                }
                return 1;
            }
        }
    }

    public void startGame(){
        System.err.println("START GAME");
        setupGame(onlyOneCommonCard);
        System.err.println("IS ONE COMMON: " + onlyOneCommonCard);
        changeState(new RunningGameState());
        System.err.println("STATE: " + state);
        checkThread = new Thread(() -> {
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
        checkThread.start();
    }

    public void addInSocketUserMapping() {}

    /* todo da spostare lato client!!!
    public String checkMessageType(String message) throws IncorrectNicknameException {
        String[] words= message.split(" ");
        if(words[0].startsWith("@")){
            for(Player player:playerList){
                if(player.getNickname().equals(words[0].substring(1)))
                    return player.getNickname();
            }
            throw new IncorrectNicknameException();
        }
        return null;
    }*/

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
                    body.setPersonalCard(p.getPersonalTargetCard());
                    System.err.println("MANDO A: " + p.getNickname());
                    break;
                }
            }
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Your Target", body);
        }
        server.sendPersonalTargetCardsRMI();
    }

    public void updateView() throws RemoteException {
        for(String s : getNickToTCPMessageControllerMapping().keySet()) {
            Body body = new Body();
            body.setView(playerList);
            getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Update View", body);
        }
        server.updateViewRMI();
    }

    public void peerToPeerMsg(String sender, String receiver, String text) throws InvalidNicknameException, RemoteException {
        if(!nickToTCPMessageControllerMapping.containsKey(receiver) && !server.checkReceiver(receiver)) {
            throw new InvalidNicknameException();
        }

        // send the message to receiver
        if(nickToTCPMessageControllerMapping.containsKey(receiver)) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            nickToTCPMessageControllerMapping.get(receiver).printTCPMessage("New Msg", body);
        }
        else {
            server.peerToPeerMsg(sender, receiver, text);
        }

        // send the message to the sender
        if(nickToTCPMessageControllerMapping.containsKey(sender)) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            nickToTCPMessageControllerMapping.get(sender).printTCPMessage("New Msg", body);
        }
        else {
            server.peerToPeerMsg(sender, sender, text);
        }
    }

    public void broadcastMsg(String sender, String text) throws RemoteException {
        for(String s : nickToTCPMessageControllerMapping.keySet()) {
            Body body = new Body();
            body.setSenderNickname(sender);
            body.setText(text);
            nickToTCPMessageControllerMapping.get(s).printTCPMessage("New Msg", body);
        }
        server.broadcastMsg(sender, text);
    }

    // le funzioni di disconnessione dell'utente sono diverse tra tcp e rmi secondo me
    // tanto un utemte tcp che decide di disconnettersi deve sicuro ricevere un messaggio tcp,
    // mentre uno rmi riceverà una chiamata a metodo rmi
    public void disconnectUserTCP(TCPMessageController tcpMessageController) {
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

    public void disconnectUserRMI() {}  // todo da fare!!!

}

    //mancherebbero
    // -sendMessageToAll(String message)
    // -checkNicknameForMessage(String message,String nickname)
    // -sendMessageToUser(String message,String nickname)
    //O forse di queste solo check la fa controller, il resto il server?
