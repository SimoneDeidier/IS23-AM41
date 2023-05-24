package it.polimi.ingsw.server.servercontroller.controllerstates;

import com.google.gson.Gson;
import it.polimi.ingsw.Save.Save;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ServerInitState implements GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        return -1;
    }
    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        //Never will be called here
        return false;
    }

    @Override
    public int checkNicknameAvailability(String nickname,List<Player> playerList){
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
        //todo QUESTA E' DA TESTARE
        Gson gson=new Gson();
        try (FileReader reader = new FileReader("src/main/java/it/polimi/ingsw/Save/OldGame.json")) {
            Save save = gson.fromJson(reader, Save.class);
            for(Player player: save.getPlayerList()){
                player.setConnected(false);
            }
            controller.setPlayerList(save.getPlayerList());
            controller.setMaxPlayerNumber(save.getPlayerList().size());
            controller.setLastTurn(save.isLastTurn());
            controller.setOnlyOneCommonCard(save.getCommonTargetCardList().size()==1);
            controller.setActivePlayer(save.getActivePlayer());
            controller.setBoard(save.getBoard());
            controller.setState(save.getState());
            controller.setCommonTargetCardsList(save.getCommonTargetCardList());
            controller.setGameOver(save.isGameOver());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
