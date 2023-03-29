package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public void addPlayer(Player player, ArrayList<Player> playerList) {
        playerList.add(player);
    }

    @Override
    public void setupGame() {
        return;
    }
}
