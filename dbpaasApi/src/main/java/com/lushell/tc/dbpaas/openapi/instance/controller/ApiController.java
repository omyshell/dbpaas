/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.instance.controller;

import com.alibaba.fastjson.JSONObject;
import com.lushell.tc.dbpaas.openapi.instance.service.InstanceService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tangchao
 */
@Controller
@RequestMapping("/api/task")
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    InstanceService service;

    @ResponseBody
    @RequestMapping("/getTask")
    public String getTask(HttpServletRequest request) {
        //JSONObject result = new JSONObject();
        return service.getTask(8);
    }
}
