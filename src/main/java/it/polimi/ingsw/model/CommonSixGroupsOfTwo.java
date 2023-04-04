package it.polimi.ingsw.model;

public class CommonSixGroupsOfTwo implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int count = 0;
        boolean[][] visited = new boolean[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
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
            if (row >= 0 && row < 6 && col >= 0 && col < 5 && !visited[row][col] && shelf.getItemByCoordinates(row, col).getType() == value) {
                count += dfs(shelf, visited, row, col, value);
            }
        }
        return count;
    }
}
