package day04;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P04_CampCleanup {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day04/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day04/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 562
    static void doPart1(List<String> inputLines) {
        long answer = inputLines.stream()
                // tranform "2-4,6-8"  into  [[2,3,4],[6,7,8]]
                .map(l -> toAssignedSections(l))
                // transform [[2,3,4],[6,7,8]  into  [|2||3||4|", "|6||7||8|]
                .map(as -> toSectionStrings(as))
                // filter the string pairs where one pair element fully contains the other element
                .filter(strings ->  (strings.get(0).contains(strings.get(1)) || strings.get(1).contains(strings.get(0))) )
                // and count
                .count();

        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);
    }

    // answer: 924
    static void doPart2(List<String> inputLines) {
        long answer = inputLines.stream()
                // tranform "2-4,6-8"  into  into [[2,4],[6,8]]
                .map(l -> toAssignedSectionBoundaries(l))
                // see if either the lower bound or the heigher bound of the first elf
                // is in between the lower and heigher bound of the second elf
                // and also the other way around
                .filter(elfBounds -> {
                    long elf1Low = elfBounds.get(0).get(0);
                    long elf1Heigh =  elfBounds.get(0).get(1);
                    long elf2Low = elfBounds.get(1).get(0);
                    long elf2Heigh =  elfBounds.get(1).get(1);
                    return (elf2Low <= elf1Low && elf1Low <= elf2Heigh ||
                        elf2Low <= elf1Heigh && elf1Heigh <= elf2Heigh) ||
                        (elf1Low <= elf2Low && elf2Low <= elf1Heigh ||
                         elf1Low <= elf2Heigh && elf2Heigh <= elf1Heigh);
                }  )
                // and count
                .count();

        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

    // tranform "2-4,6-8"
    // into [[2,3,4],[6,7,8]]
    static List<List<Long>> toAssignedSections(String line) {
        String[] elfs = line.split(",");
        String[] elf1Boundaries = elfs[0].split("-");
        String[] elf2Boundaries = elfs[1].split("-");

        List<Long> elf1Sections = new ArrayList<>();
        for (long i = Long.parseLong(elf1Boundaries[0]); i <= Long.parseLong(elf1Boundaries[1]); i++) {
            elf1Sections.add(i);
        }
        List<Long> elf2Sections = new ArrayList<>();
        for (long i = Long.parseLong(elf2Boundaries[0]); i <= Long.parseLong(elf2Boundaries[1]); i++) {
            elf2Sections.add(i);
        }

        List<List<Long>> assignedSections = new ArrayList<>();
        assignedSections.add(elf1Sections);
        assignedSections.add(elf2Sections);
        return assignedSections;
    }

    // transform [[2,3,4],[6,7,8]]
    // into ["|2||3||4|", "|6||7||8|"]
    private static List<String> toSectionStrings(List<List<Long>> assignedSections) {
        return assignedSections.stream()
                .map(longList -> longList.stream().map(l -> "|" + l + "|").collect(Collectors.joining("")))
                .collect(Collectors.toList());
    }

    // tranform "2-4,6-8"
    // into [[2,4],[6,8]]
    static List<List<Long>> toAssignedSectionBoundaries(String line) {
        String[] elfs = line.split(",");

        List<List<Long>> assignedSectionBoundaries = new ArrayList<>();
        assignedSectionBoundaries.add(Arrays.asList(elfs[0].split("-")).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList()));
        assignedSectionBoundaries.add(Arrays.asList(elfs[1].split("-")).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList()));

        return assignedSectionBoundaries;
    }
}
