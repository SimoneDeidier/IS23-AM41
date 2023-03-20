package it.polimi.ingsw.model;

public class Board {
    private Board instance;
    private Item[][] boardMatrix;
    private ItemsBag itemsBag;
    private Board Board();
    public getBoard(){
        if(instance==null)
            instance=new Board();
        return instance;
    }
    public refillBoard()
    {
        //Da implementare
    }

    public Item pickItem(int x, int y){
        Item savedItem;
        if(isPositionFree(x,y)==false && checkNumberofPlayers()){ //Check correttezza mossa, serve qui oppure è fatto prima?
            savedItem=boardMatrix[x][y];  //DA IMPLEMENTARE CHECKNUMBEROFPLAYERS
        //è una funzione che ci dovrebbe dire se la casella è usata nella partita o meno
            //(alcune caselle sono usabili solo con certo numero di giocatori
            //Molto lunga da scrivere con matrice da 45 posti, meglio cambiarla in 9x9?? Probabilmente yes
            boardMatrix[x][y]=null;
            return savedItem;
        }
        else{
            //Da implementare
        }

    }

    public boolean isPositionFree(int x,int y){  //Check correttezza mossa, serve qui oppure è fatto prima?
        if(boardMatrix[x][y]==null)
            return true; //Yes,it's free
        return false;
    }

}
