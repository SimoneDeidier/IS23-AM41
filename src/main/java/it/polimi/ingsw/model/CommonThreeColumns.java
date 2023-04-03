package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommonThreeColumns implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {

        int[] ricorrenze = new int[6]; //array per contare le riccorenze di ciascun tipo
        int tipiDisponibili = 3;
        int colonneCorrette = 0;

        for( int col = 0; col < 5; col ++ ){
            if( shelf.getItemByCoordinates(0, col) != null ){
                //se la colonna è piena
                for( int row = 0; row < 6; row++){
                    switch (shelf.getItemByCoordinates(row, col).getType()) {
                        case TROPHIES -> ricorrenze[0]++;
                        case PLANTS -> ricorrenze[1]++;
                        case FRAMES -> ricorrenze[2]++;
                        case GAMES -> ricorrenze[3]++;
                        case BOOKS -> ricorrenze[4]++;
                        case CAT -> ricorrenze[5]++;
                    }
                }

            }
            int i = 0;
            while( i < 6 ){
                if(ricorrenze[i] > 0){
                    tipiDisponibili--;
                }
                i++;
            }

            if(tipiDisponibili == 0){
                //la colonna soddifa la condizione
                colonneCorrette++;
                if(colonneCorrette == 3)
                    return true;
            }


        }


        return false;
    }
}
