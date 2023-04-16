package it.polimi.ingsw.model;

public class CommonFourCorners extends CommonTargetCard {
    public CommonFourCorners(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        ItemColor type= shelf.getItemByCoordinates(0, 0).getColor();
        return type == shelf.getItemByCoordinates(0, COLUMNS - 1).getColor() && type == shelf.getItemByCoordinates(ROWS - 1, COLUMNS - 1).getColor() && type == shelf.getItemByCoordinates(ROWS - 1, 0).getColor();
    }
}
