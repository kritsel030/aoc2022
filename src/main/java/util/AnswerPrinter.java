package util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class AnswerPrinter {

    // print answer with context details
    static public void printAnswerDetails(int day, int part, Object answer, Map<String, Object> context, boolean test) {
        String appendix = test ? "(!!!TEST!!!)" : "";
        System.out.printf("answer day %s, part #%d: %d (%s) %s",
                String.format("%02d", day),
                part,
                answer,
                contextToString(context),
                appendix);
    }

    // print answer without context details
    static public void printAnswerDetails(int day, int part, Object answer,boolean test) {
        String appendix = test ? "(!!!TEST!!!)" : "";
        System.out.printf("answer day %s, part #%d: %d %s",
                String.format("%02d", day),
                part,
                answer,
                appendix);
    }

    static private String contextToString(Map<String, Object> context) {
        // convert context into comma separated list of key:value pairs
        StringJoiner stringJoiner = new StringJoiner(", ");
        context.entrySet().stream().forEach(e -> stringJoiner.add(e.getKey() + ": " + e.getValue().toString()));
        return stringJoiner.toString();
    }

    // test this thing
    public static void main (String[] args) {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "kristel");
        context.put("female", true);

        printAnswerDetails(25, 1, 1234567, context, true);
    }
}
