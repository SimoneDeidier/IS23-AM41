package it.polimi.ingsw.model;

public class CommonX extends CommonTargetCard {
    public CommonX(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        
        Item tipoSelezionato;

        for( int row = 0; row < 4; row++ ) {
            for (int col = 0; col < 3; col++) {
                tipoSelezionato = shelf.getItemByCoordinates(row, col);
                
                if(     shelf.getItemByCoordinates(row    , col    ) != null &&
                        shelf.getItemByCoordinates(row    , col + 2)!= null &&
                        shelf.getItemByCoordinates(row + 1, col + 1) != null &&
                        shelf.getItemByCoordinates(row + 2, col    ) != null &&
                        shelf.getItemByCoordinates(row + 2, col + 2) != null &&
                        
                        shelf.getItemByCoordinates(row    , col    ).getType()== tipoSelezionato.getType() &&
                        shelf.getItemByCoordinates(row    , col + 2).getType()== tipoSelezionato.getType() &&
                        shelf.getItemByCoordinates(row + 1, col + 1).getType()== tipoSelezionato.getType() &&
                        shelf.getItemByCoordinates(row + 2, col    ).getType()== tipoSelezionato.getType() &&
                        shelf.getItemByCoordinates(row + 2, col + 2).getType()== tipoSelezionato.getType()

                ){
                    return true;
                }

            }
        }
        
        return false;
    }
}
