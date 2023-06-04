package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

public class CommonStairway extends CommonTargetCard {
    public CommonStairway(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonStairway";
    }
    @Override
    public boolean check(Shelf shelf) {
        boolean first = true, second = true, third = true, fourth = true, check = true;
        int[] columnSpaces = new int[COLUMNS];

        for (int col = 0; col < COLUMNS; col++) {
            columnSpaces[col] = shelf.freeSpaces(col);
        }
        //check which of the four possible diagonals
        for (int col = 0; col < COLUMNS; col++) {
            if (columnSpaces[col] > col) {
                first = false;
            }
            if (columnSpaces[col] > col + 1) {
                second = false;
            }
            if (columnSpaces[col] > ROWS - 2 - col) {
                third = false;
            }
            if (columnSpaces[col] > ROWS - 1 - col) {
                fourth = false;
            }
        }

        return first || second || third || fourth;
    }
}

