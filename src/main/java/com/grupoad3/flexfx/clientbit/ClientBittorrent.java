/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.clientbit;

/**
 *
 * @author daniel_serna
 */
public abstract class ClientBittorrent implements IClientBit{
    protected static String session;
    
    protected String user;
    protected String pass;   
    protected String url;
    protected String pathToSave;
    protected boolean addPauseState;
    

    public ClientBittorrent(String url) {        
        this.url = url;
        addPauseState = true;
    }    

    public String getPathToSave() {
        return pathToSave;
    }

    public void setPathToSave(String pathToSave) {
        this.pathToSave = pathToSave;
    }        
    

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isAddPauseState() {
        return addPauseState;
    }

    public void setAddPauseState(boolean addPauseState) {
        this.addPauseState = addPauseState;
    }
        
    
        
    /**
     * Erase session data
     * @return 
     */
    protected String logout(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
    
}
