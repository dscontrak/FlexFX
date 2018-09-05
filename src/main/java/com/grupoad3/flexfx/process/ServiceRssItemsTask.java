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
import com.grupoad3.flexfx.db.services.MediaFilterService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
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

    final String errorStatus = ItemStatus.ERROR.toString();
    final String downStatus = ItemStatus.DOWNLOADED.toString();
    final String ignoreStatus = ItemStatus.IGNORED.toString();

    private final BooleanProperty proxyuse = new SimpleBooleanProperty(this, "proxyuse", false);
    private final IntegerProperty proxyport = new SimpleIntegerProperty(this, "proxyport");
    private final StringProperty proxyhost = new SimpleStringProperty(this, "proxyhost");

    private Rss rss;
    //private List<MediaFilters> filters;
    private String path;

    public final void setRss(Rss value) {
        rss = value;
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
    protected Task<List<RssItems>> createTask() {
        final Rss _rss = rss;
        final String _path = path;

        return new Task<List<RssItems>>() {
            @Override
            protected List<RssItems> call() throws Exception {

                if (_rss == null || _rss.getLinkrss() == null || _rss.getLinkrss().isEmpty()) {
                    updateMessage("Link Rss is empty");
                    Thread.sleep(2000);
                    return null;
                }

                if (_path == null || _path.isEmpty()) {
                    updateMessage("Path is empty");
                    Thread.sleep(2000);
                    return null;
                }

                // Data and rss
                HashSet<RssItems> itemsAll = new HashSet<>();
                TreeSet<RssItems> itemsToAdd = new TreeSet<>(new ComparatorRssDate());

                String proxyHost;
                Integer proxyPort;
                HttpHost proxyHostObject = null;
                HttpClient client;

                // Database
                RssItemService itemService;
                MediaFilterService filterService;
                List<RssItems> lastItems;
                List<MediaFilters> filters;

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
                    lastItems = itemService.getLastAlltemsByRss(_rss, feed.getEntries().size(), 0, false);
                    lastItems.forEach(i -> {
                        itemsAll.add(i);
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

                        itemsAll.add(item);

                    }

                    // Get all filters
                    filterService = new MediaFilterService();
                    filters = filterService.getAllActiveByRss(rss);

                    int indexItem = 1;
                    // Analize data wiht local and remote data
                    for (RssItems itemToAnalize : itemsAll) {

                        updateMessage("Analyzing " + indexItem + "/" + itemsAll.size());
                        indexItem++;
                        //updateMessage(_path);
                        if (anlyzeRssItem(itemToAnalize, filters) != null) {
                            itemsToAdd.add(itemToAnalize);
                        }
                    }

                    indexItem = 1;
                    for (RssItems itemToAnalize : itemsToAdd) {
                        updateMessage("Save " + indexItem + "/" + itemsToAdd.size());
                        indexItem++;
                        downloadAndSave(itemToAnalize, itemService);
                    }
                    updateMessage("Terminated... ");
                    Thread.sleep(700);

                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                return new ArrayList<>(itemsToAdd);
            }

            private void downloadAndSave(RssItems itemToAnalize, RssItemService db) {

                // if has been donwloaded return
                if (itemToAnalize.getStatus().equals(downStatus)) {
                    return;
                }

                if (itemToAnalize.isApplyMainFilter()
                        && itemToAnalize.isApplySecondFilter()) {
                    itemToAnalize.setStatus(ItemStatus.DOWNLOADED);
                }

                if(itemToAnalize.isApplyMainFilter() && itemToAnalize.isApplyIgnoreFilter()){
                    itemToAnalize.setStatus(ItemStatus.IGNORED);
                }

                if (itemToAnalize.getStatus().equals(downStatus)) {

                    String url = itemToAnalize.getLink();
                    String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

                    DownloadFileHttpCilent downloadFile = new DownloadFileHttpCilent(url, path + "/" + fileName);
                    try {
                        downloadFile.download();
                    } catch (IOException ex) {
                        Logger.getLogger(ServiceRssItemsTask.class.getName()).log(Level.SEVERE, null, ex);
                        itemToAnalize.setStatus(ItemStatus.ERROR);
                    }
                    itemToAnalize.setFile(fileName);
                    itemToAnalize.setDatedown(LocalDateTime.now());

                }

                try {
                    if (itemToAnalize.isOriginRss()) {
                        db.create(itemToAnalize);
                    } else if(itemToAnalize.getStatus().equals(downStatus) || itemToAnalize.getStatus().equals(errorStatus)) {
                        db.update(itemToAnalize);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServiceRssItemsTask.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private RssItems anlyzeRssItem(RssItems item, List<MediaFilters> _filters) {

                //String url = item.getLink();
                //String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
                //String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
                try {

                    // With error
                    if (item.getStatus().equals(errorStatus)) {
                        //db.create(item);
                        return item;
                    }
                    // Is downloaded from DATABASE
                    if (item.getStatus().equals(downStatus)) {
                        return item;
                    }

                    _filters.forEach(filter -> {
                        //boolean applyMainFilter = false;
                        //boolean applySecondaryFilter = false;

                        if (item.isApplyMainFilter()) {
                            return; // Continue
                        }

                        // Main filter
                        if (filter.getFiltermain().isEmpty()) {
                            return; // Continue
                        } else if (CompareUtil.containsIgnoreCase(item.getTitle(), filter.getFiltermain())) {
                            item.setApplyMainFilter(true);
                            item.setMediafilter(filter);
                        }

                        // Secondary Filter
                        if (filter.getFiltersecondary().isEmpty()) {
                            item.setApplySecondFilter(true);
                        } else if (CompareUtil.containsIgnoreCase(item.getTitle(), filter.getFiltersecondary())) {
                            item.setApplySecondFilter(true);
                        }

                        // Ignored filter
                        if (filter.getFilterignore() != null
                                && filter.getFilterignore().isEmpty() == false
                                && CompareUtil.containsIgnoreCase(item.getTitle(), filter.getFilterignore())) {
                            item.setApplyIgnoreFilter(true);
                        }


                    });

                } catch (Exception e) {
                    item.setStatus(ItemStatus.ERROR);
                    e.printStackTrace();
                }

                if (item.isApplyMainFilter()) {
                    return item;
                } else {
                    return null;
                }

            }

        };
    }

}
