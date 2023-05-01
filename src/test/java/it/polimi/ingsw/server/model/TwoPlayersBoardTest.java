package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TwoPlayersBoardTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board=TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
    }

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

    @Test
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));

    }

    @Test
    public void testPickItemInInvalidPosition() {
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));

    }

}
