package it.polimi.ingsw.model;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

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
        p1.addScoringToken(card2players.assignToken(p1));
        assertEquals(p1.getScoringToken(0).getValue(), 8);
        p1.addScoringToken(card2players.assignToken(p1));
        //test 2
        p2.addScoringToken(card2players.assignToken(p2));
        assertEquals(p2.getScoringToken(0).getValue(), 4);
        //test 3
        p3.addScoringToken(card3players.assignToken(p3));
        p1.addScoringToken(card3players.assignToken(p1));
        assertEquals(p3.getScoringToken(0).getValue(), 8);
        assertEquals(p1.getScoringToken(1).getValue(), 6);
        p4.addScoringToken(card3players.assignToken(p4));
        assertEquals(p4.getScoringToken(0).getValue(), 4);
        //test 4
        p4.addScoringToken(card4players.assignToken(p4));
        assertEquals(p4.getScoringToken(1).getValue(), 8);
        p2.addScoringToken(card4players.assignToken(p2));
        assertEquals(p2.getScoringToken(1).getValue(), 6);
        p3.addScoringToken(card4players.assignToken(p3));
        assertEquals(p3.getScoringToken(1).getValue(), 4);




        //assertDoesNotThrow(InvocationTargetException.class,()->CommonTargetCard.getRandomCommon(2));
    }
}