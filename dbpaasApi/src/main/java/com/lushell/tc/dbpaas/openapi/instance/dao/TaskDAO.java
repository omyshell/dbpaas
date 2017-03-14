/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.instance.dao;

import com.lushell.tc.dbpaas.openapi.instance.entity.TaskDO;

/**
 *
 * @author tangchao
 */
public interface TaskDAO {

    public TaskDO getTask(int taskId);

    public int startTask(int taskId);
}
