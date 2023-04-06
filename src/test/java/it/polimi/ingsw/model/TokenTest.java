package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import junit.framework.Assert;
import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TokenTest extends TestCase {

    @Test
    public void testIsTakeable() {
        Token token=new ScoringToken(5);
        Player player=new Player("Samuele");
        assert (token.isTakeable());
        token.setOwner(player);
        assert(!token.isTakeable());



    }
}