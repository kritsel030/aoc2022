package day12;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P12_HillClimbing {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day12/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day12/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 440
    static void doPart1(List<String> inputLines) {
        Location[][] grid = initGrid(inputLines);
        Location start = findLocations('S', grid).get(0);
        Location end = findLocations('E', grid).get(0);

        List<Location> pathStartToEnd = findPath(start, end, grid);

        // the path also contains the start element, to get to the number of steps taken
        // we need to substract 1
        AnswerPrinter.printAnswerDetails(1, 1, pathStartToEnd.size() -1, TEST);

    }

    // answer: 439
    static void doPart2(List<String> inputLines) {
        Location[][] grid = initGrid(inputLines);
        List<Location> aLevels = findLocations('a', grid);
        Location end = findLocations('E', grid).get(0);

        List<Integer> pathLenghts = aLevels.stream().map(l -> findPath(l, end, grid).size() -1).collect(Collectors.toList());

        AnswerPrinter.printAnswerDetails(1, 1, Collections.min(pathLenghts), TEST);
    }

    static Location[][] initGrid(List<String> inputLines) {
        int rows = inputLines.size();
        int columns = inputLines.get(0).trim().length();
        Location[][] grid = new Location[columns][rows];
        for (int r = 0; r < inputLines.size(); r++) {
            for (int c = 0; c < inputLines.get(r).length(); c++) {
                grid[c][r] = new Location(inputLines.get(r).charAt(c), r, c);
            }
        }
        return grid;
    }

    static List<Location> findLocations(Character charr, Location[][] grid) {
        List<Location> locations = new ArrayList<>();
        for (int c = 0; c < grid.length; c++) {
            for (int r = 0; r < grid[c].length; r++) {
                if (grid[c][r].heightChar.equals(charr)) {
                    locations.add(grid[c][r]);
                }
            }
        }
        return locations;
    }

    static List<Location> findPath(Location start, Location end, Location[][] grid) {
        start.shortestPathFromStart.add(start);
        List<Location> path = new ArrayList<>();
        path.add(start);
        fanOut(start, end, path, grid);
        return end.shortestPathFromStart;
    }

    // determine the locations 1 step up, down, left or right from the current location
    // taking into account the edges of the grid
    static Map<Character, Location> findNeighbours(Location here, Location[][] grid) {
        Map<Character, Location> locations = new HashMap<>();
        // try up
        if (here.row > 0) {
            locations.put('^', grid[here.column][here.row-1]);
        }
        // try down
        if (here.row < grid[0].length-1) {
            locations.put('v', grid[here.column][here.row+1]);
        }

        // try left
        if (here.column > 0) {
            locations.put('<', grid[here.column-1][here.row]);
        }

        // try right
        if (here.column < grid.length -1) {
            locations.put('>', grid[here.column+1][here.row]);
        }

        return locations;
    }

    static void fanOut(Location here, Location end, List<Location> path, Location[][] grid) {
        // find locations not yet visited in the current path, 1 step away from current location
        Map<Character, Location> neighbours = findNeighbours(here, grid);

        // visit these locations
        for (Map.Entry<Character, Location> neighbour: neighbours.entrySet()) {
            Location next = neighbour.getValue();
//            System.out.println("from {" + here.column + ", " + here.row + "} at " + here.heightChar + " try to go to " + neighbour.getKey() + " and visit {" + next.column + ", " + next.row + "} at " + next.heightChar);

            if (!path.contains(neighbour.getValue())) {
                if (next.height <= here.height + 1) {
                    if (next.shortestPathFromStart.size() == 0 || path.size() + 1 < next.shortestPathFromStart.size()) {
                        List newPath = new ArrayList<>(path);
                        newPath.add(next);

                        // we've found a shorter path to this next location
                        next.shortestPathFromStart = new ArrayList<>(newPath);

                        // continue when we haven't reached the end yet
                        if (next.heightChar != 'E') {
                            fanOut(next, end, newPath, grid);
                        }
                    }
                }
            }
        }
    }

    public static class Location {
        Character heightChar;
        int height;

        int row;
        int column;
        List<Location> shortestPathFromStart = new ArrayList<>();

        public Location(Character input, int row, int column) {
            this.row = row;
            this.column = column;
            heightChar = input;
            if (heightChar.equals('S')) {
                height = 1;
            } else if (heightChar.equals('E')) {
                height = 26;
            } else {
                height = heightChar.charValue() - new Character('a') + 1;
            }
        }
    }


}
