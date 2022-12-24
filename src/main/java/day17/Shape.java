package day17;

public abstract class Shape {

    int[] position;

    public Shape(int[] position) {
        this.position = position;
    }

    // right = true: try to blow 1 position to the right
    // right = false: try to blow 1 position to the left
    // result: true when blow was successful, true when the shape couldn't move
    public abstract boolean tryBlow(boolean right, char[][] grid);

    // drop the shape 1 position
    // result = false: shape has hit something, we can stop the movement for this shape
    // result = true; shape has not yet hit something, we can continue moving this shape
    public abstract boolean tryDrop(char[][] grid);

    public String positionAsString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        buf.append(position[0]);
        buf.append(",");
        buf.append(position[1]);
        buf.append("}");
        return buf.toString();
    }

    public boolean positionAvailable(char[][] grid, int[] checkPosition) {
        boolean available = false;
        // does the position exist within the grid?
        if (0 <= checkPosition[0] && checkPosition[0] <= grid.length-1) {
            // is the position free?
            if (grid[checkPosition[0]][checkPosition[1]] == '.') {
                available = true;
            }
        }
        return available;
    }
}
