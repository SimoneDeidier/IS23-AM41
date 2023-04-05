package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.CommonTargetCard;
import it.polimi.ingsw.model.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        playerList.add(player);
    }

    @Override
    public void setupGame(int maxPlayerNumber) {
    }

    public List<CommonTargetCard> generateRandomCommonCards(boolean firstMatch,int maxPlayerNumber) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<CommonTargetCard> list =new ArrayList<>();
        list.add(CommonTargetCard.getRandomCommon(maxPlayerNumber));
        if(firstMatch){
            return list;
        }
        CommonTargetCard common2= CommonTargetCard.getRandomCommon(maxPlayerNumber);
        while(list.get(0).getClass().equals(common2.getClass())){
            common2=CommonTargetCard.getRandomCommon(maxPlayerNumber);
        }
        list.add(common2);
        return list;
    }


}
