package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.commons.CommonX;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonXTest {

    @Test
    void check() throws NotEnoughSpaceInColumnException {

        Shelf shelf = new Shelf();
        Shelf shelf2 = new Shelf();
        Shelf shelf3 = new Shelf();

        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.BLUE);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.YELLOW);
        Item i5 = new Item(ItemColor.PINK);
        Item i6 = new Item(ItemColor.WHITE);

        List<Item> list1 = new ArrayList<Item>();
        Collections.addAll(list1, i1, i1, i1);
        List<Item> list2 = new ArrayList<Item>();
        Collections.addAll(list2, i1, i2, i1);
        List<Item> list3 = new ArrayList<Item>();
        Collections.addAll(list3, i2, i1, i2);
        List<Item> list4 = new ArrayList<Item>();
        Collections.addAll(list4, i6, i6);


        CommonTargetCard card = new CommonX(2);



        //test 1
        shelf.insertItems(0, list1);
        assertFalse(card.check(shelf));
        shelf.insertItems(1, list1);
        assertFalse(card.check(shelf));
        shelf.insertItems(2, list1);
        assertTrue(card.check(shelf));


        //test 2
        shelf2.insertItems(2, list3);
        assertFalse(card.check(shelf2));
        shelf2.insertItems(3, list2);
        assertFalse(card.check(shelf2));
        shelf2.insertItems(4, list3);
        assertTrue(card.check(shelf2));




        //test 3
        shelf3.insertItems(2, list4);
        assertFalse(card.check(shelf3));
        shelf3.insertItems(3, list4);
        assertFalse(card.check(shelf3));
        shelf3.insertItems(4, list4);
        assertFalse(card.check(shelf3));
        shelf3.insertItems(2, list1);
        assertFalse(card.check(shelf3));
        shelf3.insertItems(3, list1);
        assertFalse(card.check(shelf3));
        shelf3.insertItems(4, list1);
        assertTrue(card.check(shelf3));

    }
}