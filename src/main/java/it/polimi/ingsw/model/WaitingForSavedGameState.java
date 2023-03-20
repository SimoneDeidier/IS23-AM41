package it.polimi.ingsw.model;

import java.util.List;

public class WaitingForSavedGameState extends GameState{

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        //DA IMPLEMENTARE
        //Io l'avevo pensata come
        // chiamata a funzione check che il nome sia di quelli della partita salvata
        //e nel caso positivo playerList.add(player)
        //altrimenti diciamo al controller di dire a quel client che già che
        //una partita che sta re iniziando
        //(Dopo init state si può andare solo o in waiting for player o waitinf for saved
    }

    @Override
    public void setupGame() {
        super.setupGame();
    }
}
