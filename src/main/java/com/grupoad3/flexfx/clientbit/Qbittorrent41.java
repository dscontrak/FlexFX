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
import org.apache.http.Header;
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

    private final String apiLogin = "/api/v2/auth/login";
    private final String apiAddFileTorrent = "/api/v2/torrents/add";
    private final String cookieKey = "set-cookie";

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

            HttpPost post = new HttpPost(url + apiAddFileTorrent);
            post.setHeader("User-Agent", USER_AGENT);
            post.setHeader("Host", "127.0.0.1");
            post.setHeader("Cookie", "SID=" + session);


            // Send Data
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

            // set attributes
            builder.addPart("torrents", fileBody);
            if (pathToSave != null && pathToSave.isEmpty() == false) {
                builder.addTextBody("savepath", pathToSave);
            }
            builder.addTextBody("paused", String.valueOf(addPauseState));
            HttpEntity multipart = builder.build();
            post.setEntity(multipart);

            CloseableHttpResponse response = client.execute(post);
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

        if(session != null && session.isEmpty() == false){
            return session;
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url + apiLogin);

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

            //get all headers
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                System.out.println("Key : " + header.getName()
                        + " ,Value : " + header.getValue());

                if(cookieKey.equals(header.getName())){
                      return session = getSessionFromCookie(header.getValue());
                }

            }

        }

        return null;
    }

    private String getSessionFromCookie(String valueCookie){
        String []comaSeparetor = valueCookie.split(";");

        if(comaSeparetor.length == 0){
            return null;
        }

        for (String currentVal : comaSeparetor) {
            if(currentVal.contains("SID")){
                int positionEqual = currentVal.indexOf("=");

                return currentVal.substring(positionEqual + 1, currentVal.length());
            }
        }

        return null;
    }

}
