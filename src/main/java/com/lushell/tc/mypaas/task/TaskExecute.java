/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.task;

import com.lushell.tc.mypaas.entity.TaskStatus;
import com.lushell.tc.mypaas.meta.DbmetaManager;
import com.lushell.tc.mypaas.utils.ActionEnum;
import com.lushell.tc.mypaas.utils.TaskStatusConsts;
import com.lushell.tc.mypaas.utils.Worker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tangchao
 */
public class TaskExecute {

    private final int taskId;
    private final DbmetaManager dbm;

    public TaskExecute(int taskId) {
        this.taskId = taskId;
        dbm = new DbmetaManager();
    }

    private void setNextOps() {
        TaskStatus task = dbm.getTaskById(taskId);
        String taskName = task.getTaskName();
        ActionEnum action = null;

        if ("master".equalsIgnoreCase(task.getRole())) {
            List<ActionEnum> master = master();
            for (int i = 0; i < master.size(); i++) {
                action = master.get(i);
                if (action.getScript().equals(taskName)) {
                    i++;
                    if (i < master.size()) {
                        action = master.get(i);
                    }
                }
            }
        }

        if ("slave".equalsIgnoreCase(task.getRole())
                && "semiSync".equalsIgnoreCase(task.getDataSync())) {
            slaveSemiSync();
        }

        if ("slave".equalsIgnoreCase(task.getRole())
                && "async".equalsIgnoreCase(task.getDataSync())) {
            slaveAsync();
        }

        dbm.updateTaskName(taskId, action.getScript());
        dbm.updateStatus(taskId, TaskStatusConsts.WAIT);
    }

    public final List<ActionEnum> master() {
        List<ActionEnum> master = new ArrayList<>();
        master.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        master.add(ActionEnum.INITALIZE_INSTANCE);
        master.add(ActionEnum.INSTALL_MASTER_SEMI_SYNC);
        master.add(ActionEnum.SET_MASTER_SEMI_SYNC_ON);
        master.add(ActionEnum.INITALIZE_SYSTEM_USER);
        return master;
    }

    public final List<ActionEnum> slaveAsync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    public final List<ActionEnum> slaveSemiSync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.INSTALL_SLAVE_SEMI_SYNC);
        slave.add(ActionEnum.SET_SLAVE_SEMI_SYNC_ON);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    public void run() {

        TaskStatus task = dbm.getTaskById(taskId);
        /**
         * We setup task_name if the new task.
         */
        if (task.getTaskName() == null) {
            dbm.updateTaskName(taskId, ActionEnum.INSTALL_MYSQL_INSTANCE.getScript());
            task.setTaskName(ActionEnum.INSTALL_MYSQL_INSTANCE.getScript());
        }

        String status = task.getStatus();
        if (status == null) {
            return;
        }

        switch (status) {
            case TaskStatusConsts.RUNNING:
                ActionEnum action = ActionEnum.getBycript(task.getTaskName());
                Worker checker = new Worker(task.getIp(), action.getCheckScript());

                checker.exec();
                if (checker.getExitStatus() != 0) {
                    dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                    return;
                }

                if (action.getTimeout() > 0
                        && action.getTimeout() > dbm.getTaskRunTime(taskId)) {

                    if (action.getOkStatus().equals(checker.getExitInfo())) {
                        /**
                         * task done.
                         */
                        dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS);
                        setNextOps();
                    }
                } else {
                // TIMEOUT we need kill the task.
                    //dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                }
                break;
            case TaskStatusConsts.WAIT:
                Worker worker = new Worker(task.getIp(), null);
                /**
                 * update current task status and name.
                 */
                if (!dbm.updateStatus(taskId, TaskStatusConsts.START)
                        || !dbm.updateTaskBeginTime(taskId)) {
                    return;
                }
                worker.exec();
                if (worker.getExitStatus() != 0) {
                    dbm.updateStatus(taskId, TaskStatusConsts.FAILED);
                    return;
                }
                dbm.updateStatus(taskId, TaskStatusConsts.RUNNING);

                break;
            case TaskStatusConsts.START:

                /**
                 * The reasons for this state is work be terminated. 
                 * Clean this job site,  then se status WAIT.
                 */
                break;
            case TaskStatusConsts.TIMEOUT:

                /**
                 * 1. kill this task. 2. clean job site. 3. terminate task.
                 */
                break;
            default:
                

        }
    }
}
