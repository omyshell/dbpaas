/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.api.instance.service;

import com.alibaba.fastjson.JSONObject;
import com.lushell.tc.dbpaas.utils.DataSource;

/**
 *
 * @author tangchao
 */
public interface InstanceService {
    @DataSource(name=DataSource.RS)
    public JSONObject getTask(int taskId);
}
