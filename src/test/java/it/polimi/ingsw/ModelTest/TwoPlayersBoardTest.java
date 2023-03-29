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
        board.pickItem(5,4);
        board.refillBoard();
        int count=0;

        for(int i=0;i<9;i++){

            for(int j=0;j<9;j++){

                if(board.getBitMaskElement(i,j)==1 && board.getBoardMatrixElement(i,j)!=null)

                    count ++;

            }
        }
        assertEquals(29,count);

        //ADD assertThrows
    }


    public void testPickItem() throws InvalidBoardPositionException,NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        Board board = TwoPlayersBoard.getTwoPlayersBoard(itemsBag);
        Item itemTest;
        itemTest=board.pickItem(5,4);
        if(itemTest!=null){
            assertNull(board.getBoardMatrixElement(5,4));
        }
        //ADD assertThrows

    }
}