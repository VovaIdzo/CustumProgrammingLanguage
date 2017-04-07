package com.idzodev.ui.start;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.io.IOException;

/**
 * Created by vova on 09.01.2016.
 */
public class StartScreen implements MainView{

    @FXML
    Button btExit;
    @FXML
    Button btSettings;
    @FXML
    ToggleButton btStart;
    @FXML
    Label lbState;

    @Override
    public void showStop() {
        btStart.setSelected(false);
        btStart.setSelected(false);
    }
}
