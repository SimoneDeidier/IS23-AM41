package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.util.List;

/**
 * after ServerInitState, the controller enters this state if the first player's nickname is found in the saved game
 */
public class WaitingForSavedGameState implements GameState {

    /**
     * checks the connected attribute of players, to check how many of them are still missing before restarting the game
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return how many players are still missing before restarting the game
     */
    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        int count=0;
        for(Player player:playerList){
            if(!player.isConnected()){
                count++;
            }
        }
        return count;
    }

    /**
     * checks whether the game should start right now or not
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return whether the game should start right now or not
     */
    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        for (Player player:playerList){
            if(!player.isConnected()){
                return false;
            }
        }
        return true;
    }

    /**
     * checks whether a nickname is available
     * @param nickname is the nickname to be checked
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return -1 whether the nickname is not available, 1 otherwise
     */
    @Override
    public int checkNicknameAvailability(String nickname,List<Player> playerList){
        for(Player player:playerList){
            if(!player.isConnected() && player.getNickname().equals(nickname)){
                return 1;
            }
        }
        return -1;
    }

    /**
     * sets for the latest connected player the attribute connected to true
     * @param player is the Player that needs to be added
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     */
    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        for(Player play:playerList){
            if(!play.isConnected() && play.getNickname().equals(player.getNickname())){
                play.setConnected(true);
            }
        }
    }

    /**
     * does nothing in this state
     * @param isOnlyOneCommon is whether the game should have one or two CommonTargetCard (one if true, two otherwise)
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return null
     */
    @Override
    public List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber) {
        return null;
    }

    /**
     * does nothing in this state
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return null
     */
    @Override
    public BoardFactory setupBoard(int maxPlayerNumber) {
        return null;
    }

    /**
     * does nothing in this state
     * @param boardFactory the Board in the current game
     */
    @Override
    public void boardNeedsRefill(BoardFactory boardFactory) {
    }

    /**
     * does nothing in this state
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param commonTargetCardList the list of CommonTargetCard for the current game
     * @param board the Board of the current game
     * @param controller the reference to the GameController
     */
    @Override
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board, GameController controller) {

    }
}
