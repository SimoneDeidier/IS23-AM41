package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tokens.ScoringToken;
import org.junit.jupiter.api.Test;

public class ScoringTokenTest {

    @Test
    public void testIsTakeable() {
        ScoringToken token=new ScoringToken(5);
        assert(token.isTakeable());
    }

    @Test
    public void testIsNotTakeable() {
        ScoringToken token=new ScoringToken(5);
        token.setWhoItWasTakenBy("sam");
        assert(!token.isTakeable());
    }
}