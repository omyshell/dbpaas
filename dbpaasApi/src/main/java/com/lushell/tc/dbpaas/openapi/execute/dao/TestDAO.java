/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.execute.dao;

import com.lushell.tc.dbpaas.openapi.execute.entity.EnviromentDO;
import com.lushell.tc.dbpaas.openapi.execute.entity.TestCaseDO;
import java.util.List;

/**
 *
 * @author tangchao
 */
public interface TestDAO {

    public String getDomian(String enviroment);
    public List<EnviromentDO> getAllEnviroment();
    
    public void saveOrUpdate(Object obj);

    public void delete(Class c, Integer id);
    public void delete(TestCaseDO obj);

    public List<TestCaseDO>  getTestCaseByEnviroment(String enviroment);
    public TestCaseDO getTestCaseById(Integer id);
}
