/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.configration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class PropertyCache {

    private static final String server = "/etc/server.properties";

    private static final Properties props = new Properties();

    private static String sshMysqlUser;
    private static String sshMysqlPsw;
    private static String jdbcUrl;
    private static String jdbcUser;
    private static String jdbcPsw;

    private PropertyCache() {
        try {
            props.load(new FileInputStream(server));
            PropertyCache.jdbcUrl = props.getProperty("task.jdbc.url");
            PropertyCache.jdbcUser = props.getProperty("task.jdbc.user");
            PropertyCache.jdbcPsw = props.getProperty("task.jdbc.password");
            PropertyCache.sshMysqlUser = props.getProperty("task.mysql.user");
            PropertyCache.sshMysqlPsw = props.getProperty("task.mysql.password");
        } catch (IOException ex) {
            Logger.getLogger(PropertyCache.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    private static volatile PropertyCache instance;

    public static PropertyCache getIstance() {
        if (instance == null) {
            synchronized (PropertyCache.class) {
                if (instance == null) {
                    instance = new PropertyCache();
                }
            }
        }
        return instance;
    }

    public static String getUrl() {
        return jdbcUrl;
    }

    public static String getJdbcUser() {
        return jdbcUser;
    }

    public static String getJdbcPsw() {
        return jdbcPsw;
    }

    public static String getMysqlSshUser() {
        return sshMysqlUser;
    }

    public static String getMysqlSshPsw() {
        return sshMysqlPsw;
    }
}
