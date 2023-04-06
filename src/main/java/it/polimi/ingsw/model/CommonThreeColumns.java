package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommonThreeColumns extends CommonTargetCard {
    public CommonThreeColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] ricorrenze = new int[6]; //array per contare le riccorenze di ciascun tipo
        int tipiDisponibili = 3;
        int colonneCorrette = 0;

        for( int col = 0; col < 5; col ++ ){
            if( shelf.getItemByCoordinates(0, col) != null ){
                //se la colonna è piena
                for( int row = 0; row < 6; row++){
                    switch (shelf.getItemByCoordinates(row, col).getColor()) {
                        case GREEN -> ricorrenze[0]++;
                        case BLUE-> ricorrenze[1]++;
                        case LIGHT_BLUE -> ricorrenze[2]++;
                        case YELLOW -> ricorrenze[3]++;
                        case WHITE -> ricorrenze[4]++;
                        case PINK -> ricorrenze[5]++;
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
                tipiDisponibili = 3;
                if(colonneCorrette == 3)
                    return true;
            }


            for(int j = 0; j < 6; j++){
                ricorrenze[j] = 0;
            }

        }


        return false;
    }
}
