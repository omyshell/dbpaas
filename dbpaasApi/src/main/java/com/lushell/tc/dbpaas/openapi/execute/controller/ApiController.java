/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.execute.controller;

import com.alibaba.fastjson.JSONObject;
import com.lushell.tc.dbpaas.openapi.execute.service.TestService;
import com.lushell.tc.dbpaas.openapi.model.TestData;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tangchao
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    TestService testService;

    List<TestData> testExampleCache;

    @ResponseBody
    @RequestMapping("/getEnviromentList")
    public List<JSONObject> getEnviromentList() {
        return testService.getAllEnviroment();
    }

    @ResponseBody
    @RequestMapping("/add")
    public JSONObject addTestModel(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String apiName = request.getParameter("apiName");
        String api = request.getParameter("api");
        String enviroment = request.getParameter("enviroment");
        List<String> apiGroupByName = testService.createTestCase(enviroment, apiName, api);
        return result;
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    public JSONObject delete(@PathVariable("id") Integer id) {
        JSONObject result = new JSONObject();
        testService.deleteById(id, testExampleCache);
        return result;
    }
}
