/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.task;

import com.lushell.tc.mypaas.configration.PropertyCache;
import com.lushell.tc.mypaas.entity.TaskStatus;
import com.lushell.tc.mypaas.meta.DbmetaManager;
import com.lushell.tc.mypaas.utils.ActionEnum;
import com.lushell.tc.mypaas.utils.TaskStatusConsts;
import com.lushell.tc.mypaas.utils.Worker;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class TaskExecute {

    private static final Logger logger = Logger.getLogger(TaskExecute.class.getName());

    private final int taskId;

    public TaskExecute(int taskId) {
        this.taskId = taskId;
    }

    private String getRealExecCommand(TaskStatus task) {
        String command = PropertyCache.getMysqlSrcPath();
        String script = task.getTaskName();
        ActionEnum action = ActionEnum.getBycript(script);
        if (action.isSyncTask()) {
            command = command + "./" + script;
        } else {
            command = command + " nohup /bin/bash " + script + " &";
        }
        return command;
    }

    private boolean isSyncTask(String taskName) {
        return ActionEnum.getBycript(taskName).isSyncTask();
    }

    public void run() {
        ActionEnum action;
        Worker worker;
        Worker checker;
        final DbmetaManager dbm = new DbmetaManager();
        TaskStatus task = dbm.getTaskById(taskId);
        if (task == null) {
            return;
        }

        /**
         * We setup task_name if the new task.
         */
        if (task.getTaskName() == null) {
            dbm.updateTaskName(taskId, ActionEnum.INSTALL_MYSQL_INSTANCE.getScript());
            task.setTaskName(ActionEnum.INSTALL_MYSQL_INSTANCE.getScript());
        }

        String status = task.getStatus();
        if (status == null) {
            System.err.println("status is null.");
            return;
        }
        System.out.println(taskId + "=====" + status);

        switch (status) {
            case TaskStatusConsts.RUNNING:
                /**
                 * block task.
                 */
                if (isSyncTask(task.getTaskName())) {
                    if (!dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS)) {
                        System.err.println("set sync task success failed.");
                    }
                    action = PropertyCache.getNextAction(task);
                    if (action == null) {
                        dbm.updateStatus(taskId, TaskStatusConsts.FINISHED);
                    } else {
                        dbm.updateTaskName(taskId, action.getCheckScript());
                    }
                    break;
                }

                /**
                 * unblock task has a timeout set.
                 */
                action = ActionEnum.getBycript(task.getTaskName());
                checker = new Worker(task.getIp(),
                        PropertyCache.getMysqlSrcPath() + "./" + action.getCheckScript());

                checker.exec();
                if (checker.getExitStatus() != 0) {
                    if (!dbm.updateStatus(taskId, TaskStatusConsts.FAILED)) {
                        System.err.println("status running async task set failed failed.");
                    }
                    break;
                }

                if (action.getTimeout() > dbm.getTaskRunTime(taskId)) {

                    if (action.getOkStatus().equals(checker.getExitInfo())) {
                        /**
                         * task done.
                         */
                        if (!dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS)) {
                            System.err.println("status running async task set success failed.");
                            break;
                        }
                        action = PropertyCache.getNextAction(task);
                        if (action == null) {
                            dbm.updateStatus(taskId, TaskStatusConsts.FINISHED);
                        } else {
                            dbm.updateTaskName(taskId, action.getScript());
                        }
                    }
                } else {
                    if (!dbm.updateStatus(taskId, TaskStatusConsts.TIMEOUT)) {
                        System.err.println("status running async task set timeout failed.");
                    }
                }

                break;
            case TaskStatusConsts.SUCCESS:
            case TaskStatusConsts.WAIT:
                worker = new Worker(task.getIp(), getRealExecCommand(task));

                /**
                 * update current task.
                 */
                if (!dbm.updateTaskBeginTime(taskId)) {
                    System.err.println("update task start time failed.");
                    break;
                }
                if (!dbm.updateStatus(taskId, TaskStatusConsts.RUNNING)) {
                    System.err.println("set status running failed.");
                    break;
                }
                System.out.println("exec1" + new Date());
                worker.exec();
                System.out.println("exec2" + new Date());
                if (worker.getExitStatus() != 0) {
                    System.err.println("worker.getExitStatus() " + worker.getExitStatus());
                    dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                    System.err.println("execute worker failed.");
                    break;
                }

                break;
            case TaskStatusConsts.TIMEOUT:
                /**
                 * 1. kill this task. 2. clean job site. 3. terminate task.
                 */
                System.err.println("set task TIMEOUT.");
                break;
            case TaskStatusConsts.FAILED:
                /**
                 * start work from current pos.
                 */
                //task.setStatus(TaskStatusConsts.START);
                //dbm.updateStatus(taskId, TaskStatusConsts.START);
                break;
            default:
                logger.log(Level.SEVERE, status);
        }
    }
}
