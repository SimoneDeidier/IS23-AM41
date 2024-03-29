package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

/**
 * CommonTwoRowsClass represents a specific CommonTargetCard in the game
 */
public class CommonTwoRows extends CommonTargetCard {
    private final static int NUMBER_OF_ROWS_FOR_CONDITION = 2;
    private final static int MAX_OCCURENCE_PER_COLOR = 1;

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonTwoRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonTwoRows";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {

        int rowsThatSatisfiedCondition = 0;
        boolean rowSatisfiedCondition = true;
        int[] counterOccurence = new int[COLORS];
        int rows_to_check = 0;
        int[] columnFreeSpaces = new int[COLUMNS];

        int max = 0;
        for(int col = 0; col < COLUMNS; col++){
            if( shelf.freeSpaces(col) > max )
                max = shelf.freeSpaces(col);
        }
        rows_to_check = COLUMNS - max + 1;

        for(int row = 0 ; row < rows_to_check ; row++){
            for( int col = 0; col < COLUMNS; col++){
                switch (shelf.getItemByCoordinates(ROWS - row - 1, col).getColor()) {
                    case PINK -> counterOccurence[0]++;
                    case BLUE -> counterOccurence[1]++;
                    case LIGHT_BLUE -> counterOccurence[2]++;
                    case YELLOW -> counterOccurence[3]++;
                    case GREEN -> counterOccurence[4]++;
                    case WHITE -> counterOccurence[5]++;
                }
            }

            for( int i = 0; i < COLORS; i++ ){
                if(counterOccurence[i] > MAX_OCCURENCE_PER_COLOR){
                    rowSatisfiedCondition = false;
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
