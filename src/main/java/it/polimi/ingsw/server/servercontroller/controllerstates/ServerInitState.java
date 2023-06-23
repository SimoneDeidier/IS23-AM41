package it.polimi.ingsw.server.servercontroller.controllerstates;

import com.google.gson.Gson;
import it.polimi.ingsw.save.Save;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.ThreePlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * a state the controller is in before that any player joins the game. Leaves this state right after the first player correctly joins the macth
 */
public class ServerInitState implements GameState {

    /**
     * @param maxPlayerNumber the max number of players allowed in the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return -1, to let the controller know the game is in this state
     */
    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        return -1;
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
     * @param nickname is the nickname to be checked
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return 0, to let the controller know the game is in this state
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
     * starts creating the ArrayList for the CommmontargetCard list
     * @param isOnlyOneCommon is whether the game should have one or two CommonTargetCard (one if true, two otherwise)
     * @param maxPlayerNumber the max number of players allowed in the game
     * @return a new Arraylist
     */
    @Override
    public List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber) {
        return new ArrayList<>();
    }

    /**
     * handles the creation of the right Board based on the number of players allowed in the game
     * @param maxPlayerNumber the max number of players allowed in the game
     * @return
     */
    @Override
    public BoardFactory setupBoard(int maxPlayerNumber) {
        if(maxPlayerNumber==2)
            return new TwoPlayersBoard();
        else if(maxPlayerNumber==3)
            return new ThreePlayersBoard();
        return new FourPlayersBoard();

    }

    /**
     * checks whether the Board needs to be refilled, won't be called during this state
     * @param boardFactory the Board in the current game
     */
    @Override
    public void boardNeedsRefill(BoardFactory boardFactory) {
    }

    /**
     * In case the first player that connects to the game provides a nickname that belongs to a previous valid server save, this function
     * handles the recreation of all that is present in the file
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param commonTargetCardList the list of CommonTargetCard for the current game
     * @param board the Board of the current game
     * @param controller the reference to the GameController
     */
    @Override
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board, GameController controller) {
        Gson gson = new Gson();
        File file = new File(System.getProperty("user.dir"), "savings.json");
        try (FileReader reader = new FileReader(file)) {
            Save save = gson.fromJson(reader, Save.class);
            controller.setLastTurn(save.isLastTurn());
            controller.setMaxPlayerNumber(save.getMaxPlayerPlayer());
            if (save.getMaxPlayerPlayer() == 2) {
                BoardFactory board2 = new TwoPlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            } else if (save.getMaxPlayerPlayer() == 3) {
                BoardFactory board2 = new ThreePlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            } else if (save.getMaxPlayerPlayer() == 4) {
                BoardFactory board2 = new FourPlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            }
            controller.setState(new RunningGameState());
            controller.setGameOver(save.isGameOver());
            for (String name : save.getCommonTargetCardMap().keySet()) {
                switch (name) {
                    case "CommonDiagonal" -> {
                        CommonTargetCard commonTargetCard = new CommonDiagonal(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonEightSame" -> {
                        CommonTargetCard commonTargetCard = new CommonEightSame(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonFourCorners" -> {
                        CommonTargetCard commonTargetCard = new CommonFourCorners(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonFourGroupsOfFour" -> {
                        CommonTargetCard commonTargetCard = new CommonFourGroupsOfFour(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonFourRows" -> {
                        CommonTargetCard commonTargetCard = new CommonFourRows(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonSixGroupsOfTwo" -> {
                        CommonTargetCard commonTargetCard = new CommonSixGroupsOfTwo(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonStairway" -> {
                        CommonTargetCard commonTargetCard = new CommonStairway(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonThreeColumns" -> {
                        CommonTargetCard commonTargetCard = new CommonThreeColumns(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonTwoColumns" -> {
                        CommonTargetCard commonTargetCard = new CommonTwoColumns(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonTwoRows" -> {
                        CommonTargetCard commonTargetCard = new CommonTwoRows(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonTwoSquares" -> {
                        CommonTargetCard commonTargetCard = new CommonTwoSquares(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                    case "CommonX" -> {
                        CommonTargetCard commonTargetCard = new CommonX(save.getMaxPlayerPlayer());
                        commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                        controller.getCommonTargetCardsList().add(commonTargetCard);
                    }
                }
                controller.setOnlyOneCommonCard(controller.getCommonTargetCardsList().size() == 1);
                List<Player> newPlayerList = new ArrayList<>();
                for (String nickname : save.getNicknameToShelfMap().keySet()) {
                    Player player = new Player(nickname);
                    player.setConnected(false);
                    player.setPlayerScore(save.getNicknameToPointsMap().get(nickname));
                    player.setBoard(controller.getBoard());
                    if (nickname.equals(save.getEndGameTokenAssignedToWhom())) {
                        player.setEndGameToken(EndGameToken.getEndGameToken());
                    }
                    Shelf newShelf = new Shelf();
                    newShelf.setShelfMatrix(save.getNicknameToShelfMap().get(nickname));
                    player.setShelf(newShelf);
                    player.setScoringTokenList(save.getNicknameToScoringTokensMap().get(nickname));
                    player.setCommonTargetCardList(controller.getCommonTargetCardsList());
                    player.setPersonalTargetCard(save.getNicknameToPersonalTargetCard().get(nickname));
                    newPlayerList.add(player);
                }
                controller.setPlayerList(newPlayerList);
                for (Player player : controller.getPlayerList()) {
                    if (player.getNickname().equals(save.getActivePlayerNickname()))
                        controller.setActivePlayer(player);
                }
                System.err.println(save.getActivePlayerNickname());
            }


        }catch(IOException e) {
            System.out.println("There is no old game json");
        }
    }
}
