/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.clientbit;

import com.grupoad3.flexfx.ConfigApp;
import com.grupoad3.flexfx.db.model.ClientAppTorrentType;
import java.io.File;

/**
 *
 * @author daniel_serna
 */
public class ManageClientBit {

    private final String url;
    
    private final String user;
    private final String pass;
    
    private final String directoryFile;
    private final ClientAppTorrentType app;

    private ClientBittorrent cliente;
    private String currentSession;

    public ManageClientBit() throws Exception {
        
        StringBuilder urlConfig = new StringBuilder("http://");

        String host = ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_HOST);
        String port = ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_PORT);
        
        user = ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_USER);
        pass = ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_PASS);
        
        directoryFile = ConfigApp.readProperty(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD);
        app = ClientAppTorrentType.valueOf(ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_APP));
        
        // Create url with api
        urlConfig.append(host);
        if (port.isEmpty() == false) {
            urlConfig.append(":");
            urlConfig.append(port);
        }
        
        url = urlConfig.toString();
        
    }
    
    public String getSession() throws ClientBitorrentException, Exception{
        
        if(currentSession != null && currentSession.isEmpty() == false){
            return currentSession;
        }
        
        switch (app) {
            case QBITTORRENT41:
                cliente = new Qbittorrent41(url);
                return currentSession = cliente.login(user, pass);                
            case QBITTORRENT32:
                cliente = new Qbittorrent41(url);
                return currentSession = cliente.login(user, pass);                
            default:
                throw new ClientBitorrentException("Client not supported");
        }
    }

    public void sendFile(String fileName, String pathToSave) throws ClientBitorrentException, Exception {
        
        File file;

        getSession();
        
        if (currentSession == null || currentSession.length() <= 1) {
            throw new ClientBitorrentException("Config client is not correct");
        }

        file = new File(directoryFile + "/" + fileName);
        if (file.exists() == false) {
            throw new ClientBitorrentException("File dont exist anymore");
        }

        if (pathToSave != null && pathToSave.isEmpty() == false) {
            cliente.setPathToSave(pathToSave);
        }

        cliente.addTorrent(file);

    }

}
