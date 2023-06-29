package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.Serializable;
import java.util.List;

/**
 * The states are almost entirely used to separate the setup phase of the game; when that phase is done, to signal the controller
 * the game has started, the controller enters this state
 */
public class RunningGameState implements GameState {

    /**
     * won't be called during this state
     * @param maxPlayerNumber the max number of players allowed in the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return 0, a symbolic value
     */
    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        return 0;
    }

    /**
     * won't be called during this state
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param maxPlayerNumber the max number of players allowed in the game
     * @return false, a symbolic value
     */
    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        return false;
    }

    /**
     * won't be called during this state
     * @param nickname is the nickname to be checked
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return 0, a symbolic value
     */
    @Override
    public int checkNicknameAvailability(String nickname,List<Player> playerList){
        return 0;
    }

    /**
     * won't be called during this state
     * @param player is the Player that needs to be added
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     */
    @Override
    public void addPlayer(Player player, List<Player> playerList) {
    }

    /**
     * won't be called during this state
     * @param isOnlyOneCommon is whether the game should have one or two CommonTargetCard (one if true, two otherwise)
     * @param maxPlayerNumber the max number of players allowed in the game
     * @return null, a symbolic value
     */
    @Override
    public List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber) {
        return null;
    }

    /**
     * won't be called during this state
     * @param maxPlayerNumber the max number of players allowed in the game
     * @return null, a symbolic value
     */
    @Override
    public BoardFactory setupBoard(int maxPlayerNumber) {
        return null;
    }

    /**
     * won't be called during this state
     * @param boardFactory the Board in the current game
     */
    @Override
    public void boardNeedsRefill(BoardFactory boardFactory) {

    }

    /**
     * won't be called during this state
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param commonTargetCardList the list of CommonTargetCard for the current game
     * @param board the Board of the current game
     * @param controller the reference to the GameController
     */
    @Override
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board, GameController controller) {

    }
}

