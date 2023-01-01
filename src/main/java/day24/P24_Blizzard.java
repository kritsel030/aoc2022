package day24;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.List;
import java.util.NoSuchElementException;

public class P24_Blizzard {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day24/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day24/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
//        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {

        AnswerPrinter.printAnswerDetails(24, 1, 0, TEST);

    }

}
