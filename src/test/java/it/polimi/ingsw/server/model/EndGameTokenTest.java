package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameTokenTest {

    @Test
    void testResetEndGameToken() {
        EndGameToken endGameToken= EndGameToken.getEndGameToken();
        endGameToken.setOwner(new Player("Marco"));
        assertFalse(endGameToken.isTakeable());
        endGameToken.resetEndGameToken();
        assertTrue(endGameToken.isTakeable());
    }
}