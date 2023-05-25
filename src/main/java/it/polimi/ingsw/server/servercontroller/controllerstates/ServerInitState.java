package it.polimi.ingsw.server.servercontroller.controllerstates;

import com.google.gson.Gson;
import it.polimi.ingsw.Save.Save;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.ThreePlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
        Gson gson=new Gson();
        try (FileReader reader = new FileReader("src/main/java/it/polimi/ingsw/save/OldGame.json")) {
            Save save = gson.fromJson(reader, Save.class);
            controller.setLastTurn(save.isLastTurn());
            controller.setMaxPlayerNumber(save.getMaxPlayerPlayer());
            if(save.getMaxPlayerPlayer()==2) {
                BoardFactory board2 = new TwoPlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            }
            else if(save.getMaxPlayerPlayer()==3) {
                BoardFactory board2 = new ThreePlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            }
            else if(save.getMaxPlayerPlayer()==4) {
                BoardFactory board2 = new FourPlayersBoard();
                board2.setBoardMatrix(save.getBoardItems());
                board2.setBitMask(save.getBoardBitMask());
                controller.setBoard(board2);
            }
            controller.setState(new RunningGameState());
            controller.setGameOver(save.isGameOver());
            List<CommonTargetCard> newCommonList=new ArrayList<>();
            for(String name:save.getCommonTargetCardMap().keySet()){
                if(name.equals("CommonDiagonal")){
                    CommonTargetCard commonTargetCard=new CommonDiagonal(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonEightSame")){
                    CommonTargetCard commonTargetCard=new CommonEightSame(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonFourCorners")){
                    CommonTargetCard commonTargetCard=new CommonFourCorners(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonFourGroupsOfFour")){
                    CommonTargetCard commonTargetCard=new CommonFourGroupsOfFour(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonFourRows")){
                    CommonTargetCard commonTargetCard=new CommonFourRows(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonSixGroupsOfTwo")){
                    CommonTargetCard commonTargetCard=new CommonSixGroupsOfTwo(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonStairway")){
                    CommonTargetCard commonTargetCard=new CommonStairway(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonThreeColumns")){
                    CommonTargetCard commonTargetCard=new CommonThreeColumns(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonTwoColumns")){
                    CommonTargetCard commonTargetCard=new CommonTwoColumns(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonTwoRows")){
                    CommonTargetCard commonTargetCard=new CommonTwoRows(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonTwoSquares")){
                    CommonTargetCard commonTargetCard=new CommonTwoSquares(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                else if(name.equals("CommonX")){
                    CommonTargetCard commonTargetCard=new CommonX(save.getMaxPlayerPlayer());
                    commonTargetCard.setScoringTokensList(save.getCommonTargetCardMap().get(name));
                    newCommonList.add(commonTargetCard);
                }
                controller.setCommonTargetCardsList(newCommonList);
                controller.setOnlyOneCommonCard(newCommonList.size()==1);
                List<Player> newPlayerList=new ArrayList<>();
                for(String nickname:save.getNicknameToShelfMap().keySet()){
                    Player player=new Player(nickname);
                    player.setConnected(false);
                    player.setPlayerScore(save.getNicknameToPointsMap().get(nickname));
                    player.setBoard(controller.getBoard());
                    if(nickname.equals(save.getEndGameTokenAssignedToWhom())) {
                        player.setEndGameToken(EndGameToken.getEndGameToken());
                    }
                    Shelf newShelf=new Shelf();;
                    newShelf.setShelfMatrix(save.getNicknameToShelfMap().get(nickname));
                    player.setShelf(newShelf);
                    player.setScoringTokenList(save.getNicknameToScoringTokensMap().get(nickname));
                    player.setCommonTargetCardList(newCommonList);
                    player.setPersonalTargetCard(save.getNicknameToPersonalTargetCard().get(nickname));
                    newPlayerList.add(player);
                }
                for(Player player:newPlayerList){
                    if(player.getNickname().equals(save.getActivePlayerNickname()))
                        controller.setActivePlayer(player);
                }
                if(save.getActivePlayerNickname()==null)
                    controller.setActivePlayer(null);
                controller.setPlayerList(newPlayerList);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
