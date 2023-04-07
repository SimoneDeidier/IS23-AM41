package it.polimi.ingsw.model;

public class CommonTwoSquares extends CommonTargetCard {

    private final static int SQUARES_NEEDED = 2;

    public CommonTwoSquares(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        boolean[][] bitMask = new boolean[ROWS][COLUMNS];
        Item squaresColor = null;
        int squareFound = 0;


        for( int row = 0; row < ROWS - 1; row++ ){
            for( int col = 0; col < COLUMNS - 1; col ++) {
                if (!bitMask[row][col]) {
                    if(squareFound == 0){
                        squaresColor = shelf.getItemByCoordinates(row, col);
                    }

                    if (    shelf.getItemByCoordinates(row, col) != null &&
                            shelf.getItemByCoordinates(row, col + 1) != null &&
                            shelf.getItemByCoordinates(row + 1, col) != null &&
                            shelf.getItemByCoordinates(row + 1, col + 1) != null &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row    , col + 1).getColor() &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row + 1, col    ).getColor() &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row + 1, col + 1).getColor())
                    {
                        //first check if positions are null,
                        //if not, then check if all four items have the same color
                        //if so, we found a square

                        bitMask[row    ][ col    ] = true;
                        bitMask[row    ][ col + 1] = true;
                        bitMask[row + 1][ col    ] = true;
                        bitMask[row + 1][ col + 1] = true;


                        squareFound++;
                        if (squareFound == SQUARES_NEEDED)
                            return true;
                    }

                }
            }
        }







        return false;
    }

}
