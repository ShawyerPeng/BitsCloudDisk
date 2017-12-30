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
    `file_status` TINYINT(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '文件状态，[1=正常][2=回收站][3=已删除][4=未分享][5=已分享][6=私密]',
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

DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '文件分享者ID',
  `file_id` INT(11) UNSIGNED NOT NULL COMMENT '文件ID',
  `share_type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '分享类型，[1:公共分享][2:私密共享][3:好友共享][4:群组共享]',
  `share_pass` VARCHAR(30) DEFAULT '' COMMENT '分享密码',
  `share_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  `share_deadline` TIMESTAMP NOT NULL COMMENT '分享有效时间',
  `share_link` VARCHAR(255) NOT NULL COMMENT '分享链接',
  `thumb_up` INT COMMENT '点赞次数',
  `thumb_down` INT COMMENT '反对次数'
#   CONSTRAINT UserID_ShareFile_FK FOREIGN KEY () References UserInfo(UserID) -- 设置 UserID 为外键
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='文件分享表';

DROP TABLE IF EXISTS `signin`;
CREATE TABLE `signin` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '用户ID',
  `sign_count` int(11) DEFAULT NULL COMMENT '连续签到次数',
  `before_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '前天签到排名',
  `yesterday_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '昨天签到排名',
  `today_rank` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '今天签到排名',
  `sign_history` bigint(4) DEFAULT NULL COMMENT '签到历史，bit位数表示历史签到',
  `modify_time` datetime DEFAULT NULL COMMENT '签到时间(也即修改时间)',
  `ext` varchar(32) DEFAULT NULL COMMENT '预留字段'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='每日签到表';

DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '图片名称',
  `md5` CHAR(32) NOT NULL UNIQUE COMMENT 'md5值',
  `url` VARCHAR(255) NOT NULL UNIQUE COMMENT '图片保存路径',
  `description` VARCHAR(100) DEFAULT NULL COMMENT '图片描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='图床表';

DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '用户ID',
  `title` VARCHAR(128) NOT NULL COMMENT '笔记标题',
  `content` TEXT NOT NULL COMMENT '笔记内容',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否删除[0:未删除][1:已删除]',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='云笔记表';

CREATE TABLE `friend` (
  `id` INT(11) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键',
  `friend_type` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '好友类型[1:个人好友][2:群组好友]）',
  `user_id` INT(11) UNSIGNED NOT NULL COMMENT '用户ID',
  `friend_id` INT(11) UNSIGNED NOT NULL COMMENT '好友ID',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '好友关系建立时间',
  CONSTRAINT FK_UserId_UserId FOREIGN KEY(user_id) REFERENCES user(user_id),    # 设置 UserId 为外键
  CONSTRAINT FK_FriendId_UserId FOREIGN KEY(friend_id) REFERENCES user(user_id)	# 设置 FriendId 为外键
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=10000 COMMENT='好友表';

-- 插入 / 更新 / 插入日志表
CREATE TABLE `UpdateLog` (
  UserID varchar(30) not null COMMENT '',   -- 用户 ID，外键
  UserDO varchar(80) not null COMMENT '',  -- 用户做的操作
  DOTime datetime not null COMMENT '', -- 操作的时间
  Constraint UserID_UpdateLog_FK Foreign Key(UserID) References UserInfo(UserID) -- 设置 UserID 为外键
);