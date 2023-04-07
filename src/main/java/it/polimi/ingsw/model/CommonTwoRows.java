package it.polimi.ingsw.model;

public class CommonTwoRows extends CommonTargetCard {
    private final static int NUMBER_OF_ROWS_FOR_CONDITION = 2;
    private final static int MAX_OCCURENCE_PER_COLOR = 1;
    public CommonTwoRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int rowsThatSatisfiedCondition = 0;
        boolean rowSatisfiedCondition = true;
        int[] counterOccurence = new int[COLORS];

        for( int row = 0; row < ROWS; row++ ){
            for( int col = 0; col < COLUMNS; col++){
                if(shelf.getItemByCoordinates(row, col) != null){
                    switch (shelf.getItemByCoordinates(row, col).getColor()) {
                        case PINK -> counterOccurence[0]++;
                        case BLUE -> counterOccurence[1]++;
                        case LIGHT_BLUE -> counterOccurence[2]++;
                        case YELLOW -> counterOccurence[3]++;
                        case GREEN -> counterOccurence[4]++;
                        case WHITE -> counterOccurence[5]++;
                    }
                }
                else{
                    counterOccurence[0] = MAX_OCCURENCE_PER_COLOR + 1;
                    break;
                }
            }

            for( int i = 0; i < COLORS; i++ ){
                if(counterOccurence[i] > MAX_OCCURENCE_PER_COLOR){
                    rowSatisfiedCondition = false;
                    break;
                }
                counterOccurence[i] = 0;
            }

            if (rowSatisfiedCondition) {
                rowsThatSatisfiedCondition++;
                if(rowsThatSatisfiedCondition == NUMBER_OF_ROWS_FOR_CONDITION){
                    return true;
                }
            }
            rowSatisfiedCondition = true;
        }
    
    return false;
    }
}
