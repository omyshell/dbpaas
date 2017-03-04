/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.executor;

import com.lushell.tc.mypaas.configration.PropertyCache;
import com.lushell.tc.mypaas.task.TaskExecute;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private final ThreadPoolExecutor pool;

    public ThreadManager() {
        pool = new ThreadPoolExecutor(PropertyCache.getThreadMin(),
                PropertyCache.getThreadMax(), 3600, TimeUnit.SECONDS,
                new BlockQueue(128),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void submit(TaskExecute task) {
        pool.execute(() -> {
                   task.run();
        });
    }

    public void shutdown() {
        pool.shutdown();
    }
}
