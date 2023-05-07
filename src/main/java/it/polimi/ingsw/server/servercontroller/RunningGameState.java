package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class RunningGameState extends GameState {

    //Nessuna funzione è utile qui -> Lo stato è inutile??

    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        //Never will be called here
        return 0;
    }

    @Override
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean onlyOneCommon) {
        //Never will be called here
    }

    @Override
    public boolean checkSavedGame(String player){
        //Never will be called here
        return false;
    }
    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        //Never will be called here
        return false;
    }
}

