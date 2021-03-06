package me.creese.file.magic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import me.creese.file.magic.util.Saves;
import me.creese.file.magic.util.TypesFiles;

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
    private boolean selectedMode;
    private String rootDir;
    private ArrayList<File> realFiles;
    private String saveDir;
    private boolean deleteFail;


    public FileCore(MainActivity mainActivity) {
        currentDir = new StringBuilder();
        this.activity = mainActivity;
        statesRecycleView = new LinkedList<>();
        dateFile = Calendar.getInstance();
        realFiles = new ArrayList<>();
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

        ArrayList<File> list = getListFiles();

        if (list == null) {
            clearCurrentDir();
            currentDir.append(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
            list = getListFiles();
        }
        activity.getTextDir().setText(currentDir.toString());
        for (File listFile : list) {

            ModelFiles modelFiles = new ModelFiles(listFile.getName(),
                    listFile.isDirectory(),
                    getSize(listFile), getPermissions(listFile), getDate(listFile),listFile.lastModified(),
                    getExtension(listFile.getName()));

            checkType(listFile, modelFiles);
            activity.getAdapter().addItem(modelFiles);
        }

        rootDir = currentDir.toString();


    }


    private void openDir(String directory, boolean isNotAppend) {
        if (!isNotAppend) {
            statesRecycleView.add(activity.getRecyclerView().getLayoutManager().onSaveInstanceState());
            activity.getAdapter().saveData();
        }
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

            if (!activity.getAdapter().restoreData()) {
                for (File listFile : list) {
                    if(listFile.isHidden()) continue;
                    ModelFiles modelFiles = new ModelFiles(listFile.getName(),
                            listFile.isDirectory(), getSize(listFile),
                            getPermissions(listFile), getDate(listFile),listFile.lastModified(),getExtension(listFile.getName()));
                    checkType(listFile, modelFiles);
                    activity.getAdapter().addItem(modelFiles);
                }
            }

            if (isNotAppend)
                activity.getRecyclerView().getLayoutManager().onRestoreInstanceState(statesRecycleView.pollLast());

            if (list.size() == 0) activity.addEmptySign(R.string.empty_folder);
        } else {
            activity.addEmptySign(R.string.no_permission_read);
        }
    }

    private void refreshDir() {
        activity.runOnUiThread(() -> {
            Parcelable save = activity.getRecyclerView().getLayoutManager().onSaveInstanceState();

            activity.getAdapter().clear();
            ArrayList<File> list = getListFiles();

            for (File listFile : list) {

                if(listFile.isHidden()) continue;
                ModelFiles modelFiles = new ModelFiles(listFile.getName(),
                        listFile.isDirectory(), getSize(listFile),
                        getPermissions(listFile), getDate(listFile),listFile.lastModified(),getExtension(listFile.getName()));
                checkType(listFile, modelFiles);

                activity.getAdapter().addItem(modelFiles);
            }

            activity.getRecyclerView().getLayoutManager().onRestoreInstanceState(save);
        });


    }

    private void checkType(File listFile, ModelFiles modelFiles) {
        if (listFile.isDirectory()) return;

        String name = listFile.getName();
        name = name.toLowerCase();
        for (int i = 0; i < TypesFiles.IMAGES.length; i++) {
            if (name.endsWith(TypesFiles.IMAGES[i])) {
                modelFiles.setLoadImagePreview(true);
                break;
            }
        }


    }

    public void clearCurrentDir() {
        currentDir = new StringBuilder();
    }

    public String getCurrentDir() {
        return currentDir.toString();
    }


    public void backDir() {

        activity.getAdapter().saveData();

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


        if (sizeFile.isDirectory()) {

            int elem = 0;

            if (sizeFile.list() != null)
                elem = sizeFile.list().length;

            return elem + " " + activity.getString(R.string.elements);
        }

        if (size >= 0) {

            if (size > G_BYTES) {
                return String.format("%(.2f Gb", (float) size / G_BYTES);
            }
            if (size > M_BYTES) {
                return String.format("%(.2f Mb", (float) size / M_BYTES);
            }
            if (size > K_BYTES) {
                return String.format("%(.2f Kb", (float) size / K_BYTES);
            }
            return size + " bytes";
        }

        return "";
    }

    private String getPermissions(File file) {
        String perm = "";

        if (file.canRead()) perm += "r";
        else perm += "-";
        if (file.canWrite()) perm += "w";
        else perm += "-";
        if (file.canExecute()) perm += "x";
        else perm += "-";
        return perm;
    }

    @SuppressLint("DefaultLocale")
    private String getDate(File fileData) {
        long date = fileData.lastModified();
        if (date > 0) {
            dateFile.setTimeInMillis(date);
            return String.format("%02d.%02d.%4d", dateFile.get(Calendar.DAY_OF_MONTH),
                    dateFile.get(Calendar.MONTH) + 1, dateFile.get(Calendar.YEAR));
        } else {
            return "--";
        }


    }

    public String getRootDir() {
        return rootDir;
    }

    public boolean isSelectedMode() {
        return selectedMode;
    }

    public void startSelectedMode() {
        selectedMode = true;
    }

    private void showNotPermissionToast() {

        Toast.makeText(activity, R.string.no_permission, Toast.LENGTH_SHORT).show();
    }

    private void calculateRealCountFiles(File[] files) {

        for (int i = 0; i < files.length; i++) {
            realFiles.add(files[i]);
            if (files[i].isDirectory()) {
                File[] listFiles = files[i].listFiles();
                if (listFiles != null)
                    calculateRealCountFiles(listFiles);
            }
        }

    }

    public void renameFile(File fileRename, String which) {

        try {
            if (fileRename.renameTo(new File(currentDir + which))) {
                refreshDir();
            } else {
                showNotPermissionToast();

            }
        } catch (SecurityException e) {
            e.printStackTrace();
            showNotPermissionToast();

        }

    }

    public void moveFile(File[] sources, boolean isCopy) {
        //if (!dest.isDirectory()) throw new NoSuchElementException("dest is file");

        Thread threadMove = new Thread(() -> {
            realFiles.clear();
            calculateRealCountFiles(sources);


            int fullProgress = 0;
            for (int i = 0; i < realFiles.size(); i++) {
                InputStream is = null;
                FileOutputStream os = null;

                String path = currentDir.toString() + realFiles.get(i).getAbsolutePath().replaceAll(saveDir, "");

                File destFile = new File(path);

                fullProgress = (int) (((float) i / realFiles.size()) * 100);
                int finalFullProgress1 = fullProgress;
                int finalI = i;
                activity.runOnUiThread(() ->
                        Dialogs.getInstanse().tickFullProgress(finalFullProgress1,
                                finalI + " " + activity.getString(R.string.from) + " " + realFiles.size()));

                if (realFiles.get(i).isDirectory()) {
                    destFile.mkdirs();

                } else {
                    try {
                        destFile.createNewFile();
                        is = new FileInputStream(realFiles.get(i));
                        os = new FileOutputStream(destFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        int isLength = is.available();
                        int progLength = 0;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer);
                            progLength += length;
                            int fLength = (int) (((float) progLength / isLength) * 100);
                            activity.runOnUiThread(() ->
                                    Dialogs.getInstanse().tickFileProgress(fLength, path));

                        }
                        is.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            activity.runOnUiThread(() ->
                    Dialogs.getInstanse().tickFullProgress(100,
                            realFiles.size() + " " + activity.getString(R.string.from) + " " + realFiles.size()));
            if (!isCopy) deleteFiles(sources);
            refreshDir();
        });
        threadMove.start();

    }

    public void deleteFiles(File[] files) {

        Thread threadDelete = new Thread(() -> {

            delete(files);
            if (!deleteFail)
                activity.runOnUiThread(() -> activity.getAdapter().deleteSelected());
            else {
                deleteFail = false;
                activity.getAdapter().deselectAll();
                activity.getAdapter().notifyDataSetChanged();
            }

        }, "delete thread");

        threadDelete.start();

    }

    private void delete(File[] files) {

        if(files == null) {
            if(!deleteFail)
            activity.runOnUiThread(this::showNotPermissionToast);
            deleteFail = true;
            return;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                delete(files[i].listFiles());
                if (!files[i].delete()) {
                    if(!deleteFail)
                    activity.runOnUiThread(this::showNotPermissionToast);
                    deleteFail = true;
                    return;
                }
            } else if (!files[i].delete()) {
                if(!deleteFail)
                activity.runOnUiThread(this::showNotPermissionToast);
                deleteFail = true;
                return;
            }
        }
    }


    public void openFile(String mime, String name, String extension) {
        openFile(mime,name,extension,false);
    }

    public void openFile(String mime, String name, String extension,boolean isOpenWith) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri contentUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationContext().getPackageName() + ".provider",
                    new File(currentDir + name));
            intent.setDataAndTypeAndNormalize(contentUri, mime);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            Uri fileUri = Uri.fromFile(new File(currentDir + name));
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, mime);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        String launchActivity = Saves.getInstanse().getPref.getString(extension,null);

        if(launchActivity == null) {

            List<ResolveInfo> pkgAppsList = activity.getPackageManager().queryIntentActivities(intent, 0);

            if(pkgAppsList.size() == 0) {
                activity.showDialogOpenWith(what -> {
                    openFile(what.toString(),name, extension);
                });
                return;
            }

            /*for (ResolveInfo resolveInfo : pkgAppsList) {
                System.out.println(resolveInfo);
            }*/

            if(isOpenWith) {
                String ext = getExtension(name);
                if(Saves.getInstanse().getPref.getString(ext,null) != null) {
                    intent.putExtra("ext",ext);
                    intent.putExtra("how",true);
                }
                if(ext != null) intent.putExtra("ext",ext);
            }
            else intent.putExtra("ext",extension);
            Dialogs.getInstanse().showDialogListActivities(activity, pkgAppsList, intent);
        }
        else {
            String[] launch = launchActivity.split("/",2);
            intent.setClassName(launch[0],launch[1]);
            activity.startActivity(intent);
        }
    }

    public void saveCurDir() {
        saveDir = currentDir.toString();
    }

    public String getExtension(String name) {
        int indexBegin = name.lastIndexOf('.')+1;
        if(indexBegin > 0) {
            String extension = name.substring(indexBegin, name.length());
            return extension.toLowerCase();
        }
        else return null;
    }
}
