package day19;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P19_NotEnoughMinerals {

    static boolean TEST = true;

    static Map<String, Integer> maxGeodeCache = new HashMap<>();

    public static void main (String[] args) throws CloneNotSupportedException {
//        Date start = new Date();
//        System.out.println("start at " + start.getTime());
        String path = "src/main/resources/day19/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day19/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart2(inputLines);

//        Date end = new Date();
//        System.out.println("done at " + end.getTime() + ", duration is " + (end.getTime() - start.getTime()) );
//        doPart2(inputLines);
    }

    // answer: 1009
    static void doPart1(List<String> inputLines) {
        long qualityLevelTotal = inputLines.stream()
                .map(l -> {
                    BluePrint blueprint = new BluePrint(l);
                    maxGeodeCache = new HashMap<>();
                    Date start = new Date();
                    System.out.print("Start blueprint " + blueprint.id + " ... ");
                    long geodeCount = maximizeGeodes(new Resources(), blueprint, 0, 24);
                    long durationInSeconds = (new Date().getTime() - start.getTime()) / 1000;
                    long qualityLevel = blueprint.id * geodeCount;
                    System.out.println(" produces " + geodeCount + " geodes (quality level: " + qualityLevel + ", duration: " + durationInSeconds + " seconds)");
                    return qualityLevel;
                }).collect(Collectors.summingLong(Long::longValue));
        AnswerPrinter.printAnswerDetails(1, 1, qualityLevelTotal, TEST);
    }

    static void doPart2(List<String> inputLines) {
        inputLines = inputLines.subList(0, Math.min(3, inputLines.size()));
        List<Long> geodeCounts = inputLines.stream()
                .map(l -> {

                    BluePrint blueprint = new BluePrint(l);
                    maxGeodeCache = new HashMap<>();
                    Date start = new Date();
                    System.out.print("Start blueprint " + blueprint.id + " ... ");
                    long geodeCount = maximizeGeodes(new Resources(), blueprint, 0, 32);
                    long durationInSeconds = (new Date().getTime() - start.getTime()) / 1000;
                    System.out.println(" produces " + geodeCount + " geodes (duration: " + durationInSeconds + " seconds)");
                    return geodeCount;
                }).collect(Collectors.toList());
        long answer = 1;
        for (Long geodeCount : geodeCounts) {
            answer *= geodeCount;
        }
        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

    static void doTest1(List<String> inputLines) {
        BluePrint bluePrint1 = new BluePrint();
        bluePrint1.oreRobotCosts_ore = 4;
        bluePrint1.clayRobotCosts_ore = 2;
        bluePrint1.obsidianRobotCosts_ore = 3;
        bluePrint1.obsidianRobotCosts_clay = 14;
        bluePrint1.geodeRobotCosts_ore = 2;
        bluePrint1.geodeRobotCosts_obsidian = 7;

        BluePrint bluePrint2 = new BluePrint();
        bluePrint2.oreRobotCosts_ore = 2;
        bluePrint2.clayRobotCosts_ore = 3;
        bluePrint2.obsidianRobotCosts_ore = 3;
        bluePrint2.obsidianRobotCosts_clay = 8;
        bluePrint2.geodeRobotCosts_ore = 3;
        bluePrint2.geodeRobotCosts_obsidian = 12;

//        test1(inputLines);

//        Date start = new Date();
//        int answer = maximizeGeodes(new Resources(), bluePrint1, 0, 24);
//        Map<String, Object> context = new LinkedHashMap<>();
//        context.put("blueprint", "blueprint 1");
//        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);
//        long duration = new Date().getTime() - start.getTime();
//        System.out.println("duration: " + (duration / 1000) + " seconds");

//        Date start = new Date();
//        int answer = maximizeGeodes(new Resources(), bluePrint2, 0, 24);
//        Map<String, Object> context = new LinkedHashMap<>();
//        context.put("blueprint", "blueprint 2");
//        AnswerPrinter.printAnswerDetails(1, 1, answer, context, TEST);
//        long duration = new Date().getTime() - start.getTime();
//        System.out.println("duration: " + (duration / 1000) + " seconds");
    }

    static void test1(List<String> inputLines) {
        BluePrint bluePrint = new BluePrint();
        bluePrint.oreRobotCosts_ore = 4;
        bluePrint.clayRobotCosts_ore = 2;
        bluePrint.obsidianRobotCosts_ore = 3;
        bluePrint.obsidianRobotCosts_clay = 14;
        bluePrint.geodeRobotCosts_ore = 2;
        bluePrint.geodeRobotCosts_obsidian = 7;

        Resources resources0 = new Resources();

        Step step1 = new Step();
        Resources resources1 = resources0.executeStep(step1, bluePrint);

        Step step2 = new Step();
        Resources resources2 = resources1.executeStep(step2, bluePrint);

        Step step3 = new Step();
        step3.buildClayRobots = 1;
        Resources resources3 = resources2.executeStep(step3, bluePrint);

        Step step4 = new Step();
        Resources resources4 = resources3.executeStep(step4, bluePrint);

        Step step5 = new Step();
        step5.buildClayRobots = 1;
        Resources resources5 = resources4.executeStep(step5, bluePrint);

        Step step6 = new Step();
        Resources resources6 = resources5.executeStep(step6, bluePrint);

        System.out.println("blueprint 1, after 6 minutes");
        resources6.print();
    }

    // returns max number of geodes which can be cracked
    public static int maximizeGeodes(Resources resources, BluePrint blueprint, int currentMinute, int totalMinutes) {
        if (currentMinute < totalMinutes-1) {
            // resources instance contains 'minute' attribute
            String cacheKey = "" + resources.hashCode();
            if (maxGeodeCache.containsKey(cacheKey)) {
                return maxGeodeCache.get(cacheKey);
            } else {
                int nextMinute = currentMinute + 1;
                List<Step> possibleNextSteps = resources.possibleNextSteps(blueprint);
                int maxGeodes = Collections.max(possibleNextSteps.stream()
                        .map(step -> resources.executeStep(step, blueprint))
                        .map(a -> maximizeGeodes(a, blueprint, nextMinute, totalMinutes))
                        .collect(Collectors.toList()));
                maxGeodeCache.put(cacheKey, maxGeodes);
                return maxGeodes;
            }
        } else {
//            System.out.println("minute " + nextMinute + " reached");
            return resources.executeStep(new Step(), blueprint).geode;
        }
    }


    public static class Resources {
        int minute;

        int oreRobots;

        int clayRobots;

        int obsidianRobots;

        int geodeRobots;

        int ore;

        int clay;

        int obsidian;

        int geode;
        
        public Resources() {
            this.oreRobots = 1;
        }

        public List<Step> possibleNextSteps(BluePrint blueprint) {
            List<Step> possibleNextSteps = new ArrayList<>();
            // there's always the option to do nothing
            possibleNextSteps.add(new Step());

            if (this.ore >= blueprint.oreRobotCosts_ore) {
                Step step = new Step();
                step.buildOreRobots = 1;
                possibleNextSteps.add(step);
            }
            if (this.ore >= blueprint.clayRobotCosts_ore) {
                Step step = new Step();
                step.buildClayRobots = 1;
                possibleNextSteps.add(step);
            }
            if (this.ore >= blueprint.obsidianRobotCosts_ore && this.clay >= blueprint.obsidianRobotCosts_clay) {
                Step step = new Step();
                step.buildObsidianRobots = 1;
                possibleNextSteps.add(step);
            }
            if (this.ore >= blueprint.geodeRobotCosts_ore && this.obsidian >= blueprint.geodeRobotCosts_obsidian) {
                Step step = new Step();
                step.buildGeodeRobots = 1;
                possibleNextSteps.add(step);
            }

            return possibleNextSteps;
        }
        public Resources executeStep(Step step, BluePrint blueprint) {
            Resources newResources = this.clone();
            newResources.minute++;
            newResources.ore = this.ore + this.oreRobots -
                    step.buildOreRobots * blueprint.oreRobotCosts_ore -
                    step.buildClayRobots * blueprint.clayRobotCosts_ore -
                    step.buildObsidianRobots * blueprint.obsidianRobotCosts_ore -
                    step.buildGeodeRobots * blueprint.geodeRobotCosts_ore;
            newResources.clay = this.clay + this.clayRobots -
                    step.buildObsidianRobots * blueprint.obsidianRobotCosts_clay;
            newResources.obsidian = this.obsidian + this.obsidianRobots -
                    step.buildGeodeRobots * blueprint.geodeRobotCosts_obsidian;
            newResources.geode = this.geode + this.geodeRobots;

            newResources.oreRobots = this.oreRobots + step.buildOreRobots;
            newResources.clayRobots = this.clayRobots + step.buildClayRobots;
            newResources.obsidianRobots = this.obsidianRobots + step.buildObsidianRobots;
            newResources.geodeRobots = this.geodeRobots + step.buildGeodeRobots;
            return newResources;
        }

        @Override
        protected Resources clone()  {
            Resources clone = new Resources();
            clone.minute = this.minute;
            clone.ore = this.ore;
            clone.clay = this.clay;
            clone.obsidian = this.obsidian;
            clone.geode = this.geode;
            clone.oreRobots = this.oreRobots;
            clone.clayRobots = this.clayRobots;
            clone.obsidianRobots = this.obsidianRobots;
            clone.geodeRobots = this.geodeRobots;
            return clone;
        }

        public void print() {
            System.out.println("after " + minute + " minutes you have");
            System.out.println(" " + oreRobots + " ore-collecting robots and " + ore + " ore")    ;
            System.out.println(" " + clayRobots + " clay-collecting robots and " + clay + " clay");
            System.out.println(" " + obsidianRobots + " obsidian-collecting robots and " + obsidian + " obsidian");
            System.out.println(" " + geodeRobots + " geode-cracking robots and " + geode + " geode");
            System.out.println();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Resources resources = (Resources) o;
            return minute == resources.minute && oreRobots == resources.oreRobots && clayRobots == resources.clayRobots && obsidianRobots == resources.obsidianRobots && geodeRobots == resources.geodeRobots && ore == resources.ore && clay == resources.clay && obsidian == resources.obsidian && geode == resources.geode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minute, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geode);
        }
    }

    public static class BluePrint {

        public BluePrint() {

        }

        // Blueprint 1: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 3 ore and 11 obsidian.
        public BluePrint(String inputLine) {
            // id
            // "Blueprint 1: "
            String idPartStart = "Blueprint ";
            String idPartEnd = ": ";
            id = Integer.valueOf(between(inputLine, idPartStart, idPartEnd).trim());
            String remainingInput = after(inputLine, idPartEnd);

            // ore robot, ore costs
            // "Each ore robot costs 2 ore. "
            String oreRobotStart = "Each ore robot costs ";
            String oreCostsEnd = " ore. ";
            this.oreRobotCosts_ore = Integer.valueOf(between(remainingInput, oreRobotStart, oreCostsEnd).trim());
            remainingInput = after(remainingInput, oreCostsEnd);

            // clay robot, ore costs
            // "Each clay robot costs 4 ore. "
            String clayRobotStart = "Each clay robot costs ";
            String clayCostsEnd = " ore. ";
            this.clayRobotCosts_ore = Integer.valueOf(between(remainingInput, clayRobotStart, clayCostsEnd).trim());
            remainingInput = after(remainingInput, clayCostsEnd);

            // "Each obsidian robot costs 4 ore and 17 clay. "
            // obsidian robot, ore costs
            String obsidianRobotStart = "Each obsidian robot costs ";
            String obsidianOreCostsEnd = " ore and ";
            this.obsidianRobotCosts_ore = Integer.valueOf(between(remainingInput, obsidianRobotStart, obsidianOreCostsEnd).trim());
            remainingInput = after(remainingInput, obsidianOreCostsEnd);

            // obsidian robot, clay costs
            String obsidianClayCostsEnd = " clay. ";
            this.obsidianRobotCosts_clay = Integer.valueOf(between(remainingInput, "", obsidianClayCostsEnd).trim());
            remainingInput = after(remainingInput, obsidianClayCostsEnd);

            // "Each geode robot costs 3 ore and 11 obsidian."
            // geode robot, ore costs
            String geodeRobotStart = "Each geode robot costs ";
            String geodeOreCostsEnd = " ore and ";
            this.geodeRobotCosts_ore = Integer.valueOf(between(remainingInput, geodeRobotStart, geodeOreCostsEnd).trim());
            remainingInput = after(remainingInput, geodeOreCostsEnd);

            // geode robot, clay costs
            String geodeObsidianCostsEnd = " obsidian.";
            this.geodeRobotCosts_obsidian = Integer.valueOf(between(remainingInput, "", geodeObsidianCostsEnd).trim());
        }

        private String between(String input, String start, String end) {
            return input.substring(start.length(), input.indexOf(end)).trim();
        }

        private String after(String input, String after) {
            return input.substring(input.indexOf(after) + after.length());
        }

        int id;
        int oreRobotCosts_ore = 0;
        int clayRobotCosts_ore = 0;
        int obsidianRobotCosts_ore = 0;
        int obsidianRobotCosts_clay = 0;
        int geodeRobotCosts_ore = 0;
        int geodeRobotCosts_obsidian = 0;
    }

    public static class Step {
        int buildOreRobots = 0;
        int buildClayRobots = 0;
        int buildObsidianRobots = 0;
        int buildGeodeRobots = 0;
    }
}
