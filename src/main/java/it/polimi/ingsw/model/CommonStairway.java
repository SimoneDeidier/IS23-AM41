package it.polimi.ingsw.model;

public class CommonStairway extends CommonTargetCard {
    public CommonStairway(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int neededFreeSpaces = 0;
        boolean decrescente = false;
        int offset = 0;

        /*
        * i casi possibili sono 4:
        *
        * 1) le colonne sono occupate così: 6 - 5 - 4 - 3 - 2
        *
        * 2) le colonne sono occupate così: 5 - 4 - 3 - 2 - 1
        *
        * 3) le colonne sono occupate così: 2 - 3 - 4 - 5 - 6
        *
        * 4) le colonne sono occupate così: 1 - 2 - 3 - 4 - 5
        *
        * */

        if( shelf.getItemByCoordinates(0, 0) == null ){
            if( shelf.getItemByCoordinates(1, 0) == null){
                if( shelf.getItemByCoordinates(0, 4) == null){
                    if( shelf.getItemByCoordinates(1, 4) == null){
                        //non siamo in nessun caso possibile
                        return false;
                    }
                    else{
                        //allora controlliamo il caso 4
                        offset = 1;
                    }
                }
                else{
                    //allora controlliamo il caso 3

                }
            }
            else{
                //allora controlliamo il caso 2
                decrescente = true;
                offset = 1;
            }

        }
        else{
            //allora controlliamo il caso 1
            decrescente = true;
        }

        if(decrescente){
            for(int i = 1; i < 5; i++){
                if( !(shelf.getItemByCoordinates(i + offset, i) != null && shelf.getItemByCoordinates(i + offset - 1, i) == null )){
                    //se non è vero che non c'è un elemento nella colonna i e nella riga i (+ 1 di offset nel caso 2) e la riga sopra non è vuota, allora la condizione non è soddisfatta
                    return false;
                }
            }
        }
        else{

            for(int i = 3; i >= 0; i--){
                if( !(shelf.getItemByCoordinates(4 - i + offset, i) != null && shelf.getItemByCoordinates(3 - i + offset, i) == null )){
                    //se non è vero che non c'è un elemento nella colonna i e nella riga i (+ 1 di offset nel caso 2) e la riga sopra non è vuota, allora la condizione non è soddisfatta
                    return false;
                }
            }
        }



        return true;
    }
}
