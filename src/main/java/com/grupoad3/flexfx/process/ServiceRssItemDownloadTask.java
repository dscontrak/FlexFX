/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Info: https://www.manthanhd.com/2015/08/24/configuring-java-httpclient-to-use-proxy/
 */
package com.grupoad3.flexfx.process;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.RssItemService;
import com.grupoad3.flexfx.util.DownloadFileHttpCilent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author daniel
 */
public class ServiceRssItemDownloadTask extends Service<Boolean> {

    final String errorStatus = ItemStatus.ERROR.toString();
    final String downStatus = ItemStatus.DOWNLOADED.toString();
    final String ignoreStatus = ItemStatus.IGNORED.toString();

    private final BooleanProperty proxyuse = new SimpleBooleanProperty(this, "proxyuse", false);
    private final IntegerProperty proxyport = new SimpleIntegerProperty(this, "proxyport");
    private final StringProperty proxyhost = new SimpleStringProperty(this, "proxyhost");

    private RssItems rssItem;
    //private List<MediaFilters> filters;
    private String path;

    public final void setRssItem(RssItems value) {
        rssItem = value;
    }

    /*public void setFilters(List<MediaFilters> filters) {
        this.filters = filters;
    }*/

    public void setPath(String path) {
        this.path = path;
    }

    public final void setProxyuse(Boolean value) {
        proxyuse.set(value);
    }

    public final void setProxyhost(String value) {
        proxyhost.set(value);
    }

    public final void setProxyport(Integer value) {
        proxyport.set(value);
    }

    public final Boolean getProxyuse() {
        return proxyuse.get();
    }

    public final String getProxyhost() {
        return proxyhost.get();
    }

    public final Integer getProxyport() {
        return proxyport.get();
    }

    @Override
    protected Task<Boolean> createTask() {
        final RssItems _item = rssItem;
        final String _path = path;

        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                if (_item == null || _item.getLink() == null || _item.getLink().isEmpty()) {
                    updateMessage("Link Rss Item is empty");
                    Thread.sleep(2000);
                    return false;
                }

                if (_path == null || _path.isEmpty()) {
                    updateMessage("Path is empty");
                    Thread.sleep(2000);
                    return false;
                }
                              
              
                //HttpGet getRequest = new HttpGet(_item.getLinkrss());
                updateMessage("Downloading... ");
                downloadAndSave(_item);
                updateMessage("Terminated... ");
                Thread.sleep(700);

                return true;
            }

            private void downloadAndSave(RssItems itemToAnalize) throws IOException {
                RssItemService db = new RssItemService();
                // if has been donwloaded return
                if (itemToAnalize.getStatus().equals(downStatus)) {
                    return;
                }               

                String url = itemToAnalize.getLink();
                String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
                File fileToSave = null;
                
                if(DownloadFileHttpCilent.isFilenameValid(fileName) == false){
                    fileToSave = File.createTempFile(itemToAnalize.getMediafilter().getFiltermain(), ".torrent", new File(path));                        
                }else{
                    fileToSave = new File(path + "/" + fileName);
                }

                DownloadFileHttpCilent downloadFile = new DownloadFileHttpCilent(url, fileToSave);
                
                try {
                    downloadFile.download();
                } catch (IOException ex) {
                    Logger.getLogger(ServiceRssItemDownloadTask.class.getName()).log(Level.SEVERE, null, ex);
                    itemToAnalize.setStatus(ItemStatus.ERROR);
                }
                
                itemToAnalize.setFile(fileToSave.getName());
                itemToAnalize.setDatedown(LocalDateTime.now());

                try {                    
                    db.update(itemToAnalize);                    
                } catch (IOException ex) {
                    Logger.getLogger(ServiceRssItemDownloadTask.class.getName()).log(Level.SEVERE, null, ex);
                }

            }            

        };
    }

}
