/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.executor;

import com.lushell.tc.mypaas.task.TaskExecute;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private ThreadPoolExecutor threadpool;

    /**
     * Param: corePoolSize - 池中所保存的线程数，包括空闲线程。 
     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。 
     * keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间。 
     * unit - keepAliveTime参数的时间单位，线程池维护线程所允许的空闲时间的单位:秒 。 
     * workQueue - 执行前用于保持任务的队列（缓冲队列）。此队列仅保持由execute 方法提交的 Runnable 任务。
     * RejectedExecutionHandler - 线程池对拒绝任务的处理策略(重试添加当前的任务，自动重复调用execute()方法)
     */
    public ThreadManager() {
        threadpool = new ThreadPoolExecutor(16, 128, 3600, TimeUnit.SECONDS, 
                new BlockQueue(128),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    //add task into thread pool
    public void submit(TaskExecute task) {
        threadpool.execute(() -> {
                   task.run();
        });
    }

    /**
     * close thread pool
     */
    public void shutdown() {
        threadpool.shutdown();
    }
}
