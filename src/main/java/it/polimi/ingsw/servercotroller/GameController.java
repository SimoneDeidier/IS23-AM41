package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.BoardFactory;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TwoPlayersBoard;

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
       if(move.getPlayer().equals(activePlayer)) {
            for(int[] choices: move.positionsPicked){
                if(maxPlayerNumber==2){
                    board = TwoPlayersBoard.getTwoPlayersBoard();
                }
            }
       }
        return true;
    }

    public boolean executeMove(Move move){
        checkMove(move);
        return true;
    }



    public void checkBoard() {

    }
    public void changeState(GameState state) {
        this.state = state;
    }
    public int getAvailableSlot() {
        return state.getAvailableSlot(maxPlayerNumber, playerList.size());
    }
    public void addPlayer(Player player) {
        state.addPlayer(player, playerList);
    }

    public void setupGame() {
        state.setupGame();
    }

    public boolean checkLastTurn() {
        for(Player p : playerList) {
            if(p.getShelf().isFull()) {
                return true;
            }
        }
        return false;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }
}
