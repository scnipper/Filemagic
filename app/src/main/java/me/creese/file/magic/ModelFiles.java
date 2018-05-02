package me.creese.file.magic;

/**
 * Created by scnipper on 25.04.2018.
 */

public class ModelFiles {
    private final String size;
    private final String permissons;
    private final String date;
    private String name;
    private boolean isDir;
    private boolean selectedMode;
    private boolean select;
    private boolean modeCopyAndMove;

    public ModelFiles(String name, boolean isDir, String size, String permissions, String date) {
        this.size = size;
        this.name = name;
        this.isDir = isDir;
        this.permissons = permissions;
        this.date = date;
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

    public boolean isModeCopyAndMove() {
        return modeCopyAndMove;
    }
}
