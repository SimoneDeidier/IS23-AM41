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
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean onlyOneCommon) {
        //Does nothing in this state
    }
    @Override
    public boolean checkSavedGame(String player){
        //todo
        return false;
    }
    @Override
    public boolean isGameReady(){
        //Never will be called here
    }
}
