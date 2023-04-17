package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class CommonDiagonalTest {

    @Test
    void checkLtoRDiag() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);

        // create a sample shelf with a left-to-right diagonal
        Shelf shelfA = new Shelf();

        // Insert the values inside each row
        shelfA.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.WHITE)));
        shelfA.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfA.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfA.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert (CommonDiagonal.check(shelfA));
    }
    @Test
    void checkUpLtoRDiag() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);

        // create a sample shelf with a upper left-to-right diagonal
        Shelf shelfB = new Shelf();

        shelfB.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.WHITE)));
        shelfB.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfB.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfB.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE)));


        assert (CommonDiagonal.check(shelfB));
    }

    @Test
    void checkRtoLDiag() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);

        // create a sample shelf with a right-to-left diagonal
        Shelf shelfC = new Shelf();

        shelfC.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        shelfC.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelfC.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfC.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelfC.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert(CommonDiagonal.check(shelfC));

    }

    @Test
    void checkUpRtoLDiag() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);

        // create a sample shelf with a upper right-to-left diagonal
        Shelf shelfD = new Shelf();

        shelfD.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelfD.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelfD.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfD.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW)));
        shelfD.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert(CommonDiagonal.check(shelfD));

    }

    @Test
    void checkNoDiag() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);

        // create a sample shelf with no diagonal
        Shelf shelfE = new Shelf();
        shelfE.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelfE.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelfE.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelfE.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW)));
        shelfE.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert(!CommonDiagonal.check(shelfE));
    }
}