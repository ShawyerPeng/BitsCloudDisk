/* set MySQL default character set to utf-8 before create database */

CREATE DATABASE onecloud;
USE onecloud;

CREATE TABLE user (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	ldt_create DATETIME NOT NULL,
	ldt_modified DATETIME NOT NULL,

	username VARCHAR(20) NOT NULL UNIQUE,
	password VARCHAR(20) NOT NULL,
	nickname VARCHAR(30) NOT NULL,
	photo_url VARCHAR(255) NOT NULL UNIQUE,
	used_capacity BIGINT UNSIGNED NOT NULL,
	safe_password VARCHAR(20)
);

/* type保存文件的真实类型 */
CREATE TABLE file (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	ldt_create DATETIME NOT NULL,
	ldt_modified DATETIME NOT NULL,

	md5 CHAR(32) NOT NULL UNIQUE,
	size BIGINT UNSIGNED NOT NULL,
	type VARCHAR(100) NOT NULL,
	url VARCHAR(255) NOT NULL UNIQUE
);

/*
 *关于插入新记录时的查重：
 *客户端判断出命名有冲突，将该信息传递给服务器
 *服务器判断两个文件是否是同一个文件，如果是，不插入新记录；否则在名字后面加上(1)，插入
 */
 
 /* 为了让唯一约束生效，把没有后缀约定为"" */
CREATE TABLE local_file (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	ldt_create DATETIME NOT NULL,
	ldt_modified DATETIME NOT NULL,

	user_id BIGINT UNSIGNED NOT NULL,
	file_id BIGINT UNSIGNED NOT NULL,
	local_name VARCHAR(255) NOT NULL,
	local_type VARCHAR(255) NOT NULL,
	parent BIGINT UNSIGNED NOT NULL,
	

	UNIQUE(user_id, parent, local_name, local_type)
);

/* 文件夹的查重在客户端进行 */
CREATE TABLE local_folder (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	ldt_create DATETIME NOT NULL,
	ldt_modified DATETIME NOT NULL,

	user_id BIGINT UNSIGNED NOT NULL,
	local_name VARCHAR(255) NOT NULL,
	parent BIGINT UNSIGNED NOT NULL,


	UNIQUE(user_id, parent, local_name)
);

/* Insert initial data */
INSERT INTO user VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10','admin','admin','admin','users/photo/001.jpg',0,null);

INSERT INTO local_folder VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'home',0);
INSERT INTO local_folder VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'safebox',0);
INSERT INTO local_folder VALUES(null,'2017-10-10 10:10:10','2017-10-10 10:10:10',0,'trash',0);


