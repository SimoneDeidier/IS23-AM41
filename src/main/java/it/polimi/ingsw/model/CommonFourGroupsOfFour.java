package it.polimi.ingsw.model;

public class CommonFourGroupsOfFour implements CommonTargetCard {
    @Override
    public boolean check(Shelf shelf) {
        int numGroups = 0;
        boolean[][] visited = new boolean[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    Item val = shelf[i][j];
                    boolean foundGroup = false;
                    //check square
                    //check vertical group
                    //check horizontal group
                    //check vertical, right facing L, start from small end
                    //check vertical, left facing L, start from small end
                    //check vertical, right facing L, start from long end
                    //check vertical, left facing L, start from long end
                    //check horizontal, up facing L, start from small end
                    //check horizontal, down facing L, start from small end
                    //check horizontal, up facing L, start from long end
                    //check horizontal, down facing L, start from long end
                    //check T, pointing down (always start from down part of regular T)
                    //check T, pointing right (always start from down part of regular T)
                    //check T, pointing up (always start from down part of regular T)
                    //check T, pointing left (always start from down part of regular T)
                    //check S --> to finish


                    //OLD CODE
                    // check horizontal group
                    if (j < 4 && shelf[i][j+1].getType() == val.getType()) {
                        numGroups++;
                        foundGroup = true;
                        visited[i][j] = true;
                        visited[i][j+1] = true;
                    }
                    // check vertical group
                    if (i < 5 && shelf[i+1][j].getType() == val.getType() && !foundGroup) {
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
