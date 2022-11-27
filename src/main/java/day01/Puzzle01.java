package day01;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle01 {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day01/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day01/input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {

        int answer = 0;
        Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);

    }

    static void doPart2(List<String> inputLines) {
        int answer = 0;
        Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 2, answer, context, TEST);
    }
}
