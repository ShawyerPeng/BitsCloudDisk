# 创建数据库
CREATE DATABASE IF NOT EXISTS `bits_cloud_disk` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
# 进入数据库
USE `bits_cloud_disk`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `username` VARCHAR(32) UNIQUE NOT NULL COMMENT '用户名',
  `password` VARCHAR(32) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(32) UNIQUE DEFAULT '请更换昵称' COMMENT '用户昵称',
  `realname` VARCHAR(32) DEFAULT '' COMMENT '真实姓名',
  `gender` TINYINT(4) DEFAULT 0 COMMENT '性别（0:未知，1:男，2:女）',
  `phone` VARCHAR(11) DEFAULT '' COMMENT '手机号码',
  `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱地址',
  `iconimg` VARCHAR(180) DEFAULT 'avatar.jpg' COMMENT '用户头像',
  `info` VARCHAR(255) DEFAULT '' COMMENT '个人简介',
  `level` INT(2) UNSIGNED DEFAULT 1 COMMENT '用户等级',
  `is_vip` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0:不是vip, 1:是vip',
  `memory_size` BIGINT(20) UNSIGNED DEFAULT 1073741824 COMMENT '用户默认内存(1G)',
  `used_size` BIGINT(20) UNSIGNED DEFAULT 0 COMMENT '用户已用内存',
  `private_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否开启私密空间（0:未开启，1:已开启）',
  `private_pass` VARCHAR(32) COMMENT '私密空间密码',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间',
  `last_login` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '账号最近登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='用户信息表';

CREATE TABLE `origin_file` (
  `origin_file_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '原始文件ID，主键',
  `file_md5` char(32) NOT NULL DEFAULT '' COMMENT 'md5值',
  `file_size` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '文件大小(字节)',
  `file_path` varchar(1000) NOT NULL DEFAULT '' COMMENT '保存路径',
  `file_count` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '引用计数/下载次数',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  # `user_id` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '上传者id',
  `file_status` tinyint(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '文件状态[1=正常][2=不允许上传][3=未经允许的上传][4=已屏蔽]',
  PRIMARY KEY (`origin_id`),
  KEY `file_md5` (`file_md5`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='源文件';

CREATE TABLE `user_file` (
    `file_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '用户文件ID，主键',
    `user_id` INT(11) UNSIGNED NOT NULL COMMENT '文件所属者',
    `file_name` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '文件名称',
    `parent_path` varchar(1000) COLLATE utf8_bin NOT NULL DEFAULT '/' COMMENT '父级路径',
    `file_type` tinyint(2) UNSIGNED NOT NULL DEFAULT '0' COMMENT '文件类型',
    `file_size` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '文件大小',
    `file_status` tinyint(2) UNSIGNED NOT NULL DEFAULT '5' COMMENT '文件状态  [3=删除中][4=回收站][5=正常状态][6=已分享]',
    `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
    `modify_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
    `is_shared` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:私有(未分享) 1:共享(已分享)',
    `is_dir` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否是文件夹，[0=文件]，[1=文件夹]',
    `is_deleted` int(1) COMMENT '文件删除标志 (1:表示在回收站)',
    `delete_time` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除时间',
    `origin_id` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT ' 源文件id',
    `origin_path` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'oss保存路径',
    KEY `uid` (`user_id`) USING BTREE,
    KEY `parent_path` (`parent_path`(255)) USING BTREE,
    KEY `file_name` (`file_name`) USING BTREE,
    CONSTRAINT `file_id` FOREIGN KEY (`file_id`) REFERENCES `origin_file` (`origin_file_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户文件信息表';

CREATE TABLE `share` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(30) not null COMMENT '文件分享者ID',
  `file_id` text not null COMMENT '文件ID',
  `share_type` INT not null COMMENT '分享类型，共享，好友共享，私密共享',
  `share_pass` varchar(30) COMMENT '分享密码，如果没设的话，表示共享',
  `share_time` datetime not null COMMENT '分享时间',
  `share_deadline` datetime COMMENT '分享有效时间',
  `share_link` INT not null COMMENT '分享连接',
  `thumb_up` INT COMMENT '点赞次数',
  `thumb_down` INT COMMENT '反对次数',
  CONSTRAINT UserID_ShareFile_FK FOREIGN KEY () References UserInfo(UserID) -- 设置 UserID 为外键
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='文件分享表';

-- 插入 / 更新 / 插入日志表
Create table UpdateLog(
  UserID varchar(30) not null,   -- 用户 ID，外键
  UserDO varchar(80) not null,  -- 用户做的操作
  DOTime datetime not null, -- 操作的时间
  Constraint UserID_UpdateLog_FK Foreign Key(UserID) References UserInfo(UserID) -- 设置 UserID 为外键
);

-- 好友表
Create table `friend`(
  UserID varchar(30) not null,   -- 用户 ID，外键
  FriendType varchar(30) default '我的好友',  -- 好友类型
  FriendID varchar(30) not null, -- 好友 ID
  AddTime datetime not null, -- 添加时间
  Constraint UserID_FriendInfo_FK Foreign Key(UserID) References UserInfo(UserID) -- 设置 UserID 为外键
    Constraint FK_FrinedID foreign key(FriendID) references UserInfo(UserID)	-- 设置 FrinedID 为外键
);
-- 删除表
-- drop table FriendInfo;

alter table FriendInfo add Constraint FK_FrinedID foreign key(FriendID) references UserInfo(UserID)