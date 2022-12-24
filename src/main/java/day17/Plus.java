package day17;

/**
 * For shape Plus, the position P is determined by its central element:
 *
 *     @
 *   @ P @
 *     @
 *
 */
public class Plus extends Shape {

    public Plus(int[] position) {
        super(position);
    }

    @Override
    // depending on whether we're blowing right or left: check of the positions marked with ? are available
    //    ? @ ?
    //  ? @ P @ ?
    //    ? @ ?
    public boolean tryBlow(boolean right, char[][] grid) {
        if (right) {
            int[] rightCheck1 = {position[0] + 1, position[1] + 1};
            int[] rightCheck2 = {position[0] + 2, position[1]};
            int[] rightCheck3 = {position[0] + 1, position[1] - 1};
            boolean ok = positionAvailable(grid, rightCheck1) && positionAvailable(grid, rightCheck2) && positionAvailable(grid, rightCheck3);
            if (ok) {
                position[0]++;
                return true;
            }
        } else {
            int[] leftCheck1 = {position[0] - 1, position[1] + 1};
            int[] leftCheck2 = {position[0] - 2, position[1]};
            int[] leftCheck3 = {position[0] - 1, position[1] - 1};
            boolean ok = positionAvailable(grid, leftCheck1) && positionAvailable(grid, leftCheck2) && positionAvailable(grid, leftCheck3);
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
        // check if it still can drop: see if all three elements directly below it are still free
        //
        // all positions marked ? must be checked
        //   @
        // @ P @
        // ? @ ?
        //   ?
        if (position[1] - 1 == 0) {
            goOn = false;
        } else {
            if (grid[position[0] - 1][position[1] - 1] != '.') {
                goOn = false;
            } else if (grid[position[0]][position[1] - 2] != '.') {
                goOn = false;
            } else if (grid[position[0] + 1][position[1] - 1] != '.') {
                goOn = false;
            }
        }

        // freeze the shape in the grid when we cannot go on
        if (!goOn) {
            // top
            grid[position[0]][position[1]+1] = '#';
            // left
            grid[position[0]-1][position[1]] = '#';
            // center
            grid[position[0]][position[1]] = '#';
            // right
            grid[position[0]+1][position[1]] = '#';
            // bottom
            grid[position[0]][position[1]-1] = '#';
        } else {
            // drow one row
            position[1]--;
        }
        return goOn;
    }
}
