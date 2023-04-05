package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonSixGroupsOfTwoTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard commonSixGroupsOfTwo = new CommonSixGroupsOfTwo();

        // create a sample shelf with six groups of two
        Shelf shelfA = new Shelf();

        shelfA.insertItems(0, Arrays.asList(new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemType.BOOKS), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.BOOKS)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));

        assertTrue(commonSixGroupsOfTwo.check(shelfA));

        // create a sample shelf without six groups of two
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.BOOKS)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));

        assertFalse(commonSixGroupsOfTwo.check(shelfB));
    }
}