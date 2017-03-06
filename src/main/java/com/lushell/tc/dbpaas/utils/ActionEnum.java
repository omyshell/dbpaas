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
public enum ActionEnum {

    SPACE_ACTION("0", "0", "0", 0),
    /**
     * action name(script name)，check script，ok status，timeout
     */
    INSTALL_MYSQL_INSTANCE("epcc_instance_install.sh", "check_instance_install.sh", "INSTANCE_INSTALL_OK", 30 * 60),
    INITALIZE_INSTANCE("epcc_instance_init.sh", "check_instance_init.sh", "INSTANCE_INIT_OK", 10 * 60),
    START_INSTANCE("epcc_instance_start.sh", "check_instance_start.sh", "INSTANCE_START_OK", 5 * 60),
    INITALIZE_SYSTEM_USER("epcc_instance_user_init.sh", "", "INIT_USER_OK", 0),
    INSTALL_MASTER_SEMI_SYNC("epcc_install_semi_sync_master.sh", "", "INSTALL_SEMI_SYNC_MASTER_OK", 0),
    SET_MASTER_SEMI_SYNC_ON("epcc_semi_sync_master_start.sh", "", "START_SEMI_SYNC_MASTER_OK", 0),
    ADD_SLAVE_TO_MASTER("epcc_add_slave.sh", "check_add_slave.sh", "ADD_SLAVE_OK", 15 * 60),
    SET_READ_ONLY("epcc_set_read_only.sh", "", "SET_READ_ONLY_OK", 0),
    START_SLAVE("epcc_start_slave.sh", "", "SLAVE_START_OK", 0),
    STOP_SLAVE("epcc_stop_slave.sh", "", "SLAVE_STOP_OK", 0),
    INSTALL_SLAVE_SEMI_SYNC("epcc_install_semi_sync_slave.sh", "", "INSTALL_SEMI_SYNC_SLAVE_OK", 0),
    SET_SLAVE_SEMI_SYNC_ON("epcc_semi_sync_slave_start.sh", "", "START_SEMI_SYNC_SLAVE_OK", 0),
    END(null, null, null, 0);

    private final String script;
    private final String checkScript;
    private final String okStatus;
    private final int timeout;

    private ActionEnum(String script, String checkScript, String okStatus, int timeout) {
        this.script = script;
        this.checkScript = checkScript;
        this.okStatus = okStatus;
        this.timeout = timeout;
    }

    public String getScript() {
        return script;
    }

    public String getCheckScript() {
        return checkScript;
    }

    public String getOkStatus() {
        return okStatus;
    }

    public int getTimeout() {
        return timeout;
    }

    public static final ActionEnum getBycript(String script) {
        if (script == null || script.isEmpty()) {
            return null;
        }
        for (ActionEnum item : ActionEnum.values()) {
            if (script.equals(item.getScript())) {
                return item;
            }
        }
        return null;
    }

    public boolean isSyncTask() {
        return timeout == 0;
    }
}


