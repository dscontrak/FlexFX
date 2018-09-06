/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.clientbit;

import java.io.File;
import java.net.URL;

/**
 *
 * @author daniel_serna
 */
public interface IClientBit {
    /**
     * Add file to download by bittorrent client
     * @param file
     * @return 
     * @throws java.lang.Exception 
     */    
    public String addTorrent(File file)  throws Exception;
    
    /**
     * Add url to download by bittorrent client
     * @param file
     * @return 
     * @throws java.lang.Exception 
     */
    public String addTorrent(URL file) throws Exception;    
    
    /**
     * If is necesary login and set session data
     * @param user
     * @param pass
     * @return sssion data
     * @throws java.lang.Exception
     */
    public String login(String user, String pass) throws Exception;
}
