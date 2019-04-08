/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author daniel
 */
public class OpenFile {

    // https://stackoverflow.com/questions/325299/cross-platform-way-to-open-a-file-using-java-1-5

    private File fileToOpen;

    public OpenFile(File fileToOpen) throws IOException {
        if(fileToOpen.exists() == false){
            throw new IOException("File dont exist anymore");
        }

        this.fileToOpen = fileToOpen;
    }

    public void openWithProcess() throws IOException{
        Desktop desktop = Desktop.getDesktop();
        if(isLinux()){
            Runtime.getRuntime().exec("xdg-open "+ fileToOpen.getAbsolutePath());
        }else if(isMac()){
            Runtime.getRuntime().exec("open "+ fileToOpen.getAbsolutePath());
        }else if(isWindows()){
            //Runtime.getRuntime().exec("command.com /C start "+ fileToOpen.getAbsolutePath());
            desktop.open(fileToOpen);
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("windows") || os.contains("nt");
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("linux");
    }

    public static boolean isWindows9X() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.equals("windows 95") || os.equals("windows 98");
    }

}
