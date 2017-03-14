/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.execute.service;

import com.alibaba.fastjson.JSONObject;
import com.lushell.tc.dbpaas.openapi.model.TestData;
import java.util.List;
import com.lushell.tc.dbpaas.openapi.execute.entity.TestCaseDO;
import com.lushell.tc.dbpaas.utils.DataSource;

/**
 *
 * @author tangchao
 */
public interface TestService {
    @DataSource(name=DataSource.RS)
    public List<String> createTestCase(String enviroment, String apiName, String api);
    @DataSource(name=DataSource.RS)
    public void modifyApiExample(Integer id,  List<TestData> testExampleCache);
    @DataSource(name=DataSource.RS)
    public List<TestCaseDO> getAllTestCase(String enviroment);
    @DataSource(name=DataSource.RS)
    public List<JSONObject>  getAllEnviroment();
    @DataSource(name=DataSource.RS)
    public List<TestData> executeCc(String enviroment);
    @DataSource(name=DataSource.RS)
    public List<String> getApiNameForEnviroment(String enviroment);
    @DataSource(name=DataSource.RS)
    public void deleteById(Integer id, List<TestData> testExampleCache);
}
