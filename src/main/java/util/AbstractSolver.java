package util;

import java.io.File;
import java.util.Map;

public abstract class AbstractSolver {

    private boolean TEST;

    public AbstractSolver(boolean isTest) {
        TEST = isTest;
    }

    public void run() {
        String fileName = "real-input.txt";
        if (TEST) {
            fileName = "test-input.txt";
        }
        System.out.println("[AbstractSolver] classloader: " + getClass().getClassLoader().toString());
        System.out.println("Resource for " + fileName + ": " + getClass().getClassLoader().getResource(fileName));
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        runWithFile(file);
    }

    protected abstract void runWithFile(File file);

    public void printAnswerDetails(int day, int part, Object answer, Map<String, Object> context, boolean test) {
        AnswerPrinter.printAnswerDetails(day, part, answer, context, test);
    }

    public void printAnswerDetails(int day, int part, Object answer, boolean test) {
        AnswerPrinter.printAnswerDetails(day, part, answer, test);
    }
}
