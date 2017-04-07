package com.idzodev.controller;

import com.idzodev.model.ProjectConfiguration;
import com.idzodev.utils.FileHelper;

import java.io.File;

/**
 * Created by vladimir on 22.05.16.
 */
public class ProjectConfigurationController {
    public static final String CONF_FILE = "jua.conf";
    public static final String GEN_DIR = "згенерований_код";
    public static final String GEN_SOURCE_DIR = "джава_код";
    public static final String GEN_COMPILE_DIR = "скомпільований_код";
    public static final String APP_DIR = "програмний_код";
    public static final String APP_MAIN_CLASS = "назва_головного_початкового_класу";

    private ProjectConfiguration projectConfiguration;

    public void initProjectConfiguration(File rootProjectDir){
        if (!rootProjectDir.isDirectory())
            return;

        File projectConfigFile = null;
        File projectGenDir = null;
        File projectAppDir = null;
        File projectGenSourceDir = null;
        File projectGenCompileDir = null;

        File[] files = rootProjectDir.listFiles();
        if (files != null){
            for (File file : files){
                if (projectConfigFile == null && file.getName().equals(CONF_FILE)){
                    projectConfigFile = file;
                }

                if (projectGenDir == null && file.getName().equals(GEN_DIR)){
                    projectGenDir = file;
                }

                if (projectAppDir == null && file.getName().equals(APP_DIR)){
                    projectAppDir = file;
                }
            }

            if (projectGenDir != null && projectAppDir != null && projectConfigFile != null){
                File[] genFiles = projectGenDir.listFiles();
                if (genFiles != null){
                    for (File genFile : genFiles){
                        if (projectGenSourceDir == null && genFile.getName().equals(GEN_SOURCE_DIR)){
                            projectGenSourceDir = genFile;
                        }

                        if (projectGenCompileDir == null && genFile.getName().equals(GEN_COMPILE_DIR)){
                            projectGenCompileDir = genFile;
                        }
                    }
                }

                if (projectGenSourceDir == null)
                    projectGenSourceDir = FileHelper.createDir(projectGenDir, GEN_SOURCE_DIR);

                if (projectGenCompileDir == null)
                    projectGenCompileDir = FileHelper.createDir(projectGenDir, GEN_COMPILE_DIR);

                projectConfiguration = new ProjectConfiguration(
                        projectAppDir,
                        projectGenSourceDir,
                        projectGenCompileDir,
                        projectConfigFile
                );
                return;
            }
        }

        projectAppDir = FileHelper.createDir(rootProjectDir, APP_DIR);
        projectGenDir= FileHelper.createDir(rootProjectDir, GEN_DIR);
        projectGenCompileDir = FileHelper.createDir(projectGenDir, GEN_COMPILE_DIR);
        projectGenSourceDir = FileHelper.createDir(projectGenDir, GEN_SOURCE_DIR);
        projectConfigFile = FileHelper.createFile(rootProjectDir, CONF_FILE);

        FileHelper.saveFile(projectConfigFile,
                        APP_DIR+"="+projectAppDir.getPath()+"\n" +
                        GEN_DIR+"="+projectGenDir.getPath()+"\n" +
                        CONF_FILE+"="+projectConfigFile.getPath()+"\n"+
                        APP_MAIN_CLASS+"="
        );

        projectConfiguration = new ProjectConfiguration(
                projectAppDir,
                projectGenSourceDir,
                projectGenCompileDir,
                projectConfigFile
        );
    }

    public ProjectConfiguration getConfiguration(){
        return projectConfiguration;
    }
}
