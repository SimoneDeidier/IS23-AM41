package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonDiagonalTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);
        // create a sample shelf with a left-to-right diagonal
        Shelf shelfA = new Shelf();

        // Insert the values inside each row
        shelfA.insertItems(0, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.BOOKS)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));

        assertTrue(CommonDiagonal.check(shelfA));

        // create a sample shelf with a upper left-to-right diagonal
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.BOOKS)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));


        assertTrue(CommonDiagonal.check(shelfB));

        // create a sample shelf with a right-to-left diagonal
        Shelf shelfC = new Shelf();

        shelfC.insertItems(0, Arrays.asList(
                new Item(ItemType.PLANTS), new Item(ItemType.PLANTS),
                new Item(ItemType.FRAMES), new Item(ItemType.CAT),
                new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)
        ));

        shelfC.insertItems(1, Arrays.asList(
                new Item(ItemType.FRAMES), new Item(ItemType.BOOKS),
                new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES),
                new Item(ItemType.BOOKS), new Item(ItemType.BOOKS)
        ));

        shelfC.insertItems(2, Arrays.asList(
                new Item(ItemType.CAT), new Item(ItemType.CAT),
                new Item(ItemType.FRAMES), new Item(ItemType.PLANTS),
                new Item(ItemType.TROPHIES), new Item(ItemType.CAT)
        ));

        shelfC.insertItems(3, Arrays.asList(
                new Item(ItemType.GAMES), new Item(ItemType.FRAMES),
                new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS),
                new Item(ItemType.GAMES), new Item(ItemType.GAMES)
        ));

        shelfC.insertItems(4, Arrays.asList(
                new Item(ItemType.PLANTS), new Item(ItemType.FRAMES),
                new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES),
                new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)
        ));

        assertTrue(CommonDiagonal.check(shelfC));

        // create a sample shelf with a upper right-to-left diagonal
        Shelf shelfD = new Shelf();

        shelfD.insertItems(0, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)));
        shelfD.insertItems(1, Arrays.asList(new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.BOOKS)));
        shelfD.insertItems(2, Arrays.asList(new Item(ItemType.CAT), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.CAT)));
        shelfD.insertItems(3, Arrays.asList(new Item(ItemType.GAMES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.GAMES), new Item(ItemType.PLANTS), new Item(ItemType.GAMES)));
        shelfD.insertItems(4, Arrays.asList(new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)));

        assertTrue(CommonDiagonal.check(shelfD));

        // create a sample shelf with no diagonal
        Shelf shelfE = new Shelf();
        shelfE.insertItems(0, Arrays.asList(
                new Item(ItemType.PLANTS),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.FRAMES),
                new Item(ItemType.CAT),
                new Item(ItemType.FRAMES),
                new Item(ItemType.PLANTS)
        ));
        shelfE.insertItems(1, Arrays.asList(
                new Item(ItemType.FRAMES),
                new Item(ItemType.BOOKS),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.PLANTS),
                new Item(ItemType.BOOKS)
        ));
        shelfE.insertItems(2, Arrays.asList(
                new Item(ItemType.CAT),
                new Item(ItemType.CAT),
                new Item(ItemType.FRAMES),
                new Item(ItemType.PLANTS),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.CAT)
        ));
        shelfE.insertItems(3, Arrays.asList(
                new Item(ItemType.GAMES),
                new Item(ItemType.FRAMES),
                new Item(ItemType.PLANTS),
                new Item(ItemType.GAMES),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.GAMES)
        ));
        shelfE.insertItems(4, Arrays.asList(
                new Item(ItemType.PLANTS),
                new Item(ItemType.PLANTS),
                new Item(ItemType.PLANTS),
                new Item(ItemType.TROPHIES),
                new Item(ItemType.PLANTS),
                new Item(ItemType.PLANTS)
        ));

        assertFalse(CommonDiagonal.check(shelfE));
    }
}