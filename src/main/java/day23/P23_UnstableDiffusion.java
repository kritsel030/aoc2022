package day23;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P23_UnstableDiffusion {

    static boolean TEST = false;

    static String initialDirectionOrder = "NSWE";

    static String directionOrder;

    // key: an elf
    // value: list of direction (NW, N, NE, etc.) where there is another elf
//    static Map<int[], List<String>> elvesAroundMap;

    public static void main (String[] args) {
        String path = "src/main/resources/day23/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day23/real-input.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 4109

    static void doPart1(List<String> inputLines) {
        // initialize
        directionOrder = initialDirectionOrder;
//        elvesAroundMap = new HashMap<>();
        List<int[]> elves = parseInput(inputLines);

//        int movedElves = 1;
//        while(movedElves > 0) {
//            movedElves = executeRound(elves);
//        }
        int round = 1;
        while(round <= 10) {
            System.out.println("start round " + round);
//            print(elves);
            int movedElves = executeRound(elves);
            round++;
            System.out.println(movedElves + " elves have moved");
            System.out.println("--------------------------------------------------------");
        }

//        System.out.println("after 10 rounds");
//        print(elves);

        // find edges of rectangle
        int smallestX = Collections.min(elves.stream().map(e -> e[0]).collect(Collectors.toList()));
        int largestX = Collections.max(elves.stream().map(e -> e[0]).collect(Collectors.toList()));
        int smallestY = Collections.min(elves.stream().map(e -> e[1]).collect(Collectors.toList()));
        int largestY = Collections.max(elves.stream().map(e -> e[1]).collect(Collectors.toList()));

        int answer = ((largestY - smallestY + 1) * (largestX - smallestX + 1)) - elves.size();
        AnswerPrinter.printAnswerDetails(23, 1, answer, TEST);
    }

    // answer: 1055
    static void doPart2(List<String> inputLines) {
        // initialize
        directionOrder = initialDirectionOrder;
//        elvesAroundMap = new HashMap<>();
        List<int[]> elves = parseInput(inputLines);

        int movedElves = 1;
        int round = 1;
        while(movedElves > 0) {
            movedElves = executeRound(elves);
//            System.out.println(movedElves + " elves moved in round " + round);
            if (movedElves > 0) {
                round++;
            }
        }

//        System.out.println("after 10 rounds");
//        print(elves);

        AnswerPrinter.printAnswerDetails(23, 2, round, TEST);
    }


    static List<int[]> parseInput(List<String> inputLines) {
        List<int[]> elves = new ArrayList<>();
        for (int y = 0; y < inputLines.size(); y++) {
            String inputLine = inputLines.get(y);
            for (int x = 0; x < inputLine.length(); x++) {
                if (inputLine.charAt(x) == '#') {
                    // 3rd and 4th position reserved to store the proposed location during a round
                    // 5th position to indicate if an elf will move at all
                    int[] elf = {x, y, 0, 0, 0};
                    elves.add(elf);
                }
            }
        }
        return elves;
    }

    static int executeRound(List<int[]> elves) {
        // phase 1: propose move
        List<int[]> proposedLocations = proposeMoves(elves);

        // phase 2: move
        int movedElves = move(elves, proposedLocations);

        rotateDirectionOrder();
        return movedElves;
    }

    static List<int[]> proposeMoves(List<int[]> elves) {
        List<int[]> proposedMoves = new ArrayList<>();
        for (int[] elf : elves) {
            List<String> availableDirections = determineFreeDirections(elf, elves);
//            System.out.print("[propose] elf at " + elf[0] + "," + elf[1]);
            if (availableDirections.size() == 8) {
//                System.out.print(" does not need to move");
            } else if (availableDirections.size() > 0) {
                int[] proposedLocation = new int[2];
                char moveDirection = 0;
                for (char direction : directionOrder.toCharArray()) {
                    switch (direction) {
                        case 'N':
                            if (availableDirections.contains("NW") && availableDirections.contains("N") && availableDirections.contains("NE")) {
                                // this elf proposes to move north
                                proposedLocation[0] = elf[0];
                                proposedLocation[1] = elf[1]-1;
                                moveDirection = 'N';
                            }
                            break;
                        case 'S':
                            if (availableDirections.contains("SW") && availableDirections.contains("S") && availableDirections.contains("SE")) {
                                // this elf proposes to move south
                                proposedLocation[0] = elf[0];
                                proposedLocation[1] = elf[1]+1;
                                moveDirection = 'S';
                            }
                            break;
                        case 'E':
                            if (availableDirections.contains("NE") && availableDirections.contains("E") && availableDirections.contains("SE")) {
                                // this elf proposes to move east
                                proposedLocation[0] = elf[0]+1;
                                proposedLocation[1] = elf[1];
                                moveDirection = 'E';
                            }
                            break;
                        case 'W':
                            if (availableDirections.contains("NW") && availableDirections.contains("W") && availableDirections.contains("SW")) {
                                // this elf proposes to move west
                                proposedLocation[0] = elf[0]-1;
                                proposedLocation[1] = elf[1];
                                moveDirection = 'W';
                            }
                            break;
                    }
                    if (moveDirection != 0) {
                         break;
                    }
                }
                if (moveDirection != 0) {
//                    System.out.println( " proposes to move " + moveDirection + " to " + proposedLocation[0] + ", " + proposedLocation[1]);
                    proposedMoves.add(proposedLocation);
                    elf[2] = proposedLocation[0];
                    elf[3] = proposedLocation[1];
                    elf[4] = 1;
                } else {
//                    System.out.println( " does not propose a move");
                }
            }
        }
        return proposedMoves;
    }

    // return false when there are no elves around
    static List<String> determineFreeDirections(int[] elf, List<int[]> elves) {
//        System.out.print("[scan] elf at " + elf[0] + "," + elf[1]);
        String[] allDirections = {"NW", "N", "NE", "E", "SE", "S" , "SW", "W"};
        List<String> availableDirections = new ArrayList<>(Arrays.asList(allDirections));
        for (int[] otherElf : elves) {
            if ( ! (otherElf[0] == elf[0] && otherElf[1] == elf[1]) ) {
                // north-west = 0,0
                // check north
                if (otherElf[1] == elf[1] - 1) {
                    if (otherElf[0] == elf[0] - 1) {
                        availableDirections.remove("NW");
                    } else if (otherElf[0] == elf[0]) {
                        availableDirections.remove("N");
                    } else if (otherElf[0] == elf[0] + 1) {
                        availableDirections.remove("NE");
                    }
                }

                // check south
                 else if (otherElf[1] == elf[1] + 1) {
                    if (otherElf[0] == elf[0] - 1) {
                        availableDirections.remove("SW");
                    } else if (otherElf[0] == elf[0]) {
                        availableDirections.remove("S");
                    } else if (otherElf[0] == elf[0] + 1) {
                        availableDirections.remove("SE");
                    }
                }

                // check east
                else if (otherElf[0] == elf[0] + 1) {
                    if (otherElf[1] == elf[1]) {
                        availableDirections.remove("E");
                    }
                }

                // check west
                else if (otherElf[0] == elf[0] - 1) {
                    if (otherElf[1] == elf[1]) {
                        availableDirections.remove("W");
                    }
                }
            }
        }
//        elvesAroundMap.put(elf, availableDirections);
//        System.out.println(" can move to " + availableDirections);
        return availableDirections;
    }

//    static double distance(int[] elf1, int[] elf2) {
//        return Math.sqrt(Math.pow(elf2[0]-elf1[0], 2) + Math.pow(elf2[1] - elf1[1], 2));
//    }

    // returns the number of moved elves
    static int move(List<int[]> elves, List<int[]> proposedLocations) {
        int movedElves = 0;
        for (int[] elf : elves) {
//            System.out.print("[move] elf at " + elf[0] + "," + elf[1]);
            // only move when its proposed location is not proposed by others as well
            long newLocationProposedBy = proposedLocations.stream().filter(pl -> pl[0] == elf[2] && pl[1] == elf[3]).count();
            if (newLocationProposedBy == 1) {
                if (elf[4] == 1) {
//                    System.out.println(" moves to " + elf[2] + "," + elf[3]);
                    elf[0] = elf[2];
                    elf[1] = elf[3];
                    movedElves++;
                } else {
//                    System.out.println(" does not move because there are no close-by others");
                }
            } else {
//                System.out.println(" does not move because others want to move to the same location");
            }
            elf[2] = 0;
            elf[3] = 0;
            elf[4] = 0;
        }
        return movedElves;
    }

    // move the first character to the back
    static void rotateDirectionOrder() {
        directionOrder = directionOrder.substring(1) + directionOrder.charAt(0);
    }

    static void print(List<int[]> elves) {
        int smallestX = Collections.min(elves.stream().map(e -> e[0]).collect(Collectors.toList()));
        int largestX = Collections.max(elves.stream().map(e -> e[0]).collect(Collectors.toList()));
        int smallestY = Collections.min(elves.stream().map(e -> e[1]).collect(Collectors.toList()));
        int largestY = Collections.max(elves.stream().map(e -> e[1]).collect(Collectors.toList()));

        int xOffset = Math.max(0, 0-smallestX);
        int yOffset = Math.max(0, 0-smallestY);

        int xRange = largestX - smallestX + 1;
        int yRange = largestY - smallestY + 1;

        char[][] grid = new char[xRange][yRange];
        for (int x = 0; x < xRange; x++) {
            for (int y = 0; y < yRange; y++) {
                grid[x][y] = '.';
            }
        }

        for (int[] elf : elves) {
            grid[elf[0] + xOffset][elf[1] + yOffset] = '#';
        }

        for (int y = 0; y < yRange; y++) {
            StringBuffer row = new StringBuffer();
            for (int x = 0; x < xRange; x++) {
                row.append(grid[x][y]);
            }
            System.out.println(row.toString());
        }
    }

}
