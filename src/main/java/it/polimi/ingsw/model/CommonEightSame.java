package it.polimi.ingsw.model;

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
                    switch( shelf.getItemByCoordinates(row, col).getType() ){
                        case CAT -> countColorOccurence[0]++;
                        case BOOKS -> countColorOccurence[1]++;
                        case GAMES -> countColorOccurence[2]++;
                        case FRAMES -> countColorOccurence[3]++;
                        case PLANTS -> countColorOccurence[4]++;
                        case TROPHIES -> countColorOccurence[5]++;
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
