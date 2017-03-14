/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.api.instance.dao;

import com.lushell.tc.dbpaas.api.instance.entity.TaskDO;
import java.util.List;

/**
 *
 * @author tangchao
 */
public interface TaskDAO {

    public List<TaskDO> getAllTask();

    public TaskDO getTask(int taskId);

    public boolean setTaskReady(int taskId, int ready);
}
