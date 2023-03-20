package it.polimi.ingsw.model;

import java.util.List;

public abstract class GameState {

    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return 0;
    }

    public void addPlayer(Player player, List<Player> playerList) {

    }

    public void setupGame() {

    }
}
