package it.polimi.ingsw.model;

public class CommonSixGroupsOfTwo implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int numGroups = 0;
        boolean[][] visited = new boolean[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    Item val = shelf.getItemByCoordinates(i, j);
                    boolean foundGroup = false;
                    // check horizontal group
                    if (j < 4 && shelf.getItemByCoordinates(i, j+1).getType() == val.getType()) {
                        numGroups++;
                        foundGroup = true;
                        visited[i][j] = true;
                        visited[i][j+1] = true;
                    }
                    // check vertical group
                    if (i < 5 && shelf.getItemByCoordinates(i+1, j).getType() == val.getType() && !foundGroup) {
                        numGroups++;
                        foundGroup = true;
                        visited[i][j] = true;
                        visited[i+1][j] = true;
                    }
                    if (foundGroup && numGroups >= 6) {
                        return true; // found all groups
                    }
                }
            }
        }
        return false; // didn't find enough groups
    }
}
