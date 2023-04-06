package it.polimi.ingsw.model;

public class CommonSixGroupsOfTwo extends CommonTargetCard {
    public CommonSixGroupsOfTwo(int maxPlayerNumber) {
        super(maxPlayerNumber);
    }
    @Override
    public boolean check(Shelf shelf) {
        int count = 0;
        boolean[][] visited = new boolean[6][5];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (!visited[i][j]) {
                    ItemType value = shelf.getItemByCoordinates(i, j).getType();
                    if (dfs(shelf, visited, i, j, value) >= 2) {
                        count++;
                    }
                }
            }
        }
        return count >= 6;
    }

    private static int dfs(Shelf shelf, boolean[][] visited, int i, int j, ItemType value) {
        visited[i][j] = true;
        int count = 1;
        int[][] neighbors = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        for (int[] neighbor : neighbors) {
            int row = i + neighbor[0];
            int col = j + neighbor[1];
            if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && !visited[row][col] && shelf.getItemByCoordinates(row, col).getType() == value) {
                count += dfs(shelf, visited, row, col, value);
            }
        }
        return count;
    }
}
