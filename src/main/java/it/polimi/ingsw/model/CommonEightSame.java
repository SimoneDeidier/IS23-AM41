package it.polimi.ingsw.model;

public class CommonEightSame implements CommonTargetCard {
    @Override
    public boolean check(Item[][] shelf) {

        int[] ricorrenze = new int[6]; //array per contare le riccorenze di ciascun tipo

        for(int row = 0; row < 6; row++){
            for(int col = 0; col < 5; col++){
                switch( shelf[row][col].getType() ){
                    case CAT -> ricorrenze[0]++;
                    case BOOKS -> ricorrenze[1]++;
                    case GAMES -> ricorrenze[2]++;
                    case FRAMES -> ricorrenze[3]++;
                    case PLANTS -> ricorrenze[4]++;
                    case TROPHIES -> ricorrenze[5]++;
                }
            }
        }

        for(int i = 0; i < 6; i++){
            if(ricorrenze[i] >= 8){
                return true;
            }
        }

        return false;
    }
}