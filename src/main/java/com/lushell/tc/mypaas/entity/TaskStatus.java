/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.entity;

import java.util.Date;

/**
 *
 * @author tangchao
 */
public class TaskStatus  {


    private Integer taskId;

    private String ip;

    private int port;

    private int maxConnection;

    private String bufferPoolSize;

    private String role;

    private Integer serverId;

    private String dataSync;

    private Short slaveRunning;

    private short readOnly;

    private String masterIp;

    private Integer masterPort;

    private String status;
    
    private String taskName;
    
    private String taskReady;

    private String exitStatus;

    private Date updateTime;

    private Date createTime;

    private int  taskBeginTime;

    private Date taskDoneTime;

    private Short deployHa;

    private String vip;
    
    private String sshUser;

    private String sshPsw;
    

    public TaskStatus()
    {
    }
    
    public TaskStatus(Integer taskId, String ip, int port,
            int maxConnection, String bufferPoolSize, String role, 
            String dataSync, short readOnly, String status, Date updateTime, 
            Date createTime) {
        this.taskId = taskId;
        this.ip = ip;
        this.port = port;
        this.maxConnection = maxConnection;
        this.bufferPoolSize = bufferPoolSize;
        this.role = role;
        this.dataSync = dataSync;
        this.readOnly = readOnly;
        this.status = status;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public String getTaskReady() {
        return taskReady;
    }

    public void setTaskReady(String taskReady) {
        this.taskReady = taskReady;
    }

    public int getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(int taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getBufferPoolSize() {
        return bufferPoolSize;
    }

    public void setBufferPoolSize(String bufferPoolSize) {
        this.bufferPoolSize = bufferPoolSize;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getDataSync() {
        return dataSync;
    }

    public void setDataSync(String dataSync) {
        this.dataSync = dataSync;
    }

    public Short getSlaveRunning() {
        return slaveRunning;
    }

    public void setSlaveRunning(Short slaveRunning) {
        this.slaveRunning = slaveRunning;
    }

    public short getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(short readOnly) {
        this.readOnly = readOnly;
    }

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    public Integer getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(Integer masterPort) {
        this.masterPort = masterPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    public Short getDeployHa() {
        return deployHa;
    }

    public void setDeployHa(Short deployHa) {
        this.deployHa = deployHa;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
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

}
