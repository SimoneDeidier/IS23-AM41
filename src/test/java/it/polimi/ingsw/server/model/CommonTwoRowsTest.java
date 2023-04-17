package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonTwoRowsTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();
        Shelf shelf2 = new Shelf();
        CommonTargetCard card = new CommonTwoRows(4);
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

        Collections.addAll(list1, i1, i3);
        Collections.addAll(list2, i2, i6);
        Collections.addAll(list3, i3, i2);
        Collections.addAll(list4, i4, i1);
        Collections.addAll(list5, i5, i5);
        Collections.addAll(list6, i6, i4);

        
        //test 1
        shelf.insertItems(0, list1);
        shelf.insertItems(1, list2);
        shelf.insertItems(2, list3);
        shelf.insertItems(3, list4);
        shelf.insertItems(4, list5);

        assertTrue(card.check(shelf));
        
        //test2
        shelf2.insertItems(0, list1);
        shelf2.insertItems(1, list2);
        shelf2.insertItems(2, list3);
        shelf2.insertItems(3, list1);
        shelf2.insertItems(4, list5);

        assertFalse(card.check(shelf2));

    }
}