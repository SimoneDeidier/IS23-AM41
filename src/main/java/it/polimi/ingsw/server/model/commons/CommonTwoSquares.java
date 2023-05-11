package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.Shelf;

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
        boolean perimeterClean = true;

        for( int row = 0; row < ROWS - 1; row++ ){
            for( int col = 0; col < COLUMNS - 1; col ++) {
                perimeterClean = true;
                if (!bitMask[row][col]) {
                    squaresColor = shelf.getItemByCoordinates(row, col);

                    if (    shelf.getItemByCoordinates(row, col) != null &&
                            shelf.getItemByCoordinates(row, col + 1) != null &&
                            shelf.getItemByCoordinates(row + 1, col) != null &&
                            shelf.getItemByCoordinates(row + 1, col + 1) != null &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row    , col + 1).getColor() &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row + 1, col    ).getColor() &&
                            squaresColor.getColor() == shelf.getItemByCoordinates(row + 1, col + 1).getColor())
                    {

                        //checkPerimeter
                        if( row < ROWS - 2 ){
                            //check down
                            if( shelf.getItemByCoordinates(row + 2, col).getColor() == squaresColor.getColor() || shelf.getItemByCoordinates(row + 2, col + 1).getColor() == squaresColor.getColor() ) {
                                perimeterClean = false;
                            }
                        }
                        if( row > 0 ){
                            //check up
                            if(shelf.getItemByCoordinates(row - 1, col) != null && shelf.getItemByCoordinates(row - 1, col + 1) != null &&
                              (shelf.getItemByCoordinates(row - 1, col).getColor() == squaresColor.getColor() || shelf.getItemByCoordinates(row - 1, col + 1).getColor() == squaresColor.getColor() ) ){
                                perimeterClean = false;
                            }

                        }
                        if( col < COLUMNS - 3 ){
                            //check right
                            if(shelf.getItemByCoordinates(row , col + 2) != null && shelf.getItemByCoordinates(row + 1, col + 2) != null &&
                              (shelf.getItemByCoordinates(row, col + 2).getColor() == squaresColor.getColor() || shelf.getItemByCoordinates(row + 1, col + 2).getColor() == squaresColor.getColor() ) ){
                                perimeterClean = false;
                            }
                        }
                        if( col != 0 ){
                            //check left
                            if(shelf.getItemByCoordinates(row , col - 1) != null && shelf.getItemByCoordinates(row + 1, col - 1) != null &&
                                    (shelf.getItemByCoordinates(row, col - 1).getColor() == squaresColor.getColor() || shelf.getItemByCoordinates(row + 1, col - 1).getColor() == squaresColor.getColor() ) ){
                                perimeterClean = false;
                            }
                        }


                        if( perimeterClean ){
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
        }







        return false;
    }



}
