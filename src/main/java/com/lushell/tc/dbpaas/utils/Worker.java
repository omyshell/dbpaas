/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.lushell.tc.dbpaas.configration.PropertyCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author tangchao
 */
public class Worker {

    private final String host;
    private final String command;
    private int exitStatus;
    private String exitInfo;

    public Worker(String host, String command) {
        this.host = host;
        this.command = command;
        this.exitStatus = 0;
        this.exitInfo = null;
    }

    public String exec() {
        exitInfo = "";
        Session session;
        ChannelExec channel;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(PropertyCache.getMysqlSshUser(), host, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(PropertyCache.getMysqlSshPsw());
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            //((ChannelExec) channel).setErrStream(System.err);
            channel.connect();

                        InputStream in = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf;
            while ((buf = reader.readLine()) != null) {
                 exitInfo += new String(buf.getBytes("UTF-8"), "UTF-8");
            }
            exitStatus = channel.getExitStatus();
            System.out.println("exie Status " + exitStatus + " [" + command + "]" + exitInfo);
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            exitInfo += e.getMessage();
            System.err.println("SSH ERROR==>>" + exitInfo);
            exitStatus = 2;
        }
        return exitInfo;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public String getExitInfo() {
        return exitInfo;
    }
}
