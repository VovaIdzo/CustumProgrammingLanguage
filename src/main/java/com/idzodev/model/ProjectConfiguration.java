package com.idzodev.model;

import com.idzodev.utils.FileHelper;

import java.io.File;
import java.util.List;

import static com.idzodev.controller.ProjectConfigurationController.APP_MAIN_CLASS;

/**
 * Created by vladimir on 22.05.16.
 */
public class ProjectConfiguration {
    File appDir;
    File genSourceDir;
    File genComplileDir;
    File conf;

    public ProjectConfiguration(File appDir, File genSourceDir, File genComplileDir, File conf) {
        this.appDir = appDir;
        this.genSourceDir = genSourceDir;
        this.genComplileDir = genComplileDir;
        this.conf = conf;
    }

    public File getAppDir() {
        return appDir;
    }

    public File getGenSourceDir() {
        return genSourceDir;
    }

    public File getGenComplileDir() {
        return genComplileDir;
    }

    public void setGenComplileDir(File genComplileDir) {
        this.genComplileDir = genComplileDir;
    }

    public File getConf() {
        return conf;
    }

    public File getMainClassFile() {
        return findMainClass();
    }

    private File findMainClass(){
        File file = getConf();
        String conf = FileHelper.readFile(file);
        int index = conf.indexOf(APP_MAIN_CLASS);
        if (index > 0){
            conf = conf.substring(index + APP_MAIN_CLASS.length() + 1);
            conf = conf.replace("\n","");
            File mainClassFile = new File(conf);
            if (mainClassFile.exists()){
                return mainClassFile;
            }
        }
        return null;
    }
}
