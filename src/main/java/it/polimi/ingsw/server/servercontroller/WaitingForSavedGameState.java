package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class WaitingForSavedGameState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public int handleNewPlayer(Player player, List<Player> playerList){
        //DA IMPLEMENTARE
        //Io l'avevo pensata come
        // chiamata a funzione check che il nome sia di quelli della partita salvata
        //e nel caso positivo playerList.add(player)
        //altrimenti diciamo al controller di dire a quel client che già che
        //una partita che sta re iniziando
        //(Dopo init state si può andare solo o in waiting for player o waitinf for saved
        return 0;
    }

    @Override
    public void addPlayer(Player player, BoardFactory board, List<CommonTargetCard> commonList) {
    }

    @Override
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean firstGame) {
        //This needs to "re-setup" the game as it is in the gson
    }
}
