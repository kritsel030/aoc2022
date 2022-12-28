package day15;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class P15_BeaconExclusionZone {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day15/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day15/real-input.txt";
        }

        int checkline = 10;
        if (!TEST) {
            checkline = 2000000;
        }

        int range = 20;
        if (!TEST) {
            range = 4000000;
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines, checkline);
//        doPart2(inputLines, range);
    }

    // answer: 5100463
    static void doPart1(List<String> inputLines, int checkline) {
        // parse input
        List<int[]> input = inputLines.stream().map(l -> parseInputline(l)).collect(Collectors.toList());

        // determine beacon free positions
        Date start = new Date();
        Set<Integer> result = input.stream().map(i -> beaconFreePositionsOnLine(i[0], i[1], i[2], i[3], checkline, false)).flatMap(Set::stream).collect(Collectors.toSet());
        System.out.println("duration: " + (new Date().getTime() - start.getTime()) + " ms.");
        // collect the positions of beacons present on this row
        List<Integer> beaconsOnCheckLine = inputLines.stream().map(l -> parseInputline(l)).filter(c -> c[3] == checkline).map(c -> c[2]).collect(Collectors.toList());

        // remove these positions from the result set
        beaconsOnCheckLine.stream().forEach(b -> result.remove(b));

        AnswerPrinter.printAnswerDetails(1, 1, result.size(), TEST);
    }

    static void doPart2(List<String> inputLines, int range) {
        List<int[]> input = inputLines.stream().map(l -> parseInputline(l)).collect(Collectors.toList());

        int distressSignalX = -1;
        int distressSignalY = -1;

        for (int l = 0; l <= range; l++) {
            final int l2 = l;
            Set<Integer> takenPositionsOnLine = input.stream().map(i -> beaconFreePositionsOnLine(i[0], i[1], i[2], i[3], l2, false)).flatMap(Set::stream).filter(v -> 0 <= v && v <= range).collect(Collectors.toSet());
            if (takenPositionsOnLine.size() == range) {
                distressSignalY = l;
                System.out.println("distress signal on line " + distressSignalY);
                // now look for the missing element within the range
                for (int i = 0; i <= range; i++) {
                    if (!takenPositionsOnLine.contains(i)) {
                        distressSignalX = i;
                        System.out.println("distress signal on x position " + distressSignalX);
                        break;
                    }
                }
                break;
            }
        }

        AnswerPrinter.printAnswerDetails(1, 2, 4000000 * distressSignalX + distressSignalY, TEST);
    }

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    static Set<Integer> beaconFreePositionsOnLine(String inputLine, int checkLine, boolean debug) {
        int[] coordinates = parseInputline(inputLine);
        return beaconFreePositionsOnLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3], checkLine, debug);
    }

    static int[] parseInputline(String inputLine) {
        int sensorX = Integer.valueOf(between(inputLine,"Sensor at x=", ","));
        String remainingLine = after(inputLine, ", ");
        int sensorY = Integer.valueOf(between(remainingLine, "y=", ":"));
        remainingLine = after(remainingLine, "closest beacon is at ");
        int beaconX = Integer.valueOf(between(remainingLine, "x=", ","));
        remainingLine = after(remainingLine, "y=");
        int beaconY = Integer.parseInt(remainingLine);
        int[] coordinates = {sensorX, sensorY, beaconX, beaconY};
        return coordinates;
    }

    static Set<Integer> beaconFreePositionsOnLine(int sensorX, int sensorY, int beaconX, int beaconY, int checkLine, boolean debug) {
//        System.out.println("sensor at " + sensorX + "," + sensorY + "; beacon at " + beaconX + "," + beaconY + "; looking at line " + checkLine);
        int distance = Math.abs(sensorX-beaconX) + Math.abs(sensorY-beaconY);
        if (debug)System.out.println("  distance: " + distance);
        int verticalDistanceToCheckLine = Math.abs(sensorY - checkLine);
        if (debug)System.out.println("  vertical distance to checkline: " + verticalDistanceToCheckLine);
        Set<Integer> nonBeaconPositions = new HashSet<>();
        if (verticalDistanceToCheckLine < distance) {
            if (debug)System.out.println("  checkline within reach");
            int horizontalReachOnCheckLine = distance - verticalDistanceToCheckLine;
            if (debug)System.out.println("  horizontal reach on checkline: " + horizontalReachOnCheckLine);
            for (int i = -horizontalReachOnCheckLine; i <= horizontalReachOnCheckLine; i++) {
                if (debug)System.out.print(" " + (sensorX + i));
                nonBeaconPositions.add(sensorX + i);
            }
            if (debug) System.out.println();
        } else {
            if (debug)System.out.println("  checkline out of reach");
        }
        return nonBeaconPositions;
    }

    private static String between(String input, String start, String end) {
        return input.substring(start.length(), input.indexOf(end)).trim();
    }

    private static String after(String input, String after) {
        return input.substring(input.indexOf(after) + after.length());
    }

}
