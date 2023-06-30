package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
* Class to test the board with four pkayers
*/
public class FourPlayersBoardTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board= new FourPlayersBoard();
        board.refillBoard();
    }

/**
* Checks if the board gets refilled with the correct amount of items 
*/
    @Test
    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<ROWS;i++){

            for(int j=0;j<COLUMNS;j++){

                if(board.getBitMaskElement(i,j) == true && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(45,count);
    }

/**
* Checks if the bitmask creates the exact amount of valid positions
*/
    @Test
    public void testCreateBitMask() {
        //assert verifies the bitmask creates exactly 45 valid positions
        int count=0;
        for(int i=0;i<COLUMNS;i++){

            for(int j=0;j<ROWS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(45,count);
    }

/**
* Checks if the correct exception gets thrown if the item picked is null
*/
    @Test
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(0,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(0,4));
    }

/**
* Checks if the correct exception gets thrown if the item picked is in an invalid position
*/
    @Test
    public void testPickItemInInvalidPosition() throws InvalidBoardPositionException, NullItemPickedException {
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));
    }
}