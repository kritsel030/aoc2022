package day22;

import org.jetbrains.annotations.NotNull;
import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P22_MonkeyMap {
    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day22/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day22/real-input.txt";
        }

        List<String> inputLines = FileUtil.readAllLines(path);
//        doTests(inputLines);
//        doPart1(inputLines);
        doTests_real_part2(inputLines);
//        doPart2(inputLines);
    }

    // answer: 117102
    static void doPart1(List<String> inputLines) {
        int initX = 51;
        if (TEST) {
             initX = 9;
        }
        Grid grid = new Grid(inputLines.subList(0, inputLines.size()-2), initX, 1, TEST, 1);

        grid.print(4);

        // parse the last input line containing the commands
        List<String> commands = parseCommandLine(inputLines);

        // execute the commands
        commands.stream().forEach(c -> {
            System.out.println("execute " + c);
            if (Character.isDigit(c.charAt(0))) {
                int steps = Integer.valueOf(c);
                grid.move(steps, false);
            } else {
                grid.turn(c.charAt(0));
            }
//            grid.print(4);
        });

        int row = grid.y;
        int column = grid.x;
        int facing = grid.getDirectionValue();
        int answer = 1000 * row + 4 * column + facing;
        HashMap context = new LinkedHashMap();
        context.put("row", row);
        context.put("column", column);
        context.put("facing", facing);

        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);
    }

    // answer: 135297
    static void doPart2(List<String> inputLines) {
        int initX = 51;
        if (TEST) {
            initX = 9;
        }
        Grid grid = new Grid(inputLines.subList(0, inputLines.size()-2), initX, 1, TEST, 2);

        grid.print(4);

        // parse the last input line containing the commands
        List<String> commands = parseCommandLine(inputLines);

        // execute the commands
        commands.stream().forEach(c -> {
            System.out.println("execute " + c);
            if (Character.isDigit(c.charAt(0))) {
                int steps = Integer.valueOf(c);

                System.out.println("move " + steps);
                grid.move(steps, false);
            } else {
                System.out.println("turn " + c.charAt(0));
                grid.turn(c.charAt(0));
            }
//            grid.print(4);
        });

        System.out.println("end situation");
        grid.print(4);

        int row = grid.y;
        int column = grid.x;
        int facing = grid.getDirectionValue();
        int answer = 1000 * row + 4 * column + facing;
        HashMap context = new LinkedHashMap();
        context.put("row", row);
        context.put("column", column);
        context.put("facing", facing);

        // 120177 is too low
        // 184030 is too high
        // 15463 is too low
        // 106189 is wrong

        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);
    }

    @NotNull
    private static List<String> parseCommandLine(List<String> inputLines) {
        String commandLine = inputLines.get(inputLines.size()-1);
        List<String> commands = new ArrayList<>();
        StringBuffer command = new StringBuffer();
        for (Character c : commandLine.toCharArray()) {
            if (Character.isDigit(c)) {
                command.append(c);
            } else {
                // close the number command
                commands.add(command.toString());
                // create and add a new letter command
                command = new StringBuffer();
                command.append(c);
                commands.add(command.toString());
                // and prepare for a new number command
                command = new StringBuffer();
            }
        }

        if (command.length() > 0) {
            commands.add(command.toString());
        }
        return commands;
    }

    static void doTests_test_part1(List<String> inputLines) {
        // test wrap around from top to bottom
        test(inputLines, 9, 1, '^', 1,  0, 0, (char)0,true, 1);

        // test wrap around from bottom to top
        test(inputLines, 10, 12, 'v', 1,  0, 0, (char)0,true, 1);

        // test wrap around from right to left
        test(inputLines, 12, 2, '>', 1,  0, 0, (char)0,true, 1);

        // test wrap around from left to right
        test(inputLines, 9, 2, '<', 1,  0, 0, (char)0,true, 1);
    }

    static void doTests_real_part2(List<String> inputLines) {
        // edge A - up
        test(inputLines, 1, 101, '^', 1, 51, 51, '>',false, 2);
        // edge A - right
        test(inputLines, 51, 51, '<', 1, 1, 101, 'v',false, 2);

        // edge B - left
        test(inputLines, 1, 101, '<', 1, 51, 50, '>',false, 2);
        // edge B - left
        test(inputLines, 51, 50, '<', 1, 1, 101, '>',false, 2);
        // edge B - left - hit rock
        test(inputLines, 1, 104, '<', 1, 1, 104, '<',false, 2);

        // edge C - down
        test(inputLines, 51, 150, 'v', 1, 50, 151, '<',false, 2);
        // edge C - right
        test(inputLines, 50, 151, '>', 1, 51, 150, '^',false, 2);

        // edge D - down
        test(inputLines, 101, 50, 'v', 1, 100, 51, '<',false, 2);
        // edge D - right
        test(inputLines, 100, 51, '>', 1, 101, 50, '^',false, 2);

        // edge E - right
        test(inputLines, 150, 1, '>', 1, 100, 150, '<',false, 2);
        // edge E - right
        test(inputLines, 100, 150, '>', 1, 150, 1, '<',false, 2);

        // edge F - up
        test(inputLines, 101, 1, '^', 1, 1, 200, '^',false, 2);
        // edge F - down
        test(inputLines, 1, 200, 'v', 1, 101, 1, 'v',false, 2);

        // edge G - up
        test(inputLines, 52, 1, '^', 1, 1, 152, '>',false, 2);
        // edge G - left
        test(inputLines, 1, 152, '<', 1, 52, 1, 'v',false, 2);
    }


    static void test(List<String> inputLines, int initX, int initY, char direction, int steps, int expectedX, int expectedY, char expectedDirection, boolean isTest, int strategy) {
        Grid grid = new Grid(inputLines.subList(0, inputLines.size()-2), initX, initY, isTest, strategy);

        System.out.println("start location: " + grid.x + "," + grid.y + "; facing " + direction);
        grid.setDirection(direction);
//        grid.print(4);
//        System.out.println();

        System.out.println("move " + steps + " steps");
        grid.move(steps, false);

        System.out.println("new location: " + grid.x + "," + grid.y + "; facing " + grid.direction);
//        grid.print(4);

        if (expectedX != 0 && expectedY != 0 && expectedDirection != 0) {
            if (grid.x != expectedX || grid.y != expectedY || grid.direction != expectedDirection) {
                System.out.println("ERROR! expected " + expectedX + "," + expectedY + "," + expectedDirection + "; got " + grid.x + "," + grid.y + "," + grid.direction);
            } else {
                System.out.println("OK");
            }
        }

        System.out.println("---------------------------------------------------------------------------");
    }

    public static class Grid {

        String[][] data;

        // current direction we're moving in
        // ^ up
        // v down
        // > right
        // < left
        char direction;

        // x part of our current coordinate
        int x;

        // y part of our current coordinate
        int y;

        // key: wrap around marker like +A1
        // value: connecting coordinate (x=first value, y = second value)
        Map<String, int[]> wrapAroundMap = new HashMap<>();

        Grid setDirection(char direction) {
            if ("^v><".indexOf(direction) < 0) {
                throw new IllegalArgumentException("setDirection(d) only accepts ^, v, > or < (current argument: " + direction + ")");
            }
            this.direction = direction;
            return this;
        }

        int getDirectionValue() {
            return ">v<^".indexOf(direction);
        }

        Grid setCurrentPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        // assumption: top left corner: x=0; y=0

        public Grid(List<String> gridInputLines, int initX, int initY, boolean test, int strategy) {
            setDirection('>');
            x = initX;
            y = initY;
            // create 2D grid with an additional border on each side
            int maxY = gridInputLines.size() + 2;
            int maxX = Collections.max(gridInputLines.stream().map(l -> l.length()).collect(Collectors.toList())) + 2;
            data = new String[maxX][maxY];

            // init all grid values with ""
            for (int x = 0; x < data.length; x++) {
                for (int y = 0; y < data[0].length; y++) {
                    data[x][y] = "";
                }
            }

            // populate the grid with input
            // - keep top row empty
            // - keep first column empty
            for (int l = 0; l < gridInputLines.size(); l++) {
                String line = gridInputLines.get(l);
                for (int c = 0; c < line.length(); c++) {
                    data[c+1][l+1] = "" + line.charAt(c);
                }
            }

            // mark corresponding wrap-around coordinates
            if (TEST) {
                if (strategy == 1) {
                    linkWrapAroundCoordinates_part1_test();
                } else {
                    linkWrapAroundCoordinates_part2_test();
                }
            } else {
                if (strategy == 1) {
                    linkWrapAroundCoordinatesForRealInput();
                } else {
                    linkWrapAroundCoordinates_part2_real();
                }
            }
        }

        private void linkWrapAroundCoordinatesForRealInput() {
            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6
            int width = 50;

            // horizontal edge A: top of quadrant 1, connects to bottom of quadrant 5
            char edgeMarker = 'A';
            int startX = (1 * width) + 1;
            int topY = (0 * width) + 1;
            int bottomY = (3 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);

            // horizontal edge B: top of quadrant 2, connects to bottom of quadrant 2
            edgeMarker = 'B';
            startX = (2 * width) + 1;
            topY = (0 * width) + 1;
            bottomY = (1 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);

            // vertical edge C: right of quadrant 2, connects to left of quadrant 1
            edgeMarker = 'C';
            int startY = (0 * width) + 1;
            int leftX = (1 * width) + 1;
            int rightX = (3 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // vertical edge D: right of quadrant 3, connects to left of quadrant 3
            edgeMarker = 'D';
            startY = (1 * width) + 1;
            leftX = (1 * width) + 1;
            rightX = (2 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // vertical edge E: right of quadrant 5, connects to left of quadrant 4
            edgeMarker = 'E';
            startY = (2 * width) + 1;
            leftX = (0 * width) + 1;
            rightX = (2 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // vertical edge F: right of quadrant 6, connects to left of quadrant 6
            edgeMarker = 'F';
            startY = (3 * width) + 1;
            leftX = (0 * width) + 1;
            rightX = (1 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // horizontal edge G: top of quadrant 4, connects to bottom of quadrant 6
            edgeMarker = 'G';
            startX = (0 * width) + 1;
            topY = (2 * width) + 1;
            bottomY = (4 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);
        }


        private void linkWrapAroundCoordinates_part1_test() {
            // quadrant identifiers:
            //      1
            //  2 3 4
            //      5  6
            int width = 4;

            // horizontal edge A: top of quadrant 1, connects to bottom of quadrant 5
            char edgeMarker = 'A';
            int startX = (2 * width) + 1;
            int topY = (0 * width) + 1;
            int bottomY = (3 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);

            // vertical edge B: right side of quadrant 1, connects to left side of quadrant 1
            edgeMarker = 'B';
            int startY = (0 * width) + 1;
            int leftX = (2 * width) + 1;
            int rightX = (3 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // vertical edge C: right side of quadrant 4, connects to left side of quadrant 2
            edgeMarker = 'C';
            startY = (1 * width) + 1;
            leftX = (0 * width) + 1;
            rightX = (3 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // horizontal edge D: top of quadrant 6, connects to bottom of quadrant 6
            edgeMarker = 'D';
            startX = (3 * width) + 1;
            topY = (2 * width) + 1;
            bottomY = (3 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);

            // vertical edge E: right side of quadrant 6, connects to left side of quadrant 5
            edgeMarker = 'E';
            startY = (2 * width) + 1;
            leftX = (2 * width) + 1;
            rightX = (4 * width);
            markVerticalEdge(wrapAroundMap, edgeMarker, startY, width, leftX, rightX);

            // horizontal edge F: top of quadrant 3, connects to bottom of quadrant 3
            edgeMarker = 'F';
            startX = (1 * width) + 1;
            topY = (1 * width) + 1;
            bottomY = (2 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);

            // horizontal edge G: top of quadrant 2, connects to bottom of quadrant 2
            edgeMarker = 'G';
            startX = (0 * width) + 1;
            topY = (1 * width) + 1;
            bottomY = (2 * width);
            markHorizontalEdge(wrapAroundMap, edgeMarker, startX, width, topY, bottomY);
        }

        private void linkWrapAroundCoordinates_part2_test() {
            // quadrant identifiers:
            //      1
            //  2 3 4
            //      5  6
            int width = 4;

            // edge G: left of quadrant 5 connects to bottom of 3 (reversed)
            char edgeMarker = 'G';
            // sequence 1: left of 5
            char seq1WalkOffDirection = '<';
            boolean seq1IsColumn = true;
            boolean seq1Increase = true;
            int seq1StartX = (2 * width) + 1;
            int seq1StartY = (2 * width) + 1;
            // sequence 2: bottom of 3 (in reverse, start at the right)
            char seq2WalkOffDirection = 'v';
            boolean seq2IsColumn = false;
            boolean seq2Increase = false;
            int seq2StartX = (2 * width);
            int seq2StartY = (2 * width);
            char turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge F: bottom of quadrant 5 connects to bottom of 2 (reversed)
            edgeMarker = 'F';
            // sequence 1: bottom of 5
            seq1WalkOffDirection = 'v';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (2 * width) + 1;
            seq1StartY = (3 * width);
            // sequence 2: bottom of 2 (in reverse, start at the right)
            seq2WalkOffDirection = 'v';
            seq2IsColumn = false;
            seq2Increase = false;
            seq2StartX = (1 * width);
            seq2StartY = (2 * width) ;
            turn1To2 = 'B'; // B for back (or 2x R, or 2x L)
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge E: bottom of quadrant 6 connects to left of quadrant 2 (reversed)
            edgeMarker = 'E';
            // sequence 1: bottom of 6
            seq1WalkOffDirection = 'v';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (3 * width) + 1;
            seq1StartY = (3 * width);
            // sequence 2: left of 2 (in reverse, start at bottom)
            seq2WalkOffDirection = '<';
            seq2IsColumn = true;
            seq2Increase = false;
            seq2StartX = (0 * width) + 1;
            seq2StartY = (2 * width) ;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge D: right of quadrant 4 connects to top of quadrant 6 (reversed)
            edgeMarker = 'D';
            // sequence 1: right of 4
            seq1WalkOffDirection = '>';
            seq1IsColumn = true;
            seq1Increase = true;
            seq1StartX = (3 * width);
            seq1StartY = (1 * width) + 1;
            // sequence 2: top of 6 (in reverse)
            seq2WalkOffDirection = '^';
            seq2IsColumn = false;
            seq2Increase = false;
            seq2StartX = (4 * width);
            seq2StartY = (2 * width) + 1;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge C: right of quadrant 1 connects to right of quadrant 6 (in reverse)
            edgeMarker = 'C';
            // sequence 1: right of 1
            seq1WalkOffDirection = '>';
            seq1IsColumn = true;
            seq1Increase = true;
            seq1StartX = (3 * width);
            seq1StartY = (0 * width) + 1;
            // sequence 2: right of 6 (in reverse)
            seq2WalkOffDirection = '>';
            seq2IsColumn = true;
            seq2Increase = false;
            seq2StartX = (4 * width);
            seq2StartY = (3 * width);
            turn1To2 = 'B';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge B: top of quadrant 1 connects to top of quadrant 2
            edgeMarker = 'B';
            // sequence 1: top of 1
            seq1WalkOffDirection = '^';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (2 * width) + 1;
            seq1StartY = (0 * width) + 1;
            // sequence 2: top of 2 (in reverse)
            seq2WalkOffDirection = '^';
            seq2IsColumn = false;
            seq2Increase = false;
            seq2StartX = (1 * width);
            seq2StartY = (1 * width) + 1;
            turn1To2 = 'B';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // edge A: top of quadrant 3 connects to left of quadrant 1
            edgeMarker = 'A';
            // sequence 1: top of 3
            seq1WalkOffDirection = '^';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (1 * width) + 1;
            seq1StartY = (1 * width) + 1;
            // sequence 2: left of 1
            seq2WalkOffDirection = '<';
            seq2IsColumn = true;
            seq2Increase = true;
            seq2StartX = (2 * width) + 1;
            seq2StartY = (0 * width) + 1;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);
        }

        private void linkWrapAroundCoordinates_part2_real() {
            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6
            //             *       *
            //             G G G   F F F
            //            ---------------
            //          B| 1 1 1 | 2 2 2 |E*
            //          B| 1 1 1 | 2 2 2 |E
            //         *B| 1 1 1 | 2 2 2 |E
            //           |-------|-------
            //         *A| 3 3 3 |*D D D
            //     *    A| 3 3 3 |D
            //     A A   | 3 3 3 |D
            //    -------|-------|
            //  *| 4 4 4 | 5 5 5 |E
            //  B| 4 4 4 | 5 5 5 |E
            //  B| 4 4 4 | 5 5 5 |E*
            //   |-------|-------
            // *G| 6 6 6 |*C C C
            //  G| 6 6 6 |C
            //  G| 6 6 6 |C
            //    -------
            //     F F F
            //     *
            int width = 50;

            // edge G: top of quadrant 1 connects to left of 6
            char edgeMarker = 'G';
            // sequence 1: top of 1
            char seq1WalkOffDirection = '^';
            boolean seq1IsColumn = false;
            boolean seq1Increase = true;
            int seq1StartX = (1 * width) + 1;
            int seq1StartY = (0 * width) + 1;
            // sequence 2: left of 6
            char seq2WalkOffDirection = '<';
            boolean seq2IsColumn = true;
            boolean seq2Increase = true;
            int seq2StartX = (0 * width) + 1;
            int seq2StartY = (3 * width) + 1;
            char turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge F: top of quadrant 2 connects to bottom of 6
            edgeMarker = 'F';
            // sequence 1: top of 2
            seq1WalkOffDirection = '^';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (2 * width) + 1;
            seq1StartY = (0 * width) + 1;
            // sequence 2: bottom of 6
            seq2WalkOffDirection = 'v';
            seq2IsColumn = false;
            seq2Increase = true;
            seq2StartX = (0 * width) + 1;
            seq2StartY = (4 * width) ;
            turn1To2 = 0; // 0 for no turn
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge E: right of quadrant 2 connects to right of quadrant 5 (reversed)
            edgeMarker = 'E';
            // sequence 1: right of 2
            seq1WalkOffDirection = '>';
            seq1IsColumn = true;
            seq1Increase = true;
            seq1StartX = (3 * width);
            seq1StartY = (0 * width) + 1;
            // sequence 2: right of 5 (in reverse, start at bottom)
            seq2WalkOffDirection = '>';
            seq2IsColumn = true;
            seq2Increase = false;
            seq2StartX = (2 * width) ;
            seq2StartY = (3 * width) ;
            turn1To2 = 'B';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge D: bottom of quadrant 2 connects to right of quadrant 3
            edgeMarker = 'D';
            // sequence 1: bottom of 2
            seq1WalkOffDirection = 'v';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (2 * width) + 1 ;
            seq1StartY = (1 * width) ;
            // sequence 2: right of 3
            seq2WalkOffDirection = '>';
            seq2IsColumn = true;
            seq2Increase = true;
            seq2StartX = (2 * width);
            seq2StartY = (1 * width) + 1;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge C: bottom of quadrant 5 connects to right of quadrant 6
            edgeMarker = 'C';
            // sequence 1: bottom of 5
            seq1WalkOffDirection = 'v';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (1 * width) + 1;
            seq1StartY = (3 * width);
            // sequence 2: right of 6
            seq2WalkOffDirection = '>';
            seq2IsColumn = true;
            seq2Increase = true;
            seq2StartX = (1 * width) ;
            seq2StartY = (3 * width) + 1;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge B: left of quadrant 4 connects to left of quadrant 1 (in reverse)
            edgeMarker = 'B';
            // sequence 1: left of 4
            seq1WalkOffDirection = '<';
            seq1IsColumn = true;
            seq1Increase = true;
            seq1StartX = (0 * width) + 1;
            seq1StartY = (2 * width) + 1;
            // sequence 2: left of 1 (in reverse)
            seq2WalkOffDirection = '<';
            seq2IsColumn = true;
            seq2Increase = false;
            seq2StartX = (1 * width) + 1;
            seq2StartY = (1 * width);
            turn1To2 = 'B';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);

            // quadrant identifiers:
            //    1 2
            //    3
            //  4 5
            //  6

            // edge A: top of quadrant 4 connects to left of quadrant 3
            edgeMarker = 'A';
            // sequence 1: top of 4
            seq1WalkOffDirection = '^';
            seq1IsColumn = false;
            seq1Increase = true;
            seq1StartX = (0 * width) + 1;
            seq1StartY = (2 * width) + 1;
            // sequence 2: left of 3
            seq2WalkOffDirection = '<';
            seq2IsColumn = true;
            seq2Increase = true;
            seq2StartX = (1 * width) + 1;
            seq2StartY = (1 * width) + 1;
            turn1To2 = 'R';
            connectEdges(wrapAroundMap, edgeMarker, width, seq1WalkOffDirection, seq1IsColumn, seq1Increase, seq1StartX, seq1StartY, seq2WalkOffDirection, seq2IsColumn, seq2Increase, seq2StartX, seq2StartY, turn1To2);
        }


        // topY must be smaller than bottomY!
        private void markHorizontalEdge(Map<String, int[]> wrapAroundMap, char edgeMarker, int startX, int width, int topY, int bottomY) {
            for (int x = startX; x < startX + width; x++) {
                data[x][topY-1] = "w" + edgeMarker + "^";
                int[] connectingBottomCoordinate = {x, bottomY};
                wrapAroundMap.put(x + "-" + topY + "-^",connectingBottomCoordinate);

                data[x][bottomY+1] = "w" + edgeMarker + "v";
                int[] connectingTopCoordinate = {x, topY};
                wrapAroundMap.put(x + "-" + bottomY + "-v",connectingTopCoordinate);
            }
        }

        // leftX must be smaller than rightX!
        private void markVerticalEdge(Map<String, int[]> wrapAroundMap, char edgeMarker, int startY, int width, int leftX, int rightX) {
            for (int y = startY; y < startY + width; y++) {
                data[leftX-1][y] = "w" + edgeMarker + "<";
                int[] connectingRightCoordinate = {rightX, y};
                wrapAroundMap.put(leftX + "-" + y + "-<",connectingRightCoordinate);

                data[rightX+1][y] = "w" + edgeMarker + ">";
                int[] connectingLeftCoordinate = {leftX, y};
                wrapAroundMap.put(rightX + "-" + y + "->",connectingLeftCoordinate);
            }
        }

        private void connectEdges(Map<String, int[]> wrapAroundMap, char edgeMarker, int width,
                                  char seq1WalkOffDirection, boolean seq1IsColumn, boolean seq1Increase, int seq1StartX, int seq1StartY,
                                  char seq2WalkOffDirection, boolean seq2IsColumn, boolean seq2Increase, int seq2StartX, int seq2StartY,
                                  char turn1To2) {
            for (int i = 0; i < width; i++) {
                int coordinate1X = seq1IsColumn ? seq1StartX : (seq1Increase ? seq1StartX+i : seq1StartX-i);
                int coordinate1Y = !seq1IsColumn ? seq1StartY : (seq1Increase ? seq1StartY+i : seq1StartY-i);
                int coordinate2X = seq2IsColumn ? seq2StartX : (seq2Increase ? seq2StartX+i : seq2StartX-i);
                int coordinate2Y = !seq2IsColumn ? seq2StartY : (seq2Increase ? seq2StartY+i : seq2StartY-i);

                // add info to wrapAroundMap
                int[] coordinate1 = {coordinate1X, coordinate1Y, turn1To2 == 'R' ? 'L' : (turn1To2 == 'L' ? 'R' : turn1To2)};
                int[] coordinate2 = {coordinate2X, coordinate2Y, turn1To2};
                wrapAroundMap.put(coordinate1X + "-" + coordinate1Y + "-" + seq1WalkOffDirection, coordinate2);
                wrapAroundMap.put(coordinate2X + "-" + coordinate2Y + "-" + seq2WalkOffDirection, coordinate1);

                // add info to the grid
                int info1X = coordinate1X + (seq1IsColumn ? (seq1WalkOffDirection == '<' ? -1 : 1) : 0);
                int info1Y = coordinate1Y + (!seq1IsColumn ? (seq1WalkOffDirection == 'v' ? 1 : -1) : 0);
                data[info1X][info1Y] = "" + edgeMarker + seq1WalkOffDirection + (i==0 ? "*" : "");
                int info2X = coordinate2X + (seq2IsColumn ? (seq2WalkOffDirection == '<' ? -1 : 1) : 0);
                int info2Y = coordinate2Y + (!seq2IsColumn ? (seq2WalkOffDirection == 'v' ? 1 : -1) : 0);
                data[info2X][info2Y] = "" + edgeMarker + seq2WalkOffDirection + (i==0 ? "*" : "");
            }
        }

        // move 'positions' into the current direction
        public Grid move(int steps, boolean debug) {
            for (int s = 0; s < steps; s++) {
                int nextY = this.y;
                int nextX = this.x;
                switch (direction) {
                    case '^':
                        nextY--;
                        break;
                    case 'v':
                        nextY++;
                        break;
                    case '>':
                        nextX++;
                        break;
                    case '<':
                        nextX--;
                        break;
                }
                String positionValue = data[nextX][nextY];

                char turnInstruction = 0;
                if (!positionValue.contains(".") && !positionValue.contains("#")) {
                    if (debug) System.out.println(nextX + ", " + nextY + ": we've hit a wrap-around location");
                    int[] wrappedAroundPosition = wrapAroundMap.get(this.x + "-" + this.y + "-" + direction);
                    if (debug) System.out.println("  proceed to " + wrappedAroundPosition[0] + "," + wrappedAroundPosition[1]);
                    positionValue = data[wrappedAroundPosition[0]][wrappedAroundPosition[1]];
                    nextX = wrappedAroundPosition[0];
                    nextY = wrappedAroundPosition[1];
                    if (wrappedAroundPosition.length == 3) {
                        // 3rd element is a turn instruction
                        if (debug) System.out.println("  and turn " + (char)wrappedAroundPosition[2] + " when successful");
                        turnInstruction = (char)wrappedAroundPosition[2];
                    }
                }

                if (positionValue.equals(".")) {
                    // we've hit a free spot, we can continue
                    if (debug) System.out.println(nextX + "," + nextY + ": we've hit a free spot");
                    this.x = nextX;
                    this.y = nextY;
                    if (turnInstruction != 0) {
                        turn(turnInstruction);
                    }
                } else if (positionValue.equals("#")) {
                    if (debug) System.out.println(nextX + "," + nextY + ": we've hit a rock, don't finish this current step and stop moving");
                    // we've hit a rock, we can stop
                    break;
                }
            }
            return this;
        }

        // change our current direction
        public Grid turn(char turnDirection) {
            switch (turnDirection) {
                case 'R':
                    switch(direction) {
                        case '^':
                            direction = '>';
                            break;
                        case '>':
                            direction = 'v';
                            break;
                        case 'v':
                            direction = '<';
                            break;
                        case '<':
                            direction = '^';
                            break;
                    }
                    break;
                case 'L':
                    switch(direction) {
                        case '^':
                            direction = '<';
                            break;
                        case '>':
                            direction = '^';
                            break;
                        case 'v':
                            direction = '>';
                            break;
                        case '<':
                            direction = 'v';
                            break;
                    }
                    break;
                case 'B':
                    switch(direction) {
                        case '^':
                            direction = 'v';
                            break;
                        case '>':
                            direction = '<';
                            break;
                        case 'v':
                            direction = '^';
                            break;
                        case '<':
                            direction = '>';
                            break;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("turn(t) only accepts R, L or B (current argument: " + turnDirection + ")");
            }
            return this;
        }

        public void print(int w) {
            for (int y = 0; y < data[0].length; y++) {
                StringBuffer row = new StringBuffer();
                for (int x = 0; x < data.length; x++) {
                    String value = data[x][y];
                    if (x == this.x && y == this.y) {
                        value = "" + direction + direction + direction;
                    }
                    row.append(String.format("%" + w + "s", value));
                }
                System.out.println(row);
            }
        }
    }

}
