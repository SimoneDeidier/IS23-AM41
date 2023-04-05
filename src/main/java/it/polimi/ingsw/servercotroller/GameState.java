package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameState {

    public abstract int getAvailableSlot(int maxPlayerNumber, int listSize);

    public abstract void addPlayer(Player player, List<Player> playerList) throws URISyntaxException, IOException;

    public abstract void setupGame(int maxPlayerNumber);
}
