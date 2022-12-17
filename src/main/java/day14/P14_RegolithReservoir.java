package day14;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class P14_RegolithReservoir {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day14/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day14/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 843
    static void doPart1(List<String> inputLines) {
        // parse input
        System.out.println("parse input");
        List<List<List<Integer>>> rockPaths = determineRockPaths(inputLines);

        // initialize empty grid (grid has an extra column to the right and to the left)
        System.out.println("init empty grid");
        List<Integer> maxXandY = findMaxXandY(rockPaths);
        int xOffset = 1;
        char[][] grid = initGrid(maxXandY.get(0) + (2*xOffset) + 1, maxXandY.get(1) + 1);


        // mark rocks in the grid
        System.out.println("mark rocks in the grid");
        addRockPathsToGrid(grid, rockPaths, xOffset);

        // pour sand until it starts dropping into the abyss
        long safeSandUnits = pourSand(grid, 500+xOffset, 1);

        // print the grid
        // these numbers only make sense for the test input
//        System.out.println("print the grid");
//        printGrid(grid, 494, 12, 0, 10);

        AnswerPrinter.printAnswerDetails(1, 1, safeSandUnits, TEST);
    }

    // answer: 27625
    static void doPart2(List<String> inputLines) {
        // parse input
        System.out.println("parse input");
        List<List<List<Integer>>> rockPaths = determineRockPaths(inputLines);

        // initialize empty grid (grid has an extra column to the right and to the left)
        System.out.println("init empty grid");
        List<Integer> maxXandY = findMaxXandY(rockPaths);
        int xOffset = maxXandY.get(1);
        // add two extra rows
        char[][] grid = initGrid(maxXandY.get(0) + (2*xOffset), maxXandY.get(1) + 1 + 2);

        // mark rocks in the grid
        System.out.println("mark rocks in the grid");
        addRockPathsToGrid(grid, rockPaths, xOffset);

        // and mark the full bottom row of the grid as rocks
        System.out.println("mark bottom row as rocks");
        for (int c = 0; c < grid.length; c++) {
            grid[c][grid[0].length-1] = '#';
        }

        // pour sand until it starts dropping into the abyss
        long safeSandUnits = pourSand(grid, 500+xOffset, 2);

        // print the grid
        // these numbers only make sense for the test input
//        System.out.println("print the grid");
//        printGrid(grid, 494, 12, 0, 10);

        AnswerPrinter.printAnswerDetails(1, 2, safeSandUnits, TEST);
    }

    /*
     * Returns a list of coordinate pairs (each pair is a 2 element list, and each coordinate is a 2 element list).
     * Each coordinate pair represents the start and the end of a straight rock path (either vertically or horizontally)
     *
     * Example input line: 498,4 -> 498,6 -> 496,6
     */
    static List<List<List<Integer>>> determineRockPaths(List<String> inputLines) {
        List<List<List<Integer>>> paths = new ArrayList<>();

        for (String inputLine : inputLines) {
            // turn 498,4 -> 498,6 -> 496,6 into a list with 3 coordinates, each coordinate being a 2-element list
            List<List<Integer>> coordinates = new ArrayList<>();
            String[] coordinateStrings = inputLine.split(" -> ");
            for (String coordinateString : coordinateStrings) {
                List<Integer> coordinate = Arrays.stream(coordinateString.trim().split(",")).map(i -> Integer.valueOf(i)).collect(Collectors.toList());
                coordinates.add(coordinate);
            }
            // turn each pair of 2 consecutive coordinates into a single path, and add to the result
            for (int i = 0; i < coordinates.size() - 1; i++) {
                paths.add(new ArrayList<>(List.of(coordinates.get(i), coordinates.get(i+1))));
            }
        }
        return paths;
    }

    static List<Integer> findMaxXandY (List<List<List<Integer>>> paths) {
        Integer maxX = Collections.max(paths.stream().flatMap(List::stream).map(c -> c.get(0)).collect(Collectors.toList()));
        Integer maxY = Collections.max(paths.stream().flatMap(List::stream).map(c -> c.get(1)).collect(Collectors.toList()));
        return new ArrayList<>(List.of(maxX, maxY));
    }

    static char[][] initGrid(int columns, int rows) {
        char[][] grid = new char[columns][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                grid[c][r] = '.';
            }
        }
        return grid;
    }

    static void addRockPathsToGrid(char[][] grid, List<List<List<Integer>>> rockPaths, int xOffset) {
        for (List<List<Integer>> path : rockPaths) {
            // helper variables for x-axis
            int startX = path.get(0).get(0);
            int endX = path.get(1).get(0);
            int xDelta = endX - startX;
            int xDeltaAbs = Math.abs(xDelta);

            // helper variables for y-axis
            int startY = path.get(0).get(1);
            int endY = path.get(1).get(1);
            int yDelta = endY - startY;
            int yDeltaAbs = Math.abs(yDelta);

            for (int x = 0; x <= xDeltaAbs; x++) {
                for (int y = 0; y <= yDeltaAbs; y++) {
                    // the grid starts with additional column to the left,
                    // so we need a +xOffset to mark the correct horizontal rock position
                    // multiply by xDelta/xDeltaAbs is to take into account whether we need to move right or left, the same for the y coordingate
                    // xDeltaAbs can be 0, need to prevent divide by zero
                    grid[startX + (x * (xDeltaAbs == 0 ? 0 : xDelta/xDeltaAbs)) + xOffset][startY + (y * (yDeltaAbs == 0 ? 0 : yDelta/yDeltaAbs))] = '#';
                }
            }
        }
    }

    static void printGrid (char[][] grid, int startColumn, int width, int startRow, int depth) {
        for (int row = startRow; row < startRow + depth; row++) {
            StringBuffer buf = new StringBuffer();
            for (int column = startColumn; column < startColumn + width; column++) {
                if (column > grid.length-1) {
                    System.out.println("column " + column + " too large for grid width " + grid.length);
                }
                if (row > grid[0].length-1) {
                    System.out.println("row " + row + " too large for grid depth " + grid[0].length);
                }
                buf.append(grid[column][row]);
            }
            System.out.println(buf.toString());
        }
    }

    static long pourSand(char[][] grid, int dropColumn, int strategy) {
        long safeSandUnits = 0;
        while (true) {
            // drop a new unit of sand into dropColumn and see where it ends up
            int[] sandUnit = {dropColumn, 0};
            safeSandUnits++;

            int[] finalLocation = moveSandUnit(grid, sandUnit, strategy);
            if (strategy == 1 && unitPoursOver(grid, finalLocation)) {
                // we've hit the bottom row, that last sand unit was no longer safe
                safeSandUnits--;
                break;
            } else if (strategy == 2 && finalLocation[1] == 0) {
                // resting position of this unit of sand is the top row, we're done
                break;
            }

            // these numbers only make sense for the test input
//            System.out.println("after " + safeSandUnits + " units of sand");
//            printGrid(grid, 494, 12, 0, 10);
//            System.out.println();
        }
        return safeSandUnits;
    }

    static int[] moveSandUnit(char[][] grid, int[] sandUnit, int strategy) {
        // air below this position? -> drop one down
        if (grid[sandUnit[0]][sandUnit[1]+1] == '.') {
            sandUnit[1]++;
            return moveSandUnit(grid, sandUnit, strategy);
            // no air below this position, try diagonally to the left
        } else if (grid[sandUnit[0]-1][sandUnit[1] + 1] == '.') {
            sandUnit[0]--;
            sandUnit[1]++;
            if (strategy == 2 || !unitPoursOver(grid, sandUnit)) {
                return moveSandUnit(grid, sandUnit, strategy);
            }
            return sandUnit;
            // no air below this position, diagonally left has failed, so try diagonally to the right
        } else if (grid[sandUnit[0]+1][sandUnit[1] + 1] == '.') {
            sandUnit[0]++;
            sandUnit[1]++;
            if (strategy == 2 || !unitPoursOver(grid, sandUnit)) {
                return moveSandUnit(grid, sandUnit, strategy);
            }
            return sandUnit;
        } else {
            // found a resting position, mark the sand in the grid
            grid[sandUnit[0]][sandUnit[1]] = 'o';
            return sandUnit;
        }
    }

    // return true when the sand unit has hit the bottom row of the grid;
    static boolean unitPoursOver(char[][] grid, int[] sandUnit) {
        return sandUnit[1] == grid[0].length-1;
    }

}
