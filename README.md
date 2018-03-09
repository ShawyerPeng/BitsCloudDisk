# 简介
BitsCloudDisk（哔呲云盘），一个基于 Java 开发的公有云盘系统~  
后端采用 Spring + Spring MVC + Mybatis 框架编写，接口使用 RESTful 风格设计，实现了前后端分离；前端采用 Bootstrap + jQuery 以及一些其他组件编写。

# 功能描述
本系统实现了以下模块功能：
* 用户信息管理模块。用户可以通过注册免费获取一个自己的网络硬盘，然后进行登录就可以进行相应的在给定权限内的操作，如修改密码、修改个人资料、安全退出等。还包括会员充值和管理，会员用户有更多的特权和福利。
* 文件管理模块。用户在网盘内可以分类浏览各种格式的文件，可以预览编辑图片、视频、音乐、PDF、Office等类型的文件，可以进行文件/文件夹的创建、删除、重命名、移动，可以批量上传、下载文件，并且支持断点续传等高级功能。
* 文件分享模块。用户可以自由分享选定的文件或文件夹到公共空间、好友或群组，并且支持访问密码设置和取消分享功能。
* 回收站模块。用户删除文件到回收站后，可以进行文件的恢复与清空。
* 保险箱/私人空间模块：密码保护。
* 云笔记模块。用户可以在平台上创建笔记，并能在其他设备上同步浏览、编辑、分享笔记，方便办公场景。
* 图床模块。为博客用户提供图床平台，用户可上传多种格式的图片，生成URL链接后方便引用。

# TO-DO List
* 文档加入版本控制功能
* 离线下载、种子下载

# 开发及运行环境
* JDK 1.8 及以上
* Apache Tomcat 8 及以上
* MySQL 5.6.3 及以上

# 部署步骤
1. 导入数据库 `bits_cloud_disk.sql`
2. 导入项目到 IntelliJ IDEA
3. 修改数据库和文件下载地址的配置文件
    * 在 service/impl/FileService.java 中修改 `FILE_BASE` 为服务端保存文件的根路径
4. 部署启动项目
5. 浏览器访问：http://localhost:8080
6. 用户名:admin 密码: 123456

# 部分效果展示
用户登录：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/25604404.jpg)

文件下载：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/47315732.jpg)  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/77469506.jpg)

文件上传：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/67603566.jpg)

任务列表：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/4977357.jpg)  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/91170026.jpg)

文件搜索：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/55481353.jpg)

分类查看：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/61458204.jpg)  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/6414230.jpg)

回收站：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/23476963.jpg)

图床：  
![](http://oqcr0rg2c.bkt.clouddn.com/18-3-9/24522640.jpg)

图片不方便展示，更多功能还请自己运行体验~