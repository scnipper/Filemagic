package me.creese.file.magic;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by scnipper on 24.04.2018.
 */

public class FileCore {

    private final MainActivity activity;
    private StringBuilder currentDir;


    public FileCore(MainActivity mainActivity) {
        currentDir = new StringBuilder();
        this.activity = mainActivity;
    }

    public String[] getListFilesCurrentDir() {
        File file = new File(currentDir.toString());
        return file.list();
    }

    private ArrayList<File> getListFiles() {
        File file = new File(currentDir.toString());

        File[] list = file.listFiles();
        ArrayList<File> files = new ArrayList<>();


        for (File aList : list) {
            if (aList.isDirectory()) {
                files.add(0, aList);
            } else files.add(aList);
        }


        return files;
    }

    public void openDir(String directory) {
        openDir(directory, false);
    }

    public void openDir(String directory, boolean isNotAppend) {

        activity.getRecyclerView().scrollToPosition(0);

        if (!directory.equals("/") && !isNotAppend) {
            currentDir.append(directory);
            currentDir.append('/');
        }
        else {
            if(!isNotAppend)
            currentDir.append('/');
        }




        activity.getAdapter().clear();
        activity.getTextDir().setText(currentDir.toString());

        for (File listFile : getListFiles()) {
            activity.getAdapter().addItem(new ModelFiles(listFile.getName(), listFile.isDirectory()));
        }
    }

    public String getCurrentDir() {
        return currentDir.toString();
    }


    public void backDir() {

        int startDeleteIndex = currentDir.length() - 1;
        while (true) {
            startDeleteIndex--;
            if (currentDir.charAt(startDeleteIndex) == '/') {
                break;
            }
        }

        currentDir.delete(startDeleteIndex + 1, currentDir.length());

        openDir(currentDir.toString(), true);
    }
}
