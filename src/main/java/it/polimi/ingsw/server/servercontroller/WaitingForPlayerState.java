package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingForPlayerState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return maxPlayerNumber - listSize;
    }

    private PersonalTargetCard getRandomPersonal() {
        //Can't do this without the constructor in Personal Target Card
        return new PersonalTargetCard();
    }

    @Override
    public void setupGame(int maxPlayerNumber,List<CommonTargetCard> commonList,BoardFactory board,boolean onlyOneCommonCard) {
        commonList = generateRandomCommonCards(onlyOneCommonCard,maxPlayerNumber);
        switch (maxPlayerNumber){
            case 2:
                board = TwoPlayersBoard.getTwoPlayersBoard();
            case 3:
                board= ThreePlayersBoard.getThreePlayersBoard();
            default:
                board= FourPlayersBoard.getFourPlayersBoard();
        }

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
        switch (random.nextInt(11)) {
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
            default -> {
                return new CommonX(maxPlayerNumber);
            }
        }
    }

    @Override
    public boolean checkSavedGame(String player){
        //todo
        return false;
    }

    @Override
    public boolean isGameReady(){
        //todo
        return false;
    }


}
