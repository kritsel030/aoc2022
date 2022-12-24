package day02;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P02_RockPaperScissors {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day02/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day02/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {
        List<RPSRound> rounds = runTournament(inputLines, 1);
        long totalScore = calculateScore(rounds);

        int zeroResults = rounds.stream().filter(r -> r.getScore()==0).collect(Collectors.toList()).size();
        System.out.println("number of 0 result rounds: " + zeroResults);

        //Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 1, totalScore, TEST);

    }


    static void doPart2(List<String> inputLines) {
        List<RPSRound> rounds = runTournament(inputLines, 2);
        long totalScore = calculateScore(rounds);

        AnswerPrinter.printAnswerDetails(1, 2, totalScore, TEST);
    }

    private static List<RPSRound> runTournament(List<String> inputLines, int strategy) {

        List<RPSRound> rounds = new ArrayList<>();
        inputLines.stream().forEach(l -> {
            rounds.add(new RPSRound(l, strategy));
        });

        return rounds;
    }

    private static long calculateScore(List<RPSRound> rounds) {
//        return rounds.stream().map(r -> r.score).reduce(0, Integer::sum);
        return rounds.stream().map(r -> r.score).collect(Collectors.summingLong(Long::longValue));
//        long total = 0;
//        Iterator<RPSRound> it = rounds.listIterator();
//        while (it.hasNext()) {
//            total += it.next().getScore();
//        }
//        return total;
    }

}
