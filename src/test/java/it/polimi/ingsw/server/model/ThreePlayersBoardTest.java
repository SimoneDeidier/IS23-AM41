package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.ThreePlayersBoard;
import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ThreePlayersBoardTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board= new ThreePlayersBoard();
        board.refillBoard();
    }

/**
* Checks if the board is refilled correctly with the expected amount of numbers
*/
    @Test
    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<ROWS;i++){

            for(int j=0;j<COLUMNS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(37,count);
    }

/**
* Checks if the bitmask is created with the correct number of available spots
*/
    @Test
    public void testCreateBitMask() {
        //assert verifies the bitmask creates exactly 37 valid positions
        int count=0;
        for(int i=0;i<COLUMNS;i++){

            for(int j=0;j<ROWS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(37,count);
    }

/**
* Checks if the correct exception is thrown in case of a null item picked
*/
    @Test
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(0,3);
        assertThrows(NullItemPickedException.class,()->board.pickItem(0,3));
    }

/**
* Checks if the correct exception is thrown if an item is picked from an invalid position
*/
    @Test
    public void testPickItemInInvalidPosition() throws InvalidBoardPositionException, NullItemPickedException {
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));
    }
}