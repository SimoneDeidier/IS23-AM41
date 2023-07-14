package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;

/**
 * CommonStairwayClass represents a specific CommonTargetCard in the game
 */
public class CommonStairway extends CommonTargetCard {

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonStairway(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonStairway";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
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

