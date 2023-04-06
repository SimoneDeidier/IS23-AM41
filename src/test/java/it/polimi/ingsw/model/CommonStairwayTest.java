package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonStairwayTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        CommonTargetCard card  = new CommonStairway(2);

        Shelf case1 = new Shelf();
        Shelf case2 = new Shelf();
        Shelf case3 = new Shelf();
        Shelf case4 = new Shelf();

        List<Item> oneSize = new ArrayList<Item>();
        List<Item> twoSize = new ArrayList<Item>();
        List<Item> threeSize = new ArrayList<Item>();

        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.BLUE);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.YELLOW);
        Item i5 = new Item(ItemColor.PINK);
        Item i6 = new Item(ItemColor.WHITE);

        Collections.addAll(oneSize, i3);
        Collections.addAll(twoSize, i4, i6);
        Collections.addAll(threeSize, i2, i5, i1);

        //controlliamo i 4 casi:

        //case 1:
        //first column, all six
        case1.insertItems(0, threeSize);
        case1.insertItems(0, threeSize);
        //second column, five
        case1.insertItems(1, threeSize);
        case1.insertItems(1, twoSize);
        //third column, four
        case1.insertItems(2, twoSize);
        case1.insertItems(2, twoSize);
        //fourth column, three
        case1.insertItems(3, threeSize);
        //fifth column, two
        case1.insertItems(4, twoSize);
        //check
        assertTrue(card.check(case1));


        //case 2:
        //first column, five
        case2.insertItems(0, threeSize);
        case2.insertItems(0, twoSize);
        //second column, four
        case2.insertItems(1, threeSize);
        case2.insertItems(1, oneSize);
        //third column, three
        case2.insertItems(2, threeSize);
        //fourth column, two
        case2.insertItems(3, twoSize);
        //fifth column, one
        case2.insertItems(4, oneSize);
        //check
        assertTrue(card.check(case2));

        //case 3:
        //first column, two
        case3.insertItems(0, threeSize);
        case3.insertItems(0, twoSize);
        //second column, three
        case3.insertItems(1, threeSize);
        case3.insertItems(1, oneSize);
        //third column, four
        case3.insertItems(2, threeSize);
        //fourth column, five
        case3.insertItems(3, twoSize);
        //fifth column, six
        case3.insertItems(4, oneSize);
        //check
        assertTrue(card.check(case3));

        //case 4:

        //first column, one
        case4.insertItems(0, oneSize);
        //second column, two
        case4.insertItems(1, twoSize);
        //third column, three
        case4.insertItems(2, threeSize);
        //fourth column, four
        case4.insertItems(3, twoSize);
        case4.insertItems(3, twoSize);
        //fifth column, five
        case4.insertItems(4, threeSize);
        case4.insertItems(4, twoSize);
        //check
        assertTrue(card.check(case4));

    }
}