package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonXTest {

    @Test
    void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {

        Shelf shelf = new Shelf();

        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.BLUE);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.YELLOW);
        Item i5 = new Item(ItemColor.PINK);
        Item i6 = new Item(ItemColor.WHITE);

        List<Item> list = new ArrayList<Item>();
        Collections.addAll(list, i1, i1, i1);

        CommonTargetCard card = new CommonX();

        shelf.insertItems(0, list);
        assertFalse(card.check(shelf));

        shelf.insertItems(1, list);
        assertFalse(card.check(shelf));

        shelf.insertItems(2, list);
        assertTrue(card.check(shelf));




    }
}