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
}
