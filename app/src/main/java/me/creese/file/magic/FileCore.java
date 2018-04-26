package me.creese.file.magic;

import java.io.File;

/**
 * Created by scnipper on 24.04.2018.
 */

public class FileCore {

    private String currentDir;

    public FileCore() {
        currentDir = "/";

    }

    public String[] getListFilesCurrentDir() {
        File file = new File(currentDir);
        return file.list();
    }

}
