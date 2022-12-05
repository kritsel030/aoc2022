package day05;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P05_SupplyStacks {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day05/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day05/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        moveCrates(inputLines, 1);
        moveCrates(inputLines, 2);
    }

    // answer part 1: BZLVHBWQF
    // answer part 2: TDGJQTZSL
    static void moveCrates(List<String> inputLines, int strategy) {
        // split inputlines
        List<String> crateLines = new ArrayList<>();
        boolean crateLinesDone = false;
        String stackIdsLine = null;
        List<String> movementLines = new ArrayList<>();

        for (String inputLine : inputLines) {
            if (!crateLinesDone) {
                if (inputLine.contains("[")) {
                    crateLines.add(inputLine);
                } else {
                    crateLinesDone = true;
                    stackIdsLine = inputLine;
                }
            } else {
                if (inputLine.contains ("move")) {
                    movementLines.add(inputLine);
                }
            }
        }

        // parse crate lines
        // first, initialize crateStacksTemp
        Map<String, List<Character>> crateStacksTemp = new HashMap<>();
        int stackCount = (stackIdsLine.length() + 2) / 4;
        for (int i = 0; i < stackCount; i++) {
            crateStacksTemp.put("" + (i+1), new ArrayList<>());
        }
        // then, add the crates
        for (String crateLine : crateLines) {
//            System.out.println("process '" + crateLine + "'");
            for (int i = 0; i < stackCount && crateLine.length() > i*4; i++) {
                String crateElement = crateLine.substring(i*4, i*4 + 3);
                if (crateElement.contains("[")) {
//                    System.out.println("crate element: '" + crateElement + "'");
                    crateStacksTemp.get("" + (i + 1)).add(crateElement.charAt(1));
                }
            }
        }

        // convert Map with Lists into Map with Stacks
        Map<String, Stack<Character>> crateStacks = new HashMap<>();
        for (Map.Entry<String, List<Character>> stackTemp : crateStacksTemp.entrySet()) {
            Collections.reverse(stackTemp.getValue());
            Stack<Character> stack = new Stack<>();
            stack.addAll(stackTemp.getValue());
            crateStacks.put(stackTemp.getKey(), stack);
        }

        // execute movements
        // move 1 from 2 to 1
        int instruction = 0;
        for (String movement : movementLines) {
            instruction++;
            String[] elements = movement.split(" ");
            int number = Integer.parseInt(elements[1]);
            String fromStack = elements[3];
            String toStack = elements[5];
            if (strategy == 1) {
                executeMovement1(crateStacks, number, fromStack, toStack);
            } else {
                executeMovement2(crateStacks, number, fromStack, toStack);
            }
        }

        // collect crateIds at the top of each stack
        StringBuffer topCrates = new StringBuffer();
        for (int i = 1; i <= crateStacks.size(); i++) {
            topCrates.append(crateStacks.get("" + i).peek());
        }

        AnswerPrinter.printAnswerDetails(1, strategy, topCrates.toString(), TEST);
    }

    private static void executeMovement1(Map<String, Stack<Character>> crateStacks, int number, String fromStack, String toStack) {
        for (int i = 0; i < number; i++) {
            // remove crate from fromStack
            Character crateId = crateStacks.get(fromStack).pop();
            // add crate to toStack
            crateStacks.get(toStack).push(crateId);
        }
    }

    private static void executeMovement2(Map<String, Stack<Character>> crateStacks, int number, String fromStack, String toStack) {
        List<Character> removedCrates = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            // remove crate from fromStack
            Character crateId = crateStacks.get(fromStack).pop();
            // save in removedCrates
            removedCrates.add(crateId);
        }
        // add removedCrates in reverse to the toStack
        Collections.reverse(removedCrates);
        crateStacks.get(toStack).addAll(removedCrates);
    }

    static void doPart2(List<String> inputLines) {
        int answer = 0;
        //Map<String, Object> context = new HashMap<>();
        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

}
