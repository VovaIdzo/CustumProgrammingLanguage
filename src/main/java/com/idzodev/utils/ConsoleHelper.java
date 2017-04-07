package com.idzodev.utils;

import com.idzodev.callback.DataCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vladimir on 23.05.16.
 */
public class ConsoleHelper {

    public static String make(String command, DataCallback<String> callback){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null){
                if (callback != null)
                    callback.execute(line);
                builder.append(line).append("\n");
            }
            reader.close();
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            line = "";
            while ((line = reader.readLine()) != null){
                if (callback != null)
                    callback.execute(line);
                builder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
        }
        return builder.toString();
    }

}
