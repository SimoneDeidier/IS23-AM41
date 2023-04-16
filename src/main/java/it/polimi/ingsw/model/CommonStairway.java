package it.polimi.ingsw.model;

public class CommonStairway extends CommonTargetCard {
    public CommonStairway(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] columnSpaces = new int[COLUMNS];

        for (int col = 0; col < COLUMNS; col++) {
            columnSpaces[col] = shelf.freeSpaces(col);
        }

        if (columnSpaces[0] == 0 || columnSpaces[0] == 1) {
            //decrementing case staircase: left to right
            for (int col = 1; col < COLUMNS; col++) {
                if (columnSpaces[col] - 1 != columnSpaces[col - 1]) {
                    return false;
                }
            }
            return true;
        } else if (columnSpaces[COLUMNS - 1] == 0 || columnSpaces[COLUMNS - 1] == 1) {
            //decrementing case staircase: right to left
            for (int col = COLUMNS - 2; col >= 0; col--) {
                if (columnSpaces[col] - 1 != columnSpaces[col + 1]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

