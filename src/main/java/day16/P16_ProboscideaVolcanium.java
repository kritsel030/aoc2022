package day16;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class P16_ProboscideaVolcanium {

    static boolean TEST = true;

    // key: AA-BB
    // value: distance between AA and BB
    static Map<String, Integer> shortestDistanceCache = new HashMap<>();

    public static void main (String[] args) {
        String path = "src/main/resources/day16/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day16/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
//        doPart2(inputLines);
    }

    // answer: 1947
    static void doPart1(List<String> inputLines) {
        // init valves
        Map<String, Valve> valveMap = inputLines.stream().map(l -> new Valve(l)).collect(Collectors.toMap(Valve::getId, Function.identity() ));
        valveMap.values().stream().forEach(v -> v.linkValves(valveMap));

        // and lets brute force this by walking all the possible paths to the valves with a positive flowRate
        Set<Valve> closedValvesOfInterest = valveMap.values().stream().filter(v -> v.flowRate > 0).collect(Collectors.toSet());
        Valve startValve = valveMap.get("AA");

        Path newPath = new Path(closedValvesOfInterest);
        newPath.addOpenValve(startValve, 0);
        Path bestPath = new Path(closedValvesOfInterest);
        fanOut(startValve, closedValvesOfInterest, newPath, bestPath, valveMap.values(), 30);

        System.out.println("best path");
        bestPath.openedValves.entrySet().stream().forEach(e -> System.out.println(" open " + e.getKey().id + " at minute " + e.getValue()));

        AnswerPrinter.printAnswerDetails(1, 1, bestPath.totalPressure(30), TEST);
    }

    static void doPart2(List<String> inputLines) {
        // init valves
        Map<String, Valve> valveMap = inputLines.stream().map(l -> new Valve(l)).collect(Collectors.toMap(Valve::getId, Function.identity() ));
        valveMap.values().stream().forEach(v -> v.linkValves(valveMap));

        // and lets brute force this by walking all the possible paths to the valves with a positive flowRate
        Set<Valve> closedValvesOfInterest = valveMap.values().stream().filter(v -> v.flowRate > 0).collect(Collectors.toSet());
        Valve startValve = valveMap.get("AA");

        Path newPath1 = new Path(closedValvesOfInterest);
        newPath1.addOpenValve(startValve, 0);
        Path newPath2 = new Path(closedValvesOfInterest);
        newPath2.addOpenValve(startValve, 0);
        Path bestPath = new Path(closedValvesOfInterest);
        //fanOut2(startValve, newPath1, startValve, newPath2, closedValvesOfInterest, bestPath, valveMap.values(), 26);

        System.out.println("best path");
        bestPath.openedValves.entrySet().stream().forEach(e -> System.out.println(" open " + e.getKey().id + " at minute " + e.getValue()));

        long totalPressure = bestPath.totalPressure(26) + bestPath.totalPressure(26);
        AnswerPrinter.printAnswerDetails(1, 2, totalPressure, TEST);
    }

    public static void fanOut(Valve currentLocation, Set<Valve> closedValvesOfInterest, Path currentPath, Path bestPath, Collection<Valve> allValves, int totalMinutes) {
        for (Valve nextValve : closedValvesOfInterest) {
            // find out when we can open this next valve
            allValves.stream().forEach(v -> v.reset());
            int distance = shortestDistance(currentLocation, nextValve);
            int openAtMinute = currentPath.openedValves.get(currentLocation) + distance + 1;

            Path newPath = currentPath.clone();
            if (openAtMinute <= totalMinutes) {
                newPath.addOpenValve(nextValve, openAtMinute);
            }

            if (newPath.isComplete() || openAtMinute >= totalMinutes) {
                if (newPath.totalPressure(totalMinutes) > bestPath.totalPressure(totalMinutes)) {
                    System.out.println("found new best path");
                    bestPath.replaceOpenendValves(newPath.openedValves);
                }
            } else {
                Set<Valve> remainingValves = new HashSet<>(closedValvesOfInterest);
                remainingValves.remove(nextValve);
                fanOut(nextValve, remainingValves, newPath, bestPath, allValves, totalMinutes);
            }
        }
    }

//    public static void fanOut2(Valve currentLocation1, Path currentPath1, Valve currentLocation2, Path currentPath2, Set<Valve> closedValvesOfInterest, Path bestPath, Collection<Valve> allValves, int totalMinutes) {
//        for (Valve nextValve : closedValvesOfInterest) {
//            // find out when we can open this next valve for person 1
//            allValves.stream().forEach(v -> v.reset());
//            int distance1 = shortestDistance(currentLocation1, nextValve);
//            int openAtMinute1 = currentPath1.openedValves.get(currentLocation1) + distance1 + 1;
//
//            // find out when we can open this next valve for person 2
//            allValves.stream().forEach(v -> v.reset());
//            int distance2 = shortestDistance(currentLocation2, nextValve);
//            int openAtMinute2 = currentPath1.openedValves.get(currentLocation2) + distance2 + 1;
//
//            int openAtMinute = Math.min(openAtMinute1, openAtMinute2);
//
//            Path newPath1 = currentPath1.clone();
//            Path newPath2 = currentPath2.clone();
//            Valve nextValve1 = nextValve;
//            Valve nextValve2 = currentLocation2;
//            if (openAtMinute1 > openAtMinute2) {
//                // continue with person 2 instead
//                nextValve1 = currentLocation1;
//                nextValve2 = nextValve;
//            }
//
//            if (openAtMinute <= totalMinutes) {
//                if (openAtMinute1 < openAtMinute2) {
//                    newPath1.addOpenValve(nextValve1, openAtMinute1);
//                } else {
//                    newPath2.addOpenValve(nextValve2, openAtMinute2);
//                }
//            }
//
//
//            if (newPath.isComplete() || openAtMinute1 >= totalMinutes) {
//                if (newPath.totalPressure(totalMinutes) > bestPath.totalPressure(totalMinutes)) {
//                    System.out.println("found new best path");
//                    bestPath.replaceOpenendValves(newPath.openedValves);
//                }
//            } else {
//                Set<Valve> remainingValves = new HashSet<>(closedValvesOfInterest);
//                remainingValves.remove(nextValve);
//                fanOut(nextValve, remainingValves, newPath, bestPath, allValves, totalMinutes);
//            }
//        }
//    }


    public static int shortestDistance(Valve here, Valve there) {
        if (shortestDistanceCache.containsKey(here.id + "-" + there.id)) {
            return shortestDistanceCache.get(here.id + "-" + there.id);
        } else {
            fanOutShortestDistance(here, there, new ArrayList<>());
            shortestDistanceCache.put(here.id + "-" + there.id, there.distanceFromStart);
            shortestDistanceCache.put(there.id + "-" + here.id, there.distanceFromStart);
            return there.distanceFromStart;
        }
    }

    static void fanOutShortestDistance(Valve currentValve, Valve targetValve, List<Valve> currentPath) {
        for (Valve nextValve : currentValve.connectedValves) {
            if (! currentPath.contains(nextValve)) {
                List<Valve> path = new ArrayList<>(currentPath);
                path.add(nextValve);
                if (nextValve.distanceFromStart == 0 || path.size() < nextValve.distanceFromStart) {
                    nextValve.distanceFromStart = path.size();
                }
                if (!nextValve.equals(targetValve)) {
                    fanOutShortestDistance(nextValve, targetValve, path);
                }
            }
        }
    }

    public static class Valve {
        String id;

        long flowRate;

        Set<Valve> connectedValves = new HashSet<>();

        List<String> connectedValveIds = new ArrayList<>();

        int distanceFromStart = 0;

        public Valve(String id, long flowRate, List<String> connectedValveIds) {
            this.id = id;
            this.flowRate = flowRate;
            this.connectedValveIds = connectedValveIds;
        }

        // Valve BB has flow rate=13; tunnels lead to valves CC, AA
        public Valve(String inputLine) {
            this.id = inputLine.substring(6, 8);
            this.flowRate = Long.valueOf(inputLine.substring(23, inputLine.indexOf(";")));
            this.connectedValveIds = Arrays.asList(inputLine.substring(inputLine.indexOf("valve") + 6).split(", "));
        }

        public String getId() {
            return id;
        }

        public long getFlowRate() {
            return flowRate;
        }

        public Set<Valve> getConnectedValves() {
            return connectedValves;
        }

        public List<String> getConnectedValveIds() {
            return connectedValveIds;
        }

        public void linkValves(Map<String, Valve> valveMap) {
            connectedValves = connectedValveIds.stream().map(id -> valveMap.get(id.trim())).collect(Collectors.toSet());
        }

        public void reset() {
            distanceFromStart = 0;
        }

    }

    public static class Path{

        Set<Valve> valvesOfInterest;

        public Path(Set<Valve> allValvesOfInterest) {
            this.valvesOfInterest = new HashSet<>(allValvesOfInterest);
        }

        private Path() {}

        public Path clone() {
            Path newPath = new Path();
            newPath.valvesOfInterest = valvesOfInterest;
            newPath.openedValves = new LinkedHashMap<>(openedValves);
            return newPath;
        }

        // key: a valve
        // value: minute is was opened
        Map<Valve, Integer> openedValves = new LinkedHashMap<>();

        Map<Valve, Integer> addOpenValve(Valve valve, int timestamp) {
            this.openedValves.put(valve, timestamp);
            return openedValves;
        }

        long totalPressure(int timestamp) {
            int totalPressure = 0;
            for (Map.Entry<Valve, Integer> openedValve : openedValves.entrySet()) {
                totalPressure += (timestamp - openedValve.getValue()) * openedValve.getKey().flowRate;
            }
            return totalPressure;
        }

        boolean isComplete() {
            // +1 because openedValves also includes the start valve (with no flow rate)
            return this.openedValves.size()  == valvesOfInterest.size() + 1;
        }

        void replaceOpenendValves(Map<Valve, Integer> newOpenedValves) {
            this.openedValves = newOpenedValves;
        }
    }

}
