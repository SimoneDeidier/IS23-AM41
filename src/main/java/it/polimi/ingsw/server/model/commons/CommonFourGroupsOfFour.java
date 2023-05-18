package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.Shelf;

public class CommonFourGroupsOfFour extends CommonTargetCard {

    private final static int NUMBER_OF_GROUPS_NEEDED = 4;
    private final static int NUMBER_OF_TILES_NEEDED_IN_GROUPS = 4;
    public CommonFourGroupsOfFour(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int count = 0;
        boolean[][] visited = new boolean[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (!visited[i][j]) {
                    ItemColor value = shelf.getItemByCoordinates(i, j).getColor();
                    if (dfs(shelf, visited, i, j, value) >= NUMBER_OF_TILES_NEEDED_IN_GROUPS) {
                        count++;
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
            if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && !visited[row][col] && shelf.getItemByCoordinates(row, col).getColor() != null && shelf.getItemByCoordinates(row, col).getColor() == value) {
                count += dfs(shelf, visited, row, col, value);
            }
        }
        return count;
    }
}
