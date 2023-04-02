package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonDiagonalTest {

    @Test
    void check() {
        CommonTargetCard CommonDiagonal = new CommonDiagonal();
        // create a sample shelf with a left-to-right diagonal
        Item[][] shelfA = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };
        assertTrue(CommonDiagonal.check(shelfA));

        // create a sample shelf with a upper left-to-right diagonal
        Item[][] shelfB = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(CommonDiagonal.check(shelfB));

        // create a sample shelf with a right-to-left diagonal
        Item[][] shelfC = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(CommonDiagonal.check(shelfC));

        // create a sample shelf with a upper right-to-left diagonal
        Item[][] shelfD = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.PLANTS), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertTrue(CommonDiagonal.check(shelfD));

        // create a sample shelf with no diagonal
        Item[][] shelfE = new Item[][]{
                {new Item(ItemType.PLANTS), new Item(ItemType.FRAMES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.TROPHIES), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.FRAMES), new Item(ItemType.FRAMES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.FRAMES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.CAT), new Item(ItemType.TROPHIES), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.TROPHIES)},
                {new Item(ItemType.FRAMES), new Item(ItemType.BOOKS), new Item(ItemType.TROPHIES), new Item(ItemType.TROPHIES), new Item(ItemType.PLANTS)},
                {new Item(ItemType.PLANTS), new Item(ItemType.BOOKS), new Item(ItemType.CAT), new Item(ItemType.GAMES), new Item(ItemType.PLANTS)}
        };

        assertFalse(CommonDiagonal.check(shelfE));
    }
}