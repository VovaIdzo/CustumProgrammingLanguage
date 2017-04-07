package com.idzodev;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    public static Image folderImage;
    public static Image fileImage;
    public static Image configImage;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/main.fxml"));
        primaryStage.setTitle("UAJ Редактор");
        primaryStage.setScene(createStartScene(root));
        primaryStage.setMaxWidth(1920);
        primaryStage.setMaxHeight(1080);
        folderImage = new Image(getClass().getClassLoader().getResourceAsStream("icons/ic_folder.png"));
        configImage = new Image(getClass().getClassLoader().getResourceAsStream("icons/ic_conf.png"));
        fileImage = new Image(getClass().getClassLoader().getResourceAsStream("icons/ic_code.png"));
        primaryStage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("views/java-keywords.css").toExternalForm());
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(
                getClass().getClassLoader().getResourceAsStream("icons/logo1.png")
        ));
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();
    }

    public static Scene createStartScene(Parent root) {
        Scene scene = new Scene(root, 1000, 600);
        return scene;
    }
}
