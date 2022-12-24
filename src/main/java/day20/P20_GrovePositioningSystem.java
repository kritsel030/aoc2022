package day20;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P20_GrovePositioningSystem {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day20/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day20/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 14888
    static void doPart1(List<String> inputLines) {
        List<NumberWrapper> originalList = initList(inputLines);
        List<NumberWrapper> sequence = new ArrayList<>(originalList);

        moveAround(originalList, sequence);

        // lookup the index of zero
        int indexOfZero = -1;
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i).value == 0) {
                indexOfZero = i;
                break;
            }
        }

        int _1000thIndex = (indexOfZero + 1000) % sequence.size();
        long _1000th = sequence.get(_1000thIndex).value;
        int _2000thIndex = (indexOfZero + 2000) % sequence.size();
        long _2000th = sequence.get(_2000thIndex).value;
        int _3000thIndex = (indexOfZero + 3000) % sequence.size();
        long _3000th = sequence.get(_3000thIndex).value;

        HashMap<String, Object> context = new LinkedHashMap<>();
        context.put("index of 0", indexOfZero);
        context.put("index of 1000th", _1000thIndex);
        context.put("value of 1000th", _1000th );
        context.put("index of 2000th", _2000thIndex);
        context.put("value of 2000th", _2000th );
        context.put("index of 3000th", _3000thIndex);
        context.put("value of 3000th", _3000th );
        AnswerPrinter.printAnswerDetails(1, 1, _1000th + _2000th + _3000th, context, TEST);
    }

    // answer; 3760092545849
    static void doPart2(List<String> inputLines) {
        List<NumberWrapper> originalList = initList(inputLines);
        long decryptionKey = 811589153l;
        originalList = originalList.stream().map(w -> new NumberWrapper(w.value * decryptionKey)).collect(Collectors.toList());
        List<NumberWrapper> sequence = new ArrayList<>(originalList);

        for (int i = 0; i < 10; i++) {
            moveAround(originalList, sequence);
        }

        // lookup the index of zero
        int indexOfZero = -1;
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i).value == 0) {
                indexOfZero = i;
                break;
            }
        }

        int _1000thIndex = (indexOfZero + 1000) % sequence.size();
        long _1000th = sequence.get(_1000thIndex).value;
        int _2000thIndex = (indexOfZero + 2000) % sequence.size();
        long _2000th = sequence.get(_2000thIndex).value;
        int _3000thIndex = (indexOfZero + 3000) % sequence.size();
        long _3000th = sequence.get(_3000thIndex).value;

        HashMap<String, Object> context = new LinkedHashMap<>();
        context.put("index of 0", indexOfZero);
        context.put("index of 1000th", _1000thIndex);
        context.put("value of 1000th", _1000th );
        context.put("index of 2000th", _2000thIndex);
        context.put("value of 2000th", _2000th );
        context.put("index of 3000th", _3000thIndex);
        context.put("value of 3000th", _3000th );
        AnswerPrinter.printAnswerDetails(1, 1, _1000th + _2000th + _3000th, context, TEST);
    }


    private static void moveAround(List<NumberWrapper> originalList, List<NumberWrapper> sequence) {
        System.out.println("moveAround");
        Date start = new Date();
        int steps = 0;

//        System.out.println("start sequence: ");
//        for (NumberWrapper w : sequence) {
//            System.out.print(" " + w.value);
//        }
//        System.out.println();

        for (NumberWrapper wrapper : originalList) {
            long value = 4;
            boolean log = false;
            steps++;
//            System.out.println("step " + steps);
            // find the item in the sequence
            int origIndex = sequence.indexOf(wrapper);
            if (log) System.out.println("  " + value + " found at " + origIndex);

            // remove it
            sequence.remove(wrapper);

            // determine at which index it needs to be added again
            long newIndex = origIndex + wrapper.value;
            if (log) System.out.println("  new index: " + newIndex);

            // take care of indexes smaller than 0
            newIndex = Math.floorMod(newIndex, sequence.size());

            // take care of indexes larger than the list size
            long correctedNewIndex = newIndex % (originalList.size() - 1);
            if (log) System.out.println("  corrected new index: " + newIndex);

            // apparently a value is never inserted at index 0 but at the end instead
            if (correctedNewIndex == 0) {
                correctedNewIndex = sequence.size();
                if (log) System.out.println("  corrected new index: " + newIndex);
            }
            sequence.add((int)correctedNewIndex, wrapper);
//            System.out.println("  move " + wrapper.value + " to index " + correctedNewIndex);
//            System.out.println("  result: ");
//            for (NumberWrapper w : sequence) {
//                System.out.print(" " + w.value);
//            }
//            System.out.println();
        }

//        System.out.println("resulting sequence: ");
//        for (NumberWrapper wrapper : sequence) {
//            System.out.print(" " + wrapper.value);
//        }
//        System.out.println();
        Date end = new Date();
        long durationInSeconds = (end.getTime() - start.getTime())/1000;
        long durationInMilliSeconds = (end.getTime() - start.getTime());
        System.out.println("round took " + durationInMilliSeconds + " ms");
    }

    public static List<NumberWrapper> initList(List<String> inputLines) {
        return inputLines.stream().map(l -> new NumberWrapper(Long.valueOf(l))).collect(Collectors.toList());
    }

    public static class NumberWrapper {
        long value;

        public NumberWrapper(long value ) {
            this.value = value;
        }
    }
}
