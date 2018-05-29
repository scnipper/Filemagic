package me.creese.file.magic;

/**
 * Created by scnipper on 25.04.2018.
 */

public class ModelFiles {
    private final String size;
    private final String permissons;
    private final String date;
    private long longDate;
    private String extension;
    private String name;
    private boolean isDir;
    private boolean selectedMode;
    private boolean select;
    private boolean modeCopyAndMove;
    private boolean loadImagePreview;

    public ModelFiles(String name, boolean isDir, String size,
                      String permissions, String date, long longDate, String extension) {
        this.size = size;
        this.name = name;
        this.isDir = isDir;
        this.permissons = permissions;
        this.date = date;
        this.longDate = longDate;
        if(longDate == 0) this.longDate = (long) (Math.random() % 500);
        this.extension = extension;

        if(extension == null) this.extension = "";
        if(isDir) {
            this.extension = "0"+Math.random();
        }
    }

    public String getExtension() {
        return extension;
    }

    public String getDate() {
        return date;
    }

    public String getPermissons() {
        return permissons;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setSelectedMode(boolean selectedMode) {
        this.selectedMode = selectedMode;
    }

    public boolean isSelectedMode() {
        return selectedMode;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }

    public void setModeCopyAndMove(boolean modeCopyAndMove) {
        this.modeCopyAndMove = modeCopyAndMove;
    }

    public long getLongDate() {
        return longDate;
    }

    public boolean isModeCopyAndMove() {
        return modeCopyAndMove;
    }

    public void setLoadImagePreview(boolean loadImagePreview) {
        this.loadImagePreview = loadImagePreview;
    }

    public boolean isLoadImagePreview() {
        return loadImagePreview;
    }
}
