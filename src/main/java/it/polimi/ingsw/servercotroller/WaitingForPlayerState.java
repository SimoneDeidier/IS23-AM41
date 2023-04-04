package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.servercotroller.GameState;

import java.util.List;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        playerList.add(player);
    }

    @Override
    public void setupGame() {
        return;
    }
}
