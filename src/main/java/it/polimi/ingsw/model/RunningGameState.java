package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RunningGameState extends GameState{

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return 0;
    }

    @Override
    public void addPlayer(Player player, ArrayList<Player> playerList) {
        return;
    }

    @Override
    public void setupGame() {
        return;
    }
}

