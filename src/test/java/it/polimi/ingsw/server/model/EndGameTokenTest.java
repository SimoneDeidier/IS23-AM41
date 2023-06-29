package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tokens.EndGameToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
* Test class got the EndGameToken
*/
class EndGameTokenTest {

/**
* Checks if the endgametoken gets really resetted by the resetgametomen method
*/
    @Test
    void testResetEndGameToken() {
        EndGameToken endGameToken= EndGameToken.getEndGameToken();
        endGameToken.setTakenBy("Marco");
        assertFalse(endGameToken.isTakeable());
        endGameToken.resetEndGameToken();
        assertTrue(endGameToken.isTakeable());
    }

/**
* Checks if the GetTakenBy method return the correct name
*/
    @Test
    void testGetTakenBy(){
        EndGameToken endGameToken= EndGameToken.getEndGameToken();
        endGameToken.resetEndGameToken();
        endGameToken.setTakenBy("Marco");
        assertEquals("Marco",endGameToken.getTakenBy());

    }
}