package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
* Class to test the BoardFactory class
*/
class BoardFactoryTest {

    private static final int COLUMNS = 9;
    private static final int ROWS = 9;
    private static final int ZERO = 0;
    private BoardFactory board;

    @BeforeEach
    void initialize(){
        board= new TwoPlayersBoard(); //Could have been three or four, no changes for the tests
        board.refillBoard();
    }

/**
* Test if the hasFreeSide returns false with input 4,4
*/
    @Test
    public void testHasFreeSideForFalse() { assertFalse(board.hasFreeSide(4,4)); }
/**
* Test if the HasFreeSide method returns true with input 3,6
*/
    @Test
    public void testHasFreeSideForTrue()  {  assertTrue(board.hasFreeSide(3,6));  }

/**
* Test if the board has free sides after one pick
*/
    @Test
    public void testHasFreeSideAfterOnePick() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(5,6);
        assertTrue(board.hasFreeSide(5,5));
    }

/**
* Test if the board has free sides on all four sides
*/
    @Test
    public void testHasAllFreeSide() throws InvalidBoardPositionException, NullItemPickedException {
        board.pickItem(1,3);
        board.pickItem(2,4);
        board.pickItem(3,3);
        assertTrue(board.itemHasAllFreeSide(2,3));
    }

/**
* Test the method checkInLine in the horizontal setting
*/
    @Test
    public void testCheckInLineHorizontally() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{3,4});
        list.add(new int[]{3,5});
        assertTrue(board.checkInLine(list));
    }

/**
* Test rhe method checkInLine in the vertical setting
*/
    @Test
    public void testCheckInLineVertically() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,3});
        list.add(new int[]{5,3});
        assertTrue(board.checkInLine(list));
    }

/**
* Checks chrvkInLinr in a false setting
*/
    @Test
    public void testCheckInLineFalse() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,4});
        list.add(new int[]{5,3});
        assertFalse(board.checkInLine(list));
    }

/**
* Test checkInLine in case of one element
*/
    @Test
    public void testCheckInLineOneElement() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        assertTrue(board.checkInLine(list));
    }

/**
* Test checkInLine in case of two elements
*/
    @Test
    public void testCheckInLineTwoElement() {
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{4,4});
        assertFalse(board.checkInLine(list));
    }

/**
* Test if getBitMaskElement return true on an available element
*/
    @Test
    public void testGetBitMaskElement() {
        assert(board.getBitMaskElement(4,4));
    }

    /**
     * Test if getBitMaskElement return true on an unavailable element
     */
    @Test
    public void testGetBitMaskElementForFalse() {
        assert(!board.getBitMaskElement(4,0));
    }

    /**
     * Test if getBoardMatrixElement return the expected item
     */
    @Test
    public void testGetBoardMatrixElement(){
        board.setBoardMatrixElement(new Item(ItemColor.GREEN),3,3);
        assertEquals(ItemColor.GREEN,board.getBoardMatrixElement(3,3).getColor());
    }

    /**
     * Another test to check if getBitMaskElement return true on an available element
     */
    @Test
    public void testSetBoardMatrixElement(){
        board.setBoardMatrixElement(new Item(ItemColor.LIGHT_BLUE),6,4);
        assertEquals(ItemColor.LIGHT_BLUE,board.getBoardMatrixElement(6,4).getColor());
    }

    /**
     * Test if getBoardNumberOfRows return the correct amount of rows
     */
    @Test
    public void testGetBoardNumberOfRows(){
        assertEquals(ROWS,board.getBoardNumberOfRows());
    }

    /**
     * Test if getBoardNumberOfRows return the correct amount of columns
     */
    @Test
    public void testGetBoardNumberOfColumns(){
        assertEquals(COLUMNS,board.getBoardNumberOfColumns());
    }

    /**
     * Test if checkMove return true on a correct move
     */
    @Test
    public void testCheckMoveForTrue(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{1,3});
        itemsPicked.add(new int[]{1,4});
        assert (board.checkMove(itemsPicked));
    }

    /**
     * Test if checkMove return false on a incorrect move
     */
    @Test
    public void testCheckMoveForFalseForFreeSide(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        assert (!board.checkMove(itemsPicked));
    }

    /**
     * Test if checkMove return false on a incorrect move with too many items
     */
    @Test
    public void testCheckMoveForTooManyItems(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,2});
        assert (!board.checkMove(itemsPicked));
    }

    /**
     * Test if checkMove return false on a incorrect move with items not in line
     */
    @Test
    public void testCheckMoveForFalseForNotInLine(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{6,3});
        assert (!board.checkMove(itemsPicked));
    }

    /**
     * Test if checkMove return false on a incorrect move with items not nearby
     */
    @Test
    public void testCheckMoveForFalseForNotNearby(){
        List<int[]> itemsPicked= new ArrayList<>();
        itemsPicked.add(new int[]{4,1});
        itemsPicked.add(new int[]{4,7});
        assert (!board.checkMove(itemsPicked));
    }

    /**
     * Test if the method getBoardMatrixElement return the expected element
     */
    @Test
    public void testGetBoardMatrix(){
        board.setBoardMatrixElement(new Item(ItemColor.GREEN),1,2);
        assertEquals(board.getBoardMatrix()[1][2],board.getBoardMatrixElement(1,2));
    }

    /**
     * Test if the method getBitmaskElement return the expected element
     */
    @Test
    public void testGetBitmask(){
        assertEquals(board.getBitMask()[1][2],board.getBitMaskElement(1,2));
    }

    /**
     * Test if the method setBitmask set the bitmask correctly
     */
    @Test
    public void testSetBitMask(){
        boolean[][] bitMask = new boolean[ROWS][COLUMNS];
        board.setBitMask(bitMask);
        assertEquals(board.getBitMask(),bitMask);
    }

    /**
     * Test if the method setBoardMatrix set the bitmask correctly
     */
    @Test
    public void testSetBoardMatrix(){
        Item[][] boardMatrix=new Item[ROWS][COLUMNS];
        board.setBoardMatrix(boardMatrix);
        assertEquals(board.getBoardMatrix(),boardMatrix);
    }

}