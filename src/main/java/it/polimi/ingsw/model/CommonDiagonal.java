package it.polimi.ingsw.model;

public class CommonDiagonal extends CommonTargetCard {

    public CommonDiagonal(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int unfound = 0;
        for (int k = 0; k <= 1; k++){
            int j = 0;
            for(int i = k; i < k+4; i++) {
                if (shelf.getItemByCoordinates(i, j).getType() != shelf.getItemByCoordinates(i + 1, j + 1).getType()) {
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
            for(int i = k; i < k+4; i++) {
                if (shelf.getItemByCoordinates(i, j).getType() != shelf.getItemByCoordinates(i + 1, j - 1).getType()) {
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
