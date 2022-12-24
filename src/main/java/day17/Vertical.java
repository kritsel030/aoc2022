package day17;

/**
 * For shape Vertical, the position P is determined by its lowest element:
 *
 *   @
 *   @
 *   @
 *   P
 *
 *
 */
public class Vertical extends Shape {

    public Vertical(int[] position) {
        super(position);
    }

    @Override
    // depending on whether we're blowing right or left: check of the position marked with ? is available
    //
    // ? @ ?
    // ? @ ?
    // ? @ ?
    // ? P ?
    public boolean tryBlow(boolean right, char[][] grid) {
        if (right) {
            int[] rightCheck1 = {position[0] + 1, position[1] + 3};
            int[] rightCheck2 = {position[0] + 1, position[1] + 2};
            int[] rightCheck3 = {position[0] + 1, position[1] + 1};
            int[] rightCheck4 = {position[0] + 1, position[1]};
            boolean ok = positionAvailable(grid, rightCheck1) && positionAvailable(grid, rightCheck2) && positionAvailable(grid, rightCheck3) && positionAvailable(grid, rightCheck4);
            if (ok) {
                position[0]++;
                return true;
            }
        } else {
            int[] leftCheck1 = {position[0] - 1, position[1] + 3};
            int[] leftCheck2 = {position[0] - 1, position[1] + 2};
            int[] leftCheck3 = {position[0] - 1, position[1] + 1};
            int[] leftCheck4 = {position[0] - 1, position[1]};
            boolean ok = positionAvailable(grid, leftCheck1) && positionAvailable(grid, leftCheck2) && positionAvailable(grid, leftCheck3)&& positionAvailable(grid, leftCheck4);
            if (ok) {
                position[0]--;
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean tryDrop(char[][] grid) {
        boolean goOn = true;
        // check if it can still drop: all positions marked ? must still be free
        //   @
        //   @
        //   @
        //   P
        //   ?

        if (position[1] == 0) {
            goOn = false;
        } else {
            if (grid[position[0]][position[1] - 1] != '.') {
                goOn = false;
            }
        }

        // freeze the shape in the grid when we cannot go on
        if (!goOn) {
            for (int i = 0; i < 4; i ++) {
                grid[position[0]][position[1] + i] = '#';
            }
        } else {
            // drop one row
            position[1]--;
        }

        return goOn;
    }
}
