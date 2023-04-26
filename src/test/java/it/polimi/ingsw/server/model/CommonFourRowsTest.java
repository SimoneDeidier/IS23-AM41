package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourRowsTest {

    @Test
    void checkFourRows() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard commonFourRows = new CommonFourCorners(2);

        // create a sample shelf with four rows of one, two or three different types
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.PINK), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));

        assertTrue(commonFourRows.check(shelfA));

    }

    @Test
    void checkNoFourRows() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard commonFourRows = new CommonFourCorners(2);

        // create a sample shelf with less than four rows of one, two or three different types
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE),       new Item(ItemColor.PINK),       new Item(ItemColor.YELLOW),       new Item(ItemColor.GREEN)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE),       new Item(ItemColor.YELLOW),      new Item(ItemColor.GREEN)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.YELLOW),       new Item(ItemColor.PINK),       new Item(ItemColor.BLUE),       new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW),     new Item(ItemColor.BLUE),       new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW),     new Item(ItemColor.GREEN), new Item(ItemColor.PINK)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN),      new Item(ItemColor.BLUE),       new Item(ItemColor.GREEN),      new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW),      new Item(ItemColor.PINK)));

        assertFalse(commonFourRows.check(shelfB));
    }
}