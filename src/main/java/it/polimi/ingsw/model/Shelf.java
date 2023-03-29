package it.polimi.ingsw.model;

import java.util.List;

public class Shelf {

    private final static int ROWS = 6;
    private final static int COLUMNS = 5;
    private final static int VALUES = 2;

    private int shelfPoints;
    private final Item[][] shelfMatrix;

    public Shelf() {
        shelfMatrix = new Item[ROWS][COLUMNS];
    }

    public int updateAdjacentItemsPoints(){
        int totalPoints = 0;
        boolean[][] bitMask = new boolean[ROWS][COLUMNS];
        boolean[][] visitedBitmask = new boolean[ROWS][COLUMNS];
        boolean startFound = false;
        int[] startIndexes = new int[VALUES];

        for(ItemColor color: ItemColor.values()) {
            for(int i = 0; i < ROWS; i++) {
                for(int j = 0; j < COLUMNS; j++) {
                    if(shelfMatrix[i][j].getColor() != color) {
                        bitMask[i][j] = false;
                    }
                    else {
                        bitMask[i][j] = true;
                        if(!startFound) {
                            startIndexes[0] = i;
                            startIndexes[1] = j;
                            startFound = true;
                        }
                    }
                }
            }
            // todo simo deve finire di fare cose qui
        }

        return 0;
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
