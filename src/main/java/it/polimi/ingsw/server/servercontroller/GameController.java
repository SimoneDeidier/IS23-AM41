package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewPersonalView;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.EmptyItemListToInsert;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.servercontroller.controllerstates.*;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {

    private static GameController instance;
    private List<Player> playerList;
    private int maxPlayerNumber;
    private boolean lastTurn;
    private boolean onlyOneCommonCard;
    private Player activePlayer; //acts as a kind of turn
    private BoardFactory board;
    private GameState state;
    private List<CommonTargetCard> commonTargetCardsList;
    private static Map<Socket, TCPMessageController> socketMapping = new ConcurrentHashMap<>(4);
    private static Map<Socket, String> socketUserMapping = new ConcurrentHashMap<>(4);

    public GameController() {
        this.state = new ServerInitState();
    }

    public static GameController getGameController() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void putSocketTCPController(Socket socket, TCPMessageController controller) {
        socketMapping.put(socket, controller);
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
        } catch (EmptyItemListToInsert emptyItemListToInsert)//Ma secondo me si può togliere il try and catch, perchè
        // la eseguiamo alla fine di un check, che errori potrebbe dare se controlliamo tutto prima di lanciarla?
        {
            return false;
        }
        return true;
    }

    // todo va messa a posto qui secondo me - eccezioni
    public void executeMove(Body body) throws EmptyItemListToInsert {  //ok supporto a checkMove
        if (checkMove(body)) {
            List<Item> items = getListItems(body);
            try {
                activePlayer.getShelf().insertItems(body.getColumn(), items);
            } catch (Exception NotEnoughSpaceInColumnException) {
                System.out.println("Not enough space in the column provided!");
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
        this.state = state;
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

    public boolean checkGameParameters(int maxPlayerNumber, boolean onlyOneCommonCard) { //response to create lobby
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
        return true;
    }

    public synchronized boolean clientConnection() { //response to helo from client
        return getAvailableSlot() == -1 || getAvailableSlot() > 0;
    } //todo in RMI

    public synchronized int clientPresentation(String nickname) throws FirstPlayerException, CancelGameException, GameStartException, FullLobbyException { //response to presentation
        int availableSlots = getAvailableSlot();
        if (availableSlots == -1) { //handling the first player
            if (checkSavedGame(nickname)) { //the first player is present in the saved game
                setupGame(onlyOneCommonCard);
                changeState(new WaitingForSavedGameState());
                for (Player player : playerList) {
                    if (player.getNickname().equals(nickname))
                        player.setConnected(true);
                    return 2;
                }
            } else {
                changeState(new WaitingForPlayerState()); //the first player is new
                addPlayer(new Player(nickname));
                throw new FirstPlayerException();
            }
        } else if (availableSlots <= 0)  //No place for a new player
            throw new FullLobbyException();

        switch (checkNicknameAvailability(nickname)) {
            case -1 -> { //it wasn't possible to restore a saved game, goodbye to everyone
                throw new CancelGameException();
            }
            case 0 -> {     //unavailable nickname
                return 0;
            }
            default -> {  //you're in! (case: 1)
                addPlayer(new Player(nickname));
                if (isGameReady()) {
                    throw new GameStartException();
                }
                return 1;
            }
        }  //todo in RMI
    }


    public List<String> getConnectedUsersNicks() {
        List<String> list = new ArrayList<>();
        for (Player p : playerList) {
            list.add(p.getNickname());
        }
        return list;
    }

    public void startGame(){
        setupGame(onlyOneCommonCard);
        changeState(new RunningGameState());
    }
    public NewView generateUpdatedView(){
        //todo
        NewView newView=new NewView();
        return newView;
    }

    public NewPersonalView generateUpdatedPersonal(String nickname){
        NewPersonalView newPersonalView=new NewPersonalView();
        return newPersonalView;
    }

    public static Map<Socket, TCPMessageController> getSocketMapping() {
        return socketMapping;
    }

    public void addInSocketUserMapping() {}

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
    }

}
    //mancherebbero
    // -sendMessageToAll(String message)
    // -checkNicknameForMessage(String message,String nickname)
    // -sendMessageToUser(String message,String nickname)
    //O forse di queste solo check la fa controller, il resto il server?
