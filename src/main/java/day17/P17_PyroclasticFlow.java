package day17;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P17_PyroclasticFlow {

    static boolean TEST = true;

    public static void main (String[] args) {
        String path = "src/main/resources/day17/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day17/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
//        doPart2(inputLines);
    }

    // answer: 3181
    static void doPart1(List<String> inputLines) {
        String jetStreamData = inputLines.get(0);
        int jetStreamBlasts = 0;

        List<String> shapeSequence = new ArrayList<>();
        shapeSequence.add("horizontal");
        shapeSequence.add("plus");
        shapeSequence.add("hook");
        shapeSequence.add("vertical");
        shapeSequence.add("square");

        long rounds = 25;

        char[][] grid = initGrid(7, 10000);

        for (long round = 0; round < rounds; round++) {
            Shape shape = newShape(grid, shapeSequence, round);

            boolean goOn = true;
            int step = 0;
            while (goOn) {
//                System.out.println("step " + (step + 1) + " for shape " + shape.getClass().getSimpleName());
//                System.out.println("  start position is " + toString(shape.position));
                boolean blowRight = jetStreamData.charAt(jetStreamBlasts % jetStreamData.length()) == '>';
                boolean moved = shape.tryBlow(blowRight, grid);
                jetStreamBlasts++;
//                System.out.println("  after blow to the " + (blowRight ? "right": "left") + (!moved ? " (no move)" : "") + " position is " + toString(shape.position));
                goOn = shape.tryDrop(grid);
//                System.out.println("  after drop" + (!goOn ? " (at rest now)" : "") + ", position is " + toString(shape.position));
                step++;
            }

//            System.out.println("after round " + (round+1) + " (shape: " + shape.getClass().getSimpleName() + ")");
//            System.out.println("  tower height is " + (highestFilledGridLevel(grid) + 1));
            //printGrid(grid, 0, 7, 0, highestFilledGridLevel(grid) + 1);
            System.out.println();
        }

        System.out.println("completed " + rounds + " rounds ");
        System.out.println("  tower height is " + (highestFilledGridLevel(grid) + 1));
        printGrid(grid, 0, 7, 0, highestFilledGridLevel(grid) + 1);

        int height = highestFilledGridLevel(grid);

        AnswerPrinter.printAnswerDetails(1, 1, height + 1, TEST);
    }

    static void doPart2(List<String> inputLines) {
        String jetStreamData = inputLines.get(0);
        long jetStreamBlasts = 0;

        List<String> shapeSequence = new ArrayList<>();
        shapeSequence.add("horizontal");
        shapeSequence.add("plus");
        shapeSequence.add("hook");
        shapeSequence.add("vertical");
        shapeSequence.add("square");

        long rounds = 1000000000000l;

        char[][] grid = initGrid(7, 500);

        long tempHeight = 0;

        for (long round = 0; round < rounds; round++) {
            if (round % 1000000 == 0) {
                System.out.println("round " + (round + 1));
            }
            int towerHeight = highestFilledGridLevel(grid);
            // remove lower layers of the grid when the highest column becomes too high
            if (towerHeight> 400) {
                List<Integer> heighestPerColumn = new ArrayList<>();
                for (int c = 0; c < grid.length; c++) {
                    for (int r = grid[0].length -1; r >= 0; r--) {
                        if (grid[c][r] == '#') {
                            heighestPerColumn.add(r);
                            break;
                        }
                    }
                }
                int lowestHeight = Collections.min(heighestPerColumn);
                // remove lowestHeight rows from the bottom of the grid
//                System.out.println("remove "+ lowestHeight + " grid layers");
                char[][] newGrid = initGrid(7, 500);
                for (int c = 0; c < grid.length; c++) {
                    for (int r = lowestHeight; r < grid[0].length; r++) {
                        newGrid[c][r-lowestHeight] = grid[c][r];
                    }
                }
                grid = newGrid;
                tempHeight += lowestHeight;
            }
            Shape shape = newShape(grid, shapeSequence, round);

            boolean goOn = true;
            int step = 0;
            while (goOn) {
//                System.out.println("step " + (step + 1) + " for shape " + shape.getClass().getSimpleName());
//                System.out.println("  start position is " + toString(shape.position));
                boolean blowRight = jetStreamData.charAt(Long.valueOf(jetStreamBlasts % jetStreamData.length()).intValue()) == '>';
                boolean moved = shape.tryBlow(blowRight, grid);
                jetStreamBlasts++;
//                System.out.println("  after blow to the " + (blowRight ? "right": "left") + (!moved ? " (no move)" : "") + " position is " + toString(shape.position));
                goOn = shape.tryDrop(grid);
//                System.out.println("  after drop" + (!goOn ? " (at rest now)" : "") + ", position is " + toString(shape.position));
                step++;
            }

//            System.out.println("after round " + (round+1) + " (shape: " + shape.getClass().getSimpleName() + ")");
//            System.out.println("  tower height is " + (highestFilledGridLevel(grid) + 1));
            //printGrid(grid, 0, 7, 0, highestFilledGridLevel(grid) + 1);
//            System.out.println();
        }

        long height = tempHeight + highestFilledGridLevel(grid);

        AnswerPrinter.printAnswerDetails(1, 2, height + 1, TEST);
    }


    static Shape newShape(char[][] grid, List<String> shapeSequence, long shapeCounter) {
        int heighest = highestFilledGridLevel(grid);
//        System.out.println("get shape index for round "+ shapeCounter + ": " + (shapeCounter % shapeSequence.size()));
//        System.out.println("shape name for round " + shapeCounter + ": " + shapeSequence.get(shapeCounter % shapeSequence.size()));
        String shapeName = shapeSequence.get(Long.valueOf(shapeCounter % shapeSequence.size()).intValue());

        if (shapeName.equals("horizontal")) {
            int[] position = {2, heighest + 4};
            return new Horizontal(position);
        } else if (shapeName.equals("plus")) {
            int[] position = {2 + 1, heighest + 4 + 1};
            return new Plus(position);
        } else if (shapeName.equals("hook")) {
            int[] position = {2, heighest + 4};
            return new Hook(position);
        } else if (shapeName.equals("vertical")) {
            int[] position = {2, heighest + 4};
            return new Vertical(position);
        } else if (shapeName.equals("square")) {
            int[] position = {2, heighest + 4};
            return new Square(position);
        }
        throw new IllegalStateException("No shapename match, this should not occur");
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

    static void printGrid (char[][] grid) {
        printGrid(grid, 0, grid.length, 0, grid[0].length);
    }

    static int highestFilledGridLevel(char[][] grid) {
        for (int r = 0; r < grid[0].length; r++) {
            boolean emptyRow = true;
            for (int c = 0; c < grid.length; c++) {
                if (grid[c][r] != '.') {
                    emptyRow = false;
                    break;
                }
            }
            if (emptyRow) {
                return r - 1;
            }
        }
        throw new IllegalStateException("no empty rows in the grid, this should not happen");
    }


    static void printGrid (char[][] grid, int startColumn, int width, int startRow, int height) {
        for (int row = startRow + height - 1; row >= startRow; row--) {
            StringBuffer buf = new StringBuffer();
            for (int column = startColumn; column < startColumn + width; column++) {
                if (column > grid.length-1) {
                    System.out.println("ERROR column " + column + " too large for grid width " + grid.length);
                }
                if (row > grid[0].length-1) {
                    System.out.println("ERROR row " + row + " too large for grid height " + grid[0].length);
                }
                buf.append(grid[column][row]);
            }
            System.out.println(buf.toString());
        }
    }

    static String toString(int[] position) {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        buf.append(position[0]);
        buf.append(",");
        buf.append(position[1]);
        buf.append("}");
        return buf.toString();
    }

}
