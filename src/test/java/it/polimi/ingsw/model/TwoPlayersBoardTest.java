package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TwoPlayersBoardTest extends TestCase {

    @Test
    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        BoardFactory board = TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<9;i++){

            for(int j=0;j<9;j++){

                if(board.getBitMaskElement(i, j) && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(29,count);


    }

    @Test
    public void testPickItem1() throws InvalidBoardPositionException, NullItemPickedException {
        BoardFactory board = TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));

    }

    @Test
    public void testPickItem2() {
        BoardFactory board = TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));

    }
    @Test
    public void testHasFreeSide1() {
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        assertFalse(board.hasFreeSide(0,0));
    }
    @Test
    public void testHasFreeSide2() {
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        assertTrue(board.hasFreeSide(3,6));
        }
    @Test
    public void testHasFreeSide3() throws InvalidBoardPositionException, NullItemPickedException {
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        board.pickItem(5,6);
        assertTrue(board.hasFreeSide(5,5));
    }
    @Test
    public void testHasFreeSide4() {
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        assertFalse(board.hasFreeSide(5,5));
    }
    @Test
    public void testCheckInLine1() {
        Item item1=new Item(ItemColor.GREEN),item2=new Item(ItemColor.GREEN),item3=new Item(ItemColor.GREEN);
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        board.setBoardMatrixElement(3,3,item1);
        board.setBoardMatrixElement(3,4,item2);
        board.setBoardMatrixElement(3,5,item3);
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{3,4});
        list.add(new int[]{3,5});
        assertTrue(board.checkInLine(list));
    }

    @Test
    public void testCheckInLine2() {
        Item item1=new Item(ItemColor.GREEN),item2=new Item(ItemColor.LIGHT_BLUE),item3=new Item(ItemColor.GREEN);
        BoardFactory board= TwoPlayersBoard.getTwoPlayersBoard();
        board.refillBoard();
        board.setBoardMatrixElement(3,3,item1);
        board.setBoardMatrixElement(3,4,item2);
        board.setBoardMatrixElement(3,5,item3);
        List<int[]> list= new ArrayList<>();
        list.add(new int[]{3,3});
        list.add(new int[]{3,4});
        list.add(new int[]{3,5});
        assertFalse(board.checkInLine(list));
    }
}