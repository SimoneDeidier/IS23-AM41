package it.polimi.ingsw.server.model;

public class CommonDiagonal extends CommonTargetCard {

    public CommonDiagonal(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int unfound = 0;
        for (int k = 0; k <= 1; k++){
            int j = 0;
            for(int i = k; i < k + COLUMNS - 1; i++) {
                if (shelf.getItemByCoordinates(i, j) != null && shelf.getItemByCoordinates(i + 1, j + 1) != null && shelf.getItemByCoordinates(i, j).getColor() != shelf.getItemByCoordinates(i + 1, j + 1).getColor()) {
                    unfound = 1;
                    break;
                }
                j++;
            }
            if (unfound==0){
                return true;
            }
            unfound = 0;
        }
        for (int k = 0; k <= 1; k++){
            int j = 4;
            for(int i = k; i < k + COLUMNS - 1; i++) {
                if (shelf.getItemByCoordinates(i, j) != null && shelf.getItemByCoordinates(i + 1, j - 1) != null && shelf.getItemByCoordinates(i, j).getColor() != shelf.getItemByCoordinates(i + 1, j - 1).getColor()) {
                    unfound = 1;
                    break;
                }
                j--;
            }
            if (unfound==0){
                return true;
            }
            unfound = 0;
        }
        return false;
    }
}
