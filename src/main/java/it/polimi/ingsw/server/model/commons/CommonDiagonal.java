package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

public class CommonDiagonal extends CommonTargetCard {

    public CommonDiagonal(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonDiagonal";
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

        if (first) {
            for (int i = 1; i < COLUMNS; i++) {
                if (shelf.getItemByCoordinates(0, 0).getColor() != shelf.getItemByCoordinates(i, i).getColor())
                    check = false;
            }
            if (check)
                return true;
            check = true;
        }

        if (second) {
            for (int i = 1; i < COLUMNS; i++) {
                if (shelf.getItemByCoordinates(1, 0).getColor() != shelf.getItemByCoordinates(i + 1, i).getColor())
                    check = false;
            }
            if (check)
                return true;
            check = true;
        }

        if (third) {
            for (int i = 1; i < COLUMNS; i++) {
                if (shelf.getItemByCoordinates(COLUMNS - 1, COLUMNS - 1).getColor() != shelf.getItemByCoordinates(COLUMNS - 1 - i, COLUMNS - 1 - i).getColor())
                    check = false;
            }
            if (check)
                return true;
            check = true;
        }

        if (fourth) {
            for (int i = 1; i < COLUMNS; i++) {
                if (shelf.getItemByCoordinates(COLUMNS, COLUMNS - 1).getColor() != shelf.getItemByCoordinates(COLUMNS - i, COLUMNS - 1 - i).getColor())
                    check = false;
            }
            return check;
        }
        return  false;
    }
}
