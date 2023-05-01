package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardFactoryTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private static final int ZERO = 0;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board=TwoPlayersBoard.getTwoPlayersBoard(); //Could have been three or four, no changes for the tests
        board.refillBoard();
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
    public void testHasAllFreeSide() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(1,3);
        board.pickItem(2,4);
        board.pickItem(3,3);
        assertTrue(board.itemHasAllFreeSide(2,3));
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

    @Test
    public void testGetBitMaskElement() {
        assert(board.getBitMaskElement(4,4));
    }

    @Test
    public void testGetBitMaskElementForFalse() {
        assert(!board.getBitMaskElement(4,0));
    }

    @Test
    public void testGetBoardMatrixElement(){
        board.setBoardMatrixElement(new Item(ItemColor.GREEN),3,3);
        assertEquals(ItemColor.GREEN,board.getBoardMatrixElement(3,3).getColor());
    }

    @Test
    public void testSetBoardMatrixElement(){
        board.setBoardMatrixElement(new Item(ItemColor.LIGHT_BLUE),6,4);
        assertEquals(ItemColor.LIGHT_BLUE,board.getBoardMatrixElement(6,4).getColor());
    }

    @Test
    public void testGetBoardNumberOfRows(){
        assertEquals(ROWS,board.getBoardNumberOfRows());
    }

    @Test
    public void testGetBoardNumberOfColumns(){
        assertEquals(COLUMNS,board.getBoardNumberOfColumns());
    }

    @Test
    public void testCheckMoveForTrue(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{1,3});
        itemsPicked.add(new int[]{1,4});
        assert (board.checkMove(itemsPicked));
    }

    @Test
    public void testCheckMoveForFalseForFreeSide(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        assert (!board.checkMove(itemsPicked));
    }

    @Test
    public void testCheckMoveForTooManyItems(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        assert (!board.checkMove(itemsPicked));
    }

    @Test
    public void testCheckMoveForFalseForNotInLine(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{6,3});
        assert (!board.checkMove(itemsPicked));
    }

    @Test
    public void testCheckMoveForFalseForNotNearby(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,7});
        assert (!board.checkMove(itemsPicked));
    }

    @Test
    public void testResetBoard(){
        board.resetBoard();
        board.refillBoard();
        board.resetBoard();
        int count=0;
        for(int i=0;i<COLUMNS;i++){

            for(int j=0;j<ROWS;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(ZERO,count);
    }

}