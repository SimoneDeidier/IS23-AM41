package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonFourCorners;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourCornersTest {

    @Test
    void checkFourCorners() throws NotEnoughSpaceInColumnException {

        CommonTargetCard commonFourCorners = new CommonFourCorners(2);

        // create a sample shelf with four corners of the same Color
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));

        assertTrue(commonFourCorners.check(shelfA));
    }
    @Test
    void checkNoFourCorners() throws NotEnoughSpaceInColumnException {

        CommonTargetCard commonFourCorners = new CommonFourCorners(2);

        // create a sample shelf with four corners of different Colors
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.YELLOW)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));

        assertFalse(commonFourCorners.check(shelfB));
    }
}