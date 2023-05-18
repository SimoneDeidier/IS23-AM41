package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

public class CommonTwoColumns extends CommonTargetCard {
    public CommonTwoColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int countAcceptableColumns = COLUMNS;
        int columnAcceptable;
        for (int j = 0; j < COLUMNS; j++) {
            if( shelf.freeSpaces(j) == 0 ){
                columnAcceptable = 1;
                for (int i = 0; i < (ROWS-1); i++) {
                    for (int k = i+1; k < ROWS; k++) {
                        if (shelf.getItemByCoordinates(i, j).getColor() == shelf.getItemByCoordinates(k, j).getColor() && i != k){
                            columnAcceptable = 0;
                            break;
                        }
                    }
                    if (columnAcceptable == 0){
                        countAcceptableColumns--;
                        break;
                    }
                }
            }
        }
        return countAcceptableColumns >= 2;
    }
}
