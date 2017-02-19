package com.lushell.tc.mypaas.configration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class ServerProps {

    private final String server = "/etc/server.properties";

    private final Properties props = new Properties();

    public ServerProps() {
        try {
            props.load(new FileInputStream(server));
        } catch (IOException ex) {
            Logger.getLogger(ServerProps.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public String getKeyValue(String key) {
        return props.getProperty(key);
    }

    public void writeProperties(String keyname, String keyvalue) {
        try {
            OutputStream fos = new FileOutputStream(server);
            props.setProperty(keyname, keyvalue);
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    public void updateProperties(String keyname, String keyvalue) {
        try {
            props.load(new FileInputStream(server));
            OutputStream fos = new FileOutputStream(server);
            props.setProperty(keyname, keyvalue);
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerProps cfm = new ServerProps();
        System.out.println(cfm.getKeyValue("task.jdbc.user"));
    }
    
}
