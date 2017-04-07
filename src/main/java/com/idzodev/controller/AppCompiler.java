package com.idzodev.controller;

import com.idzodev.callback.DataCallback;
import com.idzodev.model.ProjectConfiguration;
import com.idzodev.utils.ConsoleHelper;
import com.idzodev.utils.FileHelper;
import com.idzodev.utils.LanguageMapper;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//vlazoryk@ramble.ru
//v.lazoryk@chnu.edu.ua

public class AppCompiler {


    public void runProject(ProjectConfiguration projectConfiguration, DataCallback<String> consoleCallback){
        if (projectConfiguration == null){
            consoleCallback.execute("Проект не створений. Створіть проект або перевірте конфігураційний файл jua.conf");
            return;
        }
        cleanGenLibs(projectConfiguration.getGenSourceDir());
        List<File> javaFiles = generateJavaFiles(
                projectConfiguration.getGenSourceDir(),
                projectConfiguration.getAppDir());
        File mainClass = assembleFiles(projectConfiguration.getGenSourceDir(), projectConfiguration.getMainClassFile(), javaFiles, consoleCallback);
        run(mainClass, consoleCallback);
    }

    public void buildProject(ProjectConfiguration projectConfiguration, DataCallback<String> consoleCallback){
        if (projectConfiguration == null){
            consoleCallback.execute("Проект не створений. Створіть проект або перевірте конфігураційний файл jua.conf");
            return;
        }
        cleanGenLibs(projectConfiguration.getGenSourceDir());
        List<File> javaFiles = generateJavaFiles(
                projectConfiguration.getGenSourceDir(),
                projectConfiguration.getAppDir());
        File mainClass = assembleFiles(projectConfiguration.getGenSourceDir(), projectConfiguration.getMainClassFile(), javaFiles, consoleCallback);
        build(mainClass, consoleCallback, javaFiles);
    }

    private void cleanGenLibs(File genDir){
        FileHelper.deleteFile(genDir);
        FileHelper.createDir(genDir.getParentFile(), genDir.getName());
    }

    private List<File> generateJavaFiles(File genDir, File appDir){
        Set<File> files = new HashSet<>();
        Set<File> dirs = new HashSet<>();
        getAllCreateAppClasses(appDir, files, dirs, LanguageMapper.getFileClassTranslation());

        for (File file : dirs){
            String appDirPath = file.getPath();
            String genDirPath = appDirPath.substring(appDirPath.indexOf(ProjectConfigurationController.APP_DIR) + ProjectConfigurationController.APP_DIR.length());
            genDirPath = LanguageMapper.translateDirName(genDirPath);
            new File(genDir.getPath()+genDirPath).mkdirs();
        }

        List<File> javaFiles = new ArrayList<>(files.size());
        for (File file : files){
            String appDirPath = file.getParent();
            String genDirPath = appDirPath.substring(appDirPath.indexOf(ProjectConfigurationController.APP_DIR) + ProjectConfigurationController.APP_DIR.length());
            genDirPath = LanguageMapper.translateDirName(genDirPath)+"/"+LanguageMapper.translateFileName(file.getName());
            try {
                File genFile = new File(genDir.getPath()+genDirPath);
                genFile.createNewFile();
                FileHelper.saveFile(genFile, LanguageMapper.translate(FileHelper.readFile(file)));
                javaFiles.add(genFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return javaFiles;
    }

    private File assembleFiles(File genDir, File mainClass, List<File> files, DataCallback<String> callback){
        String[] fs = new String[files.size()];
        int i = 0;
        for (File file : files){
            fs[i] = file.getPath();
            i++;
        }
        String appDirPath = mainClass.getParent();
        String genDirPath = appDirPath.substring(appDirPath.indexOf(ProjectConfigurationController.APP_DIR) + ProjectConfigurationController.APP_DIR.length());
        genDirPath = LanguageMapper.translateDirName(genDirPath)+"/"+LanguageMapper.translateFileName(mainClass.getName());
        File mainGenClass = new File(genDir.getPath()+genDirPath);
        ConsoleHelper.make("javac " + String.join("\n ", fs), callback);
        return mainGenClass;
    }

    private void run(File mainClass, DataCallback<String> callback){
        String name = getClassName(mainClass);
        String path = getClassPath(mainClass);
        ConsoleHelper.make("java -classpath " + path +" " + name, callback);
    }

    private void build(File mainClass, DataCallback<String> callback, List<File> javaFiles){
        String name = getClassName(mainClass);
        String path = getClassPath(mainClass);
        File file = FileHelper.createFile(new File(path), "MANIFEST.MF");
        FileHelper.saveFile(file, "Manifest-version: 1.0\nMain-Class: "+name+"\n");

        String[] fs = new String[javaFiles.size()];
        int i = 0;
        for (File file1 : javaFiles){
            fs[i] = file1.getParent()+"/"+file1.getName().replace(".java",".class");
            i++;
        }

        String command = "jar -cvfm app.jar "+path+"/MANIFEST.MF -C "+path+" . ";
        System.out.println(command);
        ConsoleHelper.make(command, callback);
    }

    private void getAllCreateAppClasses(final File folder, Set<File> files, Set<File> dirs, String fileClassEndWord) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                dirs.add(fileEntry);
                getAllCreateAppClasses(fileEntry, files, dirs, fileClassEndWord);
            } else {
                if (fileEntry.getName().contains(fileClassEndWord)){
                    files.add(fileEntry);
                }
            }
        }
    }

    private String getClassName(File mainClass){
        String name = mainClass.getName();
        name = name.replace(".java", "");
        String fileContent = FileHelper.readFile(mainClass);
        if (fileContent.contains("package")){
            int startPos = fileContent.indexOf("package ") + "package ".length();
            for (int i = startPos; i < fileContent.length(); i++){
                if (fileContent.charAt(i) == ';'){
                    String packagePath = fileContent.substring(startPos, i);
                    name = packagePath+"."+name;
                    break;
                }
            }
        }
        return name;
    }

    private String getClassPath(File mainClass){
        return mainClass.getPath().substring(0, mainClass.getPath().indexOf(ProjectConfigurationController.GEN_SOURCE_DIR) + ProjectConfigurationController.GEN_SOURCE_DIR.length());
    }
}
