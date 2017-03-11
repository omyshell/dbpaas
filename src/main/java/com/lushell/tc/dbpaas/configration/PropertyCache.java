/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.configration;

import com.lushell.tc.dbpaas.entity.TaskStatus;
import com.lushell.tc.dbpaas.utils.ActionEnum;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties file, very important.
 *
 * @author tangchao
 */
public class PropertyCache {

    private static volatile PropertyCache cache;

    private static final String SERVER_FILE = "C:\\server.properties";

    private static final Properties PROPS = new Properties();

    private static String sshMysqlUser;
    private static String sshMysqlPsw;
    private static String jdbcUrl;
    private static String jdbcUser;
    private static String jdbcPsw;
    private static String mysqlSrc;
    
    private static int threadMin;
    private static int threadMax;

    private static List<ActionEnum> masterJobListBySemiSync;
    private static List<ActionEnum> slaveJobListBySemiSync;
    private static List<ActionEnum> slaveJobListByAsync;

    private PropertyCache() {
        try {
            PROPS.load(new FileInputStream(SERVER_FILE));
            PropertyCache.jdbcUrl = PROPS.getProperty("task.jdbc.url");
            PropertyCache.jdbcUser = PROPS.getProperty("task.jdbc.user");
            PropertyCache.jdbcPsw = PROPS.getProperty("task.jdbc.password");
            PropertyCache.sshMysqlUser = PROPS.getProperty("task.mysql.user");
            PropertyCache.sshMysqlPsw = PROPS.getProperty("task.mysql.password");
            PropertyCache.mysqlSrc = PROPS.getProperty("mysql.src.root");
            PropertyCache.threadMin = Integer.parseInt(PROPS.getProperty("workflow.thread.min"));
            PropertyCache.threadMax = Integer.parseInt(PROPS.getProperty("workflow.thread.max"));

            PropertyCache.masterJobListBySemiSync = master();
            PropertyCache.slaveJobListByAsync = slaveAsync();
            PropertyCache.slaveJobListBySemiSync = slaveSemiSync();

        } catch (IOException ex) {
            Logger.getLogger(PropertyCache.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public static PropertyCache getIstance() {
        if (cache == null) {
            synchronized (PropertyCache.class) {
                if (cache == null) {
                    cache = new PropertyCache();
                }
            }
        }
        return cache;
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

    public static int getThreadMin() {
        return threadMin;
    }

    public static int getThreadMax() {
        return threadMax;
    }

    private static List<ActionEnum> master() {
        List<ActionEnum> master = new ArrayList<>();
        master.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        master.add(ActionEnum.INITALIZE_INSTANCE);
        master.add(ActionEnum.START_INSTANCE);
        master.add(ActionEnum.INITALIZE_SYSTEM_USER);
        master.add(ActionEnum.INSTALL_MASTER_SEMI_SYNC);
        master.add(ActionEnum.SET_MASTER_SEMI_SYNC_ON);
        return master;
    }

    private static List<ActionEnum> slaveAsync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.START_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    private static List<ActionEnum> slaveSemiSync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.START_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.INSTALL_SLAVE_SEMI_SYNC);
        slave.add(ActionEnum.SET_SLAVE_SEMI_SYNC_ON);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    private static ActionEnum getNextAction(String taskName, List<ActionEnum> jobs) {
        int pos = 0;
        boolean iftask = false;
        for (; pos < jobs.size(); pos++) {
            if (jobs.get(pos).getScript().equals(taskName)) {
                iftask = true;
                break;
            }
        }

        ActionEnum action = null;
        if (iftask) {
            pos++;
            if (pos < jobs.size()) {
                action = jobs.get(pos);
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
