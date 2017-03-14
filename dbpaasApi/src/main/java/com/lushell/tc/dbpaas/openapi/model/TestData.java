/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.model;

/**
 *
 * @author tangchao
 */
public class TestData  {

    private int id;
    private String apiName;
    private String api;
    private String result;
    private String realResult;

    public TestData() {
    }

    public TestData(int id, String apiName, String api, String result, String realResult) {
        this.id = id;
        this.result = result;
        this.apiName = apiName;
        this.api = api;
        this.realResult = realResult;
    }

    public String getRealResult() {
        return realResult;
    }

    public void setRealResult(String realResult) {
        this.realResult = realResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
