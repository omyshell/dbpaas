/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.instance.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author tangchao
 */
@Entity
@Table(name = "epcc_mysql_instance_task")
public class TaskDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "task_id")
    private Integer taskId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 46)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "port")
    private int port;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "role")
    private String role;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "data_sync")
    private String dataSync;
    @Basic(optional = false)
    @NotNull
    @Column(name = "read_only")
    private short readOnly;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "task_ready")
    private short taskReady;
    @Size(max = 100)
    @Column(name = "task_name")
    private String taskName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "failed_count")
    private short failedCount;
    @Size(max = 46)
    @Column(name = "master_ip")
    private String masterIp;
    @Column(name = "master_port")
    private Integer masterPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "task_begin_time")
    private Integer taskBeginTime;
    @Column(name = "task_done_time")
    private Integer taskDoneTime;
    @Column(name = "deploy_ha")
    private Short deployHa;
    @Size(max = 100)
    @Column(name = "ha_result")
    private String haResult;
    @Column(name = "ha_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date haTime;
    @Size(max = 46)
    @Column(name = "vip")
    private String vip;
    @Size(max = 46)
    @Column(name = "arbit_ip")
    private String arbitIp;

    public TaskDO() {
    }

    public TaskDO(Integer taskId) {
        this.taskId = taskId;
    }

    public TaskDO(Integer taskId, String ip, int port, String role, String dataSync, short readOnly, String status, short taskReady, short failedCount, Date updateTime, Date createTime) {
        this.taskId = taskId;
        this.ip = ip;
        this.port = port;
        this.role = role;
        this.dataSync = dataSync;
        this.readOnly = readOnly;
        this.status = status;
        this.taskReady = taskReady;
        this.failedCount = failedCount;
        this.updateTime = updateTime;
        this.createTime = createTime;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDataSync() {
        return dataSync;
    }

    public void setDataSync(String dataSync) {
        this.dataSync = dataSync;
    }

    public short getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(short readOnly) {
        this.readOnly = readOnly;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public short getTaskReady() {
        return taskReady;
    }

    public void setTaskReady(short taskReady) {
        this.taskReady = taskReady;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public short getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(short failedCount) {
        this.failedCount = failedCount;
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

    public Integer getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(Integer taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public Integer getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Integer taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    public Short getDeployHa() {
        return deployHa;
    }

    public void setDeployHa(Short deployHa) {
        this.deployHa = deployHa;
    }

    public String getHaResult() {
        return haResult;
    }

    public void setHaResult(String haResult) {
        this.haResult = haResult;
    }

    public Date getHaTime() {
        return haTime;
    }

    public void setHaTime(Date haTime) {
        this.haTime = haTime;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getArbitIp() {
        return arbitIp;
    }

    public void setArbitIp(String arbitIp) {
        this.arbitIp = arbitIp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskDO)) {
            return false;
        }
        TaskDO other = (TaskDO) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lushell.tc.dbpaas.openapi.execute.entity.TaskDO[ taskId=" + taskId + " ]";
    }
    
}
