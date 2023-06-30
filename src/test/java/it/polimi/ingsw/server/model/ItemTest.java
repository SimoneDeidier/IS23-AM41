package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
* Class to check the item class
*/
class ItemTest {

/**
* Checks if the getColor method returns the correct colot
*/
    @Test
    void testGetColor() {
        Item i1 = new Item(ItemColor.WHITE);
        assertSame(ItemColor.WHITE, i1.getColor());
    }
}