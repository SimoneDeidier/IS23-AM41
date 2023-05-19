package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;

public class CommonTwoColumns extends CommonTargetCard {
    public CommonTwoColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonThreeColumns";
    }
    @Override
    public boolean check(Shelf shelf) {
        int countAcceptableColumns = COLUMNS;
        int columnAcceptable;
        for (int j = 0; j < COLUMNS; j++) {
            columnAcceptable = 1;
            for (int i = 0; i < (ROWS-1); i++) {
                for (int k = i+1; k < ROWS; k++) {
                    if(shelf.getItemByCoordinates(i, j).getColor() != null && shelf.getItemByCoordinates(k, j).getColor() != null) {
                        if (shelf.getItemByCoordinates(i, j).getColor() == shelf.getItemByCoordinates(k, j).getColor() && i != k) {
                            columnAcceptable = 0;
                            break;
                        }
                    }
                }
                if (columnAcceptable == 0){
                    countAcceptableColumns--;
                    break;
                }
            }
        }
        return countAcceptableColumns >= 2;
    }
}
