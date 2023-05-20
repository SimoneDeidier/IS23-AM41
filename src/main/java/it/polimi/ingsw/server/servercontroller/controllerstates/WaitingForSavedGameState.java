package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.util.List;

public class WaitingForSavedGameState implements GameState {

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

    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        for (Player player:playerList){
            if(!player.isConnected()){
                return false;
            }
        }
        return true;
    }

    @Override
    public int checkNicknameAvailability(String nickname,List<Player> playerList){
        for(Player player:playerList){
            if(!player.isConnected() && player.getNickname().equals(nickname)){
                return 1;
            }
        }
        return -1;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        for(Player play:playerList){
            if(!play.isConnected() && play.getNickname().equals(player.getNickname())){
                play.setConnected(true);
            }
        }
    }

    @Override
    public List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber) {
        return null;
    }

    @Override
    public BoardFactory setupBoard(int maxPlayerNumber) {
        return null;
    }

    @Override
    public void boardNeedsRefill(BoardFactory boardFactory) {

    }

    @Override
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board) {

    }
}
