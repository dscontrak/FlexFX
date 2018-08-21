/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author daniel
 */
public class DownloadFileHttpCilent {

    private final String url;
    private final String path;

    public DownloadFileHttpCilent(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public void download() throws IOException {

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Request Url: " + request.getURI());
            System.out.println("Response Code: " + responseCode);

            FileOutputStream fos;
            try (InputStream is = entity.getContent()) {
                String filePath = path;
                fos = new FileOutputStream(new File(filePath));
                int inByte;
                while ((inByte = is.read()) != -1) {
                    fos.write(inByte);
                }
            }
            
            fos.close();
            System.out.println("File Download Completed!!!");
        }


    }
}
