package it.polimi.ingsw.model;

public class CommonFourRows implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int countSamePerRow;
        int countAcceptableRows = 0;
        for (int i = 0; i < 6; i++) {
            countSamePerRow=0;
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (shelf.getItemByCoordinates(i, j).getType() == shelf.getItemByCoordinates(i, k).getType() && j != k){
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