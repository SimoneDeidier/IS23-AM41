package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.items.Item;

/**
 * CommonFourRowsClass represents a specific CommonTargetCard in the game
 */
public class CommonFourRows extends CommonTargetCard {

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonFourRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonFourRows";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {
        int countSamePerRow;
        int countAcceptableRows = 0;
        for (int i = 0; i < ROWS; i++) {
            countSamePerRow=0;
            for (int j = 0; j < COLUMNS; j++) {
                for (int k = 0; k < COLUMNS; k++) {
                    if(shelf.getItemByCoordinates(i, j)!=null && shelf.getItemByCoordinates(i, k)!=null) {
                        if (j != k && shelf.getItemByCoordinates(i, j).getColor() == shelf.getItemByCoordinates(i, k).getColor()) {
                            countSamePerRow++;
                            break;
                        }
                    }
                }
            }
            if (countSamePerRow >= 3)
                countAcceptableRows++;
        }
        return countAcceptableRows >= 4;
    }
}