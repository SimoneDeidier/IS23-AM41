package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonSixGroupsOfTwoTest {

    @Test
    void check() {
        CommonTargetCard commonSixGroupsOfTwo = new CommonSixGroupsOfTwo();

        // create a sample shelf with six groups of two
        Item[][] shelfA = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(commonSixGroupsOfTwo.check(shelfA));

        // create a sample shelf without six groups of two
        Item[][] shelfB = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertFalse(commonSixGroupsOfTwo.check(shelfB));
    }
}