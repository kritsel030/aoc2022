package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public abstract class AbstractSolverMultiLineInput extends AbstractSolver {

    public AbstractSolverMultiLineInput(boolean isTest) {
        super(isTest);
    }

    public final void runWithFile(File file) {
        List<String> inputLines = readLines(file);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    private List<String> readLines(File file) {
        Charset charset = StandardCharsets.UTF_8;
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath(), charset);
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
        }
        return lines;
    }



    public abstract void doPart1(List<String> inputLines);

    public abstract void doPart2(List<String> inputLines);
}
