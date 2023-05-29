package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.EmptyShelfException;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    void initialize() throws NotEnoughSpaceInColumnException {
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
    void notFullEmptyShelf() throws NotEnoughSpaceInColumnException {
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
    void insertItemNotThrowsException() {
        emptyShelf = new Shelf();
        assertDoesNotThrow(() -> emptyShelf.insertItems(random.nextInt(COLUMNS), itemList));
    }

    @RepeatedTest(5000)
    void itemExistsInShelfAfterInsertItem() throws NotEnoughSpaceInColumnException {
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

    @Test
    void isFull() throws NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();
        assertFalse(shelf.isFull());
        shelf.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelf.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelf.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK)));
        shelf.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.YELLOW)));
        shelf.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        assertTrue(shelf.isFull());
    }

    @Test
    void setShelfPoints() throws NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();
        shelf.setShelfPoints();
        assertEquals(0, shelf.getShelfPoints());
        //three blue tiles => 2 points
        //five white tiles => 5 points
        shelf.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelf.insertItems(1, Arrays.asList(new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelf.setShelfPoints();
        assertEquals(7, shelf.getShelfPoints());
    }


    @Test
    void getShelfMatrix(){
        Shelf shelf = new Shelf();
        Item item = new Item(ItemColor.YELLOW);
        shelf.setShelfItem(3, 3, item);
        Item[][] matrix = shelf.getShelfMatrix();
        assertEquals(matrix[3][3], item);
    }

    @Test
    void setShelfMatrix(){
        Shelf shelf = new Shelf();
        Item item = new Item(ItemColor.YELLOW);
        Item[][] matrix = new Item[9][9];
        matrix[3][3] = item;
        shelf.setShelfMatrix(matrix);
        assertEquals(matrix[3][3], item);
    }




}