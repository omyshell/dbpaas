/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.meta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class DbmetaConnection {

    private Connection connection;
    private final String user;
    private final String psw;
    private final String url;

    public DbmetaConnection() {
        connection = null;
        url = "jdbc:mysql://localhost:3307/dba_task_manager?zeroDateTimeBehavior"
                + "=convertToNull&characterEncoding=utf8";
        user = "epcc_dba";
        psw = "123456";
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connection = null;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, psw);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
            connection = null;
        }
        return connection;
    }
}
