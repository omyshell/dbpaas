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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class TaskExecute {

    private static final Logger logger = Logger.getLogger(TaskExecute.class.getName());

    private final int taskId;
    private final DbmetaManager dbm;

    public TaskExecute(int taskId) {
        this.taskId = taskId;
        dbm = new DbmetaManager();
    }

    private ActionEnum getNextAction(String taskName, List<ActionEnum> jobs) {
        ActionEnum action = null;
        for (int i = 0; i < jobs.size(); i++) {
            action = jobs.get(i);
            if (action.getScript().equals(taskName)) {
                i++;
                if (i < jobs.size()) {
                    action = jobs.get(i);
                }
            }
        }
        return action;
    }

    private void setNextOps() {
        TaskStatus task = dbm.getTaskById(taskId);
        String taskName = task.getTaskName();
        ActionEnum action = null;

        switch (task.getRole()) {
            case "master":
                action = getNextAction(taskName, master());
                break;
            case "slave":
                if ("semiSync".equalsIgnoreCase(task.getDataSync())) {
                    action = getNextAction(taskName, slaveSemiSync());
                } else if ("async".equalsIgnoreCase(task.getDataSync())) {
                    action = getNextAction(taskName, slaveAsync());
                }
                break;
            default:
                logger.log(Level.SEVERE, "unknow instance role, only master or slave.");
        }

        /**
         * The workflow end, action be set null.
         */
        if (action == null) {
            dbm.updateStatus(taskId, TaskStatusConsts.FINISHED);
        } else {
            dbm.updateTaskName(taskId, action.getCheckScript());
        }
    }

    private List<ActionEnum> master() {
        List<ActionEnum> master = new ArrayList<>();
        master.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        master.add(ActionEnum.INITALIZE_INSTANCE);
        master.add(ActionEnum.INSTALL_MASTER_SEMI_SYNC);
        master.add(ActionEnum.SET_MASTER_SEMI_SYNC_ON);
        master.add(ActionEnum.INITALIZE_SYSTEM_USER);
        return master;
    }

    private List<ActionEnum> slaveAsync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    private List<ActionEnum> slaveSemiSync() {
        List<ActionEnum> slave = new ArrayList<>();
        slave.add(ActionEnum.INSTALL_MYSQL_INSTANCE);
        slave.add(ActionEnum.INITALIZE_INSTANCE);
        slave.add(ActionEnum.ADD_SLAVE_TO_MASTER);
        slave.add(ActionEnum.INSTALL_SLAVE_SEMI_SYNC);
        slave.add(ActionEnum.SET_SLAVE_SEMI_SYNC_ON);
        slave.add(ActionEnum.START_SLAVE);
        return slave;
    }

    private String getRealExecCommand(TaskStatus task) {
        String command = PropertyCache.getMysqlSrcPath();
        String script = task.getTaskName();
        ActionEnum action = ActionEnum.getBycript(script);
        if (action.getTimeout() == 0) {
            command = command + "./" + script;
        } else {
            command = command + " nohup ./" + script + " > ./log.txt &";
        }
        return command;
    }

    private boolean isSyncTask(String taskName) {
        return ActionEnum.getBycript(taskName).getTimeout() == 0;
    }

    public void run() {
        ActionEnum action;
        Worker worker;
        Worker checker;
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
                    setNextOps();
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
                System.out.println("checker exit:" + checker.getExitInfo());
                System.out.println("ok exit:" + action.getOkStatus());

                if (action.getTimeout() > dbm.getTaskRunTime(taskId)) {

                    if (action.getOkStatus().equals(checker.getExitInfo())) {
                        /**
                         * task done.
                         */
                        if (!dbm.updateStatus(taskId, TaskStatusConsts.SUCCESS)) {
                            System.err.println("status running async task set success failed.");
                        }
                        setNextOps();
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
