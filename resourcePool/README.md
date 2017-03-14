# dbpaas

用于一次性大批量服务器采购快速部署MYSQL实例，配置同步，创建缺省用户等工作流。

CREATE TABLE `epcc_mysql_instance_task` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip` varchar(46) NOT NULL,
  `port` int(10) unsigned NOT NULL,
  `role` char(6) NOT NULL DEFAULT 'Master' COMMENT 'master和slave，默认是master',
  `data_sync` varchar(10) NOT NULL DEFAULT 'ASYNC' COMMENT 'ASYNC异步\nSEMISYNC半同步\n默认是ASYNC异步',
  `read_only` tinyint(3) unsigned NOT NULL COMMENT '1表示只读，0表示可写',
  `status` varchar(100) NOT NULL DEFAULT 'WAIT' COMMENT '''默认是WAIT，设置为start开始做，running是正在做，done是运行完毕，failed表示失败，ready是经过验收表示已经准备好’，finished是做完，表示实例数据已经进入epcc_db_meta。',
  `task_ready` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `task_name` varchar(100) DEFAULT NULL,
  `failed_count` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `master_ip` varchar(46) DEFAULT NULL,
  `master_port` int(10) unsigned DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `task_begin_time` int(10) unsigned DEFAULT NULL,
  `task_done_time` int(10) unsigned DEFAULT NULL,
  `deploy_ha` tinyint(4) DEFAULT NULL,
  `vip` varchar(46) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

