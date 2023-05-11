package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.commons.CommonTwoSquares;
import it.polimi.ingsw.server.model.exceptions.EmptyItemListToInsert;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
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
        Shelf shelf4 = new Shelf();
        Shelf shelf5 = new Shelf();
        Shelf shelf6 = new Shelf();
        Shelf shelf7 = new Shelf();
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
        List<Item> list9 = new ArrayList<Item>();
        List<Item> list10 = new ArrayList<Item>();

        Collections.addAll(list1, i1, i3);
        Collections.addAll(list2, i2, i6, i4);
        Collections.addAll(list3, i3, i6);
        Collections.addAll(list4, i2, i2);
        Collections.addAll(list5, i5, i5, i4);
        Collections.addAll(list6, i2, i2, i2);
        Collections.addAll(list7, i6, i2);
        Collections.addAll(list8, i1, i1);
        Collections.addAll(list9, i1, i2, i2);
        Collections.addAll(list10, i6);



        //test 1
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

        assertFalse(card.check(shelf2));


        
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

        //test 4

        shelf4.insertItems(0, list1);
        shelf4.insertItems(0, list1);
        shelf4.insertItems(0, list1);

        shelf4.insertItems(1, list2);
        shelf4.insertItems(1, list2);

        shelf4.insertItems(2, list5);
        shelf4.insertItems(2, list4);

        shelf4.insertItems(3, list8);
        shelf4.insertItems(3, list6);

        shelf4.insertItems(4, list8);
        shelf4.insertItems(4, list8);

        assertFalse(card.check(shelf4));

        //test 5
        shelf5.insertItems(0, list1);
        shelf5.insertItems(0, list1);
        shelf5.insertItems(0, list1);

        shelf5.insertItems(1, list2);
        shelf5.insertItems(1, list2);

        shelf5.insertItems(2, list5);
        shelf5.insertItems(2, list4);

        shelf5.insertItems(3, list8);
        shelf5.insertItems(3, list9);

        shelf5.insertItems(4, list8);
        shelf5.insertItems(4, list8);

        assertFalse(card.check(shelf5));

        //test 6
        shelf6.insertItems(0, list1);
        shelf6.insertItems(0, list1);
        shelf6.insertItems(0, list1);

        shelf6.insertItems(1, list2);
        shelf6.insertItems(1, list1);

        shelf6.insertItems(2, list5);
        shelf6.insertItems(2, list4);

        shelf6.insertItems(3, list8);
        shelf6.insertItems(3, list9);

        shelf6.insertItems(4, list8);
        shelf6.insertItems(4, list8);

        assertFalse(card.check(shelf6));


        //test 7
        shelf7.insertItems(0, list1);
        shelf7.insertItems(0, list1);
        shelf7.insertItems(0, list1);

        shelf7.insertItems(1, list2);
        shelf7.insertItems(1, list1);

        shelf7.insertItems(2, list5);
        shelf7.insertItems(2, list4);

        shelf7.insertItems(3, list8);
        shelf7.insertItems(3, list10);
        shelf7.insertItems(3, list4);

        shelf7.insertItems(4, list8);
        shelf7.insertItems(4, list2);

        assertTrue(card.check(shelf7));




    }
}