package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    private static final int MAXITEMS = 3;
    private static final int COLUMNS = 5;
    private static final int ROWS = 6;
    private Shelf shelf;
    private Random random;

    @BeforeEach
    void initialize() {
        shelf = new Shelf();
        random = new Random();
    }

    @Test
    void calculateAdjacentItemsPoints() {
    }

    @Test
    void exploreGraph() {
    }

    @RepeatedTest(5000)
    void decodeNegativePoints() {
        assertEquals(0, shelf.decodePoints(random.nextInt(0, 9999) * -1));
    }

    @RepeatedTest(5000)
    void decodeZeroPoints() {
        assertEquals(0, shelf.decodePoints(random.nextInt(3)));
    }

    @RepeatedTest(5000)
    void decodeMaxPoints() {
        assertEquals(8, shelf.decodePoints(random.nextInt(6, 9999)));
    }

    @Test
    void isFull() throws NotEnoughSpaceInColumnException, EmptyItemListToInsert {

        boolean res = shelf.isFull();
        assert(!res);
        List<Item> itemList = new ArrayList<>(1);
        for(int i = 0; i < MAXITEMS; i++) {
            itemList.add(new Item(ItemColor.PINK));
        }
        shelf.insertItems(0, itemList);
        shelf.insertItems(0, itemList);
        res = shelf.isFull();
        assert(!res);
        for(int i = 1; i < COLUMNS; i++) {
            shelf.insertItems(i, itemList);
            shelf.insertItems(i, itemList);
        }
        res = shelf.isFull();
        assert(res);
    }

    @Test
    void insertItems() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();
        Random random = new Random();
        int row, col;

        final List<Item> nullItemList = null;
        assertThrows(EmptyItemListToInsert.class, () -> {
            shelf.insertItems(0, nullItemList);
        });
        final List<Item> emptyItemList = new ArrayList<>(3);
        assertThrows(EmptyItemListToInsert.class, () -> {
            shelf.insertItems(0, emptyItemList);
        });
        final List<Item> threeItemsList = new ArrayList<>(3);
        for(int i = 0; i < MAXITEMS; i++) {
            threeItemsList.add(new Item(ItemColor.PINK));
        }
        assertDoesNotThrow(() -> {
            shelf.insertItems(0, threeItemsList);
        });
        for(int i = 0; i < MAXITEMS; i++) {
            assertNotNull(shelf.getItemByCoordinates(5 - i, 0));
        }
        shelf.insertItems(0, threeItemsList);
        for(int i = 1; i < COLUMNS; i++) {
            shelf.insertItems(i, threeItemsList);
            shelf.insertItems(i, threeItemsList);
        }
        row = random.nextInt(ROWS);
        col = random.nextInt(COLUMNS);
        assertNotNull(shelf.getItemByCoordinates(row, col));
        assertThrows(NotEnoughSpaceInColumnException.class, () -> {
            shelf.insertItems(0, threeItemsList);
        });
    }
}