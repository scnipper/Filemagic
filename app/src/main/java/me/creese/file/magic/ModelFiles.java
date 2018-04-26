package me.creese.file.magic;

/**
 * Created by scnipper on 25.04.2018.
 */

public class ModelFiles {
    private String name;
    private boolean isDir;

    public ModelFiles(String name, boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }

    public String getName() {
        return name;
    }

    public boolean isDir() {
        return isDir;
    }
}
