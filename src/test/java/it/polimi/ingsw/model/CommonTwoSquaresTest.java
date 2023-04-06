package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonTwoSquaresTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        Shelf shelf = new Shelf();
        Shelf shelf2 = new Shelf();
        Shelf shelf3 = new Shelf();
        CommonTargetCard card = new CommonTwoSquares(4);
        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.BLUE);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.YELLOW);
        Item i5 = new Item(ItemColor.PINK);
        Item i6 = new Item(ItemColor.WHITE);

        List<Item> list1 = new ArrayList<Item>();
        List<Item> list2 = new ArrayList<Item>();
        List<Item> list3 = new ArrayList<Item>();
        List<Item> list4 = new ArrayList<Item>();
        List<Item> list5 = new ArrayList<Item>();
        List<Item> list6 = new ArrayList<Item>();
        List<Item> list7 = new ArrayList<Item>();
        List<Item> list8 = new ArrayList<Item>();

        Collections.addAll(list1, i1, i3);
        Collections.addAll(list2, i2, i6, i4);
        Collections.addAll(list3, i3, i6);
        Collections.addAll(list4, i2, i2);
        Collections.addAll(list5, i5, i5, i4);
        Collections.addAll(list6, i2, i2, i2);
        Collections.addAll(list7, i6, i2);
        Collections.addAll(list8, i1, i1);

        shelf.insertItems(0, list1);
        shelf.insertItems(0, list1);
        shelf.insertItems(0, list1);

        shelf.insertItems(1, list2);
        shelf.insertItems(1, list2);

        shelf.insertItems(2, list5);
        shelf.insertItems(2, list4);

        shelf.insertItems(3, list3);
        shelf.insertItems(3, list6);


        shelf.insertItems(4, list3);
        shelf.insertItems(4, list6);

        assertFalse(card.check(shelf));

        
        //test 2

        shelf2.insertItems(0, list1);
        shelf2.insertItems(0, list1);
        shelf2.insertItems(0, list1);

        shelf2.insertItems(1, list2);
        shelf2.insertItems(1, list2);

        shelf2.insertItems(2, list5);
        shelf2.insertItems(2, list4);

        shelf2.insertItems(3, list7);
        shelf2.insertItems(3, list6);

        shelf2.insertItems(4, list7);
        shelf2.insertItems(4, list6);

        assertTrue(card.check(shelf2));


        
        //test 3

        shelf3.insertItems(0, list1);
        shelf3.insertItems(0, list1);
        shelf3.insertItems(0, list1);

        shelf3.insertItems(1, list2);
        shelf3.insertItems(1, list2);

        shelf3.insertItems(2, list5);
        shelf3.insertItems(2, list4);

        shelf3.insertItems(3, list8);
        shelf3.insertItems(3, list6);

        shelf3.insertItems(4, list8);
        shelf3.insertItems(4, list6);

        assertFalse(card.check(shelf3));
    }
}