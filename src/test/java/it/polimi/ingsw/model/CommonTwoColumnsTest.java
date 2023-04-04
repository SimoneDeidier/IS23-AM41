package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonTwoColumnsTest {

    @Test
    void check() {
        CommonTargetCard commonTwoColumns = new CommonTwoColumns();
        //create a sample shelf with two columns of 6 different types of items
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.GAMES)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));
        assertTrue(commonTwoColumns.check(shelfA));

        //create a sample shelf with two columns of 6 different types of items
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemType.BOOKS), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.BOOKS)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));
        assertFalse(commonTwoColumns.check(shelfB));
    }
}