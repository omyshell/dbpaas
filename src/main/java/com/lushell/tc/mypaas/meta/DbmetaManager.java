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

    public DbmetaManager() {
    }

    public List<TaskStatus> getWaitTask() {
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            List<TaskStatus> waitTasks = new ArrayList<>();
            String sql = "SELECT * FROM epcc_mysql_instance_task "
                    + "where (status != ? OR status !=  ?) AND task_ready = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, TaskStatusConsts.FAILED);
            pst.setString(2, TaskStatusConsts.FINISHED);
            pst.setInt(3, 1);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    TaskStatus waitTask = new TaskStatus();
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
                    + "where task_id = ? and ( status != ? AND status != ?)";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskId);
            pst.setString(2, TaskStatusConsts.FINISHED);
            pst.setString(3, TaskStatusConsts.FAILED);
            rs = pst.executeQuery();
            if (rs.next()) {
                task.setTaskId(rs.getInt("task_id"));
                task.setStatus(rs.getString("status"));
                task.setTaskBeginTime(rs.getInt("task_begin_time"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskReady(rs.getString("task_ready"));
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
            System.out.println("Change "+ taskId + " status " + status);
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
            System.out.println("Update task name:" + taskName);
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
