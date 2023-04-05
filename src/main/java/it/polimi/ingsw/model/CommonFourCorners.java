package it.polimi.ingsw.model;

public class CommonFourCorners extends CommonTargetCard {
    public CommonFourCorners(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        ItemType type= shelf.getItemByCoordinates(0, 0).getType();
        return type == shelf.getItemByCoordinates(0, 4).getType() && type == shelf.getItemByCoordinates(5, 4).getType() && type == shelf.getItemByCoordinates(5, 0).getType();
    }
}
