package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonFourGroupsOfFourTest {

    @Test
    void check() {
        CommonTargetCard commonFourGroupsOfFour = new CommonFourGroupsOfFour();

        // create a sample shelf with four groups of four
        Item[][] shelfA = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.BOOKS), new Item(ItemType.GAMES), new Item(ItemType.GAMES)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.GAMES)}
        };

        assertTrue(commonFourGroupsOfFour.check(shelfA));

        // create a sample shelf without four groups of four
        Item[][] shelfB = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertFalse(commonFourGroupsOfFour.check(shelfB));
    }
}