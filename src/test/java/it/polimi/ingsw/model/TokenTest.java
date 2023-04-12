package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TokenTest {

    @Test
    public void testIsTakeable() {
        Token token=new ScoringToken(5);
        assert(token.isTakeable());
    }

    @Test
    public void testIsNotTakeable() {
        Token token=new ScoringToken(5);
        token.setOwner(new Player("Sam"));
        assert(!token.isTakeable());
    }
}