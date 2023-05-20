package it.polimi.ingsw.server.servercontroller.controllerstates;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ServerInitState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        return -1;
    }

    @Override
    public void setupGame(int maxPlayerNumber, List<CommonTargetCard> commonList, BoardFactory board, boolean onlyOneCommon, List<Player> playerList, GameController controller) {
        //todo testing
        //Reload saved game
        Gson gson = new Gson();

        try {
            controller = gson.fromJson(new FileReader("src/main/resources/json/OldGame.json"), GameController.class);
            for(Player player : controller.getPlayerList()){
                player.setConnected(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo modifying other parameters???
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
}
