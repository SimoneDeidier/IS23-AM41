package it.polimi.ingsw.model;

import java.util.List;

public class Shelf {

    private final static int ROWS = 6;
    private final static int COLUMNS = 5;

    private int shelfPoints;
    private final Item[][] shelfMatrix;

    public Shelf() {
        shelfMatrix = new Item[ROWS][COLUMNS];
    }

    public int calculateAdjacentItemsPoints(){
        int totalPoints = 0, row = 0;
        boolean[][] bitMask = new boolean[ROWS][COLUMNS];
        boolean[][] visitedBitmask = new boolean[ROWS][COLUMNS];

        for(ItemColor color: ItemColor.values()) {
            for(int i = 0; i < ROWS; i++) {
                for(int j = 0; j < COLUMNS; j++) {
                    bitMask[i][j] = shelfMatrix[i][j].getColor() == color;
                }
            }
            while(row != ROWS) {
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

    public boolean isFull(){
        for(int i = 0; i < COLUMNS; i++) {
            if(shelfMatrix[0][i] == null) { // check only top of the matrix
                return false;
            }
        }
        return true;

    }
    public void insertItems(int column, List<Item> items) throws NotEnoughSpaceInColumnException{
        int freeSpace = 0;

        for(int i = 0; i < ROWS; i++) {
            if(shelfMatrix[i][column] == null) {
                freeSpace++;
            }
        }
        if(freeSpace < items.size()) {
            throw new NotEnoughSpaceInColumnException();
        }
        else {
            for(Item i : items) {
                shelfMatrix[freeSpace-1][column] = i;
                freeSpace--;
            }
        }
    }

}
