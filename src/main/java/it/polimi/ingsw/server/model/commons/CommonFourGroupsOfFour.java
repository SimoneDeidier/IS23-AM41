package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.Shelf;

/**
 * CommonFourGroupsOfFourClass represents a specific CommonTargetCard in the game
 */
public class CommonFourGroupsOfFour extends CommonTargetCard {

    private final static int NUMBER_OF_GROUPS_NEEDED = 4;
    private final static int NUMBER_OF_TILES_NEEDED_IN_GROUPS = 4;

    /**
     * Constructor is provided to set the correct name to the instance created
     * @param maxPlayerNumber is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonFourGroupsOfFour(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonFourGroupsOfFour";
    }

    /**
     * The override of the method check modifies the function in order to verify the specific CommonTargetCard
     * @return true if the condition is satisfied, false otherwise
     */
    @Override
    public boolean check(Shelf shelf) {
        int count = 0;
        boolean[][] visited = new boolean[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (!visited[i][j]) {
                    if(shelf.getItemByCoordinates(i,j)!=null) {
                        ItemColor value = shelf.getItemByCoordinates(i, j).getColor();
                        if (dfs(shelf, visited, i, j, value) >= NUMBER_OF_TILES_NEEDED_IN_GROUPS) {
                            count++;
                        }
                    }
                }
            }
        }
        return count >= NUMBER_OF_GROUPS_NEEDED;
    }

    private static int dfs(Shelf shelf, boolean[][] visited, int i, int j, ItemColor value) {
        visited[i][j] = true;
        int count = 1;
        int[][] neighbors = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        for (int[] neighbor : neighbors) {
            int row = i + neighbor[0];
            int col = j + neighbor[1];
            if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && !visited[row][col] && shelf.getItemByCoordinates(row, col) != null && shelf.getItemByCoordinates(row, col).getColor() == value) {
                count += dfs(shelf, visited, row, col, value);
            }
        }
        return count;
    }
}
