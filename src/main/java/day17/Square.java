package day17;

/**
 * For shape Square, the position P is determined by its bottom left element:
 *
 *   @ @
 *   P @
 */
public class Square extends Shape {

    public Square(int[] position) {
        super(position);
    }

    @Override
    // depending on whether we're blowing right or left: check of the position marked with ? is available
    //
    // ? @ @ ?
    // ? P @ ?
    public boolean tryBlow(boolean right, char[][] grid) {
        if (right) {
            int[] rightCheck1 = {position[0] + 2, position[1] + 1};
            int[] rightCheck2 = {position[0] + 2, position[1]};
            boolean ok = positionAvailable(grid, rightCheck1) && positionAvailable(grid, rightCheck2);
            if (ok) {
                position[0]++;
                return true;
            }
        } else {
            int[] leftCheck1 = {position[0] - 1, position[1] + 1};
            int[] leftCheck2 = {position[0] - 1, position[1]};
            boolean ok = positionAvailable(grid, leftCheck1) && positionAvailable(grid, leftCheck2);
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
        // check if it can still drop: see if the 2 horizontal positions directly beneath it are free
        //
        // all positions marked ? must be checked
        //   @ @
        //   P @
        //   ? ?
        if (position[1] == 0) {
            goOn = false;
        } else {
            for (int i = 0; i < 2; i++) {
                if (grid[position[0] + i][position[1] - 1] != '.') {
                    goOn = false;
                    break;
                }
            }
        }

        // freeze the shape in the grid when we cannot go on
        if (!goOn) {
            for (int c = 0; c < 2; c ++) {
                for (int r = 0; r < 2; r ++) {
                    grid[position[0] + c][position[1] + r] = '#';
                }
            }
        } else {
            // drop one row
            position[1]--;
        }
        return goOn;
    }
}
