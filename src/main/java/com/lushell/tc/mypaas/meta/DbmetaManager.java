/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.meta;

import com.lushell.tc.mypaas.entity.TaskStatus;
import com.lushell.tc.mypaas.utils.TaskStatusConsts;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tangchao
 */
public class DbmetaManager {

    private String sshUser;
    private String sshPsw;

    public DbmetaManager() {
    }

    public String getSshUser() {
        return sshUser;
    }

    public void setSshUser(String sshUser) {
        this.sshUser = sshUser;
    }

    public String getSshPsw() {
        return sshPsw;
    }

    public void setSshPsw(String sshPsw) {
        this.sshPsw = sshPsw;
    }

    public boolean init() {
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            try (Statement stmt = connection.createStatement()) {
                String sql = "SELECT config_value FROM default_config where config_name = 'dba_ssh_user'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sshUser = rs.getString("config_value");
                }
                rs.close();
                sql = "SELECT config_value FROM default_config where config_name = 'dba_ssh_psw'";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sshPsw = rs.getString("config_value");
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public List<TaskStatus> getWaitTask() {
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            List<TaskStatus> waitTasks = new ArrayList<>();
            try (Statement stmt = connection.createStatement()) {
                String sql = "SELECT * FROM epcc_mysql_instance_task "
                        + "where status = 'WAIT'";
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        TaskStatus waitTask = new TaskStatus();
                        waitTask.setSshUser(sshUser);
                        waitTask.setSshPsw(sshPsw);
                        waitTask.setTaskId(rs.getInt("task_id"));
                        waitTask.setIp(rs.getString("ip"));
                        waitTask.setPort(rs.getInt("port"));
                        waitTask.setDataSync(rs.getString("data_sync"));
                        waitTask.setRole(rs.getString("role"));
                        if (waitTask.getRole().equalsIgnoreCase("slave")) {
                            waitTask.setMasterIp(rs.getString("master_ip"));
                            waitTask.setMasterPort(rs.getInt("master_port"));
                        }
                        waitTasks.add(waitTask);
                    }
                }
            }
            return waitTasks;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return null;
    }

    public TaskStatus getTaskById(int taskId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        TaskStatus task = new TaskStatus();
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            String sql = "SELECT * FROM epcc_mysql_instance_task "
                    + "where task_id = ? and status != ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskId);
            pst.setString(2, TaskStatusConsts.FAILED);
            rs = pst.executeQuery();
            if (rs.next()) {

                task.setTaskId(rs.getInt("task_id"));
                System.out.println(task.getTaskId());
                task.setIp(rs.getString("ip"));
                task.setPort(rs.getInt("port"));
                task.setDataSync(rs.getString("data_sync"));
                task.setRole(rs.getString("role"));
                if (task.getRole().equalsIgnoreCase("slave")) {
                    task.setMasterIp(rs.getString("master_ip"));
                    task.setMasterPort(rs.getInt("master_port"));
                }
            }
            pst.close();
            rs.close();
            return task;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return null;
    }

    public int getTaskRunTime(int taskId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = null;
        int runTime = 0;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "SELECT UNIX_TIMESTAMP() - task_begin_time "
                    + "FROM epcc_mysql_instance_task WHERE task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskId);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.getInt(1);
            }
            pst.close();
            rs.close();
            return runTime;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return runTime;
    }

    public boolean updateStatus(int taskId, String status) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "update epcc_mysql_instance_task set status = ? where task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, status);
            pst.setInt(2, taskId);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public boolean updateTaskName(int taskId, String taskName) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "update epcc_mysql_instance_task set task_name = ? where task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, taskName);
            pst.setInt(2, taskId);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public boolean updateTaskBeginTime(int taskId) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "update epcc_mysql_instance_task set task_begin_time = UNIX_TIMESTAMP() "
                    + "where task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskId);
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public boolean update(String sql) {
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            dbc.closeConnection();
        }
    }
}
