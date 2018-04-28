package me.creese.file.magic;

import android.annotation.SuppressLint;
import android.os.Parcelable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by scnipper on 24.04.2018.
 */

public class FileCore {

    public static final int K_BYTES = 1024;
    public static final int M_BYTES = 1024 * 1024;
    public static final int G_BYTES = 1024 * 1024 * 1024;
    private final MainActivity activity;
    private final Calendar dateFile;
    private StringBuilder currentDir;
    private LinkedList<Parcelable> statesRecycleView;


    public FileCore(MainActivity mainActivity) {
        currentDir = new StringBuilder();
        this.activity = mainActivity;
        statesRecycleView = new LinkedList<>();
        dateFile = Calendar.getInstance();
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
            activity.getAdapter().addItem(new ModelFiles(listFile.getName(),
                    listFile.isDirectory(),
                    getSize(listFile),getPermissions(listFile), getDate(listFile)));
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
                activity.getAdapter().addItem(new ModelFiles(listFile.getName(), listFile.isDirectory(),
                        getSize(listFile), getPermissions(listFile),getDate(listFile)));
            }

            if (isNotAppend)
                activity.getRecyclerView().getLayoutManager().onRestoreInstanceState(statesRecycleView.pollLast());

            if (list.size() == 0) activity.addEmptySign(R.string.empty_folder);
        } else {
            activity.addEmptySign(R.string.no_permission_read);
        }
    }

    private void refreshDir() {
        Parcelable save = activity.getRecyclerView().getLayoutManager().onSaveInstanceState();

        activity.getAdapter().clear();
        ArrayList<File> list = getListFiles();

        for (File listFile : list) {
            activity.getAdapter().addItem(new ModelFiles(listFile.getName(),
                    listFile.isDirectory(), getSize(listFile), getPermissions(listFile), getDate(listFile)));
        }

        activity.getRecyclerView().getLayoutManager().onRestoreInstanceState(save);


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

    public MainActivity getActivity() {
        return activity;
    }

    public void createFile(String name) {
        File file = new File(currentDir.toString() + name);
        try {
            if (file.createNewFile()) refreshDir();
        } catch (IOException e) {
            e.printStackTrace();


        }

    }

    public void createFolder(String name) {
        File folder = new File(currentDir.toString() + name);
        if (folder.mkdir())
            refreshDir();
    }

    @SuppressLint("DefaultLocale")
    public String getSize(File sizeFile) {

        long size = sizeFile.length();



        if (sizeFile.isDirectory()) return "--";

        if (size >= 0) {

            if (size > G_BYTES) {
                return  String.format("%(.2f Gb", (float)size / G_BYTES);
            }
            if (size > M_BYTES) {
                return String.format("%(.2f Mb", (float)size / M_BYTES);
            }
            if (size > K_BYTES) {
                return String.format("%(.2f Kb", (float)size / K_BYTES);
            }
            return size + " bytes";
        }

        return "";
    }

    private String getPermissions(File file) {
        String perm = "";

        if(file.canRead()) perm+="r";
        else perm+="-";
        if(file.canWrite()) perm+="w";
        else perm+="-";
        if(file.canExecute()) perm+="x";
        else perm+="-";
        return perm;
    }

    private String getDate(File fileData) {
        long date = fileData.lastModified();
        if(date > 0) {
            dateFile.setTimeInMillis(date);
            return dateFile.get(Calendar.DAY_OF_MONTH) + "." + dateFile.get(Calendar.MONTH) + "." + dateFile.get(Calendar.YEAR);
        }
        else {
            return "--";
        }


    }
}
