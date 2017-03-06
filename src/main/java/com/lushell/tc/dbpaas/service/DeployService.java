/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.service;

import com.lushell.tc.dbpaas.configration.PropertyCache;
import com.lushell.tc.dbpaas.entity.TaskStatus;
import com.lushell.tc.dbpaas.executor.ThreadManager;
import com.lushell.tc.dbpaas.meta.DbmetaManager;
import com.lushell.tc.dbpaas.task.TaskExecute;
import java.util.List;

/**
 *
 * @author tangchao
 */
public class DeployService {

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
        List<TaskStatus> waitTask;
        DbmetaManager dba = new DbmetaManager();
        ThreadManager tm = new ThreadManager();
        while (true) {
            waitTask = dba.getWaitTask();
            waitTask.stream().forEach((item) -> {
                tm.submit(new TaskExecute(item.getTaskId()));
            });
            Thread.sleep(10 * 1000);
        }
    }
}
