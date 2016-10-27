<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8" />
	    <title>任务设计</title>
	    <meta name="description" content="formDesinger" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!--Basic Styles-->
		<link href="basic/css/bootstrap.min.css" rel="stylesheet" />
		<link href="basic/css/font-awesome.min.css" rel="stylesheet" />
		<link href="basic/css/weather-icons.min.css" rel="stylesheet" />
		<!-- switch开关插件 -->
		<link href="plug-in/switchery/switchery.css" rel="stylesheet" />
		
		<link rel="stylesheet" href="basic/formbuilder/vendor/css/vendor.css" />
		<link rel="stylesheet" href="basic/formbuilder/dist/formbuilder.css" />
		
		<!-- home样式文件 -->
<!-- 		<link href="basic/css/appFormDesigner.less" rel="stylesheet/less" /> -->
		<link href="basic/css/appFormDesigner.css" rel="stylesheet" />
		
		<!-- LESS -->
<!-- 		<script src="basic/js/less.min.js"></script> -->
	</head>
	<body>
		<div class="form-designer-box step-1">
			<!-- 第一步，选择任务类型 -->
			<div class="form-type" id="selectFormType">
				<div class="common-form">
					<div class="form-field-box form-text clearfix">
						<label class="field-label">任务名称：</label>
						<div class="field-input">
							<input id="templateName_1" type="text" placeholder="点击这里输入任务名称"/>
						</div>
					</div>
					<div class="form-field-box form-paragraph clearfix">
						<label class="field-label">任务说明：</label>
						<div class="field-input">
							<textarea id="templateDescription_1" placeholder="请输入任务说明"></textarea>
						</div>
					</div>
					<div class="form-field-box seperator"></div>
				</div>
				<div class="form-type-box">
					<div class="header">请选择任务类型</div>
					<div class="content">
						<ul class="list-style-table">
							<li id="formtype-normal">普通任务</li>
							<li id="formtype-related">关联任务</li><!-- data-toggle='modal' data-target='#sltTmpModal' -->
						</ul>
					</div>
				</div>
			</div>
			<!-- 第二步，编辑任务 -->
			<div class="form-content main">
				<div class='fb-main'></div>
				<div class="form-btn-bottom">
					<ul class="list-style-table">
						<li class="form-edit-back"><button id="formedit-back">返回</button></li>
						<li><button id="formSave_step2">保存</button></li>
						<li><button id="nextStep">下一步</button></li>
					</ul>
				</div>
			</div>
			<!-- 第三步，任务设置 -->
			<div class="form-setting">
				<div class="setting-content">
					<div class="fb-right-2-container">
						<div class="common-form">
							<div class="form-field-box form-text clearfix">
								<label>任务名称：</label>
								<input id="templateName" type="text" placeholder='请输入任务名称' />
							</div>
							<div class="form-field-box form-paragraph clearfix">
								<label>任务说明：</label>
								<textarea id="templateDescription" placeholder='请输入任务说明'></textarea>
							</div>
							<div class="form-field-box form-check clearfix">
								<label>动态选择审批人：</label>
								<input class="field-checkbox js-switch" id="isStartAssign" name="bt-checkbox" type="checkbox">
							</div>
							<div class="form-field-box form-check clearfix">
								<label>是否选择传阅人：</label>
								<input class="field-checkbox js-switch" id="notifyType" name="bt-checkbox" type="checkbox">
							</div>
							<div class="form-field-box form-check clearfix">
								<label>发布到共享文件夹：</label>
								<input class="field-checkbox js-switch" id="isSharefolder" name="bt-checkbox" type="checkbox">
							</div>
							<div class="form-field-box form-selecticon clearfix" id="selectIconBtn">
								<label>选择图标：</label>
								<img class="icon hidden" src="basic/img/logo/app_apply.png">
								<i class="fa fa-angle-right"></i>
							</div>
							<div id="field_relatedTemp" class="form-field-box form-text clearfix hidden">
								<label>关联任务：</label>
								<input id="relateTemplate" type="text" disabled>
							</div>
							<div id="field_tempUser" class="form-field-box form-selectuser clearfix">
								<div class="top clearfix">
									<label>任务使用人：</label>
								</div>
								<div id="tpltPermPerson" class="content">
									<ul class="list-style-none-h user-group">
										<!-- 
										<li>
											<img class="avatar" src="basic/img/background_home.jpg">
											<span class="name text-overflow">李四四</span>
										</li>
										<li>
											<img class="avatar" src="basic/img/background_home.jpg">
											<span class="name text-overflow">李四四</span>
										</li>
										 -->
										<li class="add-item" id="tpltPermSetBtn"> <!-- data-toggle="modal" data-target="#templateModal" -->
											<div class="btn-add">
												<img src="basic/img/group_member_add.png" alt="增加">
											</div>
										</li>
									</ul>
								</div>
								<!-- 
								<div id="tpltPermPerson" class="person-list without-arrow">
									<ul class="list-style-none-h">
										<li class="add-item" id="tpltPermSetBtn" data-toggle="modal" data-target="#templateModal">
											<div>
												<img src="basic/img/group_member_add.png" alt="增加">
											</div>
										</li>
									</ul>
								</div>
								 -->
							</div>
							<div id="aproPermPersonDiv" class="form-field-box form-selectuser clearfix">
								<div class="top clearfix">
									<label>审批人：</label>
								</div>
								<div id="aproPermPerson" class="content">
									<ul class="list-style-none-h user-group">
										<!-- 
										<li>
											<img class="avatar" src="basic/img/background_home.jpg">
											<span class="name text-overflow">李四四</span>
										</li>
										<li>
											<img class="avatar" src="basic/img/background_home.jpg">
											<span class="name text-overflow">李四四</span>
										</li>
										 -->
										<li class="add-item" id="aproPermSetBtn" > <!-- data-toggle="modal" data-target="#templateModal" -->
											<div class="btn-add">
												<img src="basic/img/group_member_add.png" alt="增加">
											</div>
										</li>
									</ul>
								</div>
								<!-- 
								<div id="aproPermPerson" class="person-list">
									<ul class="list-style-none-h">
										<li class="add-item" id="aproPermSetBtn" data-toggle="modal" data-target="#templateModal">
											<div>
												<img src="basic/img/group_member_add.png" alt="增加">
											</div>
										</li>
									</ul>
								</div>
								 -->
							</div>
						</div>
					</div>
				</div>
				<div class="form-btn-bottom">
					<ul class="list-style-table has-three">
						<li><button id="beforeStep">上一步</button></li>
						<li><button id="formSave_step3">保存</button></li>
						<li><button id="formDeploy">发布</button></li>
					</ul>
				</div>
			</div>
		</div>
		<div id="templateModal"></div>
	</body>
	
	<script src="basic/js/html5.js"></script>
	<script src="basic/js/jquery-2.0.3.min.js"></script>
	<script src="basic/js/jquery-migrate-1.1.0.js"></script>
	<!--Basic Scripts-->
	<script src="basic/js/bootstrap.min.js"></script>
	
	
	<!-------------------------------------- 基础类型扩展、方法扩展  -->
	<script src="plug-in/tools/uuid.js"></script>
	<script src="plug-in/tools/Map.js"></script>
	<script src="plug-in/tools/syUtil.js"></script>
	<script src="plug-in/tools/curdtools.js"></script>
		
	<!-- h5验证文件 -->
	<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js"></script>
	
	<script src="basic/formbuilder/vendor/js/vendor.js"></script>
	<script src="basic/formbuilder/dist/formbuilder.mobile.js"></script>
	
	<!-- switch开关插件 -->
	<script src="plug-in/switchery/switchery.min.js"></script>
	
	<script src="plug-in/tools/native-support.js"></script>
	
	<script src="basic/js/home.js"></script>
	
	<script>
	var formHome = {
		//修改任务时，先加载任务
		loadTemplate: function(){
			//debugger;
			var tpltId = "${formId}";
			
			if(tpltId.length > 0){
				$.ajax({
					url : 'flowFormController.do?getFlowForm&formId='+tpltId,
					async: false,
					type : 'post',
					dataType: "json",
					success : function(result) {
						if(result.success){
							//渲染表单信息
							fb_update = new Formbuilder({
								selector: '.fb-main',
								bootstrapData: JSON.parse(result.obj.FlowFormEntity.fieldJsonTemp).fields
							});
							
							//渲染任务信息
							$(".fb-main").attr("id",result.obj.FlowFormEntity.id);														//任务ID
							$(".fb-main").attr("data-status",result.obj.FlowFormEntity.status);											//任务状态
							$("#templateName").val(result.obj.FlowFormEntity.name);														//任务名称
							$("#templateDescription").val(result.obj.FlowFormEntity.description);  										//任务描述
							$("#relateTemplate").attr("data-id",result.obj.FlowFormEntity.parentId);  									//父任务ID
							$("#relateTemplate").val(result.obj.moduleName);  															//父任务名称
							//alert(result.obj.FlowFormEntity.logo);
							$("#selectIconBtn").children("img").attr("name",result.obj.FlowFormEntity.logo)
									.removeClass("hidden")
									.attr("src", "basic/img/logo/"+result.obj.FlowFormEntity.logo+".png");								//任务图标
							
							$("#isStartAssign").attr("checked", result.obj.FlowFormEntity.isStartAssign == 0 ? false : true);			//动态选择审批人
							$("#notifyType").attr("checked", result.obj.FlowFormEntity.notifyType == 0 ? false : true);				//动态选择审批人
							$("#isSharefolder").attr("checked", result.obj.FlowFormEntity.isSharefolder == 0 ? false : true);				//动态选择审批人
							$("#templateVersion").text(result.obj.FlowFormEntity.version);												//版本
							
							formHome.formSaveEvtBind(fb_update);
							
							if(result.obj.FlowFormEntity.isStartAssign == 0 ? false : true){
								$("#aproPermPersonDiv").hide();
							}else{
								$("#aproPermPersonDiv").show();
							}
							
							//渲染任务使用人
							$("#templateModal").data("tpltData", {"userList": []});
							//alert(JSON.stringify(result.obj));
							$.each(result.obj.AppFormUser, function(i, n){
								var avatarUrl = "";
					    		if(n.type == "user"){
					    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "basic/img/avatars/avatar_80.png"
					    		}else if(n.type == "org"){
					    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "images/organization/organization.png";
					    		}else{
					    			avatarUrl = formHome.getKindIcon(n.type);
					    		}
								$("#templateModal").data("tpltData").userList.push({
									"id": n.id, 
					    			"avatarImgUrl": avatarUrl, 
					    			"name": n.name || "张三",
					    			"orderby": n.orderby || i,
					    			"type": n.type || "user",
					    			"portrait": n.portrait || ""
								});
							}); 
							$("#tpltPermPerson > ul > li").not(".add-item").remove();
					    	formHome.dataToLi($("#templateModal").data("tpltData").userList, $("#tpltPermPerson > ul"), "prepend");
							//$userLiList.clone().prependTo($("#tpltPermPerson > ul"));
							
							//如果是关联任务，则屏蔽任务使用人的编辑功能，同时显示主任务的名称
							if(result.obj.FlowFormEntity.parentId != -1){
								//$("#field_tempUser").addClass("hidden");
								$("#tpltPermSetBtn").removeAttr("data-toggle");
								$("#tpltPermSetBtn").removeAttr("data-target");
								$("#tpltPermSetBtn").addClass("hidden");
								$("#tpltPermPerson li").off();
								
								$("#field_relatedTemp").removeClass("hidden");
							}else{
							}
					    	
							
							//渲染数据权限
							$("#templateModal").data("dtData", {"userList": []});
							$.each(result.obj.AppFormUserData, function(i, n){
								$("#templateModal").data("dtData").userList.push({
									"id": n.id, 
					    			"avatarImgUrl": ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png", 
					    			"name": n.name || "张三",
					    			"orderby": n.orderby || i
								});
							}); 
							
							//渲染审批人
							$("#templateModal").data("aproData", {"userList": []});
							$.each(result.obj.AppFormApproveUser, function(i, n){
								var avatarUrl = "";
					    		if(n.type == "user"){
					    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "basic/img/avatars/avatar_80.png"
					    		}else if(n.type == "org"){
					    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "images/organization/organization.png";
					    		}else{
					    			avatarUrl = formHome.getKindIcon(n.type);
					    		}
								$("#templateModal").data("aproData").userList.push({
									"id": n.id, 
					    			"avatarImgUrl": avatarUrl, 
					    			"name": n.name || "张三",
					    			"orderby": n.orderby || i,
					    			"type": n.type || "user",
					    			"portrait": n.portrait || ""
								});
							}); 
							$("#aproPermPerson > ul > li").not(".add-item").remove();
							formHome.dataToLi($("#templateModal").data("aproData").userList, $("#aproPermPerson > ul"), "prepend");
					    	//$userLiList.clone().prependTo($("#aproPermPerson > ul"));
						}else{
							alert("任务加载失败");
						}
					}
				});
			}
			//console.log(tpltId);
		},
		//表单保存事件绑定方法
		formSaveEvtBind : function(obj){
			obj.on('save', function(payload){
				simpleCMD.showProcessing();
				setTimeout(function(){
					formHome.formSave(payload, false);
				},500)
		    });
			obj.on('deploy', function(payload){
				simpleCMD.showProcessing();
				setTimeout(function(){
					formHome.formSave(payload, true);
				},500)
		    });
		},
		//表单保存方法
		formSave: function(payload, isDeploy){
			var saveData = {},
				saveFormButton = $(".fb-main").find(".js-save-form").attr("disabled", true),
		    	deployFormButton = $(".fb-main").find("#btn-deploy").attr("disabled", true);
			
			//处理payload的层级关系
			payload = JSON.parse(payload);
			if(isDeploy == "1"){
				$.each(payload.fields, function(i, item) {
					if(item.label == ""){
						//$("#templateName").testRemind("请输入有效的手机号码！");
						alert("要素名称不能为空");
						saveFormButton.attr("disabled", false);
						deployFormButton.attr("disabled", false);
						simpleCMD.hideProcessing();
						//home.handleLoading(false, top.$("body"));
						return false;
					}
				});
			}
			if($("#templateName").val() == ""){
				alert("任务名称不能为空！");
				saveFormButton.attr("disabled", false);
				deployFormButton.attr("disabled", false);
				simpleCMD.hideProcessing()
				//home.handleLoading(false, top.$("body"));
				return false;
			}
			if(!/^[^&%]*$/.test($("#templateName").val())){
				alert("任务名称不能包含“%”或“&”符号！");
			}
			
		    //任务信息
		    saveData.FlowFormEntity = {
	    		"id": $(".fb-main").attr("id") || "",
	    		"status": $(".fb-main").attr("data-status") || 0,
	    		"name": $("#templateName").val(),
	    		"description": $("#templateDescription").val(),
	    		"parentId": $("#relateTemplate").attr("data-id") == "" ? "-1": $("#relateTemplate").attr("data-id"), 
	    		"logo": $("#selectIconBtn").children("img").attr("name"),
	    		//"isEdit": $("#templateIsEdit").is(":checked") == true ? 1 : 0,
	    		//"isSaveEdit": $("#templateSaveIsEdit").is(":checked") == true ? 1 : 0,
	    		"isStartAssign": document.getElementById("isStartAssign").checked == true ? 1 : 0,
	    		"notifyType": document.getElementById("notifyType").checked == true ? 1 : 0,
	    		"isSharefolder": document.getElementById("isSharefolder").checked == true ? 1 : 0,
	    		//"notifyType": $("#notifyType").is(":checked") == true ? 1 : 0,
	    		"version": 0
		    };
		    //alert(saveData.FlowFormEntity.logo);
		    
		    //任务字段
		    saveData.AppFormField = payload;
		    
		    //任务权限
		    if($("#templateModal").data("tpltData")){
			    saveData.AppFormUser = {"userList": $.map($("#templateModal").data("tpltData").userList, function(n, i){
					return {"id": n.id, "type": n.type}
				})};
		    }
		    //数据权限（屏蔽）
		    /*
		    if($("#templateModal").data("dtData")){
			    saveData.AppFormUserData = {"userList": $.map($("#templateModal").data("dtData").userList, function(n, i){
					return {"id": n.id}
				})};
		    }
		    */
		    //审批权限
		    if($("#templateModal").data("aproData")){
			    saveData.AppFormApproveUser = {"userList": $.map($("#templateModal").data("aproData").userList, function(n, i){
					return {"id": n.id, "name": n.name, "orderby": n.orderby, "type": n.type}
				})};
		    }
		    
		    //执行保存
		    $.ajax({
				url : 'flowFormController.do?saveOrUpdate',
				type : 'post',
				async: false,
				data: "jsonStr="+JSON.stringify(saveData)+"&isDeploy=" + (isDeploy ? 1 : 0),
				dataType: "json",
				beforeSend: function(x){
					saveFormButton.attr("disabled", true);
					deployFormButton.attr("disabled", true);
				},
				error: function(msg){
					alert(JSON.stringify(msg));
					//home.handleLoading(false, top.$("body"));
					simpleCMD.hideProcessing();
					setTimeout(function(){
						saveFormButton.attr("disabled", false);
						deployFormButton.attr("disabled", false);
					}, 1000);
				},
				success : function(result) {
					//alert(result.msg);
					if(result.success){
						//home.handleLoading(false, top.$("body"));
						simpleCMD.hideProcessing();
						isDeploy = isDeploy ? 1 : 0;
						if(isDeploy == 1){
							//top.$(".menu-pannel-body").load("loginController.do?appSetting&load=1");
							simpleCMD.goMyTemplate();
						}else{
							$(".fb-main").attr("id",result.formId);
						}
					}else{
						alert("保存失败");
						//home.handleLoading(false);
						simpleCMD.hideProcessing();
					} 
					setTimeout(function(){
						saveFormButton.attr("disabled", false);
						deployFormButton.attr("disabled", false);
					}, 1000);
				}
			});
		},
		//人员列表HTML转化成json
	    liToData: function(liObj, dataObj){
	    	liObj.each(function(index, ele){
	    		dataObj.push({
	    			"id": ele.id, 
	    			"avatarImgUrl": $(ele).find(".avatar").children("img").attr("src"), 
	    			"name": $(ele).find(".name").text(),
	    			"type": $(ele).attr("data-type"),
	    			"orderby": index + 1
	    		});
	    	});
	    	return dataObj;
	    },
	    
	    //人员列表json转化成HTML结构
	    dataToLi: function(dataObj, container, addType){
	    	$.each(dataObj, function(i, n){
	    		container.append($("<li id='"+n.id+"' data-type='"+n.type+"'>"+
	        			"<img class='avatar' src='"+n.avatarImgUrl+"'>"+
	        			"<span class='name text-overflow' title='"+n.name+"'>"+n.name+"</span>"+
	        			//"<i class='glyphicon glyphicon-arrow-right'></i>"+
	        			"</li>").click(function(){
	        				var thisId = $(this).closest(".content").attr("id");
	        				$(this).remove();
	        				formHome.updateUserData(thisId);
	        			}).data("uData", n.uData));
	    	});
	    	if(addType == "prepend"){
	    		container.children(".add-item").appendTo(container);
	    	}
	    },
	    
	    //点击以选择人员头像，删除人员后，更新存储的人员数据
	    updateUserData: function(target){
   	    	//alert(target);
	    	if(target == "tpltPermPerson"){
	    		var tpltData = {"userList": []},
	    			$userLiList = $("#tpltPermPerson > ul > li").not(".add-item"); 
    	    	
    	    	tpltData.userList = formHome.liToData($userLiList, tpltData.userList);
    	    	$("#templateModal").data("tpltData", tpltData);
	    	}else if(target == "aproPermPerson"){
	    		var aproData = {"userList": []},
	    			$userLiList = $("#aproPermPerson > ul > li").not(".add-item"); 
    	    	
	    		aproData.userList = formHome.liToData($userLiList, aproData.userList);
    	    	$("#templateModal").data("aproData", aproData);
	    	}
	    },
	    
	    //初始化formbuilder
	    initFormbuilder: function(fieldsData){
	    	fb = new Formbuilder({
	  	      selector: '.fb-main',
	  	      bootstrapData: fieldsData
	  	    });
	  	    //formbuilder保存事件
	  	    formHome.formSaveEvtBind(fb);
	        
	  	    formHome.initFormEvent();
	    },
	    
		//获取关联任务字段信息
	    getRelatedInfo: function(tid){
	    	var rtn = [];
	    	$.ajax({
				url : 'flowFormController.do?getFieldJson&formId=' + tid,
				type : 'post',
				async: false,
				dataType: "json",
				error: function(){
					alert("请求失败！");
				},
				success : function(result) {
					//alert(JSON.stringify(result));
					if(result.success){
						rtn = result.obj;
						//console.log(result.obj.fields);
					}else{
						alert("获取主任务失败！");
					} 
				}
			});
	    	
	    	return rtn;
	    },
	    
	    //初始化formbuilder之外的事件
	    initPageEvent: function(){
	        //选择任务类型时，点击“关联任务”，打开移动端原生的选任务界面
	        $("#formtype-related").on("click", function(){
	        	//alert("moban");
	        	simpleCMD.selectTemplate();
	        });
	        
	        //选择图标，打开原生界面
	        $("#selectIconBtn").on("click", function(){
	        	simpleCMD.selectIcon();
	        });
	        
	        //点击选择“普通任务”按钮，触发事件
	        $("#formtype-normal").on("click", function(){
	        	formHome.initFormbuilder([]);
	        	formHome.copyTemplateInfo();
	        	formHome.switchStep(2);
	        });
	        
	        //上一步，下一步，保存，发布 等按钮事件
	        //formSave_step2, nextStep, beforeStep, formSave_step3, formDeploy
	        $("#nextStep").on("click", function(){
	        	formHome.switchStep(3);
	        });
	        $("#beforeStep").on("click", function(){
	        	formHome.switchStep(2);
	        });
	        $("#formSave_step2").on("click", function(){
	        	$("#btn-save").trigger("click");
	        });
	        $("#formSave_step3").on("click", function(){
	        	$("#btn-save").trigger("click");
	        });
	        $("#formDeploy").on("click", function(){
	        	$("#btn-deploy").trigger("click");
	        });
	        
	        //任务名称不能以数字开头
	        $("#templateName_1").keyup(function(){
	        	//console.log("test");
	        	var regStr = /^[0-9]*$/;
	        	if(regStr.test($(this).val().charAt(0))){
	        		$(this).val("");
	        	}
	        });
	        $("#templateName").keyup(function(){
	        	//console.log("test");
	        	var regStr = /^[0-9]*$/;
	        	if(regStr.test($(this).val().charAt(0))){
	        		$(this).val("");
	        	}
	        });
	        
	        $("#formedit-back").on("click", function(){
	        	$("a[data-target='#sortField']").trigger("click");
	        	if(!$(".form-content").hasClass("main")){
        			$(".form-content").addClass("main");
        		}
	        });
	    },
	    
	    //初始化绑定formbuilder里的事件
	    initFormEvent: function(){
	    	//选人界面打开时，初始化右侧已选择的人员列表
	        $(".add-item").on("click", function(e){
	        	var $targetBtn = $(this),
	        		$this = $("#templateModal"),
	        		$userList = $(".person-list.approval-flow > ul").empty();
	        	
	        	//alert(this.id);
	        	if(this.id == "tpltPermSetBtn"){
	    	    	$this.data("modalType","tplt");
	        		selectPerson.select('{"callbackKey":"formHome.selectUser", "multiple":true,"showType":"contact,org","selectOrg":true,"onlyCurrentOrg":false, "selected":'+formHome.setUsers("tpltPermSetBtn")+'}');
	        	}else if(this.id == "aproPermSetBtn"){
	    	    	$this.data("modalType","apro");
	        		selectPerson.select('{"callbackKey":"formHome.selectUser","showType":"contact,org,role","multiple":true}');
	        	}
	        });
	        
	        //判断是否勾选动态选择审批人
	        $("#isStartAssign").next(".switchery").on("click",function(){
	        	if(document.getElementById("isStartAssign").checked){
	        		$("#aproPermPersonDiv").hide();
	        	}else{
	        		$("#aproPermPersonDiv").show();
	        	}
	        });
	        
	        
	    },
	    //切换步骤
	    switchStep: function(step){
	    	$(".form-designer-box").removeClass("step-1 step-2 step-3")
	    	if(step == 1){
	    		$(".form-designer-box").addClass("step-1");
	    	}else if(step == 2){
	    		$(".form-designer-box").addClass("step-2");
	    	}else if(step == 3){
	    		$(".form-designer-box").addClass("step-3");
	    	}
	    },
	    //选择任务类型后，把第一步填写的内容，复制到第三步
	    copyTemplateInfo: function(){
	    	$("#templateName").val($("#templateName_1").val());
        	$("#templateDescription").val($("#templateDescription_1").val());
	    },
	    //与手机原生选任务交互的回调方法
	    selectTemplateBack: function(tData){
	    	//tData = {"id": "123123", "name": "测试任务"};
	    	
	    	var relatedInfo = formHome.getRelatedInfo(JSON.parse(tData).id);

	    	formHome.initFormbuilder(relatedInfo.fieldJson.fields);
	    	$("#relateTemplate").val(JSON.parse(tData).name);
	    	$("#relateTemplate").attr("data-id",JSON.parse(tData).id);
	    	
	    	formHome.copyTemplateInfo();
	    	formHome.switchStep(2);
	    	$("#field_relatedTemp").removeClass("hidden");
	    	
	    	//禁用任务使用人选择功能，只显示主任务的使用人
	    	$("#tpltPermSetBtn").removeAttr("data-toggle");
	    	$("#tpltPermSetBtn").removeAttr("data-target");
	    	$("#tpltPermSetBtn").addClass("hidden");
	    	$("#tpltPermSetBtn").data("tpltData", {userList:[]});
	    	$.each(relatedInfo.AppFormUser, function(i, n){
				$("#tpltPermSetBtn").data("tpltData").userList.push({
					"id": n.id, 
	    			"avatarImgUrl": ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png", 
	    			"name": n.name || "张三",
	    			"orderby": n.orderby || i
				});
			}); 
	    	formHome.dataToLi($("#tpltPermSetBtn").data("tpltData").userList, $("#tpltPermPerson > ul"), "prepend");
	    	$("#tpltPermPerson li").off();
	    },
	    //与手机原生交互的选人回调
	    selectUser: function(uData){
    		//alert(parseJSON(uData));
	    	//uData = {"selected": [{"id": "111", "name": "张三", "portrait": "avatar1"},{"id": "222", "name": "李四", "portrait": "avatar2"}]};
	    	//uData = parseJSON(uData);
	    	var $userLiList = $("#templateModal .person-list.approval-flow li"),
	    		$this = $("#templateModal");
	    	
	    	if($this.data("modalType") == "tplt"){
	    		//任务权限
		    	var tpltData = {"userList":[]}; 
		    	
		    	$("#tpltPermPerson > ul > li").not(".add-item").remove();
		    	
		    	$.each(uData, function(i, n){
		    		var avatarUrl = "";
		    		if(n.type == "user"){
		    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "basic/img/avatars/avatar_80.png"
		    		}else if(n.type == "org"){
		    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "images/organization/organization.png";
		    		}else{
		    			avatarUrl = formHome.getKindIcon(n.type);
		    		}
		    		//alert("${attachForeRequest }" + n.portrait);
		    		//alert(avatarUrl);
		    		tpltData.userList.push({
						"id": n.id, 
		    			"avatarImgUrl": avatarUrl, 
		    			"name": n.name || "张三",
		    			"portrait": n.portrait || "",
		    			"type": n.type || "",
		    			"uData": n,
		    			"orderby": n.orderby || i
					});
				});
		    	formHome.dataToLi(tpltData.userList, $("#tpltPermPerson > ul"), "prepend");
		    	
		    	$this.data("tpltData", tpltData);
	    	}else if($this.data("modalType") == "apro"){
	    		//审批权限
		    	var aproData = {"userList":[]}; 
		    	
		    	$.each(uData, function(i, n){
		    		var avatarUrl = "";
		    		if(n.type == "user"){
		    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "basic/img/avatars/avatar_80.png"
		    		}else if(n.type == "org"){
		    			avatarUrl = n.portrait ? ("${attachForeRequest }" + n.portrait) : "images/organization/organization.png";
		    		}else{
		    			avatarUrl = formHome.getKindIcon(n.type);
		    		}
		    		aproData.userList.push({
						"id": n.id, 
		    			"avatarImgUrl": avatarUrl, 
		    			"name": n.name || "张三",
		    			"portrait": n.portrait,
		    			"type": n.type,
		    			"uData": n,
		    			"orderby": n.orderby || i
					});
				});
		    	formHome.dataToLi(aproData.userList, $("#aproPermPerson > ul"), "prepend");
		    	//$userLiList.clone().prependTo($("#aproPermPerson > ul"));
		    	
		    	formHome.updateUserData("aproPermPerson");
		    	//$this.data("aproData", aproData);
	    	}
	    },
	    //点击选人时，反写给移动端已选择的人员
	    setUsers: function(target){
	    	var $targetBtn = $(this),
	    		$this = $("#templateModal"),
	    		$userList = $(".person-list.approval-flow > ul").empty(),
	    		rtn = [];
	    	
	    	if(target == "tpltPermSetBtn"){
		    	$this.data("modalType","tplt");
		    	if($this.data("tpltData")){
			    	$($this.data("tpltData").userList).each(function(){
				    	rtn.push({"id":this.id, "name": this.name, "portrait": this.portrait, "type": this.type});
			    	});
		    	}else{
		    	}
	    	}else if(target == "aproPermSetBtn"){
		    	$this.data("modalType","apro");
		    	if($this.data("aproData")){
			    	$($this.data("aproData").userList).each(function(){
			    		rtn.push({"id":this.id, "name": this.name, "portrait": this.portrait, "type": this.type});
			    	});
		    	}else{
		    	}
	    	}
	    	return nulls(JSON.stringify(rtn));
	    },
	    
	    //选择图标回调
	    selectIconBack: function(iData){
	    	$("#selectIconBtn").children("img").removeClass("hidden");
	    	$("#selectIconBtn").children("img").attr("src", "basic/img/logo/"+iData+".png");
	    	$("#selectIconBtn").children("img").attr("name", iData);
	    },
	  	//初始化switch要素
	  	initSwitchBtn: function(){
	        var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));
	
			elems.forEach(function(html) {
				var switchery = new Switchery(html, {color: "#F37836"});
			});
	    },
	    //根据不同的type返回不同的图标
	    getKindIcon: function(type){
	    	var rtn = "";
	    	
	    	switch(type){
	    		case "org":
	    			return "images/organization/iconfont-org.png";
	    			break;
	    		case "role":
	    			return "images/organization/iconfont-group.png";
	    			break;
	    		default:
	    			return "";
	    	}
	    }
	};	
	

	// 移动端选择任务交互
	simpleCMD.selectTemplate=function(params){
		nativeControllerCenter('selectTemplate',params);
	};
	//移动端选择任务回调
	simpleCMD.selectTemplate_callback=function(params){
		formHome.selectTemplateBack(params);
	};
	// 移动端选择图标交互
	simpleCMD.selectIcon=function(params){
		nativeControllerCenter('selectIcon',params);
	};
	//移动端选择图标回调
	simpleCMD.selectIcon_callback=function(params){
		formHome.selectIconBack(params);
	};
	//发布完成后，跳转到我的任务
	simpleCMD.goMyTemplate=function(params){
		//simpleCMD.back();
		nativeControllerCenter('goMyTemplate',params);
	}
	
	var timer;
	
	$(function(){
		formHome.initPageEvent();
		
		if("${formId}".length > 0){
	    	formHome.switchStep(2);
			formHome.loadTemplate();
			formHome.initSwitchBtn();
		    formHome.initFormEvent();
		}else{
			formHome.initSwitchBtn();
		}
	});
	</script>
</html>

