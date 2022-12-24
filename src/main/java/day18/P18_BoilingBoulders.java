package day18;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class P18_BoilingBoulders {


    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day18/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day18/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 4400
    static void doPart1(List<String> inputLines) {
        int dimension = 22;
        Grid3D grid = new Grid3D(dimension, dimension, dimension, '.');
        for (String inputLine : inputLines) {
            List<Integer> coordinate = Arrays.asList(inputLine.split(",")).stream().map(c -> Integer.valueOf(c)).collect(Collectors.toList());
            grid.set(coordinate.get(0), coordinate.get(1), coordinate.get(2), '#');
        }

        long visibleTopsAndBottoms = sum(grid.countOpenSidesOverZAxis());
        long visibleFrontsAndBacks = sum(grid.rotate().countOpenSidesOverZAxis());
        long visibleRightsAndLefts = sum(grid.rotate().rotate().countOpenSidesOverZAxis());

        long visibleSides = visibleTopsAndBottoms + visibleFrontsAndBacks + visibleRightsAndLefts;

        AnswerPrinter.printAnswerDetails(1, 1, visibleSides, TEST);
    }

    // answer: 2522
    static void doPart2(List<String> inputLines) {
        int dimension = 22;
        Grid3D grid = new Grid3D(dimension, dimension, dimension, '.');
        for (String inputLine : inputLines) {
            List<Integer> coordinate = Arrays.asList(inputLine.split(",")).stream().map(c -> Integer.valueOf(c)).collect(Collectors.toList());
            grid.set(coordinate.get(0), coordinate.get(1), coordinate.get(2), '#');
        }

        // determine the coordinates reachable by steam
        Grid3D reachableGrid = determineReachableBySteam(grid);

        long reachableTopsAndBottoms = sum(grid.countReachableSidesOverZAxis(reachableGrid));
        long reachableFrontsAndBacks = sum(grid.rotate().countReachableSidesOverZAxis(reachableGrid.rotate()));
        long ReachableRightsAndLefts = sum(grid.rotate().rotate().countReachableSidesOverZAxis(reachableGrid.rotate().rotate()));

        long reachableSides = reachableTopsAndBottoms + reachableFrontsAndBacks + ReachableRightsAndLefts;

        AnswerPrinter.printAnswerDetails(1, 2, reachableSides, TEST);
    }

    public static long sum(long[][] grid2D) {
        long sum = 0;
        for (int x = 0; x < grid2D.length; x++) {
            for (int y = 0; y < grid2D[0].length; y++) {
                sum += grid2D[x][y];
            }
        }
        return sum;
    }

    public static Grid3D determineReachableBySteam(Grid3D grid) {
        int xRange = grid.data.length;
        int yRange = grid.data[0].length;
        int zRange = grid.data[0][0].length;
        Grid3D reachableGrid = new Grid3D(xRange, yRange, zRange);
        for (int x = 0; x < xRange; x++) {
            for (int y = 0; y < yRange; y++) {
                for (int z = 0; z < zRange; z++) {
                    if (isReachableBySteam(x, y, z, grid, reachableGrid, new ArrayList<>())) {
                        reachableGrid.set(x, y, z, 'r');
                    }
                }
            }
        }
        return reachableGrid;
    }
    
    public static boolean isReachableBySteam(int x, int y, int z, Grid3D grid, Grid3D reachableGrid, List<String> visited) {
        String coordinate = "" + x + "-" + y + "-" + z;
        if (!visited.contains(coordinate)) {
            visited.add(coordinate);
            if (grid.getData()[x][y][z] != grid.emptyValue) {
                return false;
            } else if (x == 0 || x == grid.data.length - 1 || y == 0 || y == grid.data[0].length - 1 || z == 0 || z == grid.data[0][0].length - 1) {
                return true;
            } else {
                return
                    reachableGrid.data[x-1][y][z] > 0 || 
                    reachableGrid.data[x+1][ y][ z] > 0 ||
                    reachableGrid.data[x][ y-1][ z] > 0 ||
                    reachableGrid.data[x][ y+1][ z] > 0 ||
                    reachableGrid.data[x][ y][ z-1] > 0 ||
                    reachableGrid.data[x][ y][ z+1] > 0 ||    
                    isReachableBySteam(x - 1, y, z, grid, reachableGrid, visited) ||
                    isReachableBySteam(x + 1, y, z, grid, reachableGrid, visited) || 
                    isReachableBySteam(x, y-1, z, grid, reachableGrid, visited) ||
                    isReachableBySteam(x, y+1, z, grid, reachableGrid, visited) ||
                    isReachableBySteam(x, y, z-1, grid, reachableGrid, visited) ||
                    isReachableBySteam(x, y, z+1, grid, reachableGrid, visited);
            }
        }
        return false;
    }


    public static class Grid3D {

        char[][][] data;

        char emptyValue;

        public Grid3D(int xRange, int yRange, int zRange) {
            data = new char[xRange][yRange][zRange];
        }

        public Grid3D(int xRange, int yRange, int zRange, char emptyValue) {
            this(xRange, yRange, zRange);
            this.emptyValue = emptyValue;
            if (this.emptyValue != 0) {
                for (int x = 0; x < xRange; x++) {
                    for (int y = 0; y < yRange; y++) {
                        for (int z = 0; z < zRange; z++) {
                            data[x][y][z] = emptyValue;
                        }
                    }
                }
            }
        }

        public long[][] countOpenSidesOverZAxis() {
            int xRange = data.length;
            int yRange = data[0].length;
            long[][] flattenedData = new long[xRange][yRange];
            for (int x = 0; x < xRange; x++) {
                for (int y = 0; y < yRange; y++) {
                    char[] zArray = data[x][y];
                    long countOpenSides = 0;
                    for (int z = 0; z < zArray.length; z++) {
                        if (zArray[z] != emptyValue) {
                            // front open?
                            if (z == 0) {
                                countOpenSides++;
                            } else {
                                if (zArray[z-1] == emptyValue) {
                                    countOpenSides++;
                                }
                            }
                            // back open?
                            if (z == zArray.length - 1) {
                                countOpenSides++;
                            } else {
                                if (zArray[z+1] == emptyValue) {
                                    countOpenSides++;
                                }
                            }
                        }
                    }
                    flattenedData[x][y] = countOpenSides;
                }
            }
            return flattenedData;
        }

        public long[][] countReachableSidesOverZAxis(Grid3D reachableGrid) {
            int xRange = data.length;
            int yRange = data[0].length;
            long[][] flattenedData = new long[xRange][yRange];
            for (int x = 0; x < xRange; x++) {
                for (int y = 0; y < yRange; y++) {
                    char[] zArray = data[x][y];
                    long reachableSides = 0;
                    for (int z = 0; z < zArray.length; z++) {
                        if (zArray[z] != emptyValue) {
                            // front open?
                            if (z == 0) {
                                reachableSides++;
                            } else {
                                if (reachableGrid.data[x][y][z-1] > 0) {
                                    reachableSides++;
                                }
                            }
                            // back open?
                            if (z == zArray.length - 1) {
                                reachableSides++;
                            } else {
                                if (reachableGrid.data[x][y][z+1] > 0) {
                                    reachableSides++;
                                }
                            }
                        }
                    }
                    flattenedData[x][y] = reachableSides;
                }
            }
            return flattenedData;
        }



        public long[][] flattenAndCountOverZAxis() {
            int xRange = data.length;
            int yRange = data[0].length;
            long[][] flattenedData = new long[xRange][yRange];
            for (int x = 0; x < xRange; x++) {
                for (int y = 0; y < yRange; y++) {
                    char[] zArray = data[x][y];
                    long countNonEmpty = 0;
                    for (int z = 0; z < zArray.length; z++) {
                        if (zArray[z] != emptyValue) {
                            countNonEmpty++;
                        }
                    }
                    flattenedData[x][y] = countNonEmpty;
                }
            }
            return flattenedData;
        }

        // x-axis becomes y-axis
        // y-axis becomes z-axis
        // z-axis becomes x-axis
        public Grid3D rotate() {
            int xRange = data.length;
            int yRange = data[0].length;
            int zRange = data[0][0].length;

            int newXRange = zRange;
            int newYRange = xRange;
            int newZRange = yRange;

            Grid3D newGrid = new Grid3D(newXRange, newYRange, newZRange, this.emptyValue);
            for (int x = 0; x < xRange; x++) {
                for (int y = 0; y < yRange; y++) {
                    for (int z = 0; z < zRange; z++) {
                        newGrid.set(z, x, y, data[x][y][z]);
                    }
                }
            }
            return newGrid;
        }

        public Grid3D set(int x, int y, int z, char value) {
            data[x][y][z] = value;
            return this;
        }

        public char[][][] getData() {
            return data;
        }
    }
}
