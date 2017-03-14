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
import com.lushell.tc.dbpaas.utils.TaskStatusConsts;
import java.util.List;

/**
 *
 * @author tangchao
 */
public class DeployService {

    private static final int T = 30;
    private static int sleepTime = T;
    /**
     * @param args the command line arguments
     * NO LINUX DEAMON AGENT RUNNING，SO EXEC(command) WILL BLOCKING UNTIL END.
     * ONE TASK PER THREAD
     */
    public static void main(String[] args) {
        /**
         * Init server cache.
         */
        PropertyCache.getIstance();
        
        /**
         * dba is task manager.
         */
        DbmetaManager dba = new DbmetaManager();
        
        /**
         * pool is thread pool manager.
         */
        ThreadManager pool = new ThreadManager();

        while (true) {
            
            List<TaskStatus> readyTask = dba.getReadyTask();
            
            if (readyTask == null || readyTask.isEmpty()) {
                sleepTime += 10;
                if (sleepTime > 900) {
                    sleepTime /= 3;
                }
                continue;
            } else {
                sleepTime = T;
            }
            
            readyTask.stream().forEach((task) -> {
                Integer taskId = task.getTaskId();
                dba.setTaskStatus(taskId, TaskStatusConsts.WAITING);
                TaskExecute te = new TaskExecute(taskId);
                //te.run();
                pool.submit(te);
            });

            try {
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException ex) {
            }
        }
    }
}
