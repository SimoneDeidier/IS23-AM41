package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.items.Item;

public class CommonFourRows extends CommonTargetCard {
    public CommonFourRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonFourRows";
    }

    @Override
    public boolean check(Shelf shelf) {
        int countSamePerRow;
        int countAcceptableRows = 0;
        for (int i = 0; i < ROWS; i++) {
            countSamePerRow=0;
            for (int j = 0; j < COLUMNS; j++) {
                for (int k = 0; k < COLUMNS; k++) {
                    if(shelf.getItemByCoordinates(i, j)!=null && shelf.getItemByCoordinates(i, k)!=null) {
                        if (j != k && shelf.getItemByCoordinates(i, j).getColor() == shelf.getItemByCoordinates(i, k).getColor()) {
                            countSamePerRow++;
                            break;
                        }
                    }
                }
            }
            if (countSamePerRow >= 3)
                countAcceptableRows++;
        }
        return countAcceptableRows >= 4;
    }
}