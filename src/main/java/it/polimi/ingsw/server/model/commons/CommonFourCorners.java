package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.items.ItemColor;

public class CommonFourCorners extends CommonTargetCard {
    public CommonFourCorners(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonFourCorners";
    }
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
