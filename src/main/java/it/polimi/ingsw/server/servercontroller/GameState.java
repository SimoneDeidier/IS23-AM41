package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public abstract class GameState {

    public abstract int getAvailableSlot(int maxPlayerNumber, List<Player> playerList);

    public abstract void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean firstGame);
    public abstract boolean checkSavedGame(String player);
    public abstract boolean isGameReady(List<Player> playerList,int maxPlayerNumber);
}
