package com.idzodev.model;

import javafx.scene.Node;

import java.io.File;

/**
 * Created by vladimir on 22.05.16.
 */
public class FileItemDvo {

    File file;
    boolean isDir;

    public FileItemDvo(File file) {
        this.file = file;
    }

    public FileItemDvo(File file, boolean isDir) {
        this.file = file;
    }


    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    public boolean isDir() {
        return isDir;
    }
}
