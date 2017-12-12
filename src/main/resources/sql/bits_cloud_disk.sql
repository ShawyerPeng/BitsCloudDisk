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
  `private_status` TINYINT(1) DEFAULT 0 COMMENT '是否开启私密空间（0:未开启，1:已开启）',
  `private_pass` VARCHAR(32) COMMENT '私密空间密码',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间',
  `last_login` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '账号最近登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='用户信息表';
INSERT INTO `user` VALUES('admin','123456','users/photo/001.jpg',0,null);

CREATE TABLE `origin_file` (
  `origin_file_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '原始文件ID，主键',
  `file_md5` CHAR(32) NOT NULL UNIQUE COMMENT 'md5值',
  `file_size` BIGINT(20) UNSIGNED NOT NULL COMMENT '文件大小(字节)',
  `file_type` VARCHAR(100) NOT NULL DEFAULT 'UNKNOWN' COMMENT '文件的真实类型',
  `file_url` VARCHAR(255) NOT NULL UNIQUE COMMENT '文件保存路径',
  `file_count` INT(11) UNSIGNED DEFAULT 1 COMMENT '引用计数/文件拥有者数量',
  `file_status` TINYINT(2) UNSIGNED DEFAULT '1' COMMENT '文件状态[1=正常][2=不允许上传][3=未经允许的上传][4=已屏蔽]',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '文件修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='源文件(多用户共享一份)';

CREATE TABLE `user_file` (
    `file_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '用户文件ID，主键',
    `user_id` INT(11) UNSIGNED NOT NULL COMMENT '文件所属者ID',
    `parent_id` INT(11) UNSIGNED NOT NULL COMMENT '父级文件夹ID',
    `origin_id` INT(11) UNSIGNED NOT NULL COMMENT '源文件ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名称',
    `file_type` VARCHAR(100) NOT NULL DEFAULT 'UNKNOWN' COMMENT '文件类型（用户可以修改后缀名，但不会影响真实文件类型）',
    `file_status` TINYINT(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '文件状态，[1=正常状态][2=回收站][3=已删除][4=未分享][5=已分享][6=私密]',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件创建时间',
    `modify_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '文件修改时间',
    `delete_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '文件删除时间',
    CONSTRAINT `file_id_FK` FOREIGN KEY (`file_id`) REFERENCES `origin_file` (`origin_file_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='用户文件信息表';

DROP TABLE IF EXISTS `user_folder`;
CREATE TABLE `user_folder` (
    `folder_id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '文件夹ID',
    `user_id` INT(11) UNSIGNED NOT NULL COMMENT '文件夹所属者ID',
    `parent_id` INT(11) UNSIGNED NOT NULL COMMENT '父级文件夹ID',
    `folder_name` VARCHAR(255) NOT NULL COMMENT '文件夹名称',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件夹创建时间',
    `modify_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '文件夹修改时间',
    `delete_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '文件夹删除时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='文件夹信息表';
INSERT INTO `user_folder` VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'home',0);
INSERT INTO `user_folder` VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'safebox',0);
INSERT INTO `user_folder` VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'trashbox',0);

CREATE TABLE `share` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '文件分享者ID',
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


CREATE TABLE `signin` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '用户ID',
  `sign_count` int(11) DEFAULT NULL COMMENT '连续签到次数',
  `before_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '前天签到排名',
  `yesterday_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '昨天签到排名',
  `today_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '今天签到排名',
  `sign_history` bigint(4) DEFAULT NULL COMMENT '签到历史，bit位数表示历史签到',
  `modify_time` datetime DEFAULT NULL COMMENT '签到时间(也即修改时间)',
  `ext` varchar(32) DEFAULT NULL COMMENT '预留字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='每日签到表';


-- 插入 / 更新 / 插入日志表
Create table `UpdateLog` (
  UserID varchar(30) not null,   -- 用户 ID，外键
  UserDO varchar(80) not null,  -- 用户做的操作
  DOTime datetime not null, -- 操作的时间
  Constraint UserID_UpdateLog_FK Foreign Key(UserID) References UserInfo(UserID) -- 设置 UserID 为外键
);

-- 好友表
Create table `friend` (
  UserID varchar(30) not null,   -- 用户 ID，外键
  FriendType varchar(30) default '我的好友',  -- 好友类型
  `friend_id` varchar(30) not null, -- 好友 ID
  AddTime datetime not null, -- 添加时间
  Constraint UserID_FriendInfo_FK Foreign Key(UserID) References UserInfo(UserID) -- 设置 UserID 为外键
    Constraint FK_FrinedID foreign key(FriendID) references UserInfo(UserID)	-- 设置 FrinedID 为外键
);
-- 删除表
-- drop table FriendInfo;

alter table FriendInfo add Constraint FK_FrinedID foreign key(FriendID) references UserInfo(UserID)