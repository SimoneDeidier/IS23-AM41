package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commons.CommonEightSame;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonEightSameTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        Shelf shelf = new Shelf();
        CommonTargetCard card  = new CommonEightSame(4);
        List<Item> list = new ArrayList<Item>();
        List<Item> list2 = new ArrayList<Item>();
        List<Item> list3 = new ArrayList<Item>();
        List<Item> list4 = new ArrayList<Item>();
        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.BLUE);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.YELLOW);
        Item i5 = new Item(ItemColor.PINK);
        Item i6 = new Item(ItemColor.WHITE);


        Collections.addAll(list, i1, i2, i3);
        Collections.addAll(list2, i4, i2, i6);
        Collections.addAll(list3, i2, i2, i1);
        Collections.addAll(list4, i2);

        shelf.insertItems(0, list2);
        assertFalse(card.check(shelf));
        shelf.insertItems(2, list2);
        assertFalse(card.check(shelf));
        shelf.insertItems(3, list);
        assertFalse(card.check(shelf));
        shelf.insertItems(3, list3);
        assertFalse(card.check(shelf));
        shelf.insertItems(4, list3);
        assertFalse(card.check(shelf));
        shelf.insertItems(1, list4);
        assertTrue(card.check(shelf));


    }
}