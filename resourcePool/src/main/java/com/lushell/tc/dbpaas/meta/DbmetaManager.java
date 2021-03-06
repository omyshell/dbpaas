/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.meta;

import com.lushell.tc.dbpaas.entity.TaskStatus;
import com.lushell.tc.dbpaas.utils.TaskStatusConsts;
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

    public List<TaskStatus> getReadyTask() {
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            List<TaskStatus> waitTasks = new ArrayList<>();
            String sql = "SELECT task_id FROM epcc_mysql_instance_task "
                    + "WHERE  task_ready = ? and status = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, 1);
            pst.setString(2, TaskStatusConsts.WAIT);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    TaskStatus waitTask = new TaskStatus();
                    waitTask.setTaskId(rs.getInt("task_id"));
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
                    + "WHERE task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskId);
            rs = pst.executeQuery();
            if (rs.next()) {
                task.setTaskId(rs.getInt("task_id"));
                task.setStatus(rs.getString("status"));
                task.setTaskBeginTime(rs.getInt("task_begin_time"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskReady(rs.getInt("task_ready"));
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
                runTime= rs.getInt(1);
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

    public boolean isFinished(String ip, int port) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = null;
        String status = "";
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "SELECT status FROM epcc_mysql_instance_task "
                    + "WHERE ip = ? AND port = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, ip);
            pst.setInt(1, port);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = rs.getString(1);
            }
            pst.close();
            rs.close();
            return status.equalsIgnoreCase(TaskStatusConsts.FINISHED);
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public boolean setTaskStatus(int taskId, String status) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "UPDATE epcc_mysql_instance_task SET status = ? WHERE task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, status);
            pst.setInt(2, taskId);
            int executeUpdate = pst.executeUpdate();
            pst.close();
            if (executeUpdate == 0) {
                return false;
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }

    public boolean setTaskName(int taskId, String taskName) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "UPDATE epcc_mysql_instance_task SET task_name = ? WHERE task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, taskName);
            pst.setInt(2, taskId);
            int executeUpdate = pst.executeUpdate();
            pst.close();
            //System.out.println("Update task name:" + taskName);
            if (executeUpdate == 0) {
                return false;
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }
    
    public boolean setTaskTready(int taskId, int taskReady) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "UPDATE epcc_mysql_instance_task SET task_ready = ? WHERE task_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, taskReady);
            pst.setInt(2, taskId);
            int executeUpdate = pst.executeUpdate();
            pst.close();
            if (executeUpdate == 0) {
                return false;
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbmetaManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbc.closeConnection();
        }
        return false;
    }
    
    public boolean setTaskBeginTime(int taskId) {
        String sql = null;
        PreparedStatement pst = null;
        DbmetaConnection dbc = new DbmetaConnection();
        try {
            Connection connection = dbc.getConnection();
            sql = "UPDATE epcc_mysql_instance_task SET task_begin_time = UNIX_TIMESTAMP() "
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
