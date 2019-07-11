/*Table structure for table `pt_menu` */

DROP TABLE IF EXISTS `pt_menu`;

CREATE TABLE `pt_menu` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `menuid` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(50) NOT NULL DEFAULT '',
  `orderno` smallint(6) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_menu_module` */

DROP TABLE IF EXISTS `pt_menu_module`;

CREATE TABLE `pt_menu_module` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `menuid` varchar(32) NOT NULL DEFAULT '',
  `moduleid` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_module` */

DROP TABLE IF EXISTS `pt_module`;

CREATE TABLE `pt_module` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `moduleid` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '',
  `key` varchar(50) NOT NULL DEFAULT '',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=页面2=接口',
  `orderno` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_parm` */

DROP TABLE IF EXISTS `pt_parm`;

CREATE TABLE `pt_parm` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(50) NOT NULL DEFAULT '',
  `value` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`seqid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `pt_role` */

DROP TABLE IF EXISTS `pt_role`;

CREATE TABLE `pt_role` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleid` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_role_module` */

DROP TABLE IF EXISTS `pt_role_module`;

CREATE TABLE `pt_role_module` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleid` varchar(32) NOT NULL DEFAULT '',
  `moduleid` varchar(32) NOT NULL DEFAULT '',
  `orderno` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_role_user` */

DROP TABLE IF EXISTS `pt_role_user`;

CREATE TABLE `pt_role_user` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleid` varchar(32) NOT NULL DEFAULT '',
  `userid` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`seqid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pt_user_info` */

DROP TABLE IF EXISTS `pt_user_info`;

CREATE TABLE `pt_user_info` (
  `seqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(32) NOT NULL DEFAULT '',
  `password` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '',
  `tel` varchar(11) NOT NULL DEFAULT '' COMMENT '电话号码',
  `email` varchar(500) NOT NULL DEFAULT '' COMMENT '邮箱',
  `openid` varchar(255) NOT NULL DEFAULT '' COMMENT 'OPENID',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '(1正常11超级管理员)(-1禁用)',
  `inputtime` bigint(20) NOT NULL DEFAULT '0',
  `ssid` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`seqid`),
  UNIQUE KEY `idx_userid` (`userid`),
  KEY `idx_ssid` (`ssid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
