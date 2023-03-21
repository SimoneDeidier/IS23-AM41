package it.polimi.ingsw.model;

public abstract class BoardFactory {

    protected Item[][] boardMatrix;
    protected int[][] bitMask;
    protected ItemsBag itemsBag;

    public  void refillBoard() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(bitMask[i][j] == 1 && boardMatrix[i][j] == null) {
                    boardMatrix[i][j] = itemsBag.pickItem();
                }
            }
        }
    }
    public int[][] createBitMask() {
        int[][] bitMask = new int[9][9];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                bitMask[i][j] = 0;
            }
        }
        for(int i = 3; i < 5; i++) {
            bitMask[1][i] = 1;
        }
        for(int i = 2; i < 6; i++) {
            bitMask[2][i] = 1;
        }
        for(int i = 1; i < 8; i++) {
            bitMask[3][i] = 1;
            bitMask[4][i] = 1;
        }
        for(int i = 1; i < 7; i++) {
            bitMask[5][i] = 1;
        }
        for(int i = 3; i < 6; i++) {
            bitMask[6][i] = 1;
        }
        for(int i = 4; i < 6; i++) {
            bitMask[7][i] = 1;
        }

            return bitMask;
    }
    public Item pickItem(int x, int y) throws NullItemPickedException, InvalidBoardPositionException {
        //check server-side of the validity of the move
        if (bitMask[x][y] == 1) {
            if(boardMatrix[x][y] == null) {
                throw new NullItemPickedException();
            }
            else {
                //però il pick dovrebbe essere di tutti gli item insieme, perchè pickiamo un solo item?
                Item item = new Item(boardMatrix[x][y]);
                boardMatrix[x][y] = null;
                bitMask[x][y] = 1;
                return item;
            }
        }
        else {
            throw new InvalidBoardPositionException();
        }
    }

}
