package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;
import it.polimi.ingsw.server.servercontroller.exceptions.FirstPlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController controller= GameController.getGameController(new Server());

    @BeforeEach
    void initialize(){
        controller.prepareForNewGame();
    }

    @Test
    void checkMove() {
    }

    @Test
    void getNewView() {
    }

    @Test
    void nextIndexCalc() {
    }

    @Test
    void checkLastTurn() {
    }

    @Test
    void checkSavedGame() {
    }

    @Test
    void presentationTestForFirstPlayer() {
        //assertThrows(FirstPlayerException.class,()->controller.presentation("Samuele"));
    }
}