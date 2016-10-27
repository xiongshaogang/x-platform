var isVerificationCode = "N";
$(document).ready(function() {
	window.$but_login = $("#but_login");
	$("#isVerificationCode").hide();
	//读取cookie
	//getCookie();
	
	/**
	$("#realPerson_u").realperson({
		length: 4,
		regenerate: '<i class="awsm-icon-refresh green"/>',
	});**/
	//加载验证码
	$.ajax({
		url : 'loginController.do?isVerificationCode',
		type : 'post',
		cache : false,
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				isVerificationCode = d.obj;
				if(d.obj == 'Y'){
					s = document.createElement('script');
					s.src = 'http://api.geetest.com/get.php?callback=gtcallback';
					$("#jy-valid").append(s);
//					gtcallback();
//					$("#isVerificationCode").show();
//					$("#realPerson_u").realperson({
//						length: 4,
//						regenerate: '<i class="awsm-icon-refresh green"/>',
//					});
				}else{
//					$("#isVerificationCode").hide();
				}
			}else{
//				$("#isVerificationCode").hide();
			}
		}
	});
	
	// 	点击登录
	$('#but_login').click(function(e) {
		submit();
	});
	//回车登录
	$(document).keydown(function(e) {
		if (e.keyCode == 13 && $("#old_psd_val").length == 0) {//防止初始化密码修改回车键冲突
			submit();
		}
	});
	//验证码输入框失去焦点校验
	$("#realPerson_u").blur(function(){
		rtn = vldCraptcha(this.value);
		if(this.value){
			if(!rtn){
				$(".realPerson_u ~ .awsm-icon-lock").hide("fast");
				if(!$(".realPerson_u ~ .awsm-icon-remove").length){
					$(".realPerson_u").after("<i style='display:none' class='awsm-icon-remove red'/>");
				}
				$(".realPerson_u ~ .awsm-icon-remove").show("fast");
				$(".realPerson_u").parent().addClass('has-error');
			}else{
				if(!$(".realPerson_u ~ .awsm-icon-ok").length){
					$(".realPerson_u").after("<i style='display:none' class='awsm-icon-ok green'/>");
				}
				$(".realPerson_u ~ .awsm-icon-ok").show("fast");
				$(".realPerson_u ~ .awsm-icon-remove").hide("fast");
				$(".realPerson_u ~ .awsm-icon-lock").hide("fast");
				$(".realPerson_u").parent().removeClass('has-error');
				setTimeout(function(){
					$(".realPerson_u ~ .awsm-icon-lock").show("fast");
					$(".realPerson_u ~ .awsm-icon-ok").hide("fast");
				},1500);
			}
		}
	});
});

var gtcallback =( function() {
	var status = 0, result, apiFail;
	return function(r) {
		status += 1;
		if (r) {
			result = r;
			setTimeout(function() {
				if (!window.Geetest) {
					apiFail = true;
//					gtFailbackFrontInitial(result)
				}
			}, 1000)
		}
		else if(apiFail) {
			return
		}
		if (status == 1) {
			loadGeetest(result);
		}
	}
})()

var loadGeetest = function() {
	ajax("loginController.do?queryChallenge", function(j) {
		var config=parseJSON(j.obj);
		window.gt_captcha_obj = new window.Geetest({
			gt : config.gt,
			challenge : config.challenge,
			product : 'float',
			offline : !config.success
		});

		gt_captcha_obj.appendTo("#jy-valid");
	});
}

//function checkPasswordIsUpdate(){
//	var updateFlag = false;
//	$.ajax({
//		url : 'loginController.do?findSysParameterCode&code=passwordIsUpdate',
//		type : 'post',
//		async: false,
//		cache : false,
//		success : function(data) {
//			var d = $.parseJSON(data);
//			if (d.success) {
//				if(d.obj == 'Y'){
//					updateFlag = true;
//				}
//			}
//		}
//	});
//	return updateFlag;
//}

