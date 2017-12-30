$(function () {
    //注册处理
    //文本框失去焦点事件
    $("#regist_username").blur(function () {
        if ($("#regist_username").val() == "") {
            $("#warning_1").css("display", "block").html("用户名不能为空!");
        } else {
            $("#warning_1").css("display", "none");
        }
    });
    $("#regist_password").blur(function () {
        if ($("#regist_password").val() == "") {
            $("#warning_2").css("display", "block").html("密码不能为空!");
        } else if ($("#regist_password").val().length < 6) {
            $("#warning_2").css("display", "block").html("密码长度至少六位!");
        } else {
            $("#warning_2").css("display", "none");
        }
    });
    $("#final_password").blur(function () {
        if ($("#final_password").val() == "") {
            $("#warning_3").css("display", "block").html("确认密码不能为空!");
        } else if ($("#final_password").val() != $("#regist_password").val()) {
            $("#warning_3").css("display", "block").html("与密码不一致!");
        } else {
            $("#warning_3").css("display", "none");
        }
    });
    //注册按钮的点击事件
    $("#regist_button").click(function () {
        var ok = true;
        //进行验证
        if ($("#regist_username").val() == "") {
            ok = false;
            $("#warning_1").css("display", "block").html("用户名不能为空!");
        } else {
            $("#warning_1").css("display", "none");
        }

        if ($("#regist_password").val() == "") {
            ok = false;
            $("#warning_2").css("display", "block").html("密码不能为空!");
        } else if ($("#regist_password").val().length < 6) {
            ok = false;
            $("#warning_2").css("display", "block").html("密码长度至少六位!");
        } else {
            $("#warning_2").css("display", "none");
        }

        if ($("#final_password").val() == "") {
            ok = false;
            $("#warning_3").css("display", "block").html("确认密码不能为空!");
        } else if ($("#final_password").val() != $("#regist_password").val()) {
            ok = false;
            $("#warning_3").css("display", "block").html("与密码不一致!");
        } else {
            $("#warning_3").css("display", "none");
        }
        //发送AJAX请求
        if (ok) {
            $.ajax({
                url: "/Cloud_Note/user/registe.do",
                type: "post",
                dataType: "json",
                data: {
                    "regist_username": $("#regist_username").val(),
                    "nickname": $("#nickname").val(),
                    "regist_password": $("#regist_password").val()
                },
                async: true,
                success: function (result) {
                    if (result.status == 0) {
                        alert(result.msg);
                        //注册成功返回登录页面
                        //触发返回按钮里的单击事件
                        $("#back").click();
                    } else {
                        $("#warning_1").css("display", "block").html("用户名已存在!");
                    }
                },
                error: function () {
                    alert("注册异常");
                }
            })
        }
    })
})