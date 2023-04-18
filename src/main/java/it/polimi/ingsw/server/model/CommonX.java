package it.polimi.ingsw.server.model;

public class CommonX extends CommonTargetCard {
    public CommonX(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        
        Item chosenColor;

        for( int row = 0; row < ROWS - 2; row++ ) {
            for (int col = 0; col < COLUMNS - 2; col++) {
                chosenColor = shelf.getItemByCoordinates(row, col);
                
                if(     shelf.getItemByCoordinates(row    , col    ) != null &&
                        shelf.getItemByCoordinates(row    , col + 2)!= null &&
                        shelf.getItemByCoordinates(row + 1, col + 1) != null &&
                        shelf.getItemByCoordinates(row + 2, col    ) != null &&
                        shelf.getItemByCoordinates(row + 2, col + 2) != null &&
                        
                        shelf.getItemByCoordinates(row    , col    ).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row    , col + 2).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 1, col + 1).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 2, col    ).getColor()== chosenColor.getColor() &&
                        shelf.getItemByCoordinates(row + 2, col + 2).getColor()== chosenColor.getColor()

                ){
                    return true;
                }

            }
        }
        
        return false;
    }
}
