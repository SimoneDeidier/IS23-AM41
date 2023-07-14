package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.EmptyShelfException;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;

import java.io.Serializable;
import java.util.List;

/**
 * Class Shelf occupies of calculating the points for adjacent items
 */
public class Shelf implements Serializable {

    private final static int ROWS = 6;
    private final static int COLUMNS = 5;
    private int shelfPoints;
    private Item[][] shelfMatrix;

    /**
     * Constructor builds a new matrix of items
     */
    public Shelf() {
        shelfMatrix = new Item[ROWS][COLUMNS];
    }

    /**
     * is the function that handles the calculation of points the player should get for adjacent items in his shelf
     * @return how many points the player should get from this feature
     * @throws EmptyShelfException if the function is called on a player's empty shelf
     */
    public int calculateAdjacentItemsPoints() throws EmptyShelfException {
        int totalPoints = 0, row, count = 0;
        boolean[][] bitMask = new boolean[ROWS][COLUMNS];
        boolean[][] visitedBitmask = new boolean[ROWS][COLUMNS];

        for(int i = 0; i < COLUMNS; i++) {
            if(shelfMatrix[ROWS - 1][i] != null) {
                count++;
            }
        }
        if(count == 0) {
            throw new EmptyShelfException();
        }
        else {
            for (ItemColor color : ItemColor.values()) {
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLUMNS; j++) {
                        if (shelfMatrix[i][j] != null) {
                            bitMask[i][j] = shelfMatrix[i][j].getColor() == color;
                        } else {
                            bitMask[i][j] = false;
                        }
                    }
                }
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLUMNS; j++) {
                        visitedBitmask[i][j] = false;
                    }
                }
                row = 0;
                while (row != ROWS) {
                    for (int col = 0; col < COLUMNS; col++) {
                        if (bitMask[row][col] && !visitedBitmask[row][col]) {
                            totalPoints += decodePoints(exploreGraph(bitMask, visitedBitmask, row, col));
                        }
                    }
                    row++;
                }
            }
            return totalPoints;
        }
    }

    /**
     * is a recursive function used during the calculation of points of calculateAdjacentItemsPoints function
     * @param bitMask is a mask of bit placed above the shelf
     * @param visited is a mask of bit placed above the shelf to help the algorithm remember which slots he has already visited
     * @param row represents the row in the shelf the function should consider
     * @param col represents the column in the shelf the function should consider
     * @return how many item of one color he found to be adjacent
     */
    public int exploreGraph(boolean[][] bitMask, boolean[][] visited, int row, int col) {
        int val = 0;

        visited[row][col] = true;
        val++;
        if(col - 1 >= 0 && bitMask[row][col - 1] && !visited[row][col - 1]) {
            val += exploreGraph(bitMask, visited, row, col - 1);
        }
        if(row + 1 < ROWS && bitMask[row + 1][col] && !visited[row + 1][col]) {
            val += exploreGraph(bitMask, visited, row + 1, col);
        }
        if(col + 1 < COLUMNS && bitMask[row][col + 1] && !visited[row][col + 1]) {
            val += exploreGraph(bitMask, visited, row, col + 1);
        }
        return val;
    }

    /**
     * is a function used during the calculation of points of calculateAdjacentItemsPoints function
     * @param adjacentTiles is the number of item of one color that are adjacent
     * @return how many points should be added based on how many item were adjacent
     */
    public int decodePoints(int adjacentTiles) {
        if(adjacentTiles == 3) {
            return 2;
        }
        else if(adjacentTiles == 4) {
            return 3;
        }
        else if (adjacentTiles == 5) {
            return 5;
        }
        else if(adjacentTiles >= 6) {
            return 8;
        }
        else return 0;
    }

    /**
     * checks whether a shelf is full
     * @return true if a shelf is full, false otherwise
     */
    public boolean isFull(){
        for(int i = 0; i < COLUMNS; i++) {
            if(shelfMatrix[0][i] == null) { // check only top of the matrix
                return false;
            }
        }
        return true;

    }

    /**
     * inserts a list of items in a column of the shelf
     * @param column is the column where the items should be inserted
     * @param items is the list of items to be inserted
     * @throws NotEnoughSpaceInColumnException if there isn't enough space in a column to insert the amount of items in the list
     */
    public void insertItems(int column, List<Item> items) throws NotEnoughSpaceInColumnException {
        int freeSpace = 0;

        for (int i = 0; i < ROWS; i++) {
            if (shelfMatrix[i][column] == null) {
                freeSpace++;
            }
        }
        if (freeSpace < items.size()) {
            throw new NotEnoughSpaceInColumnException();
        }
        for (Item i : items) {
            shelfMatrix[freeSpace - 1][column] = i;
            freeSpace--;
        }
    }

    /**
     * @param row the row where we select the item
     * @param col the column where we select the item
     * @return the item at the coordinates row and col
     */
    public Item getItemByCoordinates(int row, int col) {
        return shelfMatrix[row][col];
    }


    /**
     * @param col is the column where we want to check how many slots are left
     * @return the amount of free slots in the selected column
     */
    public int freeSpaces(int col){
        int res = ROWS;

        for( int row = ROWS - 1 ; row >= 0; row-- ){
            if(shelfMatrix[row][col] == null){
                return res;
            }
            res--;
        }
        return 0;
    }

    /**
     * sets the points the player has made with the adjacent items rule in the variable shelfPoints
     */
    public void setShelfPoints(){
        try {
            this.shelfPoints = this.calculateAdjacentItemsPoints();
        }
        catch (EmptyShelfException e) {
            this.shelfPoints = 0;
        }
    }

    /**
     * @return the points the player has made with the adjacent items rule in the variable shelfPoints
     */
    public int getShelfPoints() {
        this.setShelfPoints();
        return this.shelfPoints;
    }

    /**
     * Used for test purposes
     * @param row the row where we want to set the item
     * @param column the column where we want to set the item
     * @param item is the item we want to set
     */
    public void setShelfItem(int row, int column, Item item) {
        this.shelfMatrix[row][column] = item;
    }

    /**
     * check whether a column has enough free space available to execute a move
     * @param numberOfItemsPicked is the number of items that was picked by a player
     * @param column is the column we are checking the condition for
     * @return true is there is enough space in the column, false otherwise
     */
    public boolean checkColumn(int numberOfItemsPicked, int column) {
        if (column < 0 || column > COLUMNS)
            return false;
        if(freeSpaces(column)<numberOfItemsPicked)
            return false;
        return true;
    }

    /**
     * @return the shelf matrix
     */
    public Item[][] getShelfMatrix() {
        return shelfMatrix;
    }

    /**
     * used when the controller is restoring a previous game
     * @param shelfMatrix is the shelfMatrix of the previous game, that now will be set as the current shelfMatrix
     */
    public void setShelfMatrix(Item[][] shelfMatrix){
        this.shelfMatrix=shelfMatrix;
    }
}
