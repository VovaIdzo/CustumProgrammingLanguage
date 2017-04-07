package com.idzodev.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by vladimir on 22.05.16.
 */
public class FileHelper {

    public static String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            while ((line = reader.readLine()) != null)
                builder.append(line).append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void saveFile(File file, String text){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createFile(File dir, String name){
        File file = new File(dir, name);
        if (!file.exists()){
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File createDir(File dir, String name){
        File file = new File(dir, name);
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static void renameFile(File file, String newName){
        try {
            Path source = Paths.get(file.getPath());
            Files.move(source, source.resolveSibling(newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File file){
        if (file.isDirectory()){
            deleteDirectory(file);
        } else {
            file.delete();
        }
    }

    private static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }
}
