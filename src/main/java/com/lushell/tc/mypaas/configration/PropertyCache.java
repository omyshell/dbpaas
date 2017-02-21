/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.configration;

import com.lushell.tc.mypaas.entity.TaskStatus;
import com.lushell.tc.mypaas.utils.ActionEnum;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties file, very important.
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
    private static String mysqlSrc;
    
    private static List<ActionEnum> masterJobListBySemiSync;
    private static List<ActionEnum> slaveJobListBySemiSync;
    private static List<ActionEnum> slaveJobListByAsync;

    private PropertyCache() {
        try {
            props.load(new FileInputStream(server));
            PropertyCache.jdbcUrl = props.getProperty("task.jdbc.url");
            PropertyCache.jdbcUser = props.getProperty("task.jdbc.user");
            PropertyCache.jdbcPsw = props.getProperty("task.jdbc.password");
            PropertyCache.sshMysqlUser = props.getProperty("task.mysql.user");
            PropertyCache.sshMysqlPsw = props.getProperty("task.mysql.password");
            PropertyCache.mysqlSrc = props.getProperty("mysql.src.root");

            PropertyCache.masterJobListBySemiSync = master();
            PropertyCache.slaveJobListByAsync = slaveAsync();
            PropertyCache.slaveJobListBySemiSync = slaveSemiSync();

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

    public static List<ActionEnum> getMasterJobListBySemiSync() {
        return masterJobListBySemiSync;
    }

    public static List<ActionEnum> getSlaveJobListBySemiSync() {
        return slaveJobListBySemiSync;
    }

    public static List<ActionEnum> getSlaveJobListByAsync() {
        return slaveJobListByAsync;
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
    
    public static String getMysqlSrcPath() {
        return mysqlSrc;
    }

    private static List<ActionEnum> master() {
        List<ActionEnum> master = new ArrayList<>();
        master.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        master.add(ActionEnum.INITALIZE_INSTANCE);
        master.add(ActionEnum.INSTALL_MASTER_SEMI_SYNC);
        master.add(ActionEnum.SET_MASTER_SEMI_SYNC_ON);
        master.add(ActionEnum.INITALIZE_SYSTEM_USER);
        return master;
    }

    private static List<ActionEnum> slaveAsync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    private static List<ActionEnum> slaveSemiSync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.INSTALL_SLAVE_SEMI_SYNC);
        slave.add(ActionEnum.SET_SLAVE_SEMI_SYNC_ON);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }
    
    private static ActionEnum getNextAction(String taskName, List<ActionEnum> jobs) {
        ActionEnum action = null;
        for (int i = 0; i < jobs.size(); i++) {
            action = jobs.get(i);
            if (action.getScript().equals(taskName)) {
                i++;
                if (i < jobs.size()) {
                    action = jobs.get(i);
                    System.out.println("Setup action:" + action.getScript());
                }
            }
        }
        return action;
    }

    public static ActionEnum getNextAction(TaskStatus task) {
        String taskName = task.getTaskName();
        List<ActionEnum> jobList = null;

        switch (task.getRole()) {
            case "master":
                jobList = PropertyCache.getMasterJobListBySemiSync();
                break;
            case "slave":
                if ("semiSync".equalsIgnoreCase(task.getDataSync())) {
                    jobList = PropertyCache.getSlaveJobListBySemiSync();
                } else if ("async".equalsIgnoreCase(task.getDataSync())) {
                    jobList = PropertyCache.getSlaveJobListByAsync();
                }
                break;
            default:
                System.err.println("unknow instance role, only master or slave.");
        }

         return getNextAction(taskName, jobList);
    }
}
