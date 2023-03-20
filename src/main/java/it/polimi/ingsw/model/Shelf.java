package it.polimi.ingsw.model;

import java.util.List;

public class Shelf {
    private int shelfPoints;
    private Item[][] shelfMatrix;

    public int updateAdjacentItemsPoints(){
        //da implementare
        return 0;
    }

    public boolean isFull(){
        for(int i=0;i<5;i++) {
            if (shelfMatrix[0][i] == null) { //it is enough to check the top line of the matrix
                return false;
            }
        }                                   //as the items gets inserted one by one in the same column
        return true;

    }
    public void insertItems(int column, List<Item> items){ //items is already in the order the user wants to put the items in the shelf
        if(items.get(1) == null) {
            if (columnFreeSpace(column) >= 1) { //server-side check for chosen column
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(0);
            }
        }
        else if(items.get(2) == null) {
            if (columnFreeSpace(column) >= 2) { //server-side check for chosen column
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(0);
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(1);
            }
        }
        else{
            if(columnFreeSpace(column) >= 3) { //server-side check for chosen column
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(0);
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(1);
                shelfMatrix[columnFreeSpace(column) - 1][column] = items.get(2);
            }
        }

    }

    public int columnFreeSpace(int column){  //used for checking server-side the validity of the move
    int count=6;                             // in insertItems
        for(int i = 5; i >= 1; i--) {
            if(shelfMatrix[i][column]!=null) {
                count--;
            }
            else break;
        }
        return count;
    }


}
