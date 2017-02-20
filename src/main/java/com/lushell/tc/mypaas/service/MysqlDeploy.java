/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.service;

import com.lushell.tc.mypaas.configration.PropertyCache;
import com.lushell.tc.mypaas.entity.TaskStatus;
import com.lushell.tc.mypaas.executor.ThreadManager;
import com.lushell.tc.mypaas.meta.DbmetaManager;
import com.lushell.tc.mypaas.task.TaskExecute;
import java.util.List;

/**
 *
 * @author tangchao
 */
public class MysqlDeploy {

    public static boolean shutdown = false;
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * first we init server cache.
         */
        PropertyCache.getIstance();
        
        List<TaskStatus> waitTask = null;
        ThreadManager tm = new ThreadManager();
        DbmetaManager dba = new DbmetaManager();

        while (true) {
            waitTask = dba.getWaitTask();
            waitTask.stream().forEach((item) -> {
                tm.submit(new TaskExecute(item.getTaskId()));
            });
            Thread.sleep(60 * 1000);
        }
    }
}
