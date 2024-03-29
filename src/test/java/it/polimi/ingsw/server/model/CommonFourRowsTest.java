package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonFourRows;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
* Class to test the class CommonFourRows
*/
class CommonFourRowsTest {

/**
* Check if a shelf that respects the common card reauitements gets recognised
*/
    @Test
    void checkFourRows() throws NotEnoughSpaceInColumnException {
        CommonTargetCard commonFourRows = new CommonFourRows(2);

        // create a sample shelf with four rows of one, two or three different types
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.PINK), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));

        assertTrue(commonFourRows.check(shelfA));

    }

/**
* Check if a shelf that doesn't respects the common card reauitements gets recognised
*/
    @Test
    void checkNoFourRows() throws NotEnoughSpaceInColumnException {
        CommonTargetCard commonFourRows = new CommonFourRows(2);

        // create a sample shelf with less than four rows of one, two or three different types
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE),       new Item(ItemColor.PINK),       new Item(ItemColor.BLUE),       new Item(ItemColor.GREEN)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE),       new Item(ItemColor.WHITE),      new Item(ItemColor.WHITE)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.PINK),       new Item(ItemColor.PINK),       new Item(ItemColor.BLUE),       new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW),     new Item(ItemColor.BLUE),       new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW),     new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN),      new Item(ItemColor.BLUE),       new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN),      new Item(ItemColor.GREEN)));

        assertFalse(commonFourRows.check(shelfB));
    }
}