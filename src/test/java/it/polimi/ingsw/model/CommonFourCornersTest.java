package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourCornersTest {

    @Test
    void check() {

        CommonTargetCard commonFourCorners = new CommonFourCorners();

        // create a sample shelf with four corners of the same type
        Item[][] shelfA = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(commonFourCorners.check(shelfA));

        // create a sample shelf with four corners of different types
        Item[][] shelfB = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertFalse(commonFourCorners.check(shelfB));
    }
}