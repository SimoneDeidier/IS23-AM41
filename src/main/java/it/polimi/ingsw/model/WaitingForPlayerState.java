package it.polimi.ingsw.model;

import java.util.List;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        //Non usiamo questa classe per controllare tutto il processo di aggiunta?
        // E quindi servirebbe un if(getAvailabelSlots >0) qui per esempio
        playerList.add(player);
    }

    @Override
    public void setupGame() {
        super.setupGame();
    }
}
