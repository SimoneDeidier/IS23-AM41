package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import static org.junit.jupiter.api.Assertions.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
public class TwoPlayersBoardTest extends TestCase {

    @Test
    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        BoardFactory board = TwoPlayersBoard.getTwoPlayersBoard(itemsBag);
        board.refillBoard();
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<9;i++){

            for(int j=0;j<9;j++){

                if(board.getBitMaskElement(i,j) == 1 && board.getBoardMatrixElement(i,j) != null)

                    count ++;

            }
        }
        assertEquals(29,count);


    }

    @Test
    public void testPickItem() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        BoardFactory board = TwoPlayersBoard.getTwoPlayersBoard(itemsBag);
        board.refillBoard();
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));

    }
}