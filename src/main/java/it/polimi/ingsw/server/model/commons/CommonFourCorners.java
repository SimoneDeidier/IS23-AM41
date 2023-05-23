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

        if(shelf.getItemByCoordinates(0, 0).getColor() != null &&
                shelf.getItemByCoordinates(0, COLUMNS - 1).getColor() != null &&
                shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1).getColor() != null &&
                shelf.getItemByCoordinates(ROWS - 1, 0).getColor() != null){
            return shelf.getItemByCoordinates(0, 0).getColor() == shelf.getItemByCoordinates(0, COLUMNS - 1).getColor() && shelf.getItemByCoordinates(0, 0).getColor() == shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1).getColor() && shelf.getItemByCoordinates(0, 0).getColor() == shelf.getItemByCoordinates(ROWS - 1, 0).getColor();
        } else {
            return false;
        }
    }
}
