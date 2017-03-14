/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.api.instance.dao;

import com.lushell.tc.dbpaas.api.instance.entity.TaskDO;
import com.lushell.tc.dbpaas.utils.BaseDaoSupport;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tangchao
 */
@Repository("TaskDAO")
public class TaskDAOImpl extends BaseDaoSupport implements TaskDAO {

    @Override
    public List<TaskDO> getAllTask() {
        Query query = getSession().createQuery("from TaskDO");
        return query.list();
    }
    
    @Override
    public TaskDO getTask(int taskId) {
        Query query = getSession().createQuery("from TaskDO where task_id=:task_id");
        query.setInteger("task_id", taskId);
        return (TaskDO) query.uniqueResult();
    }

    @Override
    public boolean setTaskReady(int taskId, int ready) {
        Query query = getSession().createQuery("update TaskDO set task_ready=:ready "
                + "where task_id=:id");
        query.setInteger("id", taskId);
        query.setInteger("ready", ready);
        return query.executeUpdate() == 1;
    }
}
