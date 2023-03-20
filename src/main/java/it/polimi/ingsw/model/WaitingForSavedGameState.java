package it.polimi.ingsw.model;

import java.util.List;

public class WaitingForSavedGameState extends GameState{

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return super.getAvailableSlot(maxPlayerNumber, listSize);
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        super.addPlayer(player, playerList);
    }

    @Override
    public void setupGame() {
        super.setupGame();
    }
}
