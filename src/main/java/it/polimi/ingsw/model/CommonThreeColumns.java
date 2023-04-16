package it.polimi.ingsw.model;

public class CommonThreeColumns extends CommonTargetCard {

    private final static int MAX_DIFFERENT_COLORS_IN_COLUMNS = 3;
    private final static int NUMBER_OF_COLUMNS_FOR_CONDITION = 3;
    public CommonThreeColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] colorsCount = new int[COLORS];
        int colorsAvaiable = MAX_DIFFERENT_COLORS_IN_COLUMNS;
        int columnsThatSadisfiedCondition = 0;

        for( int col = 0; col < COLUMNS; col ++ ){
            if( shelf.getItemByCoordinates(0, col) != null ){
                for( int row = 0; row < ROWS; row++){
                    switch (shelf.getItemByCoordinates(row, col).getColor()) {
                        case GREEN -> colorsCount[0]++;
                        case BLUE-> colorsCount[1]++;
                        case LIGHT_BLUE -> colorsCount[2]++;
                        case YELLOW -> colorsCount[3]++;
                        case WHITE -> colorsCount[4]++;
                        case PINK -> colorsCount[5]++;
                    }
                }
            }
            for(int i = 0; i < COLORS; i++){
                if(colorsCount[i] > 0){
                    colorsAvaiable--;
                }
                colorsCount[i] = 0;
            }

            if(colorsAvaiable >= 0 && colorsAvaiable < MAX_DIFFERENT_COLORS_IN_COLUMNS){
                columnsThatSadisfiedCondition++;
                if(columnsThatSadisfiedCondition == NUMBER_OF_COLUMNS_FOR_CONDITION)
                    return true;
            }
            colorsAvaiable = MAX_DIFFERENT_COLORS_IN_COLUMNS;
        }
        return false;
    }
}
