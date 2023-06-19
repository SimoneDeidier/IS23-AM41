package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.items.ItemColor;

/**
 * CommonFourCornersClass represents a specific CommonTargetCard in the game
 */
public class CommonFourCorners extends CommonTargetCard {

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonFourCorners(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonFourCorners";
    }
    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {
        if(shelf.getItemByCoordinates(0, 0) != null &&
            shelf.getItemByCoordinates(0, COLUMNS - 1) != null &&
            shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1) != null &&
            shelf.getItemByCoordinates(ROWS - 1, 0) != null){

            ItemColor color=shelf.getItemByCoordinates(0, 0).getColor();
            return color == shelf.getItemByCoordinates(0, COLUMNS - 1).getColor()
                    && color == shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1).getColor()
                    && color == shelf.getItemByCoordinates(ROWS - 1, 0).getColor();
            }
        else {
            return false;
        }
    }
}
