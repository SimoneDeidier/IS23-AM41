package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private static GameController instance;
    private List<Player> playerList;
    private int maxPlayerNumber;
    private boolean lastTurn;
    private boolean firstGame;
    private Player activePlayer; //acts as a kind of turn
    private BoardFactory board;
    private GameState state;
    private List<CommonTargetCard> commonTargetCardsList; //?????

    public static GameController getGameController() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public boolean checkMove(Move move){
       if(!move.getPlayerNickname().equals(activePlayer.getNickname())) {
           return false;
       }
       return board.checkMove(move.getPositionsPicked());
    }

    // todo va messa a posto qui secondo me - eccezioni
    public boolean executeMove(Move move) throws EmptyItemListToInsert {
        if(checkMove(move)) {
            List<Item> items = getListItems(move);
            try {
                activePlayer.getShelf().insertItems(move.getColumn(), items);
            }
            catch (Exception NotEnoughSpaceInColumnException){
                System.out.println("Not enough space in the column provided!");
            }
            if (checkBoardNeedForRefill()) {
                board.refillBoard();
            }
            // todo usa in modo sbagliato il check
            if(checkLastTurn()){
                activePlayer.setEndGameToken(EndGameToken.getEndGameToken());
                lastTurn=true;
            }
            activePlayer.updateScore(activePlayer);

            return true;
        }
        return false;
    }

    public List<Item> getListItems(Move move){

        List<Item> items= new ArrayList<>();
        for(int[] picks : move.getPositionsPicked()){
            items.add(board.getBoardMatrixElement(picks[0],picks[1]));
        }
        return items;

    }

    public boolean checkBoardNeedForRefill() {

        for(int i=0;i<board.getBoardNumberOfRows();i++){
            for(int j=0;j<board.getBoardNumberOfColumns();j++){
                if(board.getBitMaskElement(i,j) && board.getBoardMatrixElement(i,j)!=null && !board.itemHasAllFreeSide(i,j)){
                    return false; //There is at least one non-null element that has at least one other item on one of its side
                }
            }
        }
        return true; //Needs to be refilled

    }
    public void changeState(GameState state) {
        this.state = state;
    }
    public int getAvailableSlot() {
        return state.getAvailableSlot(maxPlayerNumber, playerList.size());
    }
    public int handleNewPlayer(Player player){
        return state.handleNewPlayer(player,playerList);
    }
    public void addPlayer() {
        state.addPlayer(playerList.get(playerList.size()-1),board,commonTargetCardsList);
    }

    public void setupGame() {
        state.setupGame(maxPlayerNumber,commonTargetCardsList,board,firstGame);
    }

    public boolean checkLastTurn() {
        for(Player p : playerList) {
            if(p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }
}
