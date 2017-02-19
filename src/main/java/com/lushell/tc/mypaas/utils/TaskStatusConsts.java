/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.utils;

/**
 *
 * @author tangchao
 */
public class TaskStatusConsts {
    public static String FAILED = "FAILED";  // 失败终止任务
    public static String SUCCESS = "SUCCESS";  // 单个任务执行成功
    public static String RUNNING = "RUNNING"; // 任务正在运行，通过CHECKER检测运行，这里有超时时间
    public static String START = "START"; // 过渡状态，被设置为START
    public static String WAITING = "WAITING"; // 阻塞任务状态
    public static String FINISHED = "FINISHED"; //整个任务全部完成
    public static String WAIT = "WAIT"; //等待执行的任务，检测到状态立即开始执行
}
