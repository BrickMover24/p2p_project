//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});

	// 给手机号输入框添加一个失去焦点的事件
	$("#phone").on("blur",function () {
		var phone = $.trim($("#phone").val());
		if (phone == "") {
			showError("phone","手机号不能为空");
		} else if (!/^1[1-9]\d{9}$/.test(phone)) {
			showError("phone","手机号不正确")
		}else {
			//showSuccess("phone")
			//1. 获取手机号 然后去后台进行验证码
			$.ajax({
				url:"/p2p/loan/checkPhone",
				type:"get",
				data:{
					"phone":phone
				},
				success:function (data) {
					if (data.code == 1) {

						//3. 如果说手机号不存在。则可以使用并且调用showSuccess()函数
						showSuccess("phone")
					} else {
						//2. 如果手机号已经存在则：提示用户 手机号已经被注册
						showError("phone","手机号：" + data.message + "已经被注册")
					}
				},
				error:function () {
					showError("系统繁忙,请稍后再试");
				}
			});

		}
	});
	// 密码输入框失去焦点事件
	$("#loginPassword").on("blur",function () {
		var loginPassword = $.trim($("#loginPassword").val());
		if (loginPassword == "") {
			showError("loginPassword","密码不能为空");
		} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
			showError("loginPassword","密码应该同时包含数字和字母")
		} else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
			showError("loginPassword","密码只能包含数字和英文的大小写字母")
		} else if (loginPassword.length < 6 || loginPassword.length > 20) {
			showError("loginPassword", "对不起，密码的长度只能在6-20位之间")
		} else {
			showSuccess("loginPassword")
		}
	});

	// 注册按钮点击事件
	$("#btnRegist").on("click",function () {
		// 触发手机号和密码框的失去焦点事件
		$("#phone").blur();
		$("#loginPassword").blur();
		var phone = $.trim($("#phone").val());
		var loginPassword = $.trim($("#loginPassword").val())
		var messageCode = $.trim($("#messageCode").val())

		// 新的问题：如何知道触发上面两个文本框事件失去焦点后有没有通过验证？
		// 思路：获取显示错误的div标签的文本内容，
		// 1. 如果文本内容为空 说明没有错误，继续往下走(请求后台)
		// 2. 如果文本内容不为空 则说明文本框验证没通过，什么也不错.
		var errorText = $("div[id$='Err']").text();
		if (errorText == "") {
			$("#loginPassword").val($.md5(loginPassword))
			$.ajax({
				url:"/p2p/loan/register",
				type:"get",
				data:{
					"phone":phone,
					"loginPassword":$.md5(loginPassword),
					"messageCode":messageCode
				},
				success:function (data) {
					if (data.code == 1) {
						// 注册成功,跳转到实名认证
						window.location.href = "/p2p/user/realname"
					} else {
						// 注册失败
						showError("messageCode",data.message)
					}
				},
				error:function () {
					// 通讯失败导致的注册失败
					showError("loginPassword","系统繁忙")
				}
			})
		}


	});

	// 实现获取验证码
    $("#messageCodeBtn").on("click",function () {
        $("#phone").blur();
        $("#loginPassword").blur();
        var phone = $.trim($("#phone").val());
        var errorText = $("div[id$='Err']").text();
        if (errorText == "") {
        	// 通过验证后 当后台发送验证码成功后才开始倒计时
			$.ajax({
				url:"/p2p/user/messageCode",
				type:"get",
				data:{
					"phone":phone
				},
				success:function (data) {
					if (data.code == 1) {
						alert(data.message)
						// 第三方已经成功的发送了短信验证码，可以开始倒计时了
						if (!$("#messageCodeBtn").hasClass("on")) {
							//倒计时效果
							$.leftTime(5,function(d){
								//d.status,值true||false,倒计时是否结束;
								if (d.status) {
									//d.s,倒计时秒;
									$("#messageCodeBtn").addClass("on");
									$("#messageCodeBtn").text(d.s == "00" ? "60秒重新获取" :d.s + '秒重新获取');
								}else {
									// 倒计时结束
									$("#messageCodeBtn").removeClass("on")
									$("#messageCodeBtn").text("获取验证码")
								}
							});
						}
					} else {
						showError("messageCode","发送验证码失败")
					}
				},
				error:function () {
					showError("messageCode","发送验证码失败,请稍后再试")
				}
			})
        }
    });
});
