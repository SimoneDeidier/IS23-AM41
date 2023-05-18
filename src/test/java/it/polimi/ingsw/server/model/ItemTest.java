package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testGetColor() {
        Item i1 = new Item(ItemColor.WHITE);
        assertSame(ItemColor.WHITE, i1.getColor());
    }
}