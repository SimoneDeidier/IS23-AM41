package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonDiagonal;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonDiagonalTest {

    @Test
    void checkLtoRDiag() throws NotEnoughSpaceInColumnException {
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
    void checkUpLtoRDiag() throws NotEnoughSpaceInColumnException {

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
    void checkRtoLDiag() throws NotEnoughSpaceInColumnException {

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
    void checkUpRtoLDiag() throws NotEnoughSpaceInColumnException {

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
    void checkNoDiag() throws NotEnoughSpaceInColumnException {

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
    
    @Test 
    void checkFirstDiagonal() throws NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);
        // create a sample shelf with first diagonal
        Shelf shelFirst = new Shelf();
        shelFirst.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelFirst.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelFirst.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelFirst.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW)));
        shelFirst.insertItems(4, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert(CommonDiagonal.check(shelFirst));
    }

    @Test
    void checkSecondDiagonal() throws NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);
        // create a sample shelf with Second diagonal
        Shelf shelSecond = new Shelf();
        shelSecond.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelSecond.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelSecond.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        shelSecond.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW)));
        shelSecond.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));

        assert(CommonDiagonal.check(shelSecond));
    }

    @Test
    void checkThirdDiagonal() throws NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);
        // create a sample shelf with Third diagonal
        Shelf shelThird = new Shelf();
        shelThird.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        assertFalse(CommonDiagonal.check(shelThird));
        shelThird.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        assertFalse(CommonDiagonal.check(shelThird));
        shelThird.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK)));
        assertFalse(CommonDiagonal.check(shelThird));
        shelThird.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.PINK), new Item(ItemColor.YELLOW)));
        assertFalse(CommonDiagonal.check(shelThird));
        shelThird.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        assertTrue(CommonDiagonal.check(shelThird));
    }
    
    @Test
    void checkFourthDiagonal() throws NotEnoughSpaceInColumnException {
        CommonTargetCard CommonDiagonal = new CommonDiagonal(2);
        // create a sample shelf with Fourth diagonal
        Shelf shelFourth = new Shelf();
        shelFourth.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelFourth.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        shelFourth.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        shelFourth.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        shelFourth.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        assertFalse(CommonDiagonal.check(shelFourth));
    }
}