package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonDiagonal;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.commons.CommonTwoSquares;
import it.polimi.ingsw.server.model.commons.CommonX;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
* Class to test the CommonTargetCard class
*/
public class CommonTargetCardTest extends TestCase {

/**
* Test the flow to get a random common target card
*/
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
    }

/**
* Test if the getName method return the expected name
*/
    @Test
    public void testGetName(){
        CommonTargetCard commonTargetCard=new CommonDiagonal(2);
        assertEquals("CommonDiagonal",commonTargetCard.getName());
    }

/**
* Test id the scoringTokenLkst gets set and returned as expected
*/
    @Test
    public void testGetAndSetScoringTokenList(){
        CommonTargetCard commonTargetCard=new CommonX(2);
        List<ScoringToken> scoringTokenList=new ArrayList<>();
        scoringTokenList.add(new ScoringToken(8));
        scoringTokenList.add(new ScoringToken(4));
        commonTargetCard.setScoringTokensList(scoringTokenList);
        assertEquals(scoringTokenList,commonTargetCard.getScoringTokensList());

    }
}