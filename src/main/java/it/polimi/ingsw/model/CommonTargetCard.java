package it.polimi.ingsw.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CommonTargetCard {


    protected final static int ROWS = 6;
    protected final static int COLUMNS = 5;
    private List<ScoringToken> scoringTokensList;
    public CommonTargetCard(int numberOfPlayers){
        ScoringToken token1=new ScoringToken(8);
        ScoringToken token2=new ScoringToken(6);
        ScoringToken token3=new ScoringToken(4);
        ScoringToken token4=new ScoringToken(2);
        scoringTokensList=new ArrayList<>();
        switch (numberOfPlayers){
            case 2:
                scoringTokensList.add(token1);
                scoringTokensList.add(token3);
            case 3:
                scoringTokensList.add(token1);
                scoringTokensList.add(token2);
                scoringTokensList.add(token3);
            case 4:
                scoringTokensList.add(token1);
                scoringTokensList.add(token2);
                scoringTokensList.add(token3);
                scoringTokensList.add(token4);

        }


    }
    public abstract boolean check(Shelf shelf);

    private static final List<Class<? extends CommonTargetCard>> subclasses = List.of(
            CommonDiagonal.class,
            CommonX.class,
            CommonThreeColumns.class,
            CommonTwoSquares.class,
            CommonFourCorners.class,
            CommonFourGroupsOfFour.class,
            CommonSixGroupsOfTwo.class,
            CommonTwoColumns.class,
            CommonEightSame.class,
            CommonFourRows.class,
            CommonStairway.class,
            CommonTwoRows.class
    );

    public static CommonTargetCard getRandomCommon(int numberOfPlayers) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Random random = new Random();
        Class<? extends CommonTargetCard> randomCommon = subclasses.get(random.nextInt(subclasses.size()));
        Constructor<? extends CommonTargetCard> constructor = randomCommon.getDeclaredConstructor();
        return constructor.newInstance(numberOfPlayers);
    }

    public ScoringToken assignToken(Player player){
        for(ScoringToken token: scoringTokensList){
            if(token.getOwner().equals(player)){
                return token;
            }
            if(token.isTakeable()){
                token.setOwner(player);
                return token;
            }
        }
        return null;

    }

}
