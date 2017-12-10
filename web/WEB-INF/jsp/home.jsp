<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="../utils/dropzone/dropzone.min.css">
<script src="../utils/dropzone/dropzone.min.js"></script>

    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link href="https://cdn.bootcss.com/bootstrap3-dialog/1.35.4/css/bootstrap-dialog.min.css">
    <script src="https://cdn.bootcss.com/bootstrap3-dialog/1.35.4/js/bootstrap-dialog.min.js"></script>

    <script>
        Dropzone.options.myAwesomeDropzone = {
                dictDefaultMessage: "拖拽上传即可上传图片",
                dictFallbackMessage:"你的浏览器不支持此类上传模式"
        };
    </script>
<style>
.dropzone {
    border: 2px dashed #0087F7;
    border-radius: 5px;
    background: white;
}
#dropzone{
    max-width: 720px;
    margin-left: auto;
    margin-right: auto;
}
</style>
</head>

<body>
<div id="dropzone">
    <form action="<%=request.getContextPath()%>/upload" class="dropzone">
        <div class="fallback">
            <input name="file" type="file"/>
        </div>
    </form>
</div>
</body>
</html>