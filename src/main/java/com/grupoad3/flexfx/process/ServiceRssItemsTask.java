/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Info: https://www.manthanhd.com/2015/08/24/configuring-java-httpclient-to-use-proxy/
 */
package com.grupoad3.flexfx.process;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.RssItemService;
import com.grupoad3.flexfx.util.ComparatorRssDate;
import com.grupoad3.flexfx.util.CompareUtil;
import com.grupoad3.flexfx.util.ConvertionUtil;
import com.grupoad3.flexfx.util.DownloadFileHttpCilent;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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
    private List<MediaFilters> filters;
    private String path;

    public final void setRss(Rss value) {
        rss = value;
    }

    public void setFilters(List<MediaFilters> filters) {
        this.filters = filters;
    }

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
    protected Task<List<RssItems>> createTask() {
        final Rss _rss = rss;
        final List<MediaFilters> _filters = filters;
        final String _path = path;

        return new Task<List<RssItems>>() {
            @Override
            protected List<RssItems> call() throws Exception {

                if (_rss == null || _rss.getLinkrss() == null || _rss.getLinkrss().isEmpty()) {
                    updateMessage("Link Rss is empty");
                    Thread.sleep(2000);
                    return null;
                }

                if(_path == null || _path.isEmpty()){
                    updateMessage("Path is empty");
                    Thread.sleep(2000);
                    return null;
                }

                // Data and rss
                TreeSet<RssItems> items = new TreeSet<>(new ComparatorRssDate());
                String proxyHost;
                Integer proxyPort;
                HttpHost proxyHostObject = null;
                HttpClient client;

                // Database
                RssItemService itemService;
                List<RssItems> lastItems;

                updateMessage("Reading rss... ");

                if (getProxyuse()) {
                    proxyHost = getProxyhost();
                    proxyPort = getProxyport();
                    proxyHostObject = new HttpHost(proxyHost, proxyPort);
                    client = HttpClientBuilder.create().setProxy(proxyHostObject).build();

                } else {
                    client = HttpClientBuilder.create().build();
                }

                HttpGet getRequest = new HttpGet(_rss.getLinkrss());

                try {



                    // Get data from RSS reading
                    HttpResponse response = client.execute(getRequest);

                    InputStream stream = response.getEntity().getContent();
                    SyndFeedInput input = new SyndFeedInput();
                    SyndFeed feed = input.build(new XmlReader(stream));

                    if (feed.getEntries() == null || feed.getEntries().isEmpty()) {
                        updateMessage("Empty Rss!! ");
                        Thread.sleep(2000);
                        return null;
                    }

                    // Get data from database and add rss
                    itemService = new RssItemService();
                    lastItems = itemService.getLastItemsByRss(_rss, feed.getEntries().size(), 0);
                    lastItems.forEach(i -> {
                        items.add(i);
                    });

                    // Add rss from rss reading
                    for (Object o : feed.getEntries()) {

                        SyndEntryImpl entry = (SyndEntryImpl) o;
                        RssItems item = new RssItems();
                        item.setOriginRss(true);
                        item.setLink(entry.getLink());
                        item.setTitle(entry.getTitle());
                        item.setDatepub(ConvertionUtil.convertToLocalDateTime(entry.getPublishedDate()));

                        item.setRss(_rss);

                        if (entry.getLink() == null || entry.getLink().isEmpty()) {
                            item.setStatus(ItemStatus.ERROR);
                            continue;
                        } else {
                            item.setGuid(String.valueOf(item.hashCode()));
                            item.setStatus(ItemStatus.IGNORED);
                        }

                        items.add(item);

                    }


                    int indexItem = 1;
                    // Analize data wiht local and remote data
                    for (RssItems itemToAnalize : items) {
                        updateMessage("Analyzing " + indexItem + "/" + items.size());
                        indexItem++;
                        //updateMessage(_path);
                        anlyzeRssItem(itemService, itemToAnalize, _filters);
                    }


                    updateMessage("Terminated... ");
                    Thread.sleep(700);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return new ArrayList<>(items);
            }

            private void anlyzeRssItem(RssItemService db, RssItems item, List<MediaFilters> _filters)  {


                String url = item.getLink();
                String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
                //String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

                try {
                    String errorStatus = ItemStatus.ERROR.toString();
                    String downStatus = ItemStatus.DOWNLOADED.toString();
                    // With error
                    if (item.getStatus().equals(errorStatus)) {
                        db.create(item);
                        return;
                    }
                    // Is downloaded from DATABASE
                    if(item.getStatus().equals(downStatus)){
                        return;
                    }

                    _filters.forEach(filter -> {
                        boolean applyMainFilter = false;
                        boolean applySecondaryFilter = false;

                        if (filter.getFiltermain().isEmpty()) {
                            return; // Continue
                        }else if(CompareUtil.containsIgnoreCase(item.getTitle(), filter.getFiltermain())){
                            applyMainFilter = true;
                        }

                        if (filter.getFiltersecondary().isEmpty()) {
                            applySecondaryFilter = true;
                        }else if(CompareUtil.containsIgnoreCase(item.getTitle(), filter.getFiltersecondary())){
                            applySecondaryFilter = true;
                        }

                        if(applyMainFilter && applySecondaryFilter){
                            item.setChangedStatus(true);
                            item.setStatus(ItemStatus.DOWNLOADED);
                        }

                    });

                    if(item.getStatus().equals(downStatus)){
                        DownloadFileHttpCilent downloadFile = new DownloadFileHttpCilent(url, path + "/" + fileName);
                        downloadFile.download();
                        item.setFile(fileName);
                    }

                    if(item.isChangedStatus() && item.isOriginRss() == false){
                        db.update(item);
                    }else if(item.isOriginRss()){
                        db.create(item);
                    }

                } catch (Exception e) {
                    try {
                        item.setStatus(ItemStatus.ERROR);
                        db.create(item);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }

            }

        };
    }

}
