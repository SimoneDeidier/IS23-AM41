package it.polimi.ingsw.model;

import java.util.List;

public class Shelf {

    private int shelfPoints;
    private final Item[][] shelfMatrix;

    public Shelf() {
        shelfMatrix = new Item[6][5];
    }

    public int updateAdjacentItemsPoints(){
        // todo da implementare
        return 0;
    }

    public boolean isFull(){
        for(int i = 0; i < 5; i++) {
            if(shelfMatrix[0][i] == null) { // check only top of the matrix
                return false;
            }
        }
        return true;

    }
    public void insertItems(int column, List<Item> items) throws NotEnoughSpaceInColumnException{
        int freeSpace = 0;

        for(int i = 0; i < 6; i++) {
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
