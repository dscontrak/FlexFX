/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Info: https://www.manthanhd.com/2015/08/24/configuring-java-httpclient-to-use-proxy/
 */
package com.grupoad3.flexfx.process;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.util.ConvertionUtil;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author daniel
 */
public class ServiceRssItemsTask extends Service<List<RssItems>> {



    private final BooleanProperty proxyuse = new SimpleBooleanProperty(this, "proxyuse", false);
    private final IntegerProperty proxyport = new SimpleIntegerProperty(this, "proxyport");
    private final StringProperty proxyhost = new SimpleStringProperty(this, "proxyhost");

    private Rss rss;

    public final void setRss(Rss value) {
        rss = value;
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
    protected Task<List<RssItems>> createTask() {
        final Rss _rss = rss;

        if(_rss == null || _rss.getLinkrss() == null || _rss.getLinkrss().isEmpty()){
            return null;
        }

        return new Task<List<RssItems>>() {
            @Override
            protected List<RssItems> call() throws Exception {
                List<RssItems> items = new ArrayList<>();
                String proxyHost;
                Integer proxyPort;
                HttpHost proxyHostObject = null;
                HttpClient client;

                updateMessage("Reading rss... ");

                if(getProxyuse()){
                    proxyHost = getProxyhost();
                    proxyPort = getProxyport();
                    proxyHostObject = new HttpHost(proxyHost, proxyPort);
                    client = HttpClientBuilder.create().setProxy(proxyHostObject).build();

                }else{
                    client = HttpClientBuilder.create().build();
                }


                HttpGet getRequest = new HttpGet(_rss.getLinkrss());

                try {

                    HttpResponse response = client.execute(getRequest);

                    InputStream stream = response.getEntity().getContent();
                    SyndFeedInput input = new SyndFeedInput();
                    SyndFeed feed = input.build(new XmlReader(stream));
                    int indexEntries = 1;
                    for (Object o : feed.getEntries()) {
                        updateMessage("Analyzing " + indexEntries + "/" + feed.getEntries().size());
                        indexEntries++;

                        SyndEntryImpl entry = (SyndEntryImpl)o;
                        RssItems item = new RssItems();
                        item.setLink(entry.getLink());
                        item.setTitle(entry.getTitle());
                        item.setDatepub( ConvertionUtil.convertToLocalDateTime(entry.getPublishedDate()) );

                        item.setRss(_rss);

                        if(entry.getLink() == null || entry.getLink().isEmpty()){
                            item.setStatus(ItemStatus.ERROR);
                            continue;
                        }else{
                            item.setGuid(String.valueOf(entry.getLink().hashCode()));
                            item.setStatus(ItemStatus.IGNORED);
                        }

                        items.add(item);

                    }



                    updateMessage("Terminated... ");
                    Thread.sleep(700);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                return items;
            }

        };
    }

}
