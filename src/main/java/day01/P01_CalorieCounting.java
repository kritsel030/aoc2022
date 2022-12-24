package day01;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P01_CalorieCounting {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day01/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day01/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {
        List<Long> totals = calculateElfCalories(inputLines);

        long answer = totals
                .stream()
                .mapToLong(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
        //Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);

    }

    static void doPart2(List<String> inputLines) {
        List<Long> totals = calculateElfCalories(inputLines);
        Collections.sort(totals);

        long largestThree = 0;
        for (int i = totals.size()-1; i >= totals.size()-3; i--) {
            largestThree += totals.get(i);
        }
        int answer = 0;
        //Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 2, largestThree, TEST);
    }

    private static List<Long> calculateElfCalories(List<String> inputLines) {
        List<Long> totals = new ArrayList<>();
        Iterator<String> iterator = inputLines.listIterator();

        long currentElfCalories = 0;
        while (iterator.hasNext()) {
            String row = iterator.next();
            if (row.length() == 0) {
                totals.add(currentElfCalories);
                currentElfCalories = 0;
            } else {
                currentElfCalories += Long.parseLong(row);
            }
        }
        totals.add(currentElfCalories);
        return totals;
    }
}
