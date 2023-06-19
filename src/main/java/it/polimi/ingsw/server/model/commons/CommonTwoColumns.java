package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.items.ItemColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CommonTwoColumnsClass represents a specific CommonTargetCard in the game
 */
public class CommonTwoColumns extends CommonTargetCard {

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonTwoColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonTwoColumns";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {
        int columnFound=0;
        for(int i=0;i<COLUMNS;i++){
            List<ItemColor> differentItemsInColumn=new ArrayList<>();
            for(int k=0;k<ROWS;k++){
                if(shelf.getItemByCoordinates(k,i)!= null && !differentItemsInColumn.contains(shelf.getItemByCoordinates(k,i).getColor())){
                    differentItemsInColumn.add(shelf.getItemByCoordinates(k,i).getColor());
                }
                else{
                    break;
                }
            }
            if(differentItemsInColumn.size()==6) {
                columnFound++;
                if(columnFound==2)
                    return true;
            }
        }
        return false;
    }
}
