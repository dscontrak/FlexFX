/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx;

import com.grupoad3.flexfx.ConfigApp.ConfigTypes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author daniel_serna
 */
public class ConfigApp {

    public static enum ConfigTypes {
        ISMIGRATED("app.ismigrated", "false"),
        
        FOLDER_DOWNLOAD("app.folder", "./downloads"),
        
        PROXY_USE("app.proxy.use", "false"),
        PROXY_HOST("app.proxy.host", "10.10.10.10"),
        PROXY_PORT("app.proxy.port", "1000"),
        
        CLIENTTORR_USE("torrent.client.use", "false"),
        CLIENTTORR_HOST("torrent.client.host", "127.0.0.1"),
        CLIENTTORR_PORT("torrent.client.port", "80"),
        CLIENTTORR_USER("torrent.client.user", "user"),
        CLIENTTORR_PASS("torrent.client.pass", "pass"),
        CLIENTTORR_APP("torrent.client.app", "QBITTORRENT")
        ;

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
    
    public static Map<String,String> readAllProperties() throws Exception {        
        Map<String,String> valuesProperties = new HashMap<>();

        if (isLoadedProperies == false) {
            throw new Exception("Properties is not loaded");
        }

        for (ConfigTypes value : ConfigTypes.values()) {
                valuesProperties.put(value.nameProp(), properties.getProperty(value.nameProp()));
        } 
        
        return valuesProperties;
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
            /*prop.put(ConfigTypes.ISMIGRATED.nameProp(), ConfigTypes.ISMIGRATED.defaultVal());
            prop.put(ConfigTypes.FOLDER_DOWLOAD.nameProp(), ConfigTypes.FOLDER_DOWLOAD.defaultVal());
            prop.put(ConfigTypes.PROXY_USE.nameProp(), ConfigTypes.PROXY_USE.defaultVal());
            prop.put(ConfigTypes.PROXY_HOST.nameProp(), ConfigTypes.PROXY_HOST.defaultVal());
            prop.put(ConfigTypes.PROXY_PORT.nameProp(), ConfigTypes.PROXY_PORT.defaultVal());            */
            
            for (ConfigTypes value : ConfigTypes.values()) {
                prop.put(value.nameProp(), value.defaultVal());
            }
            

            // store properties to the opened file
            prop.store(fos, comments);

        } catch (IOException ex) {
            throw new Exception("Error read file: " + file.getAbsolutePath() + "  \nError: " + ex.getMessage());
        }

    }
    
    public void writeAllProperties(Map<String,String> valuesProp) throws Exception {
        Properties prop = new Properties();
        FileOutputStream fos = null;      
        
        // Order by key
        SortedSet<String> keys = new TreeSet<>(valuesProp.keySet());

        try {

            // create new file in or open existing file from the project's root folder
            fos = new FileOutputStream(file);                                
            
            /*for (Map.Entry<String, String> entry : valuesProp.entrySet())
            {                
                prop.setProperty(entry.getKey(), entry.getValue());
            }*/
            
            for (String key : keys) {
                prop.setProperty(key, valuesProp.get(key));                
            }
            
            
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
