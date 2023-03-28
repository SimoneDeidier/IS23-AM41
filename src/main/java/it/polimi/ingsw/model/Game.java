package it.polimi.ingsw.model;

import java.util.List;

public class Game {

    private Game instance = null;
    private int maxPlayerNumber;
    private boolean lastTurn;
    private GameState state;
    private List<Player> playerList;

    private Game(GameState state) {
        this.state = state;
    }

    public Game getGame() {
        if(instance == null) {
            instance = new Game(new ServerInitState());
        }
        return instance;
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

    public void setupGame(ItemsBag bag) {
        state.setupGame(bag);
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
