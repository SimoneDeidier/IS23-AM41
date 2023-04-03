package it.polimi.ingsw.model;

public class CommonX implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        
        Item tipoSelezionato;

        for( int row = 0; row < 4; row++ ) {
            for (int col = 0; col < 3; col++) {
                tipoSelezionato = shelf[row][col];
                
                if(     shelf[row    ][col    ] != null &&
                        shelf[row    ][col + 2] != null &&
                        shelf[row + 1][col + 1] != null &&
                        shelf[row + 2][col    ] != null &&
                        shelf[row + 2][col + 2] != null &&
                        
                        shelf[row    ][col    ].getType()!= tipoSelezionato.getType() &&
                        shelf[row    ][col + 2].getType()!= tipoSelezionato.getType() &&
                        shelf[row + 1][col + 1].getType()!= tipoSelezionato.getType() &&
                        shelf[row + 2][col    ].getType()!= tipoSelezionato.getType() &&
                        shelf[row + 2][col + 2].getType()!= tipoSelezionato.getType()

                ){
                    return true;
                }

            }
        }
        
        return false;
    }
}
