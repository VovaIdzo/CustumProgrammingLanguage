package com.idzodev.utils;

import com.idzodev.callback.OnClickListener;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Created by vladimir on 22.05.16.
 */
public class DialogsUtils {
    public static void createEditDialog(String title, String message,String defValue, OnClickListener<String> callback){
        TextInputDialog dialog = new TextInputDialog(defValue);
        dialog.setTitle(title);
        dialog.setHeaderText(title);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            callback.onClick(result.get());
        }
    }
}
