package it.polimi.ingsw.server.model;

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
    private Shelf emptyShelf;
    private Random random;
    private List<Item> itemList;
    private Shelf fullShelf;
    private Shelf chessboardShelf;
    private final List<Item> emptyList = new ArrayList<>(3);

    @BeforeEach
    void initialize() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        emptyShelf = new Shelf();
        random = new Random();
        itemList = new ArrayList<>(3);
        for(int i = 0; i < MAXITEMS; i++) {
            itemList.add(new Item(ItemColor.PINK));
        }
        fullShelf = new Shelf();
        for(int i = 0; i < COLUMNS; i++) {
            fullShelf.insertItems(i, itemList);
            fullShelf.insertItems(i, itemList);
        }
        int x = 0;
        chessboardShelf = new Shelf();
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(x%2 == 0) {
                    chessboardShelf.setShelfItem(i, j, new Item(ItemColor.PINK));
                    x++;
                }
                else {
                    chessboardShelf.setShelfItem(i, j, new Item(ItemColor.YELLOW));
                    x++;
                }
            }
        }
    }

    @Test
    void calculateAdjacentItemsPoints() throws EmptyShelfException {
        assertThrows(EmptyShelfException.class, () -> emptyShelf.calculateAdjacentItemsPoints());
        assertEquals(8, fullShelf.calculateAdjacentItemsPoints());
        assertEquals(0, chessboardShelf.calculateAdjacentItemsPoints());
    }

   @Test
   void getShelfPoints() {
        assertEquals(0, emptyShelf.getShelfPoints());
        assertEquals(0, chessboardShelf.getShelfPoints());
        assertEquals(8, fullShelf.getShelfPoints());
   }

    @RepeatedTest(5000)
    void decodeNegativePoints() {
        assertEquals(0, emptyShelf.decodePoints(random.nextInt(0, 9999) * -1));
    }

    @RepeatedTest(5000)
    void decodeZeroPoints() {
        assertEquals(0, emptyShelf.decodePoints(random.nextInt(3)));
    }

    @RepeatedTest(5000)
    void decodeMaxPoints() {
        assertEquals(8, emptyShelf.decodePoints(random.nextInt(6, 9999)));
    }

    @Test
    void emptyShelf() {
        assert(!emptyShelf.isFull());
    }

    @RepeatedTest(5000)
    void notFullEmptyShelf() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        emptyShelf = new Shelf();
        int col = random.nextInt(COLUMNS);
        emptyShelf.insertItems(col, itemList);
        assert(!emptyShelf.isFull());
    }

    @Test
    void fullShelf() {
        assert(fullShelf.isFull());
    }

    @RepeatedTest(5000)
    void insertNullList() {
        emptyShelf = new Shelf();
        assertThrows(EmptyItemListToInsert.class, () -> emptyShelf.insertItems(random.nextInt(COLUMNS), null));
    }

    @RepeatedTest(5000)
    void insertEmptyList() {
        emptyShelf = new Shelf();
        assertThrows(EmptyItemListToInsert.class, () -> emptyShelf.insertItems(random.nextInt(COLUMNS), emptyList));
    }

    @RepeatedTest(5000)
    void insertItemNotThrowsException() {
        emptyShelf = new Shelf();
        assertDoesNotThrow(() -> emptyShelf.insertItems(random.nextInt(COLUMNS), itemList));
    }

    @RepeatedTest(5000)
    void itemExistsInShelfAfterInsertItem() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        emptyShelf = new Shelf();
        int col = random.nextInt(COLUMNS);
        emptyShelf.insertItems(col, itemList);
        int row = random.nextInt(ROWS-3, ROWS-1);
        assertNotNull(emptyShelf.getItemByCoordinates(row, col));
    }

    @RepeatedTest(5000)
    void randomPickItemInFullShelf() {
        int row = random.nextInt(ROWS);
        int col = random.nextInt(COLUMNS);
        assertNotNull(fullShelf.getItemByCoordinates(row, col));
    }

    @RepeatedTest(5000)
    void insertItemThrowsExceptionInFullShelf() {
        assertThrows(NotEnoughSpaceInColumnException.class, () -> fullShelf.insertItems(random.nextInt(COLUMNS), itemList));
    }
}