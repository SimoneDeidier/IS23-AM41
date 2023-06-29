package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tokens.ScoringToken;
import org.junit.jupiter.api.Test;

/**
* Test class for the scoring toke 
*/
public class ScoringTokenTest {

/**
* Checks if the isTakeable method is returning true on a takeable token
*/
    @Test
    public void testIsTakeable() {
        ScoringToken token=new ScoringToken(5);
        assert(token.isTakeable());
    }

/**
* Checks if the isTakeable method is returning false on a not takeable token
*/
    @Test
    public void testIsNotTakeable() {
        ScoringToken token=new ScoringToken(5);
        token.setWhoItWasTakenBy("sam");
        assert(!token.isTakeable());
    }
}