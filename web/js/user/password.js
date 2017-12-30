//修改密码
function updatePwd(){
		$.ajax({
			url:"/Cloud_Note/user/changePwd.do",
		type:"post",
		dataType:"json",
		data:{"lastpwd":$("#last_password").val(),"newpwd":$("#new_password").val(),"id":getCookie("cn_user_id")},
		success:function(result){
			if(result.status==0){
				alert("修改成功!");
				window.location.href="/Cloud_Note/html/login.html";
			}
		},
		error:function(){
			alert("修改密码异常");
		}
	})
}
$(function(){
	//文本框失去焦点函数
	$("#last_password").blur(function(){
		if($(this).val()==""){
			$("#last_password_span").html("*原始密码不能为空").css("color","red");
		}else{
			$("#last_password_span").html("");
		}
	});
	
	$("#new_password").blur(function(){
		if($(this).val()==""){
			$("#new_password_span").html("*新密码不能为空").css("color","red");
			return;
		}else if($(this).val().length<6&&$(this).val()>15){
			$("#new_password_span").html("*新密码长度在6~15之间").css("color","red");
		}else{
			$("#new_password_span").html("");
		}
		
	});
	
	$("#final_password").blur(function(){
		if($(this).val()==""){
			$("#final_password_span").html("*确认密码不能为空").css("color","red");
			return;
		}else if($("#new_password").val()!=$(this).val()){
			$("#final_password_span").html("*确认密码与新密码不一致").css("color","red");
		}else{
			$("#final_password_span").html("");
		}
		
	});
	//确定按钮点击修改密码事件
	$("#changePassword").click(function(){
		var ok=true;
		if($("#last_password").val()==""){
			ok=false;
			$("#last_password_span").html("*原始密码不能为空").css("color","red");
		}else{
			$("#last_password_span").html("");
		}
		
		if($("#new_password").val()==""){
			ok=false;
			$("#new_password_span").html("*新密码不能为空").css("color","red");
		}else if($("#new_password").val().length<6&&$(this).val()>15){
			ok=false;
			$("#new_password_span").html("*新密码长度在6~15之间").css("color","red");
		}else{
			$("#new_password_span").html("");
		}
		
		if($("#final_password").val()==""){
			ok=false;
			$("#final_password_span").html("*确认密码不能为空").css("color","red");
		}else if($("#new_password").val()!=$("#final_password").val()){
			ok=false;
			$("#final_password_span").html("*确认密码与新密码不一致").css("color","red");
		}else{
			$("#final_password_span").html("");
		}
		if(ok){
			var userid=getCookie("cn_user_id");
			var lastpassword=$("#last_password").val();
			//判断原始密码是否正确
			$.ajax({
				url:"/Cloud_Note/user/valiPwd.do",
				type:"post",
				dataType:"json",
				data:{"id":userid,"pwd":lastpassword},
				success:function(result){
					if(result.status==1){
						$("#last_password_span").html(result.msg).css("color","red");
					}else{
						$("#last_password_span").html("");
						//如果原始密码正确的话,修改密码
						updatePwd();
					}
				}
			})
		}
	});
});