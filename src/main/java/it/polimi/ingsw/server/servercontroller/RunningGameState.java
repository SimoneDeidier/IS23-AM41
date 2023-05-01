package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class RunningGameState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return 0;
    }
    @Override
    public int handleNewPlayer(Player player, List<Player> playerList){return 0;}

    @Override
    public void addPlayer(Player player, BoardFactory board, List<CommonTargetCard> commonList) {
        return;
    }

    @Override
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean onlyOneCommon) {
        //Does nothing in this state
    }
}

