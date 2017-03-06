/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.meta;

import com.lushell.tc.dbpaas.configration.PropertyCache;
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

    public DbmetaConnection() {
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
                connection = DriverManager.getConnection(PropertyCache.getUrl(),
                        PropertyCache.getJdbcUser(), PropertyCache.getJdbcPsw());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
            connection = null;
        }
        return connection;
    }
}
