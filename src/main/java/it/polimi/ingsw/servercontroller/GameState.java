package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.model.BoardFactory;
import it.polimi.ingsw.model.CommonTargetCard;
import it.polimi.ingsw.model.Player;

import java.util.List;

public abstract class GameState {

    public abstract int getAvailableSlot(int maxPlayerNumber, int listSize);

    public abstract int handleNewPlayer(Player player, List<Player> playerList);

    public abstract void addPlayer(Player player, BoardFactory board, List<CommonTargetCard> commonList);

    public abstract void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean firstGame);
}
