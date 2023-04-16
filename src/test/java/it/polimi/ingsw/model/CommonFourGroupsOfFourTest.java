package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourGroupsOfFourTest {

    @Test
    void checkFourGroupsOfFour() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard commonFourGroupsOfFour = new CommonFourGroupsOfFour(2);

        // create a sample shelf with four groups of four
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.GREEN)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.WHITE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));

        assertTrue(commonFourGroupsOfFour.check(shelfA));

    }
    @Test
    void checkNosFourGroupsOfFour() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        CommonTargetCard commonFourGroupsOfFour = new CommonFourGroupsOfFour(2);

        // create a sample shelf without four groups of four
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));

        assertFalse(commonFourGroupsOfFour.check(shelfB));
    }
}