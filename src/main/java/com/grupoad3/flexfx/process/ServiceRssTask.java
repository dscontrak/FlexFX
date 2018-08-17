/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.process;

import com.grupoad3.flexfx.db.model.Rss;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author daniel
 */
public class ServiceRssTask extends Service<Rss> {

    private StringProperty url = new SimpleStringProperty(this, "url");

    public final void setUrl(String value) {
        url.set(value);
    }

    public final String getUrl() {
        return url.get();
    }

    public final StringProperty urlProperty() {
        return url;
    }

    @Override
    protected Task<Rss> createTask() {
        final String _url = getUrl();
        
        return new Task<Rss>() {
            @Override
            protected Rss call() throws Exception {
                Rss rss = new Rss();
                
                updateMessage("Starting... at:" + System.currentTimeMillis());
                URL u = new URL(_url);
                /*BufferedReader in = new BufferedReader(
                        new InputStreamReader(u.openStream()));
                String result = in.readLine();
                in.close();*/
                
                SyndFeed feed = new SyndFeedInput().build(new InputStreamReader(u.openStream()));
                
                rss.setTitle(feed.getTitle());
                rss.setDescription(feed.getDescription());
                rss.setLinkrss(_url);
                rss.setLinkweb(feed.getLink());
                
                feed.getEntries().stream().forEach(e -> {
                    SyndEntryImpl entry = (SyndEntryImpl)e;
                    System.out.println(entry);
                });
                //System.out.println(((SyndEntryImpl)feed.getEntries().get(0)).getPublishedDate());
                
                updateMessage("Ending... at:" + System.currentTimeMillis());
                Thread.sleep(1000);
                return rss;
            }
            
        };
    }

}
