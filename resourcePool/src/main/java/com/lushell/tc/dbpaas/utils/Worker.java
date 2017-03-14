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
        
    public int exec() {
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
            System.out.println("[" + command + "]");
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

           // long tStart=System.currentTimeMillis();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    exitInfo += new String(tmp, 0, i);
                }
                //long eStart = System.currentTimeMillis();
               // if ((eStart - tStart) > 900) {
               //     break;
               // }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    exitStatus = channel.getExitStatus();
                    System.out.println(exitInfo);
                    System.out.println("exit-status " + exitStatus);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            System.err.println("SSH ERROR==>>" + e.getMessage());
            exitStatus = 2;
        }
        return exitStatus;
    }

    public String getExitInfo() {
        return exitInfo;
    }
}
