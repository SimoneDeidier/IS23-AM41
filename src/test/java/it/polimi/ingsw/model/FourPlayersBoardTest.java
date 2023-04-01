package it.polimi.ingsw.model;

import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FourPlayersBoardTest extends TestCase {

    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        BoardFactory board = FourPlayersBoard.getFourPlayersBoard(itemsBag);
        board.refillBoard();
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<9;i++){

            for(int j=0;j<9;j++){

                if(board.getBitMaskElement(i,j) == true && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(45,count);
    }

    public void testPickItem() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        BoardFactory board = FourPlayersBoard.getFourPlayersBoard(itemsBag);
        board.refillBoard();
        board.pickItem(0,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(0,4));
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));
    }
}