package it.polimi.ingsw.model;

public class CommonTwoRows extends CommonTargetCard {
    public CommonTwoRows(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] ricorrenze = new int[6]; //array per contare le riccorenze di ciascun tipo
        int RigheCorrette = 0;
        boolean NoDuplicati = true;


        for(int row = 0; row < 5 ; row++){
            int col = 0;
            while( col < 6 ){
                if(shelf.getItemByCoordinates(row, col) != null) {
                    switch (shelf.getItemByCoordinates(row, col).getType()) {
                        case TROPHIES -> ricorrenze[0]++;
                        case PLANTS -> ricorrenze[1]++;
                        case FRAMES -> ricorrenze[2]++;
                        case GAMES -> ricorrenze[3]++;
                        case BOOKS -> ricorrenze[4]++;
                        case CAT -> ricorrenze[5]++;
                    }
                }
                col++;
            }
            
            int i = 0;
            while( i < 6 ){
                //se nell'array ricorrenza c'è una posione con più di uno significa che c'è un tipo in più
                if(ricorrenze[i] > 1){
                    NoDuplicati = false;
                    break;
                }
                i++;
            }
            
            if (NoDuplicati){
                //se non ci sono duplicati la riga soddisfa la condizione, dunque aggiorniamo il contatore delle colonne che soddisfano la condizione
                RigheCorrette++;
                if( RigheCorrette == 2)
                    return true;
            }

            NoDuplicati = false;

        }
        return false;
        
    }
}
