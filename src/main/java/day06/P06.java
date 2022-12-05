package day06;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P06 {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day06/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day06/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {
        long answer = 0;
        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);

    }

    static void doPart2(List<String> inputLines) {
        long answer = 0;
        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

}
