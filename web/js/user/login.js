$(function() {
	
	//登陆处理
	/* 用户名框的失去焦点事件 */
	$("#count").blur(function() {
		if ($("#count").val() == "") {
			$("#user_span").html("用户名不能为空！");
		} else {
			$("#user_span").html("");
		}
	});
	/* 密码框的失去焦点事件 */
	$("#password").blur(function() {
		if ($("#password").val() == "") {
			$("#password_span").html("密码不能为空！");
		} else {
			$("#password_span").html("");
		}
	});
	/* 用户登录验证 */
	$("#login").click(function() {
		$("#user_span").html("");
		$("#password_span").html("");
		var ok = true;
		// 判断用户名或密码的格式
		if ($("#count").val() == "") {
			ok = false;
			$("#user_span").html("用户名不能为空！");
		}
		if ($("#password").val() == "") {
			ok = false;
			$("#password_span").html("密码不能为空！");
		}
		if (ok) {
			// 发送ajax请求
			$.ajax({
				url : "/Cloud_Note/user/login.do",
				type : "post",
				dataType : "json",
				data : {
					"username" : $("#count").val(),
					"password" : $("#password").val()
				},
				success : function(data) {
					if (data.status == 0) {
						//登录成功,将用户ID放到cookie中
						//获取用户ID
						var id=data.data;
						//将用户ID写入cookie,过期时间是5小时
						addCookie("cn_user_id",id,5);
						window.location.href = "edit.html";
					}
					if (data.status == 1) {
						$("#user_span").html(data.msg);
					}
					if (data.status == 2) {
						$("#password_span").html(data.msg);
					}
				},
				error : function() {
					// 异常处理
					alert("登陆异常");
				}
			});
		}
	})
})
