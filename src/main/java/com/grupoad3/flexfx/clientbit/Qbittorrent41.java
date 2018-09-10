/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.clientbit;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author daniel_serna
 */
public class Qbittorrent41 extends ClientBittorrent {

    public Qbittorrent41(String url) {
        super(url);
    }

    @Override
    public String logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addTorrent(File file) throws IOException {

        // Info: https://www.baeldung.com/httpclient-post-http-request
        // Info: https://www.baeldung.com/httpclient-4-cookies
        // Info: https://stackoverflow.com/questions/2304663/apache-httpclient-making-multipart-form-post
        if (session == null || session.isEmpty()) {
            throw new IllegalArgumentException("run Login() is necesary");
        }

        int responseCode;
        // Client
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Cookie", "SID=" + session);
            
            // Send Data
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            
            // set attributes
            builder.addPart("torrents", fileBody);
            if (pathToSave != null && pathToSave.isEmpty() == false) {
                builder.addTextBody("savepath", pathToSave);
            }
            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            
            CloseableHttpResponse response = client.execute(httpPost);            
            responseCode = response.getStatusLine().getStatusCode();
        }

        switch (responseCode) {
            case 200:
                return "OK";
            default:
                return "FALSE";
        }

    }

    @Override
    public String addTorrent(URL url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(String user, String pass) throws UnsupportedEncodingException, IOException {
        // Info: https://www.mkyong.com/java/apache-httpclient-examples/

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Host", "127.0.0.1");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", user));
        urlParameters.add(new BasicNameValuePair("password", pass));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == 200) {
            // TODO: Set session read lines, more info on Web
        }

        return null;
    }

}
