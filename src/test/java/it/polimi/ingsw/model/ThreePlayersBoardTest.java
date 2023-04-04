package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ThreePlayersBoardTest extends TestCase {

    public void testRefillBoard() throws InvalidBoardPositionException, NullItemPickedException {
        BoardFactory board = ThreePlayersBoard.getThreePlayersBoard();
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
        assertEquals(37,count);

    }

    public void testPickItem() throws InvalidBoardPositionException, NullItemPickedException {
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        BoardFactory board = ThreePlayersBoard.getThreePlayersBoard();
        board.refillBoard();
        board.pickItem(0,3);
        assertThrows(NullItemPickedException.class,()->board.pickItem(0,3));
        assertThrows(InvalidBoardPositionException.class,()->board.pickItem(1,1));
    }
}