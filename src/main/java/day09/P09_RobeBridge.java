package day09;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P09_RobeBridge {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day09/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day09/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);

        if (TEST) {
            path = "src/main/resources/day09/test-input-2.txt";
        }
        inputLines = FileUtil.readAllLines(path);
        doPart2(inputLines);
    }

    // answer: 6642
    static void doPart1(List<String> inputLines) {
        // initialize the grid
        long[][] grid = init2dGrid(1500, 1500);

        // do the magic part
        executeRopeScience(grid, inputLines, 2);

        // count grid locations visited by the tail knot
        long count = countVisitedGridLocations(grid);
        AnswerPrinter.printAnswerDetails(1, 2, count, TEST);
    }

    // answer: 2765
    static void doPart2(List<String> inputLines) {
        // initialize the grid
        long[][] grid = init2dGrid(1500, 1500);

        // do the magic part
        executeRopeScience(grid, inputLines, 10);

        // count grid locations visited by the tail knot
        long count = countVisitedGridLocations(grid);
        AnswerPrinter.printAnswerDetails(1, 2, count, TEST);
    }

    private static void executeRopeScience(long[][] grid, List<String> inputLines, int numberOfKnots) {
        int knots = numberOfKnots;
        int[][] knotPositions = new int[knots][2];
        for (int k = 0; k < knotPositions.length; k++) {
            int[] centerPosition = {grid.length/2, grid.length/2};
            knotPositions[k] = centerPosition;
        }

        // mark current tailPosition in the grid
        grid[knotPositions[knots-1][0]][knotPositions[knots-1][1]]++;

        // loop through command lines
        int lineCount = 1;
        for (String commandLine : inputLines) {
//            System.out.println();
//            System.out.println("executing command " + lineCount + ": " + commandLine);

            String direction = commandLine.split(" ")[0];
            int steps = Integer.parseInt(commandLine.split(" ")[1]);

            for (int s = 0; s < steps; s++) {

                // move head (=knotPosition[0])
                switch (direction) {
                    case "R":
                        knotPositions[0][0]++;
                        break;
                    case "L":
                        knotPositions[0][0]--;
                        break;
                    case "U":
                        knotPositions[0][1]++;
                        break;
                    case "D":
                        knotPositions[0][1]--;
                        break;
                }

                for (int k = 0; k < knots-1; k++) {
                    followMove(knotPositions[k], knotPositions[k+1]);
                }

                // increase visit counter of tail knot position in the grid
                grid[knotPositions[knots-1][0]][knotPositions[knots-1][1]]++;
            }

            lineCount++;
        }
    }

    private static long countVisitedGridLocations(long[][] grid) {
        long count = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void followMove(int[] headPosition, int[] tailPosition) {
        // move tail
        int xDiff = Math.abs(headPosition[0] - tailPosition[0]);
        int yDiff = Math.abs(headPosition[1] - tailPosition[1]);
        boolean sameY = headPosition[1] == tailPosition[1];
        boolean sameX = headPosition[0] == tailPosition[0];
        boolean headHigherY = headPosition[1] > tailPosition[1];
        boolean headHigherX = headPosition[0] > tailPosition[0];

        // head and tail touch, no need to move
        if (xDiff <= 1 && yDiff <= 1) {
            // no move
            //System.out.println("no move");
        } else if (sameY) {
            // move X
            if (headHigherX) tailPosition[0]++;
            else tailPosition[0]--;
//            System.out.println("move horizontally");
        } else if (sameX) {
            // move Y
            if (headHigherY) tailPosition[1]++;
            else tailPosition[1]--;
//            System.out.println("move vertically");
        } else {
            // move diagonally
            if (headHigherX && headHigherY) {
//                System.out.println("move diagonally: up and right");
                tailPosition[0]++;
                tailPosition[1]++;
            } else if (headHigherX && !headHigherY) {
//                System.out.println("move diagonally: down and right");
                tailPosition[0]++;
                tailPosition[1]--;
            } else if (!headHigherX && !headHigherY) {
//                System.out.println("move diagonally: down and left");
                tailPosition[0]--;
                tailPosition[1]--;
            } else if (!headHigherX && headHigherY) {
//                System.out.println("move diagonally: up and left");
                tailPosition[0]--;
                tailPosition[1]++;
            } else {
//                System.out.println("#######  something's wrong!!!");
            }
        }

//        System.out.println("head at " + headPosition[0] + "," + headPosition[1] + " | tail at " + tailPosition[0] + "," + tailPosition[1]);

//        if (tailPosition[0] < 0 || tailPosition[1] < 0) {
//            System.out.println("tail position off the grid: x = " + tailPosition[0] + ", y = " + tailPosition[1]);
//        }
    }

    static long[][] init2dGrid(int rows, int columns) {
        long initValue = 0;
        long[][] grid = new long[columns][rows];
        for (int c = 0; c < columns; c++) {
            Arrays.fill(grid[c], initValue);
        }
        return grid;
    }

    static void printGrid(long[][] grid, Map<int[], Character> specialMarkers) {
        for (int i = 0; i < grid[0].length; i++) {
            StringBuffer buf = new StringBuffer();
            for (int j = 0; j < grid[i].length; j++) {
                int[] position = {i, j};
                if (specialMarkers.containsKey(position)) {
                    buf.append(specialMarkers.get(position));
                } else {
                    if (grid[i][j] > 0) {
                        buf.append('#');
                    } else {
                        buf.append('.');
                    }
                }
            }
            System.out.println(buf.toString());
        }
    }

}
