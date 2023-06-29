package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.commons.CommonTwoColumns;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
* Class to test the class CommonTwoColumns
*/
class CommonTwoColumnsTest {

    @Test
    void checkTwoColumns() throws NotEnoughSpaceInColumnException {
        CommonTargetCard commonTwoColumns = new CommonTwoColumns(2);
        //create a sample shelf with two columns of 6 different Colors of items
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));
        assertTrue(commonTwoColumns.check(shelfA));
    }

    @Test
    void checkNoTwoColumns() throws NotEnoughSpaceInColumnException {
        CommonTargetCard commonTwoColumns = new CommonTwoColumns(2);
        //create a sample shelf with two columns of 6 different Colors of items
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));
        assertFalse(commonTwoColumns.check(shelfB));
    }
}