package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.Shelf;


/**
 * CommonXClass represents a specific CommonTargetCard in the game
 */
public class CommonX extends CommonTargetCard {

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonX(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonX";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {
        
        Item chosenColor;

        for( int row = 0; row < ROWS - 2; row++ ) {
            for (int col = 0; col < COLUMNS - 2; col++) {
                chosenColor = shelf.getItemByCoordinates(row, col);
                
                if(     shelf.getItemByCoordinates(row    , col    ) != null &&
                        shelf.getItemByCoordinates(row    , col + 2)!= null &&
                        shelf.getItemByCoordinates(row + 1, col + 1) != null &&
                        shelf.getItemByCoordinates(row + 2, col    ) != null &&
                        shelf.getItemByCoordinates(row + 2, col + 2) != null &&
                        
                        shelf.getItemByCoordinates(row    , col    ).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row    , col + 2).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 1, col + 1).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 2, col    ).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 2, col + 2).getColor()== chosenColor.getColor()

                ){
                    return true;
                }

            }
        }
        
        return false;
    }
}
