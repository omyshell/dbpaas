/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

/**
 *
 * @author tangchao
 */

public class DataSourceContextHolder {
    
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCurrentDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getCurrentDataSourceType() {
        return contextHolder.get();
    }

    public static void setDefaultDataSourceType() {
        contextHolder.set(null);
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
