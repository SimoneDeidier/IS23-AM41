package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.Shelf;

public class CommonFourCorners extends CommonTargetCard {
    public CommonFourCorners(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        if( shelf.freeSpaces(0) == 0 && shelf.freeSpaces(COLUMNS - 1) == 0 ){
            ItemColor type= shelf.getItemByCoordinates(0, 0).getColor();
            return type == shelf.getItemByCoordinates(0, COLUMNS - 1).getColor() && type == shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1).getColor() && type == shelf.getItemByCoordinates(ROWS - 1, 0).getColor();
        }
        return false;
    }
}
