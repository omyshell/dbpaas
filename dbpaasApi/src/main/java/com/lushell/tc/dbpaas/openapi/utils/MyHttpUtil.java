/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.utils;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 *
 * @author tangchao
 */
public class MyHttpUtil {

    private static final Logger logger = Logger.getLogger(MyHttpUtil.class.getName());

    public static String getDataFromUrl(String url, int timeout) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            //StatusLine statusLine = response.getStatusLine();
            String body = EntityUtils.toString(entity);
            return body;
        } catch (IOException ex) {
            Logger.getLogger(MyHttpUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
