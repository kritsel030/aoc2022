package day07;

import util.AnswerPrinter;
import util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class P07_NoSpaceLeftOnDevice_original {

    static boolean TEST = false;

    public static void main (String[] args) {
        String path = "src/main/resources/day07/test-input.txt";
        if (!TEST) {
            path = "src/main/resources/day07/real-input.txt.txt";
        }
        List<String> inputLines = FileUtil.readAllLines(path);
        doPart1(inputLines);
        doPart2(inputLines);
    }

    static void doPart1(List<String> inputLines) {

        FileOrDir root = setUpFileSystem(inputLines);

        // traverse directory structure
        long answer = root.findDirectoriesUnder(100000).stream().map(d -> d.getTotalSize()).collect(Collectors.summingLong(Long::longValue));
        AnswerPrinter.printAnswerDetails(1, 1, answer, TEST);

    }

    private static FileOrDir setUpFileSystem(List<String> inputLines) {
        FileOrDir root = new FileOrDir("/");
        FileOrDir currentDir = root;
        for (String inputLine : inputLines) {
            if (inputLine.equals("$ cd /")) {
                currentDir = root;
            } else if (inputLine.contains("$ cd ..")) {
                currentDir = currentDir.parentDirectory;
            } else if (inputLine.contains("$ cd")) {
                String dirName = inputLine.substring(5).trim();
                if (! currentDir.contents.containsKey(dirName)) {
                    System.err.println("current directory " + currentDir.name + " does not contain subdir " + dirName);
                }
                currentDir = currentDir.contents.get(dirName);
            } else if (inputLine.equals("$ ls")) {
                // nothing to do
            } else {
                // inputLine is content of the current directory
                if (inputLine.startsWith("dir")) {
                    // dir a
                    currentDir.add(new FileOrDir(inputLine.substring(4).trim()));
                } else {
                    // 14848514 b.txt
                    String[] parts = inputLine.split(" ");
                    currentDir.add(new FileOrDir(parts[1], Long.parseLong(parts[0])));
                }
            }
        }
        return root;
    }

    static void doPart2(List<String> inputLines) {
        FileOrDir root = setUpFileSystem(inputLines);

        // calculate free-up space
        long totalspace = 70000000;
        long neededFreespace = 30000000;
        long currentUnusedSpace = totalspace - root.getTotalSize();
        long freeUpExtraSpace = neededFreespace - currentUnusedSpace;

        Map<String, Object> context = new HashMap<>();
        context.put ("free up space", freeUpExtraSpace);

        // traverse directory structure
        long answer = root.findDirectoriesOver(freeUpExtraSpace).stream().map(d -> d.getTotalSize()).mapToLong(v -> v).min().orElseThrow(NoSuchElementException::new);
        AnswerPrinter.printAnswerDetails(1, 2, answer, context, TEST);
    }


}
