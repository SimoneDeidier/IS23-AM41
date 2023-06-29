package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.items.ItemsBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
* Class to test the class ItemsBag
*/
class ItemsBagTest {

    private final static int NUMBEROFITEM = 132;
    private final static int ZERO = 0;
    private ItemsBag itemsBag;


    @BeforeEach
    void initialize() {
    itemsBag= ItemsBag.getItemsBag();
    itemsBag.resetItemsBag();
    }

/**
* Checks if the bag gets correctly setup by the setupBag method
*/
    @Test
    void testSetupBag() {
        itemsBag.setupBag();
        assertEquals(NUMBEROFITEM,itemsBag.getRemainingItemsCount());
    }

/**
* Checks if the remaining items get counted correctly
*/
    @Test
    void testGetRemainingItemsCount() {
        assertEquals(ZERO,itemsBag.getRemainingItemsCount());
        itemsBag.setupBag();
        assertEquals(NUMBEROFITEM,itemsBag.getRemainingItemsCount());
    }

/**
* Tests the pickItem method
*/
    @Test
    void testPickItem() {
        itemsBag.setupBag();
        itemsBag.pickItem();
        assertEquals(NUMBEROFITEM-1,itemsBag.getRemainingItemsCount());
    }
}