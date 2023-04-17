package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class ServerInitState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return -1;
    }

    @Override
    public int handleNewPlayer(Player player, List<Player> playerList){
        // come gestiamo l'inserimento del primo giocatore?
        //Serve un check che dica se passare in waitingForPlayer nel caso in cui
        //il nickname del giocatore iniziale che sta provando a collegarsi sia diverso
        // da quelli salvati nel json, ed invece che vada in waiting forSaved nel caso contrario
        //Cambia molto perchè per esempio se dobbiamo andare in waitingForSaved non
        //serve chiedere per quanti giocatori vuole la partita
        //if(player== player nel json)
        // -- do something
        // else
        // -- do other thing
        return 0;
    }

    @Override
    public void addPlayer(Player player, BoardFactory board, List<CommonTargetCard> commonList) {

    }

    @Override
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean firstGame) {
        //Does nothing in this state
    }
}
