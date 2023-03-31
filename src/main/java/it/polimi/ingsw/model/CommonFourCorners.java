package it.polimi.ingsw.model;

public class CommonFourCorners implements CommonTargetCard {
    @Override
    public boolean check(Item[][] shelf) {
        if(shelf[0][0].getType() == shelf[0][4].getType()){
            if(shelf[0][0].getType() == shelf[5][4].getType()){
                if(shelf[0][0].getType() == shelf[5][0].getType()){
                    return true;
                }
            }
        }
        return false;
    }
}
