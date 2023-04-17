package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FourPlayersBoardTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board=FourPlayersBoard.getFourPlayersBoard();
        board.refillBoard();
    }

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

    @Test
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(0,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(0,4));
    }

    @Test
    public void testPickItemInInvalidPosition() throws InvalidBoardPositionException, NullItemPickedException { assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1)); }
}