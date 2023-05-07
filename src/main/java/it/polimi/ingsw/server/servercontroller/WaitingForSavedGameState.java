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
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean onlyOneCommon) {
        //This needs to "re-setup" the game as it is in the gson
    }

    @Override
    public boolean checkSavedGame(String player){
        //todo
        return false;
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
}
