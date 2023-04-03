package it.polimi.ingsw.model;

public class CommonDiagonal implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int unfound = 0;
        for (int k = 0; k <=1; k++){
            for(int i = k; i < k+5; i++)
                for (int j = 0; j < 4; j++)
                    if(shelf[i][j].getType() != shelf[i+1][j+1].getType()){
                        unfound = 1;
                        break;
                    }
            if (unfound==0){
                return true;
            }
            unfound = 0;
        }
        for (int k = 0; k <=1; k++){
            for(int i = k; i < k+5; i++)
                for (int j = 4; j >0; j--)
                    if(shelf[i][j].getType() != shelf[i+1][j-1].getType()){
                        unfound = 1;
                        break;
                    }
            if (unfound==0){
                return true;
            }
            unfound = 0;
        }
        return false;
    }
}
