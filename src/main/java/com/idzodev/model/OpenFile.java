package com.idzodev.model;

import com.idzodev.ui.start.EditTextTab;
import javafx.scene.control.Tab;

import java.io.File;

/**
 * Created by vladimir on 22.05.16.
 */
public class OpenFile {
    private File file;
    private EditTextTab tab;

    public OpenFile(File file, EditTextTab tab) {
        this.file = file;
        this.tab = tab;
    }

    public File getFile() {
        return file;
    }

    public EditTextTab getTab() {
        return tab;
    }
}
