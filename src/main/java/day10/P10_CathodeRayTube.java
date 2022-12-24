package day10;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P10_CathodeRayTube {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day10/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day10/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {
        // xCycleHistory[c] = value of X at start of cycle c, c=0 represents the first cycle
        List<Long> xCycleHistory = processCommands(inputLines);

        // x during cycle 20 = x at start of cycle 20 = xCycleHistory[20-1]
        long s20 = xCycleHistory.get(20-1) * 20;
        long s60 = xCycleHistory.get(60-1) * 60;
        long s100 = xCycleHistory.get(100-1) * 100;
        long s140 = xCycleHistory.get(140-1) * 140;
        long s180 = xCycleHistory.get(180-1) * 180;
        long s220 = xCycleHistory.get(220-1) * 220;

        long answer = s20 + s60 + s100 + s140 + s180 + s220;
        Map<String, Object> context = new HashMap<>();
        context.put ("x 20", xCycleHistory.get(20-1));
        context.put ("signal strength 20", xCycleHistory.get(20-1) * 20);

        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);

    }

    private static List<Long> processCommands(List<String> inputLines) {
        List<Long> xCycleHistory = new ArrayList<>();
        xCycleHistory.add(1l);

        for (String inputLine : inputLines) {
            if  (inputLine.equals("noop")) {
                doCycles(1, 0, xCycleHistory);
            } else {
                doCycles(2, Integer.parseInt(inputLine.split(" ")[1]), xCycleHistory);
            }
        }
        return xCycleHistory;
    }

    static void doPart2(List<String> inputLines) {
        // xCycleHistory[c] = value of X at start of cycle c, c=0 represents the first cycle
        List<Long> xCycleHistory = processCommands(inputLines);

        // draw pixels
        List<Character> pixels = new ArrayList<>();

        // pixel for location l is drawn during cycle c when the sprite during cycle c covers that location
        for (int c = 0; c < xCycleHistory.size(); c++) {
            long x = xCycleHistory.get(c);
            long location = x + 40 * Math.floorDiv(c , 40);
            if ( location-1 <= c && c <= location +1 ) {
                pixels.add('#');
            } else {
                pixels.add(' ');
            }
        }

        // print pixels
        for (int i = 0; i < pixels.size(); i++) {
            // print three-wide for better readability
            System.out.print(pixels.get(i));
            System.out.print(pixels.get(i));
            System.out.print(pixels.get(i));
            if ((i+1) % 40 == 0) {
                System.out.println();
            }
        }
        AnswerPrinter.printAnswerDetails(1, 2, 0, TEST);
    }

    static void doCycles(int cycles, int deltaX, List<Long> xCycleHistory) {
        for (int c = 0; c < cycles; c++) {
            if (c == cycles-1) {
                xCycleHistory.add(xCycleHistory.get(xCycleHistory.size() - 1) + deltaX);
            } else {
                xCycleHistory.add(xCycleHistory.get(xCycleHistory.size() - 1));
            }
        }
    }


}
