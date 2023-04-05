package it.polimi.ingsw.model;

public class CommonTwoSquares extends CommonTargetCard {
    public CommonTwoSquares(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        boolean[][] bitMask = new boolean[6][5];
        Item squaresColor = null;
        int squareFound = 0;
        boolean[] colorCheck = new boolean[6];
        for(int j = 0; j < 6; j++){
            for( int row = 0; row < 5; row++ ){
                for( int col = 0; col < 4; col ++) {
                    if (!bitMask[row][col]) {
                        if(squareFound == 0){
                            squaresColor = shelf.getItemByCoordinates(row, col);
                        }


                        if (shelf.getItemByCoordinates(row, col + 1) != null && shelf.getItemByCoordinates(row + 1, col) != null && shelf.getItemByCoordinates(row + 1, col + 1) != null &&
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
                            if (squareFound == 2)
                                return true;
                        }

                    }
                }
            }
        }





        return false;
    }
}
