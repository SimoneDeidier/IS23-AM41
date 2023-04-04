package it.polimi.ingsw.model;

public class CommonTwoColumns implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int countAcceptableColumns = 5;
        int columnAcceptable;
        for (int j = 0; j < 5; j++) {
            columnAcceptable = 1;
            for (int i = 0; i < 5; i++) {
                for (int k = i+1; k < 6; k++) {
                    if (shelf.getItemByCoordinates(i, j).getType() == shelf.getItemByCoordinates(k, j).getType() && i != k){
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
        return countAcceptableColumns >= 2;
    }
}
