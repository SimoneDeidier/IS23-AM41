package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameController instance;
    private final List<Player> playerList;
    private int maxPlayerNumber;
    private boolean lastTurn;
    private Player activePlayer; //acts as a kind of turn
    private BoardFactory board;
    private GameState state;

    private GameController(List<Player> playerList) {
        this.playerList = playerList;
    }

    public GameController getGameController(List<Player> playerList) {
        if (instance == null) {
            instance = new GameController(playerList);
        }
        return instance;
    }

    public boolean checkMove(Move move){
        //Il primo check non è utile? Aveva detto qualcosa Margara,tipo che un player potrebbe fingersi con un username
        //Quindi qua il primo check dovrebbe essere sulla porta TCP,che quindi dovrebbe essere in Move
        //Però aspe, qua il controllo è proprio sull'oggetto PLayer, probabilmente in Move non dovrebbe poter stare perchè
        //il client non può passarlo(?)
       if(!move.getPlayer().equals(activePlayer)) {
           return false;
       }
       return board.checkMove(move.getPositionsPicked());
    }

    public boolean executeMove(Move move) throws EmptyItemListToInsert {
        if(checkMove(move)) {
            //actually executes the move
            List<Item> items = getListItems(move);
            try {
                activePlayer.getShelf().insertItems(move.getColumn(), items);
            }catch (Exception NotEnoughSpaceInColumnException){
                System.out.println("Not enough space in the column provided!");
            }
            if (checkBoardNeedForRefill()) {
                board.refillBoard();
            }
            if(checkLastTurn()){
                activePlayer.setEndGameToken(EndGameToken.getEndGameToken());
                lastTurn=true;
            }
            //Manca solo cosa che faccia assegnare i token delle common al player
            activePlayer.updateScore();

            return true;
        }
        return false;
    }

    public List<Item> getListItems(Move move){
        List<Item> items= new ArrayList<>();
        for(int[] picks :move.getPositionsPicked()){
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
    public void addPlayer(Player player) throws URISyntaxException, IOException {
        state.addPlayer(player, playerList);
    }

    public void setupGame(int maxPlayerNumber) {
        state.setupGame(maxPlayerNumber);
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
