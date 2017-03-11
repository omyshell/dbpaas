/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.task;

import com.lushell.tc.dbpaas.configration.PropertyCache;
import com.lushell.tc.dbpaas.entity.TaskStatus;
import com.lushell.tc.dbpaas.meta.DbmetaManager;
import com.lushell.tc.dbpaas.utils.ActionEnum;
import com.lushell.tc.dbpaas.utils.TaskStatusConsts;
import com.lushell.tc.dbpaas.utils.Worker;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class TaskExecute implements Runnable {

    private static final Logger LOG = Logger.getLogger(TaskExecute.class.getName());

    private final int taskId;

    public TaskExecute(int taskId) {
        this.taskId = taskId;
    }

    private String getRealExecCommand(TaskStatus task) {
        String command = PropertyCache.getMysqlSrcPath();
        String script = task.getTaskName();
        ActionEnum action = ActionEnum.getBycript(script);
        String slave = " ";
        if (action.equals(ActionEnum.ADD_SLAVE_TO_MASTER)) {
            slave = slave + " 1 " + task.getMasterIp() + " " + task.getMasterPort();
        }
        
        command = command + " ./" + script + slave;
        if (!action.isSyncTask()) {
            command = command + " >/dev/null 2>&1 &";
        }

        return command;
    }

    private boolean isSyncTask(String taskName) {
        return ActionEnum.getBycript(taskName).isSyncTask();
    }

    public void execute() {
        ActionEnum action;
        Worker worker;
        Worker checker;
        String status;
        TaskStatus task;

        final DbmetaManager dbm = new DbmetaManager();

        for (int i = 0;i<100;i++) {
           task = dbm.getTaskById(taskId);
            if (task.getTaskReady() == 0) {
                break;
            }
            status = task.getStatus();
            System.out.println("==Task ID " + taskId + " " + task.getTaskName() + " " + status);
            switch (status) {
                case TaskStatusConsts.RUNNING:
                    worker = new Worker(task.getIp(), getRealExecCommand(task));
                    if (worker.exec() != 0) {
                        dbm.setTaskStatus(taskId, TaskStatusConsts.FAILED);
                    } else {
                        dbm.setTaskStatus(taskId, TaskStatusConsts.SUCCESS);
                    }
                    break;
                case TaskStatusConsts.SUCCESS:
                    if (isSyncTask(task.getTaskName())) {
                        action = PropertyCache.getNextAction(task);
                        if (action == null) {
                            dbm.setTaskStatus(taskId, TaskStatusConsts.FINISHED);
                        } else {
                            dbm.setTaskName(taskId, action.getScript());
                            dbm.setTaskStatus(taskId, TaskStatusConsts.RUNNING);
                        }
                        break;
                    }

                    action = ActionEnum.getBycript(task.getTaskName());
                    checker = new Worker(task.getIp(), PropertyCache.getMysqlSrcPath()
                            + " ./" + action.getCheckScript());
                    checker.exec();
                    if (action.getOkStatus().equals(checker.getExitInfo())) {
                        action = PropertyCache.getNextAction(task);
                        if (action == null) {
                            dbm.setTaskStatus(taskId, TaskStatusConsts.FINISHED);
                        } else {
                            dbm.setTaskName(taskId, action.getScript());
                            dbm.setTaskStatus(taskId, TaskStatusConsts.RUNNING);
                        }
                    } else {
                        dbm.setTaskStatus(taskId, TaskStatusConsts.FAILED);
                    }
                    break;
                case TaskStatusConsts.WAITING:
                    /**
                     * Initialize task, setup status and start name.
                     */
                    dbm.setTaskName(taskId, ActionEnum.INSTALL_MYSQL_INSTANCE.getScript());
                    dbm.setTaskStatus(taskId, TaskStatusConsts.RUNNING);
                    break;
                case TaskStatusConsts.FINISHED:
                    dbm.setTaskName(taskId, "DONE");
                    dbm.setTaskTready(taskId, 0);
                    break;
                case TaskStatusConsts.FAILED:
                    System.err.println(taskId + " FAILED");
                    dbm.setTaskTready(taskId, 0);
                    break;
                default:
                    System.err.println("return error status" + status);
            }
        }
    }

    @Override
    public void run() {
        execute();
    }
}
