package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

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

    public static GameController getGameController() {
        if (instance == null) {
            instance = new GameController();
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
        state.addPlayer(player,playerList);
    }

    public void setupGame() { //ok supporto a isGameReady
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

    public int checkNicknameAvailability(String nickname) {
        if(state.checkNicknameAvailability(nickname,playerList)==1)
            return 1;
        else if(state.checkNicknameAvailability(nickname,playerList)==-1)
            return -1;
        return 0;
    }

    public boolean isGameReady(){
        return state.isGameReady(playerList,maxPlayerNumber);
    }

    public void changePlayerConnectionStatus(Player player){
        player.setConnected(!player.isConnected());
    }

    public boolean checkGameParameters(int maxPlayerNumber,boolean onlyOneCommonCard){ //response to create lobby
        if( maxPlayerNumber <= 4 && maxPlayerNumber >= 2) {
            setupLobbyParameters(maxPlayerNumber, onlyOneCommonCard);
            return true;
        }
        return false;
    }

    public void setupLobbyParameters(int maxPlayerNumber,boolean onlyOneCommonCard){
            this.maxPlayerNumber = maxPlayerNumber;
            this.onlyOneCommonCard = onlyOneCommonCard;
    }

    public boolean checkSavedGame(String nickname){
        //todo look into json and look if the nickname is present there
        return true;
    }

    public boolean clientConnection(){ //response to helo from client
        return getAvailableSlot() == -1 || getAvailableSlot() > 0;
    } //todo in RMI

    public boolean clientPresentation(String nickname) throws FirstPlayerException, CancelGameException { //response to presentation
        if(getAvailableSlot()==-1){ //handling the first player
            if(checkSavedGame(nickname)){
                changeState(new WaitingForSavedGameState());
                setupGame();
                for(Player player:playerList){
                    if(player.getNickname().equals(nickname))
                        player.setConnected(true);
                    return true;
                }
            }
            else {
                changeState(new WaitingForPlayerState());
                addPlayer(new Player(nickname));
                throw new FirstPlayerException();
            }
        }
        //for all the other players
        if(checkNicknameAvailability(nickname)==1) {
            addPlayer(new Player(nickname));
            return true;
        }
        else if (checkNicknameAvailability(nickname) == -1)
                throw new CancelGameException();
            return false;

        }
    }   //todo in RMI

    //mancherebbero
    // -sendMessageToAll(String message)
    // -checkNicknameForMessage(String message,String nickname)
    // -sendMessageToUser(String message,String nickname)
    //O forse di queste solo check la fa controller, il resto il server?
