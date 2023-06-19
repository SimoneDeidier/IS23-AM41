package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

/**
 * CommonEightSameClass represents a specific CommonTargetCard in the game
 */
public class CommonEightSame extends CommonTargetCard {

    private final int  NUMBER_COLOR_EQUALS = 8;

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonEightSame(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonEightSame";
    }
    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {

        int[] countColorOccurence = new int[COLORS];

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                if(shelf.getItemByCoordinates(row, col) != null){
                    switch( shelf.getItemByCoordinates(row, col).getColor() ){
                        case GREEN -> countColorOccurence[0]++;
                        case YELLOW -> countColorOccurence[1]++;
                        case BLUE -> countColorOccurence[2]++;
                        case LIGHT_BLUE -> countColorOccurence[3]++;
                        case PINK -> countColorOccurence[4]++;
                        case WHITE -> countColorOccurence[5]++;
                    }
                }

            }
        }

        for(int i = 0; i < COLORS; i++){
            if(countColorOccurence[i] >= NUMBER_COLOR_EQUALS){
                return true;
            }
        }

        return false;
    }
}
