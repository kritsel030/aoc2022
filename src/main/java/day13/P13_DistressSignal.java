package day13;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class P13_DistressSignal {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day13/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day13/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);

        doPart1(inputLines);
        doPart2(inputLines);
    }

    // answer: 6070
    static void doPart1(List<String> inputLines) {
        // parse input
        List<List<String>> packetPairs = new ArrayList<>();
        List<String> pair = new ArrayList<>();
        for (String inputLine : inputLines) {
            if (pair.size() < 2 && inputLine.trim().length() > 0) {
                pair.add(inputLine);
            }
            if (pair.size() == 2) {
                packetPairs.add(new ArrayList<>(pair));
                pair = new ArrayList<>();
            }
        }

        // compare all packet pairs
        List<Integer> compareResults = packetPairs.stream().map(p -> doCompare(p.get(0), p.get(1))).collect(Collectors.toList());

        int indexSum = 0;
        for (int i = 1; i <= compareResults.size(); i++ ) {
            if (compareResults.get(i-1) < 0) {
                indexSum += i;
            }
        }
        AnswerPrinter.printAnswerDetails(1, 1, indexSum, TEST);

    }

    // answer: 20758
    static void doPart2(List<String> inputLines) {
        // filter out empty lines
        List<String> packets = inputLines.stream().filter(l -> l.length() > 0).collect(Collectors.toList());

        // add the two divider packets
        String divider1 = "[[2]]";
        String divider2 = "[[6]]";
        packets.add(divider1);
        packets.add(divider2);

        // sort the packets
        Collections.sort(packets, (o1, o2) -> compare(o1, o2));

        // find the indexes (1-based) of the two divider packets
        int indexDivider1 = 0;
        int indexDivider2 = 0;
        for (int i = 0; i < packets.size(); i++) {
            if (packets.get(i).equals(divider1)) {
                indexDivider1 = i + 1;
            } else if (packets.get(i).equals(divider2)) {
                indexDivider2 = i + 1;
            }
        }
        AnswerPrinter.printAnswerDetails(1, 2, indexDivider1 * indexDivider2, TEST);
    }

    static int doCompare(String left, String right) {
        int result = compare(left, right);
        System.out.println("compare " + left + " to "  + right + ": " + compare(left, right));
        return result;
    }

    static int compare (String left, String right) {
        // split into separate values
        List<String> leftValues = split(left);
        List<String> rightValues = split(right);

        // when both left and right do not compare any lists themselves, we can treat all the elements as Integers
        if ( (left.length() <= 2 || containsCount(left.substring(1, left.length()-1), '[') == 0)  &&
                (right.length() <= 2 || containsCount(right.substring(1, right.length()-1), '[') == 0)) {
            List<Integer> leftList = leftValues.stream().map(v -> Integer.parseInt(v.trim())).collect(Collectors.toList());
            List<Integer> rightList = rightValues.stream().map(v -> Integer.parseInt(v.trim())).collect(Collectors.toList());
            return compare(leftList, rightList);
        } else {
            return compareLists(leftValues, rightValues);
        }
    }

    static int compareLists (List<String> left, List<String> right) {
        for (int i = 0; i < left.size() && i < right.size(); i++) {
            int compareResult = compare(left.get(i), right.get(i));
            if (compareResult != 0) {
                return compareResult;
            }
        }

        // no decision yet based on the list values, so we compare list lengths
        return compare(left.size(), right.size());
    }

    static int compare (List<Integer> left, List<Integer> right) {
        for (int i = 0; i < left.size() && i < right.size(); i++) {
            int compareResult = compare(left.get(i), right.get(i));
            if (compareResult != 0) {
                return compareResult;
            }
        }

        // no decision yet based on the list values, so we compare list lengths
        return compare(left.size(), right.size());
    }

    static int compare (int left, int right) {
        return Integer.compare(left, right);
    }

    static int containsCount(String s, Character c) {
        String replace = c.toString();
        if (c == '[') {
            replace = "\\[";
        }
        return s.length() - s.replaceAll(replace, "").length();
    }

    // "[4, 4, 4]" -> { "4", "4", "4 }
    // "[4, [4, 4]]" -> { "4", "[4, 4]" }
    // "5" -> {"5"]
    static List<String> split(String input) {
        // remove surrounding brackets if there are any
        String s = removeOuterBracketsIfAny(input);
        List<String> elements = new ArrayList<>();
        StringBuffer element = new StringBuffer();
        int openBrackets = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '[') {
                openBrackets++;
                element.append(s.charAt(i));
            } else if (s.charAt(i) == ']') {
                openBrackets--;
                element.append(s.charAt(i));
            } else if (s.charAt(i) == ',' && openBrackets == 0) {
                // treat comma as an element separator
                if (element.length() > 0) {
                    elements.add(element.toString());
                }
                element = new StringBuffer();
            } else {
                element.append(s.charAt(i));
            }
        }
        // add last element, this was not closed by a comma
        if (element.length() > 0) {
            elements.add(element.toString());
        }

        System.out.println("split '" + s + "' -> "  + elements + " (list of " + elements.size() + " elements)");

        return elements;
    }

    // [1],[2] -> [1],[2] (no outer brackets, no change)
    // [1, 2] -> 1, 2 (does have outer brackets, remove them)
    static String removeOuterBracketsIfAny(String s) {
        int openBrackets = 0;
        boolean mightHaveOuterBrackets = false;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (c == '[') {
                openBrackets++;
            } if (c == ']') {
                openBrackets--;
            }
            if (openBrackets > 0 && (i > 0 || s.length() == 2) ) {
                mightHaveOuterBrackets = true;
                break;
            }
        }
        String result = s;
        if (mightHaveOuterBrackets && s.startsWith("[") && s.endsWith("]")) {
            result = s.substring(1, s.length()-1);
        }

        System.out.println("remove brackets '" + s + "' -> '"  + result + "'");
        return result;
    }
}
