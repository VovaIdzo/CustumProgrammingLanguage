package com.idzodev.utils;

import com.idzodev.Main;
import com.idzodev.callback.OnClickListener;
import com.idzodev.controller.ProjectConfigurationController;
import com.idzodev.model.FileItemDvo;
import com.idzodev.model.ProjectConfiguration;
import com.idzodev.ui.start.EditTextTab;
import com.idzodev.ui.start.HightLight;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by vladimir on 22.05.16.
 */
public class ViewHelper {
    public static EditTextTab createNewTab(String tabName, String tabContent, OnClickListener<EditTextTab> listener){
        Tab tab = new Tab();
        tab.setText(tabName);
        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .subscribe(change -> {
                    codeArea.setStyleSpans(0, HightLight.computeHighlighting(codeArea.getText()));
                });
        codeArea.replaceText(0,0,tabContent);
        tab.setContent(codeArea);
        EditTextTab tab1 = new EditTextTab(tab, codeArea);
        tab.setOnClosed(new EventHandler<Event>(){
            @Override
            public void handle(Event e){
                listener.onClick(tab1);
            }
        });
        return tab1;
    }

    public static void createFilesTree(File dir, TreeView<FileItemDvo> filesTree){
        findFiles(dir, null, filesTree);
    }

    private static void findFiles(File dir, TreeItem<FileItemDvo> parent, TreeView<FileItemDvo> filesTree) {
        FileItemDvo fileItemDvo = new FileItemDvo(dir,true);
        TreeItem<FileItemDvo> root = new TreeItem<>(fileItemDvo);
        root.setExpanded(true);
        try {
            File[] files = dir.listFiles();
            if (files != null){
                Collections.sort(Arrays.asList(files), new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && !o2.isDirectory()){
                            return -1;
                        }
                        else if (o2.isDirectory() && !o1.isDirectory()){
                            return 1;
                        } else
                            return o1.getName().compareTo(o2.getName());

                    }
                });
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!file.getName().equals(ProjectConfigurationController.GEN_DIR))
                            findFiles(file,root, filesTree);
                    } else {
                        root.getChildren().add(new TreeItem<>(new FileItemDvo(file,false)));
                    }
                }
            }
            if(parent==null){
                filesTree.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
