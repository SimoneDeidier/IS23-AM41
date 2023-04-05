package it.polimi.ingsw.model;

public class CommonTwoRows extends CommonTargetCard {
    public CommonTwoRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int rowsThatSatisfiedCondition = 0;
        boolean rowSatisfiedCondition = true;
        int[] counterOccurence = new int[6];

        for( int row = 0; row < ROWS; row++ ){
            if(     shelf.getItemByCoordinates(row, 0) != null &&
                    shelf.getItemByCoordinates(row, 1) != null &&
                    shelf.getItemByCoordinates(row, 2) != null &&
                    shelf.getItemByCoordinates(row, 3) != null &&
                    shelf.getItemByCoordinates(row, 4) != null
            ){
                for( int col = 0; col < COLUMNS; col++){
                    switch (shelf.getItemByCoordinates(row, col).getColor()) {
                        case PINK -> counterOccurence[0]++;
                        case BLUE -> counterOccurence[1]++;
                        case LIGHT_BLUE -> counterOccurence[2]++;
                        case YELLOW -> counterOccurence[3]++;
                        case GREEN -> counterOccurence[4]++;
                        case WHITE -> counterOccurence[5]++;
                    }
                }

                for( int i = 0; i < 6; i++ ){
                    if(counterOccurence[i] > 1){
                        rowSatisfiedCondition = false;
                        break;
                    }
                    counterOccurence[i] = 0;
               }
                if (rowSatisfiedCondition) {
                    rowsThatSatisfiedCondition++;
                    if(rowsThatSatisfiedCondition == 2){
                        return true;
                    }
                }
                rowSatisfiedCondition = true;
            }
        }




        return false;
    }
}
