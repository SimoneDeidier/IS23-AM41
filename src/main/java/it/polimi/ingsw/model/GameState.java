package it.polimi.ingsw.model;

import java.util.ArrayList;

public abstract class GameState {

    public abstract int getAvailableSlot(int maxPlayerNumber, int listSize);

    public abstract void addPlayer(Player player, ArrayList<Player> playerList);

    public abstract void setupGame();
}
