package com.idzodev.ui.start;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;

/**
 * Created by vladimir on 22.05.16.
 */
public class EditTextTab {
    private Tab tab;
    private CodeArea textArea;

    public EditTextTab(Tab tab, CodeArea textArea) {
        this.tab = tab;
        this.textArea = textArea;
    }

    public Tab getTab() {
        return tab;
    }

    public CodeArea getTextArea() {
        return textArea;
    }
}
