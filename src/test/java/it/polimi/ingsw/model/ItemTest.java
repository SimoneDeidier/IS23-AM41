package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void getColor() {
        Item i1 = new Item(ItemColor.WHITE);

        assertSame(ItemColor.WHITE, i1.getColor());
        assertSame(ItemType.BOOKS, i1.getType());

    }

    @Test
    void getType() {

    }
}