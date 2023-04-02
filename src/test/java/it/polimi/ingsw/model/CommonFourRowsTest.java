package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourRowsTest {

    @Test
    void check() {
        CommonTargetCard commonFourRows = new CommonFourCorners();

        // create a sample shelf with four rows of one, two or three different types
        Item[][] shelfA = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(commonFourRows.check(shelfA));

        // create a sample shelf with less than four rows of one, two or three different types
        Item[][] shelfB = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(commonFourRows.check(shelfB));
    }
}