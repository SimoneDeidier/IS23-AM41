package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

public class CommonEightSame extends CommonTargetCard {

    private final int  NUMBER_COLOR_EQUALS = 8;

    public CommonEightSame(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] countColorOccurence = new int[COLORS];

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                if(shelf.getItemByCoordinates(row, col) != null){
                    switch( shelf.getItemByCoordinates(row, col).getColor() ){
                        case GREEN -> countColorOccurence[0]++;
                        case YELLOW -> countColorOccurence[1]++;
                        case BLUE -> countColorOccurence[2]++;
                        case LIGHT_BLUE -> countColorOccurence[3]++;
                        case PINK -> countColorOccurence[4]++;
                        case WHITE -> countColorOccurence[5]++;
                    }
                }

            }
        }

        for(int i = 0; i < COLORS; i++){
            if(countColorOccurence[i] >= NUMBER_COLOR_EQUALS){
                return true;
            }
        }

        return false;
    }
}