//表单提交
function submit() {
	/**
	var updateFlag = checkPasswordIsUpdate();
	if(updateFlag == true){
		chgPsd();
	}else{
		if (vldForm()) {
			changeBtnSta($but_login, "disabled", "登录中...");
			Login();
		}
	}**/
	if (vldForm()) {
		changeBtnSta($but_login, "disabled", "登录中...");
		Login();
	}
}
//登录处理函数
function Login() {
	setCookie();
	var actionurl = "loginController.do?home"; //提交路径
	var checkurl = $('form').attr('check'); //验证路径
	var formData = new Object();
	var data = $(":input").each(function() {
		formData[this.name] = $(this).val();
	});
	$.ajax({
		async: false,
		cache: false,
		type: 'POST',
		url: checkurl, // 请求的action路径
		data: formData,
		error: function() { // 请求失败处理函数
			showError("登录失败！");
			changeBtnSta($but_login, "reset", "登录");
		},
		success: function(data) {
//			var updateFlag = checkPasswordIsUpdate();
			var d = parseJSON(data);
			if (d.success) {
				setTimeout(function(){
					$('#errmsg_box').hide();
					window.location.href = actionurl;
					// changeBtnSta($but_login, "reset", "登录");
				}, 1000);
			} else {
				var userName = $("#userName").val();
				if (d.msg == "a") {
					bootbox.confirm("数据库无数据,是否初始化数据?", function(rst) {
						if(rst) window.location = "init.jsp";
					});
				}else if(d.status == 'initPassword'){ 
					alert(d.msg);
					createwindow("修改密码", "userController.do?passwordEdit&userName="+userName+"&oldPwd=123456" , 530, 260, null, {optFlag : 'update'});
					//chgPsd();
					changeBtnSta($but_login, "reset", "登录");
					
				}else if(d.status == 'passwordTooOld'){ 
					alert(d.msg);
					createwindow("修改密码", "userController.do?passwordEdit&userName="+userName , 530, 260, null, {optFlag : 'update'});
					//chgPsd();
					changeBtnSta($but_login, "reset", "登录");
				}else {
					alert(d.msg);
					changeBtnSta($but_login, "reset", "登录");
				}
			}
		}
	});
}
//校验表单
function vldForm(){
	var rtn = true;
	$("#formLogin").find("input[placeholder]").each(function() {
		$(this).on("keydown", function() {
			$("#errmsg_box").hide("fast");
			$(this).parent("span").removeClass('has-error');
			changeBtnSta($but_login, "reset", "登录");
		});
		// 校验表单是否为空
		if (this.id != "realPerson_u") {
			if ($(this).val() == "") {
				$(this).parent("span").addClass('has-error');
				showTooltip({
					obj : $(this),
					msg : '请输入' + $(this).attr("placeholder"),
					trg : "manual",
					placement : "right",
					clean : false
				});
				rtn = false;
				return rtn;
			}
		}
	});
	
//	return true; //开发环境，屏蔽验证码功能
	if(isVerificationCode == "Y"){
		var validate = gt_captcha_obj.getValidate();
		if(!validate){
			rtn=false;
		}
	}
	
	/*校验验证码(离线老版)
	if(isVerificationCode == "Y" && rtn){
		if($("#realPerson_u").val() == ''){
			$("#realPerson_u").parent("span").addClass('has-error');
			showTooltip({obj:$("#realPerson_u"), msg:'请输入'+$("#realPerson_u").attr("placeholder"), trg:"manual", placement:"right"});
			rtn = false;
		}
		
		if(rtn){
			rtn = vldCraptcha($("#realPerson_u").val());
			if(!rtn){
				showTooltip($("#realPerson_u"), "验证码输入错误！" ,"manual");
				showError("验证码输入错误！");
				$(".realperson-challenge").trigger("click");
			}
		}
	}*/
	
	return rtn;
}

//设置cookie
function setCookie() {
	var onoffckd = $('#on_off').attr("checked");
	$("input[iscookie='true']").each(function() {
		if(onoffckd) 
			$.cookie(this.name, $(this).val(), {expires:24, path:'/'});
		else
			$.removeCookie(this.name, {path:'/'});
	});
}

//读取cookie
function getCookie() {
	$("input[iscookie='true']").each(function() {
		$(this).val($.cookie(this.name));
	});
}

//显示表单下方错误提示
function showError(str) {
	$('#errmsg_box').html('<strong>'+str+'</strong>').show("fast");
}

//切换按钮状态
function changeBtnSta(btn, state, str){
	if(state == "reset"){
		btn.removeClass('disabled');
	}else if(state == "disabled"){
		btn.addClass('disabled');
	}
	btn.find("span").text(str);
}

//校验验证码
function vldCraptcha(value) {
    var hash = 5381,
    	hash_iptval = $(".realperson-hash").val();
    	rtn = false; 
    value = value.toUpperCase(); 
    for(var i = 0; i < value.length; i++) { 
        hash = ((hash << 5) + hash) + value.charCodeAt(i); 
    } 
    if (hash == hash_iptval){
    	rtn = true;
    }
    return rtn;
} 
 