package it.polimi.ingsw.model;

public class CommonStairway extends CommonTargetCard {
    public CommonStairway(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {

        int[] columnSpaces = new int[COLUMNS];
        /*
         * i casi possibili sono 4:
         *
         * 1) le colonne sono occupate così: 6 - 5 - 4 - 3 - 2
         *
         * 2) le colonne sono occupate così: 5 - 4 - 3 - 2 - 1
         *
         * 3) le colonne sono occupate così: 2 - 3 - 4 - 5 - 6
         *
         * 4) le colonne sono occupate così: 1 - 2 - 3 - 4 - 5
         *
         * */
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

