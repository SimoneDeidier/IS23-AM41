package it.polimi.ingsw.model;

import java.util.List;

public class ServerInitState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return super.getAvailableSlot(maxPlayerNumber, listSize);
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        // come gestiamo l'inserimento del primo giocatore?
    }

    @Override
    public void setupGame() {
        // va implementata tutta la logica di setup del game (crea la board, le card ecc...)
    }
}
