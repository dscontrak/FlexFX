/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.db;

import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.services.RssService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author daniel_serna
 */
public class ModelRssCreate {
    
    
    final int random;

    public ModelRssCreate() {
        final int Min = 1;
        final int Max = 99999;
        random = Min + (int)(Math.random() * ((Max - Min) + 1));
    }
    
    @Test
    public void createRss(){
        try {
            Rss rss = new Rss();
            
            RssService rssService = new RssService();
            
            rss.setTitle("titulo" + random);
            rss.setLinkrss("link" + random);
            rss.setPubdate(LocalDateTime.now());
            
            //rssCreated = rssService.create(rss);
            
            Assert.assertTrue(rssService.create(rss) != null);            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ModelRssCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
