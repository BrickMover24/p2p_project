var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

$(function () {
	// 实现获取验证码
	$("#messageCodeBtn").on("click",function () {
		var phone = $.trim($("#phone").val());

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
									$("#messageCodeBtn").text(d.s == "00" ? "60秒后获取" :d.s + '秒后获取');
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

	});
})


