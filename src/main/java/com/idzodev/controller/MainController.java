package com.idzodev.controller;

import com.idzodev.Main;
import com.idzodev.callback.OnClickListener;
import com.idzodev.model.FileItemDvo;
import com.idzodev.model.OpenFile;
import com.idzodev.ui.start.EditTextTab;
import com.idzodev.utils.DialogsUtils;
import com.idzodev.utils.FileHelper;
import com.idzodev.utils.LanguageMapper;
import com.idzodev.utils.ViewHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladimir on 22.05.16.
 */
public class MainController {

    @FXML
    TreeView<FileItemDvo> fileTree;

    @FXML
    TabPane tabs;

    @FXML
    TextArea console;

    @FXML
    BorderPane consoleContainer;

    private File projectFile;

    List<OpenFile> openFiles = new ArrayList<>(20);

    private ContextMenu menu = new ContextMenu();
    private AppCompiler appCompiler = new AppCompiler();
    private ProjectConfigurationController projectConfigurationController = new ProjectConfigurationController();

    @FXML
    public void initialize(){
        initMenu();
        onConsoleClose();
        fileTree.setCellFactory(tv -> {
            final TreeCell<FileItemDvo> cell = new TreeCell<FileItemDvo>() {
                @Override
                public void updateItem(FileItemDvo item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText("");
                        setGraphic(null);
                    } else {
                        setText(item.toString()); // appropriate text for item
                        if (item.getFile().isDirectory())
                            setGraphic(new ImageView(Main.folderImage));
                        else {
                            if (item.toString().contains(".conf")){
                                setGraphic(new ImageView(Main.configImage));
                            } else {
                                setGraphic(new ImageView(Main.fileImage));
                            }
                        }
                    }
                }
            };
            return cell;
        });
        fileTree.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<FileItemDvo>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<FileItemDvo>> observable, TreeItem<FileItemDvo> oldValue, TreeItem<FileItemDvo> newValue) {
                        if (newValue != null)
                            onSelectFile(newValue.getValue());
                    }
                });
        fileTree.setContextMenu(menu);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    public void onOpenNewFile(){
        onSaveAll(null);
        Stage stage = (Stage) fileTree.getScene().getWindow();
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Виберіть папку");
        File selectedFile = fileChooser.showDialog(stage);
        if (selectedFile != null){
            reinitAll(selectedFile);
        }
    }

    public void onBuild(ActionEvent actionEvent) {
        onSaveAll(null);
        consoleContainer.setVisible(true);
        consoleContainer.setPrefHeight(150);
        appCompiler.buildProject(projectConfigurationController.getConfiguration(), item -> {
            console.appendText(item+"\n");
        });
    }

    public void onRun(ActionEvent actionEvent) {
        console.clear();
        onSaveAll(null);
        consoleContainer.setVisible(true);
        consoleContainer.setPrefHeight(150);
        appCompiler.runProject(projectConfigurationController.getConfiguration(), item -> {
            console.appendText(item+"\n");
        });
    }

    public void onRefresh(ActionEvent actionEvent) {
        onSaveAll(null);
        reinitFiles();
    }

    public void onSaveAll(ActionEvent actionEvent) {
        for (OpenFile openFile : openFiles){
            FileHelper.saveFile(openFile.getFile(), openFile.getTab().getTextArea().getText());
        }
    }

    public void onConsoleClose(){
        console.setText("");
        consoleContainer.setPrefHeight(0);
        consoleContainer.setVisible(false);
    }

    private void onSelectFile(FileItemDvo dvo){
        final File file = dvo.getFile();
        if (file.isDirectory())
            return;

        for (OpenFile openFile : openFiles){
            if (openFile.getFile().equals(dvo.getFile())){
                tabs.getSelectionModel().select(openFile.getTab().getTab());
                return;
            }
        }

        EditTextTab tab = ViewHelper.createNewTab(file.getName(),
                FileHelper.readFile(file),
                item -> {
                    FileHelper.saveFile(file, item.getTextArea().getText());
                    for (OpenFile openFile : openFiles){
                        if (openFile.getTab().equals(item)){
                            openFiles.remove(openFile);
                            break;
                        }
                    }
                });
        openFiles.add(new OpenFile(file, tab));
        tabs.getTabs().add(tab.getTab());
        tabs.getSelectionModel().select(tab.getTab());
    }

    private void reinitAll(File rootFile){
        projectFile = rootFile;
        onSaveAll(null);
        openFiles.clear();
        tabs.getTabs().removeAll(tabs.getTabs());
        projectConfigurationController.initProjectConfiguration(rootFile);
        reinitFiles();
    }

    private void reinitFiles(){
        TreeItem<FileItemDvo> items = fileTree.getRoot();
        if (items != null)
            items.getChildren().removeAll(items.getChildren());
        ViewHelper.createFilesTree(new File(projectFile.getPath()), fileTree);
    }

    private void initMenu(){
        MenuItem addClass = new MenuItem("Новий клас");
        addClass.setOnAction(event -> {
            DialogsUtils.createEditDialog("Новий клас", "Назва класу", "", item -> {
                File file = fileTree.getSelectionModel().getSelectedItem().getValue().getFile();
                if (!file.isDirectory()){
                    file = file.getParentFile();
                }
                File newFile = FileHelper.createFile(file,  item+".jua");
                FileHelper.saveFile(newFile, LanguageMapper.getDefaultClassTemplate(item));
                reinitFiles();
            });
        });
        MenuItem addInterface = new MenuItem("Новий інтерфейс");
        addInterface.setOnAction(event -> {
            DialogsUtils.createEditDialog("Новий інтерфейс", "Назва інтерфейсу", "", item -> {
                File file = fileTree.getSelectionModel().getSelectedItem().getValue().getFile();
                if (!file.isDirectory()){
                    file = file.getParentFile();
                }
                File newFile = FileHelper.createFile(file,  item+".jua");
                FileHelper.saveFile(newFile, LanguageMapper.getDefaultInterfaceTemplate(item));
                reinitFiles();
            });
        });


        MenuItem addFile = new MenuItem("Новий файл");
        addFile.setOnAction(event -> {
            DialogsUtils.createEditDialog("Новий файл", "Назва файлу", "", item -> {
                File file = fileTree.getSelectionModel().getSelectedItem().getValue().getFile();
                if (!file.isDirectory()){
                    file = file.getParentFile();
                }
                File newFile = FileHelper.createFile(file,  item);
                reinitFiles();
            });
        });
        MenuItem addFold = new MenuItem("Нова папка");
        addFold.setOnAction(event -> {
            DialogsUtils.createEditDialog("Новий папка", "Назва папки", "", item -> {
                File file = fileTree.getSelectionModel().getSelectedItem().getValue().getFile();
                if (!file.isDirectory()){
                    file = file.getParentFile();
                }
                FileHelper.createDir(file, item);
                reinitFiles();
            });
        });
        MenuItem rename = new MenuItem("Перейменувати");
        rename.setOnAction(event -> {
            DialogsUtils.createEditDialog("Перейменувати", "Нова назва", fileTree.getSelectionModel().getSelectedItem().getValue().getFile().getName(), item -> {
                File file = fileTree.getSelectionModel().getSelectedItem().getValue().getFile();
                for (OpenFile openFile : openFiles){
                    if (openFile.getFile().equals(file)){
                        FileHelper.saveFile(openFile.getFile(), openFile.getTab().getTextArea().getText());
                        tabs.getTabs().remove(openFile.getTab().getTab());
                    }
                }
                FileHelper.renameFile(file, item);
                reinitFiles();
            });
        });

        MenuItem delete = new MenuItem("Видалити");
        delete.setOnAction(event -> {
            File file = fileTree.getFocusModel().getFocusedItem().getValue().getFile();
            for (OpenFile openFile : openFiles){
                if (openFile.getFile().equals(file)){
                    tabs.getTabs().remove(openFile.getTab().getTab());
                }
            }
            FileHelper.deleteFile(file);
            reinitFiles();
        });
        menu.getItems().add(addClass);
        menu.getItems().add(addInterface);
        menu.getItems().add(addFile);
        menu.getItems().add(addFold);
        menu.getItems().add(rename);
        menu.getItems().add(delete);
    }
}
