package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.Serializable;
import java.util.List;

public class RunningGameState implements GameState {

    //Nessuna funzione è utile qui -> Lo stato è inutile??

    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        //Never will be called here
        return 0;
    }

    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        //Never will be called here
        return false;
    }
    @Override
    public int checkNicknameAvailability(String nickname,List<Player> playerList){
        //Never will be called here
        return 0;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        //Never will be called here
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
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board, GameController controller) {

    }
}

