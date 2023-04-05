package it.polimi.ingsw.model;

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

    //used for testing checkInLine in TwoplayerBoardTest
    public void setBoardMatrixElement(int i,int j,Item item){
        boardMatrix[i][j]=item;
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

    public boolean checkMove(List<int[]> list){
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
        int x1=list.get(0)[0],y1=list.get(0)[1];
        if(list.size()==2){
            int x2=list.get(1)[0],y2=list.get(1)[1];

            if((x1 != (x2 - 1)) && (x1 != (x2 + 1)) && (y1 != (y2 + 1)) && (y1 != (y2 - 1))) {
                return false;
            }
        }

        if(list.size()==3){
            int x2=list.get(1)[0],y2=list.get(1)[1],x3=list.get(2)[0],y3=list.get(2)[1];
            if(x1 == (x2 + 1) && (x3==x2-1 || x3==x1+1)){
                return true;
            }
            if(x1 == (x2 - 1) && (x3==x1-1 || x3==x2+1)){
                return true;
            }
            if(y1 == (y2 + 1) && (y3==y2-1 || y3==y1+1)){
                return true;
            }
            return y1 == (y2 - 1) && (y3 == y2 + 1 || y3 == y1 - 1);
        }
        return true;

    }

}
