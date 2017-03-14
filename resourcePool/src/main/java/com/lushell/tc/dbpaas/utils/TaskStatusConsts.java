/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

/**
 *
 * @author tangchao
 */
public class TaskStatusConsts {
    public static final String FAILED = "FAILED";  // 失败终止任务
    public static final String SUCCESS = "SUCCESS";  // 单个任务执行成功
    public static final String RUNNING = "RUNNING"; // 任务正在运行，通过CHECKER检测运行，这里有超时时间
    public static final String START = "START"; // 过渡状态，被设置为START
    public static final String WAITING = "WAITING"; // 阻塞任务状态
    public static final String FINISHED = "FINISHED"; //整个任务全部完成
    public static final String WAIT = "WAIT"; //等待执行的任务，检测到状态立即开始执行
    public static final String TIMEOUT = "TIMEOUT";
}
