package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;

import java.io.Serializable;
import java.util.List;

public abstract class BoardFactory implements Serializable {

    protected final static int ROWS = 9;
    protected final static int COLUMNS = 9;
    protected Item[][] boardMatrix= new Item[ROWS][COLUMNS];
    protected boolean[][] bitMask;
    protected ItemsBag itemsBag;


    public boolean getBitMaskElement(int i,int j) {
        return bitMask[i][j];
    }

    public Item getBoardMatrixElement(int i,int j){
        return boardMatrix[i][j];
    }

    public int getBoardNumberOfRows(){
        return ROWS;
    }
    public int getBoardNumberOfColumns(){
        return COLUMNS;
    }

    public  void refillBoard() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (bitMask[i][j]) {
                    if (boardMatrix[i][j] == null) {
                        boardMatrix[i][j] = itemsBag.pickItem();
                    }
                }
                else{
                    boardMatrix[i][j]=null;
                    }
            }
        }
    }

    public boolean[][] createBitMask() {
        boolean[][] bitMask = new boolean[ROWS][COLUMNS];

        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                bitMask[i][j] = false;
            }
        }
        for(int i = 3; i < 5; i++) {
            bitMask[1][i] = true;
        }
        for(int i = 3; i < 6; i++) {
            bitMask[2][i] = true;
        }
        for(int i = 2; i < 8; i++) {
            bitMask[3][i] = true;
        }
        for(int i = 1; i < 8; i++) {
            bitMask[4][i] = true;
        }
        for(int i = 1; i < 7; i++) {
            bitMask[5][i] = true;
        }
        for(int i = 3; i < 6; i++) {
            bitMask[6][i] = true;
        }
        for(int i = 4; i < 6; i++) {
            bitMask[7][i] = true;
        }

        return bitMask;
    }

    public Item pickItem(int x, int y) throws NullItemPickedException, InvalidBoardPositionException {
        //check server-side of the validity of the move
        if (!bitMask[x][y]) {
            throw new InvalidBoardPositionException();
        }
        if(boardMatrix[x][y] == null) {
            throw new NullItemPickedException();
        }
        Item item = new Item(boardMatrix[x][y].getColor());
        boardMatrix[x][y] = null;
        return item;
    }

    public boolean checkMove(List<int[]> list){
        int size = list.size();
        if(size < 1 || size>3) {
            return false;
        }
        //Check for validity of each position
        for(int[] i : list){
            if(!getBitMaskElement(i[0],i[1])){
                return false;
            }
        }
        //Check if all the elements have a free side
        for(int[] i : list){
            if(!hasFreeSide(i[0],i[1])){
                return false;
            }
        }
        //Check if the elements are in line
        return checkInLine(list);
    }

    public boolean hasFreeSide(int i,int j){
        //Check up
        if(getBoardMatrixElement(i-1,j)==null){
            return true;
        }
        //Check down
        if(getBoardMatrixElement(i+1,j)==null){
            return true;
        }
        //Check left
        if(getBoardMatrixElement(i,j-1)==null){
            return true;
        }
        //Check right
        return getBoardMatrixElement(i, j + 1) == null;

    }

    public boolean itemHasAllFreeSide(int i,int j){
        return (getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i+1,j)==null && getBoardMatrixElement(i,j-1)==null && getBoardMatrixElement(i, j + 1) == null);
    }

    public boolean checkInLine(List<int[]> list){
        int x1=list.get(0)[0],y1=list.get(0)[1];
        if(list.size()==2){
            int x2=list.get(1)[0],y2=list.get(1)[1];
            return (x1 == x2 - 1 && y1 == y2) || (x1 == x2 + 1 && y1 == y2) || (x1 == x2 && y1 == y2 - 1) || ((x1 == x2 && y1 == y2 + 1));
        }

        if(list.size()==3){
            int x2=list.get(1)[0],y2=list.get(1)[1],x3=list.get(2)[0],y3=list.get(2)[1];
            if(x1 == (x2 + 1) && (x3==x2-1 || x3==x1+1) && y1==y2 && y2==y3){
                return true;
            }
            if(x1 == (x2 - 1) && (x3==x1-1 || x3==x2+1) && y1==y2 && y2==y3){
                return true;
            }
            if(y1 == (y2 + 1) && (y3==y2-1 || y3==y1+1) && x1==x2 && x2==x3){
                return true;
            }
            return y1 == (y2 - 1) && (y3 == y2 + 1 || y3 == y1 - 1) && x1 == x2 && x2 == x3;
        }
        return true; //case where list.size==1

    }

    public void setBoardMatrixElement(Item item,int i,int j){
        boardMatrix[i][j]=item;
    }

    public void resetBoard(){
        boardMatrix= new Item[ROWS][COLUMNS];
    }
}
