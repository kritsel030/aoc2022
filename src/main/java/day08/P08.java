package day08;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;

public class P08 {

    static boolean TEST = false;

    public static void main(String[] args) {
        String path = "src/main/resources/day08/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day08/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {
        List<Map<Integer, List<Integer>>> forest = initForest(inputLines);
        long answer = countVisibleTrees(forest);

        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);

    }

    static void doPart2(List<String> inputLines) {
        List<Map<Integer, List<Integer>>> forest = initForest(inputLines);

        Map<String, Long> scenicScores = calculateScenicScores(forest);
        long answer = Collections.max(scenicScores.values());
        AnswerPrinter.printAnswerDetails(1, 2, answer, TEST);
    }

    // returns list of two elements
    // first element represents rows: a map with rownumber as key and list of heights as value
    // second element represents columns: a map with columnnumber as key and a list of heights as value
    static List<Map<Integer, List<Integer>>> initForest(List<String> inputLines) {
        int noOfColumns = inputLines.get(0).length();

        // init rows
        Map<Integer, List<Integer>> rows = new HashMap<>();
        for (int r = 0; r < inputLines.size(); r++) {
            List<Integer> heights = new ArrayList<>();
            rows.put(r, heights);
            for (int t = 0; t < inputLines.get(r).length(); t++) {
                heights.add(Integer.parseInt(""+inputLines.get(r).charAt(t)));
            }
        }

        // translate rows into columns
        // first init columns
        Map<Integer, List<Integer>> columns = new HashMap<>();
        for (int c = 0; c < noOfColumns; c++) {
            columns.put(c, new ArrayList<>());
        }
        for (Map.Entry<Integer, List<Integer>> row : rows.entrySet()) {
            for (int c = 0; c < row.getValue().size(); c++) {
                // c = column number
                columns.get(c).add(row.getValue().get(c));
            }
        }

        List<Map<Integer, List<Integer>>> forest = new ArrayList<>();
        forest.add(rows);
        forest.add(columns);
        return forest;
    }

    private static long countVisibleTrees(List<Map<Integer, List<Integer>>> forest) {
        Set<String> visibleTrees = new TreeSet<>();
        // rows
        Map<Integer, List<Integer>> rows = forest.get(0);
        for (Map.Entry row : rows.entrySet()) {
            registerVisibleTreesFromBothSides(row, true, visibleTrees);
        }

        // columns
        Map<Integer, List<Integer>> columns = forest.get(1);
        for (Map.Entry column : columns.entrySet()) {
            registerVisibleTreesFromBothSides(column, false, visibleTrees);
        }

        return visibleTrees.size();
    }

    private static void registerVisibleTreesFromBothSides(Map.Entry<Integer, List<Integer>> rowOrColumn, boolean isRow, Set<String> visibleTrees) {
        List<Integer> heights = rowOrColumn.getValue();
        registerVisibleTreesFromOneSide("" + rowOrColumn.getKey(), heights, isRow, true, visibleTrees);
        Collections.reverse(heights);
        registerVisibleTreesFromOneSide("" + rowOrColumn.getKey(), heights, isRow, false, visibleTrees);
    }

    private static void registerVisibleTreesFromOneSide(String rowOrColumnId, List<Integer> heights, boolean isRow, boolean fromStart, Set<String> visibleTrees) {
        //System.out.println((isRow ? "row " : "column ") + rowOrColumnId + " from the " + (fromStart ? "start" : "end"));
        long visibleHeight = -1;
        for (int t = 0; t < heights.size(); t++) {
            if (heights.get(t) > visibleHeight) {
                // add row-column coordinate to visibleTrees
                String visibleCoordinate = null;
                String otherId = "" + t;
                if (!fromStart) {
                    otherId = "" + (heights.size() - t - 1);
                }
                if (isRow) {
                    visibleCoordinate = rowOrColumnId + "-" + otherId;
                } else {
                    visibleCoordinate = otherId + "-" + rowOrColumnId;
                }
                //System.out.println("    visible tree at " + visibleCoordinate);
                visibleTrees.add(visibleCoordinate);
                visibleHeight = heights.get(t);
            }
        }
    }

    // result:
    //  key: tree coordinate row-column
    //  value: scenic score
    static Map<String, Long> calculateScenicScores(List<Map<Integer, List<Integer>>> forest) {
        Map<String, Long> scores = new HashMap<>();
        for (int row = 0; row < forest.get(0).size(); row++) {
            for (int column = 0; column < forest.get(1).size(); column++) {
                long score = calculateScenicScore(row, column, forest);
                scores.put("" + row + "-" + column, score);
            }
        }
        return scores;
    }

    static long calculateScenicScore(int rowNo, int columnNo, List<Map<Integer, List<Integer>>> forest) {
        int totalRows = forest.get(0).size();
        int totalColumns = forest.get(1).size();
        List<Integer> row = forest.get(0) // get all rows
                .get(rowNo); // get the specific row
        List<Integer> column = forest.get(1) // get all columns
                .get(columnNo); // get the specific column
        int height = row.get(columnNo);

        // look right, starting at the first tree next to the tree we're considering now
        long rightScore = 0;
        for (int c = columnNo + 1; c < totalColumns; c++) {
            rightScore += 1;
            if (row.get(c) >= height) {
                break;
            }
        }

        // look left, starting at the first tree next to the tree we're considering now
        long leftScore = 0;
        for (int c = columnNo - 1; c >= 0; c--) {
            leftScore += 1;
            if (row.get(c) >= height) {
                break;
            }
        }

        // look down, starting at the first tree next to the tree we're considering now
        long downScore = 0;
        for (int r = rowNo + 1; r < totalRows; r++) {
            downScore += 1;
            if (column.get(r) >= height) {
                break;
            }
        }

        // look up, starting at the first tree next to the tree we're considering now
        long upScore = 0;
        for (int r = rowNo - 1; r >= 0; r--) {
            upScore += 1;
            if (column.get(r) >= height) {
                break;
            }
        }

        long scenicScore =  rightScore * leftScore * downScore * upScore;
        //System.out.println("row " + rowNo + ", column " + columnNo + ": score = " + scenicScore + " (right=" + rightScore + ", left=" + leftScore + ", up=" + upScore + ", down="+ upScore + ")");
        return scenicScore;
    }
}
