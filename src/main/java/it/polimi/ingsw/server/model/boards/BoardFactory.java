package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.exceptions.InvalidBoardPositionException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;
import it.polimi.ingsw.server.model.exceptions.NullItemPickedException;

import java.io.Serializable;
import java.util.List;


/**
 * Class that represents the abstract concept of a Board, providing nearly all the methods for the boards,
 * since only the bitmask is different among the three different boards
 */
public abstract class BoardFactory implements Serializable {

    protected final static int ROWS = 9;
    protected final static int COLUMNS = 9;
    protected Item[][] boardMatrix= new Item[ROWS][COLUMNS];
    protected boolean[][] bitMask;
    protected ItemsBag itemsBag;


    /**
     * @param i represents the line of the element sought
     * @param j represents the column of the element sought
     * @return the boolean in the position (i,j) in the bitMask
     */
    public boolean getBitMaskElement(int i,int j) {
        return bitMask[i][j];
    }

    /**
     * @param i represents the line of the element sought
     * @param j represents the column of the element sought
     * @return the Item in the position (i,j) in the boardMatrix
     */
    public Item getBoardMatrixElement(int i,int j){
        if(i<ROWS && i>=0 && j<COLUMNS && j>=0)
            return boardMatrix[i][j];
        return null;
    }

    /**
     * @return the number of rows of the board, used to parameterize the code
     */
    public int getBoardNumberOfRows(){
        return ROWS;
    }

    /**
     * @return the number of columns of the board, used to parameterize the code
     */
    public int getBoardNumberOfColumns(){
        return COLUMNS;
    }

    /**
     * Fills the board, placing items where the bitMask says it's a valid position but there is no Item at the moment
     */
    public  void refillBoard() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (bitMask[i][j]) {
                    if (boardMatrix[i][j] == null) {
                        boardMatrix[i][j] = itemsBag.pickItem();
                    }
                }
                else {
                    boardMatrix[i][j]=null;
                }
            }
        }
    }

    /**
     * Starts the creation of the bitMask with the TwoPlayerBoard bitMask.
     * The bitMask for three or four players starts from this one to add more valid positions to their bitmask
     * @return TwoPlayerBoard's bitMask
     */
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

    /**
     * Called during the procedure for executing a Player's move
     * @param x represents the line of the element sought
     * @param y represents the column of the element sought
     * @return the Item picked
     * @throws NullItemPickedException if in the BoardMatrix the element is null
     * @throws InvalidBoardPositionException if in the bitMask the element is null
     */
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

    /**
     * Server-side check for the validity of the move according to the game's rules
     * @param list contains the positions of the Items a Player wants to pick
     * @return true if the move is valid, false otherwise
     */
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

    /**
     * Used during checkMove to check if all the elements have a free side
     * @param i represents the line of the element sought
     * @param j represents the column of the element sought
     * @return true if the element in position i,j has at least one free sides, false otherwise
     */
    public boolean hasFreeSide(int i,int j){
        //Check up
        if(i>0 && getBoardMatrixElement(i-1,j)==null){
            return true;
        }
        //Check down
        if(i<ROWS-1 && getBoardMatrixElement(i+1,j)==null){
            return true;
        }
        //Check left
        if(j>0 && getBoardMatrixElement(i,j-1)==null){
            return true;
        }
        //Check right
        return j<COLUMNS-1 && getBoardMatrixElement(i, j + 1) == null;

    }

    /**
     * Used to check whether a position on the board, containing an item, has all free slots around it
     * @param i represents the line of the element sought
     * @param j represents the column of the element sought
     * @return true if the Item in position i,j has all free sides, false otherwise
     */
    public boolean itemHasAllFreeSide(int i,int j){
        if(i==0){
            if(j==0){
                if(getBoardMatrixElement(i+1,j)==null && getBoardMatrixElement(i,j+1)==null)
                    return true;
            }
            else if(j==COLUMNS-1){
                if(getBoardMatrixElement(i,j-1)==null && getBoardMatrixElement(i+1,j)==null)
                    return true;
            }
            return getBoardMatrixElement(i,j-1)==null && getBoardMatrixElement(i,j+1)==null && getBoardMatrixElement(i+1,j)==null;
        }
        if(j==0){
            if(i==8){
                if(getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i,j+1)==null)
                    return true;
            }
            return getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i+1,j)==null && getBoardMatrixElement(i,j+1)==null;
        }
        if(i==8){
            if(j==8){
                if(getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i,j-1)==null)
                    return true;
            }
            return getBoardMatrixElement(i,j-1)==null && getBoardMatrixElement(i,j+1)==null && getBoardMatrixElement(i-1,j)==null;
        }
        if(j==8){
            return getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i+1,j)==null && getBoardMatrixElement(i,j-1)==null;
        }
        return getBoardMatrixElement(i-1,j)==null && getBoardMatrixElement(i+1,j)==null
                && getBoardMatrixElement(i,j-1)==null && getBoardMatrixElement(i,j+1)==null;
    }

    /**
     * Used during CheckMove to make sure that all picked element are in a straight line
     * @param list contains the positions of the Items a Player wants to pick
     * @return true if all the items are in a straight line, false otherwise
     */
    public boolean checkInLine(List<int[]> list){
        int x1=list.get(0)[0],y1=list.get(0)[1];
        if(list.size()==2){
            int x2=list.get(1)[0],y2=list.get(1)[1];
            return (x1 == x2 - 1 && y1 == y2) || (x1 == x2 + 1 && y1 == y2)
                    || (x1 == x2 && y1 == y2 - 1) || ((x1 == x2 && y1 == y2 + 1));
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
            if(y1 == (y2 - 1) && (y3 == y2 + 1 || y3 == y1 - 1) && x1 == x2 && x2 == x3)
                return true;
            if(x1==x2+2 &&  x1==x3+1 && y1==y2 && y2==y3)
                return true;
            if(x1== (x2-2) && x1==x3-1 && y1==y2 && y2==y3)
                return true;
            if(y1 == (y2+2) && y1 == y3+1 && x1==x2 && x2==x3)
                return true;
            if(y1 == y2-2 && y1 == y3-1 && x1==x2 && x2==x3)
                return true;
            return false;
        }
        return true; //case where list.size==1

    }

    /**
     * Used for testing purposes to simulate different borderline scenarios
     * @param item the Item we want to be set
     * @param i represents the line where we want to set the Item
     * @param j represents the column where we want to set the Item
     */
    public void setBoardMatrixElement(Item item,int i,int j){
        boardMatrix[i][j]=item;
    }

    /**
     * Used for backup and testing purposes
     * @return the boardMatrix of the Board
     */
    public Item[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Used for backup and testing purposes
     * @return the bitMask of the Board
     */
    public boolean[][] getBitMask() {
        return bitMask;
    }

    /**
     * Used for backup and testing purposes
     * @param boardMatrix, the specific boardMatrix to be set in the Board
     */
    public void setBoardMatrix(Item[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    /**
     * Used for backup and testing purposes
     * @param bitMask, the specific bitMask to be set in the Board
     */
    public void setBitMask(boolean[][] bitMask) {
        this.bitMask = bitMask;
    }
}
