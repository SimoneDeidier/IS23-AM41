package it.polimi.ingsw.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CommonTargetCard {


    protected final static int ROWS = 6;
    protected final static int COLUMNS = 5;

    protected final static int COLORS = 6;

    protected List<ScoringToken> scoringTokensList;

    private static final List<Class<? extends CommonTargetCard>> SUBCLASSES = List.of(
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


    public CommonTargetCard(int numberOfPlayers){

        scoringTokensList = new ArrayList<>(numberOfPlayers);
        scoringTokensList.add(new ScoringToken(8));
        scoringTokensList.add(new ScoringToken(4));
        switch (numberOfPlayers) {
            case 3 -> scoringTokensList.add(new ScoringToken(6));
            case 4 -> {
                scoringTokensList.add(new ScoringToken(6));
                scoringTokensList.add(new ScoringToken(2));
            }
        }

    }


    public abstract boolean check(Shelf shelf);

    public static CommonTargetCard getRandomCommon(int numberOfPlayers) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Random random = new Random();
        Class<? extends CommonTargetCard> randomCommon = SUBCLASSES.get(random.nextInt(SUBCLASSES.size()));
        Constructor<? extends CommonTargetCard> constructor = randomCommon.getDeclaredConstructor();
        return constructor.newInstance(numberOfPlayers);
    }



}
