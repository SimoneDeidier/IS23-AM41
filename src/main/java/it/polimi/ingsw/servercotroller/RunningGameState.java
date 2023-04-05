package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.servercotroller.GameState;

import java.util.List;

public class RunningGameState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return 0;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        return;
    }

    @Override
    public void setupGame(int maxPlayerNumber) {
        return;
    }
}

