/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx;

import static com.grupoad3.flexfx.ConfigApp.ConfigTypes.FOLDER_DOWLOAD;
import static com.grupoad3.flexfx.ConfigApp.ConfigTypes.ISMIGRATED;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author daniel_serna
 */
public class ConfigApp {

    public enum ConfigTypes {
        ISMIGRATED("app.ismigrated", "false"),
        FOLDER_DOWLOAD("app.folder", "./downloads");

        private final String nameProperty;
        private final String valueDefault;

        /**
         * @param text
         */
        ConfigTypes(String name, String value) {
            this.nameProperty = name;
            this.valueDefault = value;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return nameProperty;
        }

        public String defaultVal() {
            return valueDefault;
        }

        public String nameProp() {
            return nameProperty;
        }
    }

    private final File file;
    private final String comments = "Delete to restart properties or edit to change manually";
    private final boolean isCreatedNewFile;
    //private final File configFile; 
    public static Properties properties = new Properties();
    private static boolean isLoadedProperies = false;

    public ConfigApp() throws IOException {

        file = new File("./config.properties");
        if (file.exists()) {
            isCreatedNewFile = false;
        } else {
            file.createNewFile();
            isCreatedNewFile = true;
        }

    }

    public static String readProperty(ConfigTypes typeConfig) throws Exception {
        String key;

        if (isLoadedProperies == false) {
            throw new Exception("Properties is not loaded");
        }

        // check if properties file contains a key
        key = typeConfig.nameProp();
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else {
            throw new Exception("Dont found config value with key: " + key);
        }
    }

    public boolean isCreatedNewFile() {
        return isCreatedNewFile;
    }

    public void setup() throws Exception {
        Properties prop = new Properties();
        FileOutputStream fos = null;

        if (isCreatedNewFile == false) {
            return;
        }

        try {

            // create new file in or open existing file from the project's root folder
            fos = new FileOutputStream(file);

            // set properties
            prop.put(ISMIGRATED.nameProp(), ISMIGRATED.defaultVal());
            prop.put(FOLDER_DOWLOAD.nameProp(), FOLDER_DOWLOAD.defaultVal());

            // store properties to the opened file
            prop.store(fos, comments);

        } catch (IOException ex) {
            throw new Exception("Error read file: " + file.getAbsolutePath() + "  \nError: " + ex.getMessage());
        }

    }

    public void loadProperties() throws Exception {

        //Properties prop = new Properties();
        FileInputStream fis = null;

        try {

            // open existing file from the project's root folder
            fis = new FileInputStream(file);

            // load properties
            properties.clear();
            properties.load(fis);
            isLoadedProperies = true;

        } catch (IOException io) {
            io.printStackTrace();
            throw new Exception("Error read file: " + file.getAbsolutePath() + "  \nError: " + io.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void writeProperty(ConfigTypes typeConfig, String value) throws Exception {

        FileOutputStream out;
        FileInputStream in = new FileInputStream(file);
        Properties props = new Properties();
        // Load 
        props.load(in);
        in.close();
        // Stream        
        out = new FileOutputStream(file);
        props.setProperty(typeConfig.nameProp(), value);
        // Save
        props.store(out, comments);
        out.close();

    }

}
