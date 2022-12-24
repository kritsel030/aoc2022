package day17;

/**
 * For shape Horizontal, the position P is determined by its most left element:
 *
 *   P @ @ @
 */
public class Horizontal extends Shape {

    public Horizontal(int[] position) {
        super(position);
    }

    @Override
    // depending on whether we're blowing right or left: check of the position marked with ? is available
    // ? P @ @ @ ?
    public boolean tryBlow(boolean right, char[][] grid) {
        if (right) {
            int[] checkPosition = {position[0] + 4, position[1]};
            boolean ok = positionAvailable(grid, checkPosition);
            if (ok) {
                position[0]++;
                return true;
            }
        } else {
            int[] newPosition = {position[0] - 1, position[1]};
            boolean ok = positionAvailable(grid, newPosition);
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
        // check if it still can drop: see if all 4 horizontal positions directly beneath it are free
        //
        // all positions marked ? must be checked
        //   P @ @ @
        //   ? ? ? ?
        if (position[1] == 0) {
            goOn = false;
        } else {
            for (int i = 0; i < 4; i++) {
                if (grid[position[0] + i][position[1] - 1] != '.') {
                    goOn = false;
                    break;
                }
            }
        }

        // freeze the shape in the grid when we cannot go on
        if (!goOn) {
            for (int i = 0; i < 4; i ++) {
                grid[position[0] + i][position[1]] = '#';
            }
        } else {
            // when we can go on, move one row down
            position[1]--;
        }
        return goOn;
    }
}
