package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.commons.CommonTwoSquares;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

public class CommonTargetCardTest extends TestCase {

    public void testGetRandomCommon() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        CommonTargetCard card2players = new CommonTwoSquares(2);
        CommonTargetCard card3players = new CommonTwoSquares(3);
        CommonTargetCard card4players = new CommonTwoSquares(4);

        Player p1 = new Player("Player1");
        Player p2 = new Player("Player2");
        Player p3 = new Player("Player3");
        Player p4 = new Player("Player4");

        //test1
        card2players.assignToken(p1);
        assertEquals(p1.getScoringToken(0).getValue(), 8);
        card2players.assignToken(p1);
        //test 2
        card2players.assignToken(p2);
        assertEquals(p2.getScoringToken(0).getValue(), 4);
        //test 3
        card3players.assignToken(p3);
        card3players.assignToken(p1);
        assertEquals(p3.getScoringToken(0).getValue(), 8);
        assertEquals(p1.getScoringToken(1).getValue(), 6);
        card3players.assignToken(p4);
        assertEquals(p4.getScoringToken(0).getValue(), 4);
        //test 4
        card4players.assignToken(p4);
        assertEquals(p4.getScoringToken(1).getValue(), 8);
        card4players.assignToken(p2);
        assertEquals(p2.getScoringToken(1).getValue(), 6);
        card4players.assignToken(p3);
        assertEquals(p3.getScoringToken(1).getValue(), 4);




        //assertDoesNotThrow(InvocationTargetException.class,()->CommonTargetCard.getRandomCommon(2));
    }
}