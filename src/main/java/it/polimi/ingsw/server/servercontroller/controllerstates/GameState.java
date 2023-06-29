package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.util.List;

/**
 * Handles the function in a different way based on the state the game is in. There are four possible different states
 */
public interface GameState {

    /**
     * how many spots are still available in the current game, it may not have even been created yet
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return how many spots are still available
     */
    int getAvailableSlot(int maxPlayerNumber, List<Player> playerList);

    /**
     * checks whether the game should start right now or not
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return whether the game should start right now or not
     */
    boolean isGameReady(List<Player> playerList,int maxPlayerNumber);

    /**
     * whether a certain nickname is available for the current game
     * @param nickname is the nickname to be checked
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return whether a certain nickname is available for the current game
     */
    int checkNicknameAvailability(String nickname,List<Player> playerList);

    /**
     * handles the addition of a Player based on the GameState
     * @param player is the Player that needs to be added
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     */
    void addPlayer(Player player,List<Player> playerList);

    /**
     * handles the creation of a CommonTargetCard list for the match
     * @param isOnlyOneCommon is whether the game should have one or two CommonTargetCard (one if true, two otherwise)
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return the obtained commonList
     */
    List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber);

    /**
     * handles the creation of the board for the match
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return the Board for the game
     */
    BoardFactory setupBoard(int maxPlayerNumber);

    /**
     * check whether the Board needs to be refilled, if so it also deals with refill procedure
     * @param boardFactory the Board in the current game
     */
    void boardNeedsRefill(BoardFactory boardFactory);

    /**
     * handles the procedure to make sure that all players have access to all the objects it should have during the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param commonTargetCardList the list of CommonTargetCard for the current game
     * @param board the Board of the current game
     * @param controller the reference to the GameController
     */
    void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board,GameController controller);
}
