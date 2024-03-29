package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TwoPlayersBoardTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board= new TwoPlayersBoard();
        board.refillBoard();
    }

/**
* Test if the board gets refilled correctly containing the expect number of items
*/
    @Test
    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(4,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<COLUMNS;i++){

            for(int j=0;j<ROWS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(29,count);
    }

/**
* Checks if the bitmask contains the expected number of elements
*/
    @Test
    public void testCreateBitMask() {
        //assert verifies the bitmask creates exactly 29 valid positions
        int count=0;
        for(int i=0;i<COLUMNS;i++){

            for(int j=0;j<ROWS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(29,count);
    }

/**
* Test if the correct exception is thrown in case of a null item picked
*/
    @Test
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));
    }

/**
* Test if the correct excepruon is thrown in case of an item picked from an invalid position
*/
    @Test
    public void testPickItemInInvalidPosition() {
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));
    }

}
