/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.db;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.MediaType;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.MediaFilterService;
import com.grupoad3.flexfx.db.services.RssItemService;
import com.grupoad3.flexfx.db.services.RssService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author daniel
 */

public class ModelMediaAndItemsCreate {
    
    private Rss rss;    
    final int random;

    public ModelMediaAndItemsCreate() {
        final int Min = 1;
        final int Max = 99999;
        random = Min + (int)(Math.random() * ((Max - Min) + 1));
    }
        
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            RssService rssService = new RssService();
            rssService.getLastRss(10, 0, true).forEach(r -> {
                rss = r;
            });            
            
        } catch (IOException ex) {
            Logger.getLogger(ModelMediaAndItemsCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void rssIteamCreate() {
        try {
           
            RssItems itemCreated;
            
            if(rss == null){
                throw new NullPointerException("Dont exist one Rss to relation");
            }            
            
            RssItemService rssItemService = new RssItemService();            
            RssItems item;
            
            item = new RssItems();
            item.setTitle("titulo" + random);
            item.setGuid("guid" + random);
            item.setRss(rss);
            item.setStatus(ItemStatus.DOWNLOADED);
            item.setLink("link"+random);
            
            itemCreated = rssItemService.create(item);
            
            Assert.assertTrue(itemCreated != null);
        } catch (IOException ex) {
            Logger.getLogger(ModelMediaAndItemsCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @Test
    public void rssMediaCreate() {
        try {
            MediaFilters mediaCreated = null;
            if(rss == null){
                throw new NullPointerException("Dont exist one Rss to relation");
            }  
            
            MediaFilterService mediaService = new MediaFilterService();
            MediaFilters media = new MediaFilters();
            
            media.setTitle("titulo" + random);
            media.setRss(rss);
            media.setFiltermain("filter_main" + random);
            media.setCategory(MediaType.SERIE);
            
            mediaCreated = mediaService.create(media);
            
            Assert.assertTrue(mediaCreated != null);
            
        } catch (IOException ex) {
            Logger.getLogger(ModelMediaAndItemsCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
}
