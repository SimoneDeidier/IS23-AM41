package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

public class CommonFourRows extends CommonTargetCard {
    public CommonFourRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }

    @Override
    public boolean check(Shelf shelf) {
        int countSamePerRow;
        int countAcceptableRows = 0;
        for (int i = 0; i < ROWS; i++) {
            countSamePerRow=0;
            for (int j = 0; j < COLUMNS; j++) {
                for (int k = 0; k < COLUMNS; k++) {
                    if (j != k && shelf.getItemByCoordinates(i, j).getColor() == shelf.getItemByCoordinates(i, k).getColor()){
                        countSamePerRow++;
                        break;
                    }
                }
            }
            if (countSamePerRow >= 3)
                countAcceptableRows++;
        }
        return countAcceptableRows >= 4;
    }
}