
//同意实名认证协议
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

	// 对手机号输入框进行非空和格式验证
	$("#phone").on("blur",function () {
		var phone = $.trim($("#phone").val());
		if (phone == "") {
			showError("phone","手机号不能为空")
		} else if (!/^1[1-9]\d{9}$/.test(phone)) {
			showError("phone","请输入正确的手机号")
		}else {
			showSuccess("phone")
		}
	});

	// 对真实姓名输入框进行验证
	$("#realName").on("blur",function () {
		var realName = $.trim($("#realName").val());
		if (realName == "") {
			showError("realName","姓名不能为空")
		} else if (!/^[\u4e00-\u9fa5]{0,}$/.test(realName)) {
			showError("realName","请输入正确的姓名");
		} else {
			showSuccess("realName")
		}
	});

	// 对身份证号码进行验证
	$("#idCard").on("blur",function () {
		var idCard = $.trim($("#idCard").val());
		if (idCard == "") {
			showError("idCard","身份证不能为空")
		} else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
			showError("idCard","请输入正确的身份证号");
		} else {
			showSuccess("idCard")
		}
	});


	// 实现获取验证码
	$("#messageCodeBtn").on("click",function () {
		$("#phone").blur();
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

	// 认证按钮的单击事件
	$("#btnRegist").on("click",function () {
		
		$("#phone").blur();
		$("#realName").blur();
		$("#idCard").blur();

		var errorText = $("div[id$='Err']").text();
		if (errorText == "") {
			var phone = $.trim($("#phone").val());
			var realName = $.trim($("#realName").val());
			var idCard = $.trim($("#idCard").val());
			var messageCode = $.trim($("#messageCode").val());
			$.ajax({
				url:"/p2p/loan/realName",
				type:"get",
				data:{
					"phone":phone,
					"realName":realName,
					"idCard":idCard,
					"messageCode":messageCode
				},
				success:function (data) {
					if (data.code == 1) {
						window.location.href = "/p2p/index"
					} else {
						showError("messageCode",data.message)
					}
				},
				error:function () {
					showError("messageCode","系统异常")
				}
			})
		}
		
		
	});

});
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