package day11;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P11_Monkeys {

    static boolean TEST = true;

    public static void main (String[] args) {
        String path = "src/main/resources/day11/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day11/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {

        Map<Integer, Monkey> monkeys = initMonkeys(inputLines, 1);

        int rounds = 20;
        for (int i = 0; i < rounds; i++) {
            System.out.println("round " + (i + 1));
            for (int m = 0; m < monkeys.size(); m++) {
                System.out.println("  monkey " + m);
                monkeys.get(m).takeTurn(monkeys);
            }
        }

        List<Long> inspectedItemsCounts = monkeys.values().stream().map(m -> m.inspectedItemsCount).collect(Collectors.toList());
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("rounds", rounds);
        context.put("monkey 0", monkeys.get(0).worryLevels);
        context.put("monkey 1", monkeys.get(1).worryLevels);
        context.put("monkey 2", monkeys.get(2).worryLevels);
        context.put("monkey 3", monkeys.get(3).worryLevels);
        context.put("counts", inspectedItemsCounts);
        Collections.sort(inspectedItemsCounts);
        Collections.reverse(inspectedItemsCounts);

        long answer = inspectedItemsCounts.get(0) * inspectedItemsCounts.get(1);
        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);

    }

    // my answer for test:       2637590098
    // expected answer for test: 2713310158
    static void doPart2(List<String> inputLines) {

        Map<Integer, Monkey> monkeys = initMonkeys(inputLines, 2);

        int rounds = 10000;
        for (int i = 0; i < rounds; i++) {
            System.out.println("round " + (i + 1));
            for (int m = 0; m < monkeys.size(); m++) {
                System.out.println("  monkey " + m);
                monkeys.get(m).takeTurn(monkeys);
            }
        }

        List<Long> inspectedItemsCounts = monkeys.values().stream().map(m -> m.inspectedItemsCount).collect(Collectors.toList());
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("rounds", rounds);
//        context.put("monkey 0", monkeys.get(0).worryLevels);
//        context.put("monkey 1", monkeys.get(1).worryLevels);
//        context.put("monkey 2", monkeys.get(2).worryLevels);
//        context.put("monkey 3", monkeys.get(3).worryLevels);
        context.put("counts", inspectedItemsCounts);
        Collections.sort(inspectedItemsCounts);
        Collections.reverse(inspectedItemsCounts);

        long answer = inspectedItemsCounts.get(0) * inspectedItemsCounts.get(1);
        AnswerPrinter.printAnswerDetails(1, 2, answer, context, TEST);

    }
    static Map<Integer, Monkey> initMonkeys(List<String> inputLines, int strategy) {
        Map<Integer, Monkey> monkeys = new LinkedHashMap<>();

        List<String> monkeyLines = new ArrayList<>();
        for (String inputLine : inputLines) {
            if (inputLine.trim().length() == 0) {
                Monkey monkey = new Monkey(monkeyLines, strategy);
                monkeys.put(monkey.id, monkey);
                monkeyLines = new ArrayList<>();
            } else {
                monkeyLines.add(inputLine);
            }
        }

        // last monkey lines
        Monkey monkey = new Monkey(monkeyLines, strategy);
        monkeys.put(monkey.id, monkey);

        return monkeys;
    }

    public static class Monkey {
        int id;
        List<Long> worryLevels = new ArrayList<>();

        String inspectOperator;
        int inspectOperand;

        int testDivider;

        int nextMonkeyWhenTrue;
        int nextMonkeyWhenFalse;

        long inspectedItemsCount = 0;

        int strategy;

        /*
        Monkey 0:
          Starting items: 79, 98
          Operation: new = old * 19
          Test: divisible by 23
            If true: throw to monkey 2
            If false: throw to monkey 3
         */
        public Monkey(List<String> inputLines, int strategy) {
            this.strategy = strategy;

            // line 0 --> id
            this.id = Integer.parseInt(inputLines.get(0).trim().split(" ")[1].replace(":", ""));

            // line 1 --> initial worry levels
            String[] levelsAsText = inputLines.get(1).trim().replace("Starting items: ", "").split(",");
            for (String level : levelsAsText) {
                worryLevels.add(Long.parseLong(level.trim()));
            }

            // line 2 --> inspect operation and operand
            this.inspectOperator = inputLines.get(2).trim().replace("Operation: new = old ", "").split(" ")[0].trim();
            String operand = inputLines.get(2).trim().replace("Operation: new = old ", "").split(" ")[1].trim();
            if (operand.equals("old")) {
                this.inspectOperand = -1;
            } else {
                this.inspectOperand = Integer.parseInt(operand);
            }

            // line 3 -> test divider
            this.testDivider = Integer.parseInt(inputLines.get(3).trim().replace("Test: divisible by ", ""));

            // line 4 --> nextMonkeyWhenTrue
            this.nextMonkeyWhenTrue = Integer.parseInt(inputLines.get(4).trim().replace("If true: throw to monkey ", "").trim());
            // line 5 --> nextMonkeyWhenFalse
            this.nextMonkeyWhenFalse = Integer.parseInt(inputLines.get(5).trim().replace("If false: throw to monkey ", "").trim());
        }

        public void takeTurn(Map<Integer, Monkey> monkeys) {
            List<Long> processLevels = worryLevels;
            worryLevels = new ArrayList<>();
            for (long worryLevel :processLevels) {
                this.inspectedItemsCount++;
                // inspect
                long newLevel = 0;
                if (inspectOperator.equals("*")) {
                    if (inspectOperand == -1) {
                        newLevel = worryLevel * worryLevel;
                    } else {
                        newLevel = worryLevel * inspectOperand;
                    }
                } else {
                    if (inspectOperand == -1) {
                        newLevel = worryLevel + worryLevel;
                    } else {
                        newLevel = worryLevel + inspectOperand;
                    }
                }

                // after inspection
                if (strategy == 1) {
                    // divided by three and rounded down to the nearest integer
                    newLevel = newLevel / 3;
                }

                // execute test
                if (newLevel % testDivider == 0) {
                    monkeys.get(nextMonkeyWhenTrue).receive(newLevel);
                } else {
                    monkeys.get(nextMonkeyWhenFalse).receive(newLevel);
                }

            }
        }

        public void receive(long level) {
            worryLevels.add(level);
        }

    }


}
