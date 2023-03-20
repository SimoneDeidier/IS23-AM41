package it.polimi.ingsw.model;

import java.util.List;

public class RunningGameState extends GameState{

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return -2;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        super.addPlayer(player, playerList);
    }

    @Override
    public void setupGame() {
        //Serve anche qui il setupGame per sistemare la board nel caso di tessere insufficienti?
    }
}

