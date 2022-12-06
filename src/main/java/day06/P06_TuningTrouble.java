package day06;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P06_TuningTrouble {

    static boolean TEST = false;

    public static void main (String[] args) {
        String inputLine = null;
        if (TEST) {
//            inputLine = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";
//            inputLine = "bvwbjplbgvbhsrlpgdmjqwftvncz";
//            inputLine = "nppdvjthqldpwncqszvftbrmjlhg";
//            inputLine = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg";
            inputLine = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw";
        } else {
            String path = "src/main/resources/day06/real-input.txt";
            inputLine = FileUtil.readLine(path);
        }
        doPart1(inputLine);
        doPart2(inputLine);
    }

    static void doPart1(String inputLine) {
        Map<String, Object> context = new HashMap<>();
        int answer = 0;
        for (int i = 0; i <= inputLine.length() - 4; i++) {
            String potentialStartOfPackageMarker = inputLine.substring(i, i+4);
            if (allCharactersUnique(potentialStartOfPackageMarker)) {
                answer = i + 4;
                context.put("start of package marker", potentialStartOfPackageMarker);
                break;
            }
        }
        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);
    }

    static void doPart2(String inputLine) {
        Map<String, Object> context = new HashMap<>();
        int answer = 0;
        for (int i = 0; i <= inputLine.length() - 14; i++) {
            String potentialStartOfMessageMarker = inputLine.substring(i, i+14);
            if (allCharactersUnique(potentialStartOfMessageMarker)) {
                answer = i + 14;
                context.put("start of message marker", potentialStartOfMessageMarker);
                break;
            }
        }
        AnswerPrinter.printAnswerDetails(1, 2, answer, context, TEST);
    }

    private static boolean allCharactersUnique(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.replaceAll("" + s.charAt(i), "").length() < s.length() - 1) {
                return false;
            }
        }
        return true;
    }

}
