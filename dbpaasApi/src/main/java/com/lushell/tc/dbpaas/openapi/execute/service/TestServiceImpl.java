/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.execute.service;

import com.alibaba.fastjson.JSONObject;
import com.lushell.tc.dbpaas.openapi.execute.entity.EnviromentDO;
import com.lushell.tc.dbpaas.openapi.model.TestData;
import com.lushell.tc.dbpaas.openapi.utils.MyHttpUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lushell.tc.dbpaas.openapi.execute.entity.TestCaseDO;
import com.lushell.tc.dbpaas.openapi.execute.dao.TestDAO;
import com.lushell.tc.dbpaas.utils.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tangchao
 */
@Transactional
@Service("TestService")
public class TestServiceImpl implements TestService {

    private final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Autowired
    private TestDAO testDao;
    
    private String getEnviroment(String enviroment) {
        if (enviroment == null) {
            return null;
        }
        if ("product".equals(enviroment.toLowerCase()) 
                || "pre".equals(enviroment.toLowerCase())) {
            logger.info(enviroment + ":product");
            return "product";
        } else {
            logger.info(enviroment + ":offline");
            return "offline";
        }
    }

    @DataSource(name=DataSource.RS)
    @Override
    public List<TestCaseDO> getAllTestCase(String enviroment) {
        return testDao.getTestCaseByEnviroment(getEnviroment(enviroment));
    }

    @DataSource(name = DataSource.RS)
    @Override
    public List<JSONObject> getAllEnviroment() {
        List<EnviromentDO> list = testDao.getAllEnviroment();
        List<JSONObject> data = new ArrayList<JSONObject>();
        for (EnviromentDO e : list) {
            JSONObject json = new JSONObject();
            json.put("name", e.getEnviroment());
            json.put("site", e.getUrl());
            json.put("id", e.getId());
            data.add(json);
        }
        return data;
    }

    @DataSource(name=DataSource.RS)
    @Override
    public List<String> createTestCase(String enviroment, String apiName, String api) {
        TestCaseDO object;  
        object = new TestCaseDO();
        object.setEnviroment(enviroment);
        object.setApiName(apiName);
        object.setApi(api);
        testDao.saveOrUpdate(object);
        return getApiNameForEnviroment(getEnviroment(enviroment));
    }
    
    @DataSource(name=DataSource.RS)
    @Override
    public List<TestData> executeCc(String enviroment) {
        List<TestData> list;
        String domain;
        List<TestCaseDO> allCase;
        
        if (enviroment == null) {
            return null;
        }
        
        domain = testDao.getDomian(enviroment);
        if (domain == null) {
            return null;
        }
        
        enviroment = getEnviroment(enviroment);
        allCase = testDao.getTestCaseByEnviroment(enviroment);
        if (allCase == null) {
            return null;
        }

        list = new ArrayList<TestData>();
        for (TestCaseDO api : allCase) {
            String realResult = MyHttpUtil.getDataFromUrl(domain + api.getApi(), 10);
            if (realResult == null) {
                continue;
            }
        }

        return list;
    }

    @DataSource(name=DataSource.RS)
    @Override
    public List<String> getApiNameForEnviroment(String enviroment) {
        List<String> list;
        list = new ArrayList<String>();
        List<TestCaseDO> apiList = testDao.getTestCaseByEnviroment(getEnviroment(enviroment));
        for (TestCaseDO api : apiList) {
            String name = api.getApiName();
            if (!list.contains(name)) {
                list.add(name);
            }
        }
        return list;
    }

    @DataSource(name=DataSource.RS)
    @Override
    public void deleteById(Integer id, List<TestData> testExampleCache) {
        testDao.delete(TestCaseDO.class, id);
        Iterator<TestData> it = testExampleCache.iterator();
        while (it.hasNext()) {
            TestData data = it.next();
            if (id == data.getId()) {
                it.remove();
                break;
            }
        }
    }

    @DataSource(name=DataSource.RS)
    @Override
    public void modifyApiExample(Integer id, List<TestData> testExampleCache) {
        TestCaseDO api;
        if (id == null) {
            return;
        }
        api = testDao.getTestCaseById(id);
        if (api != null) {
            Iterator<TestData> it = testExampleCache.iterator();
            while (it.hasNext()) {
                TestData data = it.next();
                if (api.getId() == data.getId()) {
                    api.setResult(data.getRealResult());
                    it.remove();
                    break;
                }
            }
            testDao.saveOrUpdate(api);
        }
    }
}
