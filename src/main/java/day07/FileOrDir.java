package day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileOrDir {

    boolean directory;

    String name;

    // only applicable when this is a directory
    FileOrDir parentDirectory;

    // only applicable when this is a file
    long filesize;

    Map<String, FileOrDir> contents = new HashMap<>();

    public FileOrDir(String name) {
        System.out.println("(test) Resource for test-input.txt: " + getClass().getClassLoader().getResource("test-input.txt"));
        this.directory = true;
        this.name = name;
    }

    public FileOrDir(String name, long filesize) {
        this.directory = false;
        this.name = name;
        this.filesize = filesize;
    }

    public FileOrDir get(String name) {
        return contents.get(name);
    }

    public void add(FileOrDir item) {
        contents.put(item.name, item);
        if (item.directory) {
            item.parentDirectory = this;
        }
    }

    public long getTotalSize() {
        long totalSize = 0;
        for (FileOrDir item : contents.values()) {
            if (item.directory) {
                totalSize += item.getTotalSize();
            } else {
                totalSize += item.filesize;
            }
        }
        return totalSize;
    }

    List<FileOrDir> findDirectoriesUnder(long size) {
        List<FileOrDir> resultList = new ArrayList<>();
        findDirectoriesUnder(size, resultList);
        return resultList;
    }

    void findDirectoriesUnder(long size, List<FileOrDir> resultList) {
        for (FileOrDir item : contents.values()) {
            if (item.directory) {
                if (item.getTotalSize() <= size) {
                    resultList.add(item);
                }
                item.findDirectoriesUnder(size, resultList);
            }
        }
    }

    List<FileOrDir> findDirectoriesOver(long size) {
        List<FileOrDir> resultList = new ArrayList<>();
        findDirectoriesOver(size, resultList);
        return resultList;
    }

    void findDirectoriesOver(long size, List<FileOrDir> resultList) {
        for (FileOrDir item : contents.values()) {
            if (item.directory) {
                if (item.getTotalSize() >= size) {
                    resultList.add(item);
                }
                item.findDirectoriesOver(size, resultList);
            }
        }
    }
}
