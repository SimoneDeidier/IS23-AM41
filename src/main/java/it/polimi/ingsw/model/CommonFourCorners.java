package it.polimi.ingsw.model;

public class CommonFourCorners implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        if(shelf.getItemByCoordinates(0, 0).getType() == shelf.getItemByCoordinates(0, 4).getType()){
            if(shelf.getItemByCoordinates(0, 0).getType() == shelf.getItemByCoordinates(5, 4).getType()){
                if(shelf.getItemByCoordinates(0, 0).getType() == shelf.getItemByCoordinates(5, 0).getType()){
                    return true;
                }
            }
        }
        return false;
    }
}
