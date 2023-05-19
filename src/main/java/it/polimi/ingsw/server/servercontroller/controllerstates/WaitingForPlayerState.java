package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.ThreePlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        if(maxPlayerNumber == 0) {
            return -2;
        }
        else return maxPlayerNumber - playerList.size();
    }

    @Override
    public void setupGame(int maxPlayerNumber, List<CommonTargetCard> commonList, BoardFactory board, boolean onlyOneCommonCard, List<Player> playerList, GameController controller) {

        commonList = generateRandomCommonCards(onlyOneCommonCard,maxPlayerNumber);

        switch (maxPlayerNumber) {
            case 2 -> board = TwoPlayersBoard.getTwoPlayersBoard();
            case 3 -> board = ThreePlayersBoard.getThreePlayersBoard();
            case 4 -> board = FourPlayersBoard.getFourPlayersBoard();
        }

        for(Player player: playerList){
            player.setBoard(board);
            player.setShelf(new Shelf());
            player.setCommonTargetCardList(commonList);
            try {
                player.setPersonalTargetCard(generateRandomPersonal(playerList));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        //We randomize the list of players
        Collections.shuffle(playerList);

    }

    public PersonalTargetCard generateRandomPersonal(List<Player> playerList) throws IOException, URISyntaxException {
        Random random = new Random();
        int personalCode= (random.nextInt(12));
        boolean isAlreadyPresent = false;
        for(Player playerInFor: playerList){
            if(playerInFor.getPersonalTargetCard()!=null && playerInFor.getPersonalTargetCard().getPersonalNumber() == personalCode) {
                isAlreadyPresent = true;
                break;
            }
        }
        while(isAlreadyPresent) {
            isAlreadyPresent = false;
            personalCode = (random.nextInt(12));
            for (Player playerInFor : playerList) {
                if (playerInFor.getPersonalTargetCard() != null && playerInFor.getPersonalTargetCard().getPersonalNumber() == personalCode) {
                    isAlreadyPresent = true;
                    break;
                }
            }
        }
        return new PersonalTargetCard( personalCode);
    }

    public List<CommonTargetCard> generateRandomCommonCards(boolean onlyOneCommonCard,int maxPlayerNumber) {
        List<CommonTargetCard> list =new ArrayList<>();
        list.add(getRandomCommon(maxPlayerNumber));
        if(onlyOneCommonCard){
            return list;
        }
        CommonTargetCard common2= getRandomCommon(maxPlayerNumber);
        while(list.get(0).getClass().equals(common2.getClass())){
            common2=getRandomCommon(maxPlayerNumber);
        }
        list.add(common2);
        return list;
    }

    public CommonTargetCard getRandomCommon(int maxPlayerNumber){
        Random random=new Random();
        switch (random.nextInt(12)) {
            case 0 -> {
                return new CommonDiagonal(maxPlayerNumber);
            }
            case 1 -> {
                return new CommonEightSame(maxPlayerNumber);
            }
            case 2 -> {
                return new CommonFourCorners(maxPlayerNumber);
            }
            case 3 -> {
                return new CommonFourGroupsOfFour(maxPlayerNumber);
            }
            case 4 -> {
                return new CommonFourRows(maxPlayerNumber);
            }
            case 5 -> {
                return new CommonSixGroupsOfTwo(maxPlayerNumber);
            }
            case 6 -> {
                return new CommonStairway(maxPlayerNumber);
            }
            case 7 -> {
                return new CommonThreeColumns(maxPlayerNumber);
            }
            case 8 -> {
                return new CommonTwoColumns(maxPlayerNumber);
            }
            case 9 -> {
                return new CommonTwoRows(maxPlayerNumber);
            }
            case 10 -> {
                return new CommonTwoSquares(maxPlayerNumber);
            }
            default -> { //case: 11
                return new CommonX(maxPlayerNumber);
            }
        }
    }

    @Override
    public int checkNicknameAvailability(String nickname, List<Player> playerList){
        for(Player player:playerList){
            if(player.getNickname().equals(nickname)){
                return 0;
            }
        }
        return 1;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        playerList.add(player);
    }

    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        return playerList.size()==maxPlayerNumber;
    }


}
