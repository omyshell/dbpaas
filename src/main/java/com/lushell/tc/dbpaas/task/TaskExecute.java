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
public class TaskExecute {

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
            command = command + " >/dev/null 2>&1 & ";
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
        System.out.println("==Task ID " + taskId + " " + task.getTaskName()
                + " " + status);

        switch (status) {
            case TaskStatusConsts.RUNNING:
                /**
                 * block task.
                 */
                if (isSyncTask(task.getTaskName())) {
                    if (!dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS)) {
                        break;
                    }
                    action = PropertyCache.getNextAction(task);
                    if (action == null) {
                        dbm.updateStatus(taskId, TaskStatusConsts.FINISHED);
                    } else {
                        dbm.updateTaskName(taskId, action.getScript());
                    }
                    break;
                }

                /**
                 * unblock task has a timeout set.
                 * ======IMPORTANT  TEERIBLE======
                 * ASYNC TASK check exec running time over MAIN WHILE LOOP SLEEP,
                 * task will probably hit bug. So keep check script lightter.
                 */
                action = ActionEnum.getBycript(task.getTaskName());
                checker = new Worker(task.getIp(),
                        PropertyCache.getMysqlSrcPath() + "./" + action.getCheckScript());

                checker.exec();
                if (checker.getExitStatus() != 0) {
                    if (!dbm.updateStatus(taskId, TaskStatusConsts.FAILED)) {
                        System.err.println("status RUNNING [async task] set FAILED failed.");
                    }
                    break;
                }

                if (action.getTimeout() > dbm.getTaskRunTime(taskId)) {

                    if (action.getOkStatus().equals(checker.getExitInfo())) {
                        /**
                         * task done.
                         */
                        if (!dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS)) {
                            System.out.println("status running async task set success failed.");
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
                    System.out.println("update task start time failed.");
                    break;
                }
                if (!dbm.updateStatus(taskId, TaskStatusConsts.RUNNING)) {
                    System.out.println("set status running failed.");
                    break;
                }

                worker.exec();

                if (worker.getExitStatus() != 0) {
                    dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                    break;
                }

                break;
            case TaskStatusConsts.TIMEOUT:
                /**
                 * 1. kill this task. 2. clean job site. 3. terminate task.
                 */
                System.err.println("TASK TIMEOUT.");
                dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                break;
            case TaskStatusConsts.FAILED:
                /**
                 * Restart work from current position, record retry count.
                 */
                break;
            default:
                System.err.println("return error status" + status);
        }
    }
}
