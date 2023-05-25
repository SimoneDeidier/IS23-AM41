package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.util.List;

public interface GameState {

    int getAvailableSlot(int maxPlayerNumber, List<Player> playerList);
    boolean isGameReady(List<Player> playerList,int maxPlayerNumber);
    int checkNicknameAvailability(String nickname,List<Player> playerList);
    void addPlayer(Player player,List<Player> playerList);
    List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber);
    BoardFactory setupBoard(int maxPlayerNumber);
    void boardNeedsRefill(BoardFactory boardFactory);
    void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board,GameController controller);
}
