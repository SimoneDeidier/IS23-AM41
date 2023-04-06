package it.polimi.ingsw.model;

public class CommonThreeColumns extends CommonTargetCard {

    private final static int MAX_DIFFERENT_COLORS_IN_COLUMNS = 3;
    private final static int NUMBER_OF_COLUMNS_FOR_CONDITION = 3;
    public CommonThreeColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] ricorrenze = new int[COLORS]; //array per contare le riccorenze di ciascun tipo
        int colorsAvaiable = MAX_DIFFERENT_COLORS_IN_COLUMNS;
        int columnsThatSadisfiedCondition = 0;

        for( int col = 0; col < COLUMNS; col ++ ){
            if( shelf.getItemByCoordinates(0, col) != null ){
                //se la colonna è piena
                for( int row = 0; row < ROWS; row++){
                    switch (shelf.getItemByCoordinates(row, col).getColor()) {
                        case GREEN -> ricorrenze[0]++;
                        case BLUE-> ricorrenze[1]++;
                        case LIGHT_BLUE -> ricorrenze[2]++;
                        case YELLOW -> ricorrenze[3]++;
                        case WHITE -> ricorrenze[4]++;
                        case PINK -> ricorrenze[5]++;
                    }
                }

            }
            int i = 0;
            while( i < COLORS ){
                if(ricorrenze[i] > 0){
                    colorsAvaiable--;
                }
                i++;
            }

            if(colorsAvaiable == 0){
                //la colonna soddifa la condizione
                columnsThatSadisfiedCondition++;
                colorsAvaiable = MAX_DIFFERENT_COLORS_IN_COLUMNS;
                if(columnsThatSadisfiedCondition == NUMBER_OF_COLUMNS_FOR_CONDITION)
                    return true;
            }


            for(int j = 0; j < COLORS; j++){
                ricorrenze[j] = 0;
            }

        }


        return false;
    }
}
