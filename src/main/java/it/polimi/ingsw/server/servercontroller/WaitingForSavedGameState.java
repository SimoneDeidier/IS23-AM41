package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class WaitingForSavedGameState extends GameState {

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
    public void setupGame(int maxPlayerNumber, List<CommonTargetCard> commonList, BoardFactory board, boolean onlyOneCommon, List<Player> playerList, GameController controller) {
        //This does nothing, the saved game is resumed in the initState
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
}
