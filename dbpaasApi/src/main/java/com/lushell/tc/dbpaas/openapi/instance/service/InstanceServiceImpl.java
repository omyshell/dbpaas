/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.instance.service;

import com.lushell.tc.dbpaas.openapi.instance.dao.TaskDAO;
import com.lushell.tc.dbpaas.openapi.instance.entity.TaskDO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lushell.tc.dbpaas.utils.DataSource;
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
    public String getTask(int taskId) {
        TaskDO task = TaskDao.getTask(taskId);
        
        return task.getIp() + task.getRole() + task.getDataSync();
    }
}
