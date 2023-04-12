package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public void testPickNullItem() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));

    }

    @Test
    public void testPickItemInInvalidPosition() {
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));

    }
    @Test
    public void testHasFreeSideForFalse() { assertFalse(board.hasFreeSide(4,4)); }
    @Test
    public void testHasFreeSideForTrue()  {  assertTrue(board.hasFreeSide(3,6));  }
    @Test
    public void testHasFreeSideAfterOnePick() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,6);
        assertTrue(board.hasFreeSide(5,5));
    }
    @Test
    public void testCheckInLineHorizontally() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{3,4});
        list.add(new int[]{3,5});
        assertTrue(board.checkInLine(list));
    }

    @Test
    public void testCheckInLineVertically() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,3});
        list.add(new int[]{5,3});
        assertTrue(board.checkInLine(list));
    }
    @Test
    public void testCheckInLineFalse() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,4});
        list.add(new int[]{5,3});
        assertFalse(board.checkInLine(list));
    }

    @Test
    public void testCheckInLineOneElement() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        assertTrue(board.checkInLine(list));
    }

    @Test
    public void testCheckInLineTwoElement() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,4});
        assertFalse(board.checkInLine(list));
    }
}