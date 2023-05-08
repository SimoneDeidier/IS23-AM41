package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {

    private static GameController instance;
    private List<Player> playerList;
    private int maxPlayerNumber;
    private boolean lastTurn;
    private Player activePlayer; //acts as a kind of turn
    private BoardFactory board;
    private GameState state;
    private List<CommonTargetCard> commonTargetCardsList;
    private static Map<Socket, TCPMessageController> socketMapping = new ConcurrentHashMap<>(4);

    public GameController(Socket socket, TCPMessageController tcpMessageController) {
        this.state = new ServerInitState();
        socketMapping.put(socket, tcpMessageController);
    }

    public static GameController getGameController(Socket socket, TCPMessageController tcpMessageController) {
        if (instance == null) {
            instance = new GameController(socket, tcpMessageController);
        }
        if(!socketMapping.containsKey(socket)) {
            socketMapping.put(socket, tcpMessageController);
        }
        return instance;
    }

    public boolean checkMove(Body body){  //ok serve
        //da rifare
       if(!body.getPlayerNickname().equals(activePlayer.getNickname())) {
           return false;
       }
       if(!board.checkMove(body.getPositionsPicked())){
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
        if(checkMove(body)) {
            List<Item> items = getListItems(body);
            try {
                activePlayer.getShelf().insertItems(body.getColumn(), items);
            }
            catch (Exception NotEnoughSpaceInColumnException){
                System.out.println("Not enough space in the column provided!");
            }
            if (checkBoardNeedForRefill()) {
                board.refillBoard();
            }
            if(!lastTurn & checkLastTurn()){
                activePlayer.setEndGameToken(EndGameToken.getEndGameToken());
                lastTurn=true;
            }
            activePlayer.updateScore(activePlayer);
        }

    }

    public List<Item> getListItems(Body body){ //ok supporto a checkMove

        List<Item> items= new ArrayList<>();
        for(int[] picks : body.getPositionsPicked()){
            items.add(board.getBoardMatrixElement(picks[0],picks[1]));
        }
        return items;

    }

    public boolean checkBoardNeedForRefill() { //ok supporto a checkMove

        for(int i=0;i<board.getBoardNumberOfRows();i++){
            for(int j=0;j<board.getBoardNumberOfColumns();j++){
                if(board.getBitMaskElement(i,j) && board.getBoardMatrixElement(i,j)!=null && !board.itemHasAllFreeSide(i,j)){
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
        return state.getAvailableSlot(maxPlayerNumber,playerList);
    }
    public void addPlayer(Player player) { //ok
        playerList.add(player);
    }

    public void setupGame(boolean onlyOneCommonCard) { //ok supporto a isGameReady
        state.setupGame(maxPlayerNumber,commonTargetCardsList,board, onlyOneCommonCard);
    }

    public boolean checkLastTurn() {  //ok supporto a checkMove
        for(Player p : playerList) {
            if(p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }

    public void prepareForNewGame(){ //ok
        playerList=new ArrayList<>();
        lastTurn=false;
        activePlayer=null;
        board.resetBoard();
        changeState(new ServerInitState());
        commonTargetCardsList=new ArrayList<>();

    }
    public boolean checkSavedGame(String nickname){
        return state.checkSavedGame(nickname);
    }

    public boolean checkNicknameAvailability(String nickname){
        for(Player player:playerList){
            if(player.getNickname().equals(nickname)){
                return false;
            }
        }
        return true; //true==available
    }

    public boolean isGameReady(){
        return state.isGameReady(playerList,maxPlayerNumber);
    }

    public void changePlayerConnectionStatus(Player player){
        player.setConnected(!player.isConnected());
    }

    public boolean checkGameParameters(int maxPlayerNumber){
        return maxPlayerNumber <= 4 && maxPlayerNumber >= 2;
    }

    public List<String> getConnectedUsersNicks() {
        List<String> list = new ArrayList<>();
        for(Player p : playerList) {
            list.add(p.getNickname());
        }
        return list;
    }

    //mancherebbero
    // -sendMessageToAll(String message)
    // -checkNicknameForMessage(String message,String nickname)
    // -sendMessageToUser(String message,String nickname)
    //O forse di queste solo check la fa controller, il resto il server?
}
