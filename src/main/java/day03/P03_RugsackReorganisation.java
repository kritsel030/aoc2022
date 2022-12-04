package day03;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P03_RugsackReorganisation {

    static boolean TEST = true;

    public static void main (String[] args) {
        String path = "src/main/resources/day03/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day03/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 8349
    static void doPart1(List<String> inputLines) {
        List<Long> doubleItemPrios = inputLines.stream().map(l->findDoubleItem(l)).map(i->determinePriorityForItem(i)).collect(Collectors.toList());
        long answer = doubleItemPrios.stream().collect(Collectors.summingLong(Long::longValue));

//        // context stuff
//        String doubleItems = inputLines.stream().map(l->findDoubleItem(l)).map(String::valueOf).collect(Collectors.joining());
//        Map<String, Object> context = new HashMap<>();
//        context.put("doubleItems", doubleItems);
//        context.put("prio for 'p'", determinePriorityForItem('p'));
//        context.put("prio for 'P'", determinePriorityForItem('P'));

        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);
    }

    private static char findDoubleItem(String rugsack) {
        String compartment2 = rugsack.substring(rugsack.length() / 2);

        char doubleItem = ' ';
        for (int i = 0; i < rugsack.length() / 2; i++) {
            if (compartment2.contains(""+rugsack.charAt(i))) {
                doubleItem = rugsack.charAt(i);
                break;
            }
        }
        return doubleItem;
    }

    // answer: 2681
    static void doPart2(List<String> inputLines) {
        // create groups of three lines each
        List<List<String>> groups = new ArrayList<>();
        Iterator<String> lineIterator = inputLines.listIterator();
        List<String> group = new ArrayList<>();
        while(lineIterator.hasNext()) {
            group.add(lineIterator.next());
            if (group.size() == 3) {
                groups.add(group);
                group = new ArrayList<>();
            }
        }

//        // context stuff
//        String sharedItems = groups.stream().map(g->findSharedItem(g)).map(String::valueOf).collect(Collectors.joining());
//        Map<String, Object> context = new HashMap<>();
//        context.put("sharedItems", sharedItems);

        // answer
        List<Long> sharedItemPrios = groups.stream().map(P03_RugsackReorganisation::findSharedItem).map(P03_RugsackReorganisation::determinePriorityForItem).collect(Collectors.toList());
        long answer = sharedItemPrios.stream().collect(Collectors.summingLong(Long::longValue));

        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

    private static char findSharedItem(List<String> group) {
        char sharedItem = ' ';
        for (int i = 0; i < group.get(0).length(); i ++) {
            char item = group.get(0).charAt(i);
            if (group.get(1).contains(""+item) && group.get(2).contains(""+item)) {
                sharedItem = item;
                break;
            }
        }
        return sharedItem;
    }

    // A = ascii 65
    // a = ascii 97
    private static long determinePriorityForItem(char item) {
        if (item >= 'a') {
            return item - 'a' + 1;
        } else {
            return item - 'A' + 1 + 26;
        }
    }
}
