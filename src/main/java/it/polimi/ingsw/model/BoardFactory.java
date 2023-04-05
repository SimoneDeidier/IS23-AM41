package it.polimi.ingsw.model;

import javax.sql.rowset.RowSetWarning;
import java.util.List;

public abstract class BoardFactory {

    private final static int ROWS = 9;
    private final static int COLUMNS = 9;

    protected Item[][] boardMatrix= new Item[ROWS][COLUMNS];
    protected boolean[][] bitMask;
    protected ItemsBag itemsBag;


    public boolean getBitMaskElement(int i,int j) {
        return bitMask[i][j];
    }

    public Item getBoardMatrixElement(int i,int j){
        return boardMatrix[i][j];
    }


    public  void refillBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (bitMask[i][j] && boardMatrix[i][j] == null) {
                    boardMatrix[i][j] = itemsBag.pickItem();
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
        bitMask[x][y] = true;
        return item;
    }

    public boolean check(List<int[]> list){
        int size = list.size();
        if(size < 1 || size>3) {
            return false;
        }
        //Check for free side
        for(int[] i : list){
            if(!hasFreeSide(i[0],i[1])){
                return false;
            }
        }
        return checkInLine(list);
    }

    public boolean hasFreeSide(int i,int j){
        //Check border
        if(i==0||i==ROWS||j==0||j==COLUMNS){
            return true;
        }
        //Check up
        if(getBitMaskElement(i-1,j) && getBoardMatrixElement(i-1,j)==null){
            return true;
        }
        //Check down
        if(getBitMaskElement(i+1,j) && getBoardMatrixElement(i+1,j)==null){
            return true;
        }
        //Check left
        if(getBitMaskElement(i,j-1) && getBoardMatrixElement(i,j-1)==null){
            return true;
        }
        //Check right
        return getBitMaskElement(i, j + 1) && getBoardMatrixElement(i, j + 1) == null;

    }

    public boolean checkInLine(List<int[]> list){

        return true;

    }

}
