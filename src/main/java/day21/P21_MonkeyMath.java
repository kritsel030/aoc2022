package day21;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class P21_MonkeyMath {
    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day21/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day21/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 87457751482938
    static void doPart1(List<String> inputLines) {
        // initialize monkeys
        Map<String, Monkey> allMonkeys = inputLines.stream().map(l -> new Monkey(l)).collect(Collectors.toMap(Monkey::getName, Function.identity()));
        allMonkeys.values().stream().forEach(m -> m.initPrecedingMonkeys(allMonkeys));

        // find the root monkey
        Monkey root = allMonkeys.get("root");

        // find the value of root
        long answer = root.executeJob(1);

        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);

    }

    // anser: 3221245824363
    static void doPart2(List<String> inputLines) {
        // initialize monkeys
        Map<String, Monkey> allMonkeys = inputLines.stream().map(l -> new Monkey(l)).collect(Collectors.toMap(Monkey::getName, Function.identity()));
        allMonkeys.values().stream().forEach(m -> m.initPrecedingMonkeys(allMonkeys));

        // find the root monkey
        Monkey root = allMonkeys.get("root");

        try {
            long leftValue = root.precedingMonkeys.get(0).executeJob(2);
            System.out.println("root | left value: " + leftValue);
            // the right value (2nd preceding monkey) should als become this value
            root.precedingMonkeys.get(1).reverseJob(leftValue);

        } catch (Exception e) {
            System.out.println("error when executing " + root.precedingMonkeys.get(0).name + ".executeJob(2)");
        }
        try {
            long rightValue = root.precedingMonkeys.get(1).executeJob(2);
            System.out.println("root | right value: " + rightValue);
            // the left value (1st preceding monkey) should als become this value
            root.precedingMonkeys.get(0).reverseJob(rightValue);
        } catch (Exception e) {
            System.out.println("error when executing " + root.precedingMonkeys.get(1).name + ".executeJob(2)");
        }
        AnswerPrinter.printAnswerDetails(1, 2, allMonkeys.get("humn").value, TEST);
    }


    public static class Monkey {
        String name;

        Character operator;

        long value;

        boolean ready;

        List<Monkey> precedingMonkeys = new ArrayList<>();
        List<String> precedingMonkeyNames = new ArrayList<>();

        public String getName() {
            return name;
        }

        // examples:
        //  root: pppw + sjmn
        //  dbpl: 5
        public Monkey (String inputLine) {
            this.name = inputLine.substring(0, inputLine.indexOf(":")).trim();
            String job = inputLine.substring(inputLine.indexOf(":") + 2);
            if (job.trim().indexOf(" ") > 0) {
                String[] jobElements = job.split(" ");
                this.precedingMonkeyNames.add(jobElements[0].trim());
                this.precedingMonkeyNames.add(jobElements[2].trim());
                this.operator = jobElements[1].charAt(0);
            } else {
                this.value = Long.valueOf(job);
            }
        }

        public long executeJob(int strategy) {
            if (operator == null) {
                if (strategy == 2 && name.equals("humn")) {
                    throw new IllegalStateException("value of humn is requested");
                }
                return value;
            } else if (ready) {
                return value;
            } else {
                precedingMonkeys.stream().forEach(m -> m.executeJob(strategy));
                long result = 0;
                if (operator == '+') {
                    result = precedingMonkeys.get(0).value + precedingMonkeys.get(1).value;
                } else if (operator == '-') {
                    result = precedingMonkeys.get(0).value - precedingMonkeys.get(1).value;
                } else if (operator == '/') {
                    result = precedingMonkeys.get(0).value / precedingMonkeys.get(1).value;
                } else if (operator == '*') {
                    result = precedingMonkeys.get(0).value * precedingMonkeys.get(1).value;
                }
                value = result;
                ready = true;
                return result;
            }
        }

        public void reverseJob(long result) {
            System.out.println(this.name + " | outcome: " + result);
            if (operator == null) {
                // nothing to do
                if (name.equals("humn")) {
                    value = result;
                } else {
                    throw new IllegalStateException("setting the value of monkey " + name + ", this should not happen");
                }
            } else {
                value = result;
                Long leftValue = null;
                Long rightValue = null;
                try {
                    leftValue = precedingMonkeys.get(0).executeJob(2);
                    System.out.println(this.name + " | left value: " + leftValue + " (given)");
                    // no exception, meaning the value for operand1 is know, we need to find out what the value for operand2 is
                    if (operator == '+') {
                        rightValue = result - leftValue;
                    } else if (operator == '-'){
                        rightValue = -1 * (result - leftValue);
                    } else if (operator == '/') {
                        rightValue = 1 / (result / leftValue);
                    } else if (operator == '*') {
                        rightValue = result / leftValue;
                    }
                    System.out.println(this.name + " | right value: " + leftValue + " (calculated)");
                    precedingMonkeys.get(1).reverseJob(rightValue);
                } catch (Exception e) {
                    // exception, meaning the value for operand2 is know, we need to find out what the value for operand1 is
                    rightValue = precedingMonkeys.get(1).executeJob(2);
                    if (operator == '+') {
                        leftValue = result - rightValue;
                    } else if (operator == '-'){
                        leftValue = result + rightValue;
                    } else if (operator == '/') {
                        leftValue = result * rightValue;
                    } else if (operator == '*') {
                        leftValue = result / rightValue;
                    }
                    precedingMonkeys.get(0).reverseJob(leftValue);
                }
            }
        }

        public void initPrecedingMonkeys(Map<String, Monkey> allMonkeys) {
            this.precedingMonkeys = precedingMonkeyNames.stream().map(n -> allMonkeys.get(n)).collect(Collectors.toList());
        }
    }

}
