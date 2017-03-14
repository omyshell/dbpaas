/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.api.instance.service;

import com.lushell.tc.dbpaas.api.instance.dao.TaskDAO;
import com.lushell.tc.dbpaas.api.instance.entity.TaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lushell.tc.dbpaas.utils.DataSource;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tangchao
 */
@Transactional
@Service("InstanceService")
public class InstanceServiceImpl implements InstanceService {

    private final Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Autowired
    private TaskDAO TaskDao;

    @DataSource(name = DataSource.RS)
    @Override
    public JSONObject getTask(int taskId) {
        JSONObject result = new JSONObject();
        TaskDO task = TaskDao.getTask(taskId);
        result.put("task_id", task.getTaskId());
        result.put("status", task.getStatus());
        result.put("task_name", task.getTaskName());
        result.put("ip", task.getIp());
        result.put("role", task.getRole());
        result.put("sync", task.getDataSync());
        result.put("master", task.getMasterIp());
        result.put("master_port", task.getMasterPort());
        result.put("vip", task.getVip());
        result.put("aip", task.getArbitIp());
        return result;
    }
}
