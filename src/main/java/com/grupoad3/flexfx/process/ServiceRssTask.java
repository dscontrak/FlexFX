/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.process;

import com.grupoad3.flexfx.db.model.Rss;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.io.InputStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author daniel
 */
public class ServiceRssTask extends Service<Rss> {

    private StringProperty url = new SimpleStringProperty(this, "url");
    private BooleanProperty proxyuse = new SimpleBooleanProperty(this, "proxyuse", false);
    private IntegerProperty proxyport = new SimpleIntegerProperty(this, "proxyport");
    private StringProperty proxyhost = new SimpleStringProperty(this, "proxyhost");
    

    public final void setUrl(String value) {
        url.set(value);
    }

    public final String getUrl() {
        return url.get();
    }

    public final StringProperty urlProperty() {
        return url;
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
    protected Task<Rss> createTask() {
        final String _url = getUrl();

        return new Task<Rss>() {
            @Override
            protected Rss call() throws Exception {
                Rss rss = new Rss();
                String proxyHost;
                Integer proxyPort;
                HttpHost proxyHostObject = null;
                HttpClient client;
                
                updateMessage("Starting... at:" + System.currentTimeMillis());
                
                //Info: https://www.manthanhd.com/2015/08/24/configuring-java-httpclient-to-use-proxy/
                
                //String proxyUser = System.getenv("HTTP_PROXY_USER");
                //String proxyPassword = System.getenv("HTTP_PROXY_PWRD");

                //CredentialsProvider credsProvider = new BasicCredentialsProvider();
                //credsProvider.setCredentials(new AuthScope(proxyHost, Integer.parseInt(proxyPort)),
                //        new UsernamePasswordCredentials(proxyUser, proxyPassword));
                
                if(getProxyuse()){
                    proxyHost = getProxyhost();
                    proxyPort = getProxyport();
                    proxyHostObject = new HttpHost(proxyHost, proxyPort);                        
                    client = HttpClientBuilder.create().setProxy(proxyHostObject).build();
                        //.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
                        //.setDefaultCredentialsProvider(credsProvider)
                }else{
                    client = HttpClientBuilder.create().build();
                }
                                                

                HttpGet getRequest = new HttpGet(_url);
                //getRequest.addHeader("Authorization", API_KEY);
                //getRequest.addHeader("Content-Type", "application/json");
                try {
                    System.out.println("Executing request...");
                    HttpResponse response = client.execute(getRequest);
                    System.out.println("Request successfully executed.");

                    //HttpEntity entity = response.getEntity();
                    InputStream stream = response.getEntity().getContent();
                    SyndFeedInput input = new SyndFeedInput();
                    SyndFeed feed = input.build(new XmlReader(stream));
                    //System.out.println(feed.getTitle());
                    
                    rss.setTitle(feed.getTitle());
                    rss.setLinkrss(getUrl());
                    rss.setLinkweb(feed.getLink());
                    rss.setDescription(feed.getDescription());
                    
                    /*String responseString = EntityUtils.toString(entity);
                    System.out.println(responseString);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

                updateMessage("Ending... at:" + System.currentTimeMillis());
                Thread.sleep(1000);
                return rss;
            }

        };
    }

}
