package me.creese.file.magic;

import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by scnipper on 24.04.2018.
 */

public class FileCore {

    private final MainActivity activity;
    private StringBuilder currentDir;
    private LinkedList<Parcelable> statesRecycleView;


    public FileCore(MainActivity mainActivity) {
        currentDir = new StringBuilder();
        this.activity = mainActivity;
        statesRecycleView = new LinkedList<>();
    }

    public String[] getListFilesCurrentDir() {
        File file = new File(currentDir.toString());
        return file.list();
    }

    private ArrayList<File> getListFiles() {
        File file = new File(currentDir.toString());



        if (file.canRead()) {

            File[] list = file.listFiles();
            ArrayList<File> files = new ArrayList<>();


            for (File aList : list) {
                if (aList.isDirectory()) {
                    files.add(0, aList);
                } else files.add(aList);
            }
            return files;
        } else return null;


    }

    public void openDir(String directory) {
        openDir(directory, false);
    }

    public void openRootDir() {
        currentDir.append("/");
        activity.getTextDir().setText(currentDir.toString());
        ArrayList<File> list = getListFiles();

        for (File listFile : list) {
            activity.getAdapter().addItem(new ModelFiles(listFile.getName(), listFile.isDirectory()));
        }


    }

    private void openDir(String directory, boolean isNotAppend) {
        if (!isNotAppend)
            statesRecycleView.add(activity.getRecyclerView().getLayoutManager().onSaveInstanceState());

        activity.getRecyclerView().scrollToPosition(0);

        if (!directory.equals("/") && !isNotAppend) {
            currentDir.append(directory);
            currentDir.append('/');
        } else {
            if (!isNotAppend)
                currentDir.append('/');
        }


        activity.getAdapter().clear();
        activity.getTextDir().setText(currentDir.toString());

        ArrayList<File> list = getListFiles();
        if (list != null) {
            for (File listFile : list) {
                activity.getAdapter().addItem(new ModelFiles(listFile.getName(), listFile.isDirectory()));
            }

            if (isNotAppend)
                activity.getRecyclerView().getLayoutManager().onRestoreInstanceState(statesRecycleView.pollLast());

            if (list.size() == 0) activity.addEmptySign(R.string.empty_folder);
        } else {
            activity.addEmptySign(R.string.no_permission_read);
        }
    }

    public void clearCurrentDir() {
        currentDir = new StringBuilder();
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
