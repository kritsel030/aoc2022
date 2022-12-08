package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class AbstractSolverSingleLineInput extends AbstractSolver {


    public AbstractSolverSingleLineInput(boolean isTest) {
        super(isTest);
    }

    public final void runWithFile(File file) {
        String inputLine = readLine(file);
        doPart1(inputLine);
        doPart2(inputLine);
    }

    private String readLine(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void doPart1(String inputLine);

    public abstract void doPart2(String inputLine);
}
