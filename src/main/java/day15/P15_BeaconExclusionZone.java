package day15;

import util.AnswerPrinter;
import util.FileUtil;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class P15_BeaconExclusionZone {

    static boolean TEST = false;

    public static void main(String[] args) {
        String path = "src/main/resources/day15/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day15/real-input.txt";
//            path = "src/main/resources/day15/martin-input.txt";
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
//        doPart1(inputLines, checkline);
        doPart2(inputLines, range);
    }

    // answer: 5100463
    static void doPart1(List<String> inputLines, int checkline) {
        // parse
        List<int[]> input = new ArrayList<>();
        for (int i = 0; i < inputLines.size(); i++) {
            input.add(parseInputline(inputLines.get(i), i));
        }
//        List<int[]> input = inputLines.stream().map(l -> parseInputline(l)).collect(Collectors.toList());

        // determine beacon free positions
        Date start = new Date();
        Set<Integer> result = input.stream().map(i -> beaconFreePositionsOnLine(i[0], i[1], i[2], i[3], checkline, false)).flatMap(Set::stream).collect(Collectors.toSet());
        System.out.println("duration: " + (new Date().getTime() - start.getTime()) + " ms.");
        // collect the positions of beacons present on this row
        List<Integer> beaconsOnCheckLine = inputLines.stream().map(l -> parseInputline(l, 0)).filter(c -> c[3] == checkline).map(c -> c[2]).collect(Collectors.toList());

        // remove these positions from the result set
        beaconsOnCheckLine.stream().forEach(b -> result.remove(b));

        AnswerPrinter.printAnswerDetails(1, 1, result.size(), TEST);
    }

    // answer: 11557863040754
    static void doPart2(List<String> inputLines, int range) {
        List<int[]> inputData = new ArrayList<>();
        for (int i = 0; i < inputLines.size(); i++) {
            inputData.add(parseInputline(inputLines.get(i), i));
        }

//        List<int[]> inputData = new ArrayList<>(4);
//        int[] northWest = {4, 5, 1, 5, 0, 3};
//        inputData.add(northWest);
//        int[] northEast = { 7, 5, 5, 5, 1, 2};
//        inputData.add(northEast);
//        int[] southEast = { 9, 9, 12, 9, 2, 4};
//        inputData.add(southEast);
//        int[] southWest = { 4, 9, 1, 9, 3, 3};
//        inputData.add(southWest);

        // find potential sets of 4 borders who border a single square
        Set<Set<Integer>> potentialSensorSets = new HashSet<>();
        for (int[] input1 : inputData) {
            System.out.println("next input1");
            for (int[] input2 : inputData) {
//                System.out.println("next input2");
                if (!input2.equals(input1)) {
                    if (input1[5] + input2[5] + 2 == distance(input1[0], input1[1], input2[0], input2[1])) {
                        System.out.println("found 2 potential sensors: " + input1[4] + ", " + input2[4]);
                        for (int[] input3 : inputData) {
                            if (!input3.equals(input1) && !input3.equals(input2)) {
                                for (int[] input4 : inputData) {
                                    if (!input4.equals(input3) && !input4.equals(input2) && !input4.equals(input1)) {
                                        if (input3[5] + input4[5] + 2 == distance(input3[0], input3[1], input4[0], input4[1])) {
                                            System.out.println("found 4 potential sensors: " + input1[4] + ", " + input2[4] + ", " + input3[4] + " and " + input4[4]);
                                            int[] sensorIndexes = {input1[4], input2[4], input3[4], input4[4]};
                                            Set<Integer> potentialSet = new HashSet<>(Arrays.stream(sensorIndexes).boxed().collect(Collectors.toList()));
                                            potentialSensorSets.add(potentialSet);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("found " + potentialSensorSets.size() + " potential sensor sets which border the free square");
        System.out.println(potentialSensorSets);

        for (Set<Integer> sensorSetIds : potentialSensorSets) {
            // create a list of sensors based on the sensor Ids
            List<int[]> sensorSet = sensorSetIds.stream().map(i -> inputData.get(i)).collect(Collectors.toList());
            checkSensorSet(sensorSet, inputData);
       }
    }

    static void checkSensorSet(List<int[]> sensorSet, List<int []> inputData) {
        System.out.println("sensor set " + sensorSet.stream().map(s -> s[4]).collect(Collectors.toList()));
        // determine the north-west sensor
        int[] nw = northWestSensor(sensorSet);
        System.out.println("north-west sensor at " + nw[0] + "," + nw[1]);
        // determine the north-east sensor
        int[] ne = northEastSensor(sensorSet);
        System.out.println("north-east sensor at " + ne[0] + "," + ne[1]);

        // the south-east border of the north-west sensor can be described as a formula:
        // (y_1 and x_1 are the coordinates of the north-west sensor)
        // y_1 = (m_1 * x_1) + b_1
        int m_1 = -1;
        // b_1 = x-coordinate + y-coordinate + distance + 1
        int b_1 = (nw[0] + nw[1] + nw[5] + 1);
        System.out.println("south-east border of north-west sensor defined by: y = (" + m_1 + " * x) + " + b_1);

        // the south-west border of the north-east sensor can be described as a formula:
        // (y_2 and x_2 are the coordinates of the north-east sensor)
        // y_2 = (m_2 * x_2) + b_2
        int m_2 = 1;
        // b_2 = -1 * (x-coordinate - y-coordinate - distance - 1)
        int b_2 = -(ne[0] - (ne[1] + ne[5] + 1));
        System.out.println("south-west border of north-east sensor defined by: y = (" + m_2 + " * x) + " + b_2);

        // calculate the intersection point
        int[] intersection = calculateIntersectionPoint(m_1, b_1, m_2, b_2);
        System.out.print("sensor set ");
        System.out.print(sensorSet.stream().map(s -> s[4]).collect(Collectors.toList()));
        System.out.println(" intersects at " + intersection[0] + "," + intersection[1]);

        List<int []> coveredBy = checkLocation(intersection, inputData);
        System.out.println("intersection point covered by " + coveredBy.stream().map(s -> s[4]).collect(Collectors.toList()));

        long answer = (4000000l * (long)intersection[0]) + (long)intersection[1];
        System.out.println("answer: " + answer);
        System.out.println();

    }

    static int[] northWestSensor(List<int[]> sensorSet) {
        // determine the two sensors with the smallest x values
        Collections.sort(sensorSet, (s1, s2) -> s1[0] - s2[0]);
        List<int[]> smallestXes = new ArrayList<>(sensorSet.subList(0, 2));

        // determine the two sensors with the smallest values
        Collections.sort(sensorSet, (s1, s2) -> s1[1] - s2[1]);
        List<int[]> smallestYs = new ArrayList<>(sensorSet.subList(0, 2));

        // determine the north-west sensor
        int[] nw = smallestYs.stream().filter(smallestXes::contains).collect(Collectors.toList()).get(0);
        return nw;
    }

    static int[] northEastSensor(List<int[]> sensorSet) {
        // determine the two sensors with the smallest x values
        Collections.sort(sensorSet, (s1, s2) -> s1[0] - s2[0]);
        List<int[]> smallestXes = new ArrayList<>(sensorSet.subList(0, 2));

        // determine the two sensors with the smallest y values
        Collections.sort(sensorSet, (s1, s2) -> s1[1] - s2[1]);
        List<int[]> smallestYs = new ArrayList<>(sensorSet.subList(0, 2));

        // determine the north-east sensor
        int[] ne = smallestYs.stream().filter(s -> !smallestXes.contains(s)).collect(Collectors.toList()).get(0);
        return ne;
    }

    static int[] southEastSensor(List<int[]> sensorSet) {
        // determine the two sensors with the largest x values
        Collections.sort(sensorSet, (s1, s2) -> s1[0] - s2[0]);
        List<int[]> largestXes = new ArrayList<>(sensorSet.subList(2, 4));

        // determine the two sensors with the largest y values
        Collections.sort(sensorSet, (s1, s2) -> s1[1] - s2[1]);
        List<int[]> largestYs = new ArrayList<>(sensorSet.subList(2, 4));

        // determine the south-east sensor
        int[] se = largestXes.stream().filter(s -> largestYs.contains(s)).collect(Collectors.toList()).get(0);
        return se;
    }

    static int[] southWestSensor(List<int[]> sensorSet) {
        // determine the two sensors with the smallest x values
        Collections.sort(sensorSet, (s1, s2) -> s1[0] - s2[0]);
        List<int[]> smallestXes = new ArrayList<>(sensorSet.subList(0, 2));

        // determine the two sensors with the largest y values
        Collections.sort(sensorSet, (s1, s2) -> s1[1] - s2[1]);
        List<int[]> largestYs = new ArrayList<>(sensorSet.subList(2, 4));

        // determine the south-east sensor
        int[] sw = smallestXes.stream().filter(s -> largestYs.contains(s)).collect(Collectors.toList()).get(0);
        return sw;
    }

    static List<int []> checkLocation(int[] location, List<int []> inputData) {
        List<int[]> coveredBy = inputData.stream()
                .filter( s -> distance(s[0], s[1], location[0], location[1]) <= s[5])
                .collect(Collectors.toList());
        return coveredBy;
    }

    static public int[] calculateIntersectionPoint(
            int m1,
            int b1,
            int m2,
            int b2) {

        if (m1 == m2) {
            return new int[0];
        }

        int x = (b2 - b1) / (m1 - m2);
        int y = m1 * x + b1;

        int[] result = {x, y};
        return result;
    }

//    static void doPart2(List<String> inputLines, int range) {
//       List<int[]> input = inputLines.stream().map(l -> parseInputline(l)).collect(Collectors.toList());
//
//       // each int array in borderlines:
//        // element 0: x of start
//        // element 1: y of start
//        // element 2: x of end
//        // element 3: y of end
//        // element 4: index of sensor
//       List<int[]> borderLines = new ArrayList<>();
//       for (int i = 0 ; i < input.size(); i++) {
//           int[] c = input.get(i);
//           int distance = Math.abs(c[0] - c[2]) + Math.abs(c[1] - c[3]) + 1;
//           System.out.println("distance for input " + i + ": " + distance);
//           // east to south
//           int[] border1 = {c[0] + distance, c[1], c[0], c[1] + distance, i};
//           borderLines.add(border1);
//           // south to west
//           int[] border2 = {c[0], c[1] + distance, c[0] - distance, c[1], i};
//           borderLines.add(border2);
//           // west to north
//           int[] border3 = {c[0] - distance, c[1], c[0], c[1] - distance, i};
//           borderLines.add(border3);
//           // north to east
//           int[] border4 = {c[0], c[1] - distance, c[0] - distance, c[1], i};
//           borderLines.add(border4);
//       };
//
//       System.out.println(borderLines.size() + " border lines");
//
//        // for each borderline determine which borderlines - from other sensors - cross it
//        // key: border line
//        // value: the  border lines it crosses with (including itself)
//        Map<int[], Set<int[]>> crossMap = new HashMap<>();
//        borderLines.stream()
//                .forEach(line1 -> {
//                    Set<int[]> crossSet = new HashSet<>();
//                    crossSet.add(line1);
//                    crossMap.put(line1, crossSet);
//                    borderLines.stream()
//                            // skip the border lines belonging to the same sensor
//                            .filter(line2 -> line1[4] != line2[4])
//                            .filter(line2 -> doLinesCross(line1, line2))
//                            .forEach(line2 -> crossSet.add(line2));
//        });
//
//        // count the number of lines which get crossed by at least 3 other lines
//
//        Map<int[], Set<int[]>> filteredCrossMap = crossMap.entrySet().stream().filter(e -> e.getValue().size() >= 4).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        System.out.println("(before creating a set of map values) there are " + filteredCrossMap.size() + " lines which cross at least 3 borderlines of other sensors");
//
//        Set<Set<int[]>> crossSets = new HashSet<>(crossMap.values());
//        Set<Set<int[]>> filteredCrossSets = crossSets.stream().filter(s -> s.size() >= 4).collect(Collectors.toSet());
//        System.out.println("(after creating a set of map values)  there are " + filteredCrossSets.size() + " lines which cross at least 3 borderlines of other sensors");
//
//        AnswerPrinter.printAnswerDetails(1, 2, 0, TEST);
//    }


    // each line:
    // element 0: x of start
    // element 1: y of start
    // element 2: x of end
    // element 3: y of end
    static boolean doLinesCross(int[] line1, int[] line2) {
        return Line2D.linesIntersect(line1[0], line1[1], line1[2], line1[3], line2[0], line2[1], line2[2], line2[3]);
    }


//    static void doPart2(List<String> inputLines, int range) {
//        List<int[]> input = inputLines.stream().map(l -> parseInputline(l)).collect(Collectors.toList());
//
//        int distressSignalX = -1;
//        int distressSignalY = -1;
//
//        for (int l = 0; l <= range; l++) {
//            final int l2 = l;
//            Set<Integer> takenPositionsOnLine = input.stream().map(i -> beaconFreePositionsOnLine(i[0], i[1], i[2], i[3], l2, false)).flatMap(Set::stream).filter(v -> 0 <= v && v <= range).collect(Collectors.toSet());
//            if (takenPositionsOnLine.size() == range) {
//                distressSignalY = l;
//                System.out.println("distress signal on line " + distressSignalY);
//                // now look for the missing element within the range
//                for (int i = 0; i <= range; i++) {
//                    if (!takenPositionsOnLine.contains(i)) {
//                        distressSignalX = i;
//                        System.out.println("distress signal on x position " + distressSignalX);
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//
//        AnswerPrinter.printAnswerDetails(1, 2, 4000000 * distressSignalX + distressSignalY, TEST);
//    }

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    static Set<Integer> beaconFreePositionsOnLine(String inputLine, int checkLine, boolean debug) {
        int[] coordinates = parseInputline(inputLine, 0);
        return beaconFreePositionsOnLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3], checkLine, debug);
    }

    static int[] parseInputline(String inputLine, int index) {
        int sensorX = Integer.valueOf(between(inputLine,"Sensor at x=", ","));
        String remainingLine = after(inputLine, ", ");
        int sensorY = Integer.valueOf(between(remainingLine, "y=", ":"));
        remainingLine = after(remainingLine, "closest beacon is at ");
        int beaconX = Integer.valueOf(between(remainingLine, "x=", ","));
        remainingLine = after(remainingLine, "y=");
        int beaconY = Integer.parseInt(remainingLine);
        int distance = distance(sensorX, sensorY, beaconX, beaconY);
        int[] data = {sensorX, sensorY, beaconX, beaconY, index, distance};
        return data;
    }

    static int distance (int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
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
