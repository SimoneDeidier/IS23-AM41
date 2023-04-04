package it.polimi.ingsw.model;

import javax.sql.rowset.RowSetWarning;

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
                if (bitMask[i][j] == true && boardMatrix[i][j] == null) {
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
        if (bitMask[x][y] == false) {
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

}
