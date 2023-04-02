package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonTwoColumnsTest {

    @Test
    void check() {
        CommonTargetCard commonTwoColumns = new CommonTwoColumns();
        Item[][] shelfA = {
                {new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.GAMES), new Item(ItemType.PLANTS), new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.GAMES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.CAT)},
                {new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.BOOKS)}
        };
        assertTrue(commonTwoColumns.check(shelfA));

        Item[][] shelfB = {
                {new Item(ItemType.CAT), new Item(ItemType.BOOKS), new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.GAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.GAMES), new Item(ItemType.BOOKS), new Item(ItemType.PLANTS), new Item(ItemType.CAT)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.GAMES)}
        };
        assertFalse(commonTwoColumns.check(shelfB));
    }
}