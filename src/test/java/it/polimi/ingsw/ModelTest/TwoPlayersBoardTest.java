package it.polimi.ingsw.ModelTest;

import it.polimi.ingsw.model.*;
import static org.junit.jupiter.api.Assertions.*;
import junit.framework.TestCase;

public class TwoPlayersBoardTest extends TestCase {

    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        Board board = TwoPlayersBoard.getTwoPlayersBoard(itemsBag);
        //refill board it's already in the constructor for the TwoPlayerBoard

        Item item=board.pickItem(5,4);
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


    public void testPickItem() throws InvalidBoardPositionException,NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        Board board = TwoPlayersBoard.getTwoPlayersBoard(itemsBag);
        board.pickItem(5,4);
        assertThrows(NullItemPickedException.class,()->board.pickItem(5,4));
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));

    }
}