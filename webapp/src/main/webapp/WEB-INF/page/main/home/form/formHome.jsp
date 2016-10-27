<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<!-- ztree -->
<link rel="stylesheet" href="plug-in/zTree/css/metroStyle/metroStyle.css" type="text/css">

<!--Basic Styles-->
<link href="basic/css/bootstrap.min.css" rel="stylesheet" />
<link href="basic/css/font-awesome.min.css" rel="stylesheet" />
<link href="basic/css/weather-icons.min.css" rel="stylesheet" />
<link href="basic/css/bootstrap-switch.min.css" rel="stylesheet" />

<link rel="stylesheet" href="basic/formbuilder/vendor/css/vendor.css" />
<link rel="stylesheet" href="basic/formbuilder/dist/formbuilder.css" />

<!-- home样式文件 -->
<link href="basic/css/home.common.less" rel="stylesheet/less" />
<link href="basic/css/commonForm.less" rel="stylesheet/less" />
<link href="basic/css/home.less" rel="stylesheet/less" />


<!--     <script type="text/javascript" src="plug-in/zTree/jquery-1.4.4.min.js"></script>   -->
	

<!-- LESS -->
<script src="basic/js/less.min.js"></script>

<style>
  select {
    margin-bottom: 5px;
  }
</style>


<div class="form-type hidden" id="selectFormType">
	<div class="form-type-box">
		<div class="header">请选择任务类型</div>
		<div class="content">
			<ul class="list-style-table">
				<li id="formtype-normal">普通任务</li>
				<li id="formtype-related" data-toggle='modal' data-target='#sltTmpModal'>关联任务</li>
			</ul>
		</div>
	</div>
</div>

<div class='fb-main hidden'></div>

<!-- 选人弹框 -->
<div class="modal fade in-iframe" id="templateModal" role="dialog">
	<div class="modal-dialog with-select-all" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">审批权限设置</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="top">
						<span class="top-left-span">选择成员：</span>
						<span class="top-left-btn">
							<!-- <a class="select-all">全选</a> -->
						</span>
						<div class="search">
							<input type="text" id="searchUser" place-holder="搜索成员"> <i class="glyphicon glyphicon-search"></i>
						</div>
					</div>
					<div class="person-box">
						<div class="slim-div">
							<div class="pb-item" id="">
								<div class="header">工作组</div>
								<div class="content person-list">
									<ul class="list-style-none" id="allRole">
										<li id="1">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">11111</p></div>
										</li>
										<li id="2">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">22222</p></div>
										</li>
										<li id="3">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">33333</p></div>
										</li> 
										<!-- 
										-->
									</ul>
								</div>
							</div>
							<div class="pb-item" id="treeUserList">
								<div class="header">团队</div>
								<div class="content" id="ztreeDiv">
									<ul id="userList" class="ztree">
										<!-- <li id="1">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">11111</p></div>
										</li>
										<li id="2">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">22222</p></div>
										</li>
										<li id="3">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">33333</p></div>
										</li> -->
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="pull-right selected-box">
					<div class="top">
						<span class="top-left-span">新添成员：</span>
						<span class="top-left-btn">
							<a class="remove-all">全部移除</a>
						</span>
						<span class="pull-right">已选<span class="selected-count">0</span>人
						</span>
					</div>
					<div class="person-box">
						<div class="slim-div">
							<div class="person-list approval-flow">
								<ul class="list-style-none-h" id="selectedUser">
									<!-- 
									<li id="1">
										<div class="item-person">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">1111</p></div>
										</div>
										<i class="glyphicon glyphicon-remove"></i>
										<i class="glyphicon glyphicon-arrow-right"></i>
									</li>
									<li id="1">
										<div class="item-person">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">22222</p></div>
										</div>
										<i class="glyphicon glyphicon-remove"></i>
										<i class="glyphicon glyphicon-arrow-right"></i>
									</li>
									<li id="1">
										<div class="item-person">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">33333</p></div>
										</div>
										<i class="glyphicon glyphicon-remove"></i>
										<i class="glyphicon glyphicon-arrow-right"></i>
									</li>
									<li id="1">
										<div class="item-person">
											<div class="avatar"><img src="basic/img/avatars/avatar_80.png"></div>
											<div class="name"><p class="text-overflow" title="18555665566">44444</p></div>
										</div>
										<i class="glyphicon glyphicon-remove"></i>
										<i class="glyphicon glyphicon-arrow-right"></i>
									</li>
									 -->
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="tpltConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<!-- 选任务弹框 -->
<div class="modal fade in-iframe" id="sltTmpModal" role="dialog">
	<div class="modal-dialog single-select" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">选择任务</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="person-box">
						<div class="person-list all">
							<ul class="list-style-none" id="tplList">
								<!-- <li id="1">
									<div class="name">
										<p>任务1</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="2">
									<div class="name">
										<p>任务2</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li>
								<li id="3">
									<div class="name">
										<p>任务3</p>
									</div>
									<div class="status">
										<i class="glyphicon glyphicon-ok"></i>
										<i class="glyphicon glyphicon-remove"></i>
									</div>
								</li> -->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="tpltConfirm_t" type="button" class="btn btn-orange" data-dismiss="modal" disabled>确定</button>
			</div>
		</div>
	</div>
</div>



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
<!--[if lt IE 9]>
<script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
<script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
<script src="plug-in/easemob/sdk/jplayer/jquery.jplayer.min.js"></script>
<script src="plug-in/easemob/sdk/swfupload/swfupload.js"></script>
<![endif]-->

<script src="basic/formbuilder/vendor/js/vendor.js"></script>
<script src="basic/formbuilder/dist/formbuilder.js"></script>

<script src="basic/js/home.js"></script>
<script src="basic/js/selectUser.js"></script>

<script src="basic/js/jquery.slimscroll.min.js"></script>

<script src="plug-in/zTree/jquery.ztree.core-3.5.js"></script>

<script>
var zTree; 
var zNodes;
var type = "click";
var setting = {
	view: {
		showLine: false
	},
	async: {
		enable: true,
		url:"orgnaizationController.do?getTreeData",
		dataType:"json",
		autoParam:["id=id"],
		dataFilter: filter //异步返回后经过Filter 
	},
	callback: {
		beforeClick: beforeClick,
		onAsyncSuccess: onAsyncSuccess
	}
};

function filter(treeId, parentNode, childNodes) {
	/* if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;  */
}
function beforeClick(treeId, treeNode) {
	var nodeType = (treeNode.isParent ? "org" : "user");
	if(type == "click"){
    	var $thisLi = $("<li id='"+treeNode.id+"' data-type='"+nodeType+"' data-portrait='"+treeNode.portrait+"'>"+
    			"<div class='item-person'>"+
    			"<div class='item-person'><div class='avatar'><img src='"+treeNode.icon+"'></div><div class='name'><p title='"+treeNode.name+"' class='text-overflow'>"+treeNode.name+"</p></div></div>"+
    			"</div>"+
    			"<i class='glyphicon glyphicon-remove'></i>"+
    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
    			"</li>").click(function(){
    				$(this).closest("li").remove();
    				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    			});
		
    	if($("#templateModal").data("modalType") == "tplt" && $(".person-list.approval-flow ul").find("li[id='"+treeNode.id+"']").length > 0){
    		return;
    	}
    	$(".person-list.approval-flow ul").append($thisLi);
    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
	}else{
		if(!(treeNode.isParent)){
	    	var $thisLi = $("<li id='"+treeNode.id+"' data-type='"+nodeType+"' data-portrait='"+treeNode.portrait+"'>"+
	    			"<div class='item-person'>"+
	    			"<div class='item-person'><div class='avatar'><img src='"+treeNode.icon+"'></div><div class='name'><p title='"+treeNode.name+"' class='text-overflow'>"+treeNode.name+"</p></div></div>"+
	    			"</div>"+
	    			"<i class='glyphicon glyphicon-remove'></i>"+
	    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
	    			"</li>").click(function(){
	    				$(this).closest("li").remove();
	    				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
	    			});
			
	    	if($("#templateModal").data("modalType") == "tplt" && $(".person-list.approval-flow ul").find("li[id='"+treeNode.id+"']").length > 0){
	    		return;
	    	}
	    	$(".person-list.approval-flow ul").append($thisLi);
	    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
		}
	}
}
function onAsyncSuccess(event, treeId, treeNode, childNodes) {
	 var parentZNode = zTree.getNodeByParam("id", treeNode.id, null); //获取父节点  
	 parentZNode =  zTree.addNodes(parentZNode, childNodes.obj, true); 
}

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
						//console.log(result.obj.FlowFormEntity.version);
						//渲染表单信息
						fb_update = new Formbuilder({
							selector: '.fb-main',
							bootstrapData: JSON.parse(result.obj.FlowFormEntity.fieldJsonTemp).fields
							/*
							[
								  {
								    "label": "Do you have a website?",
								    "field_type": "website",
								    "required": false,
								    "field_options": {},
								    "cid": "c1"
								  },
								  {
								    "label": "Please enter your clearance number",
								    "field_type": "text",
								    "required": true,
								    "field_options": {},
								    "cid": "c6"
								  },
								  {
								    "label": "Security personnel #82?",
								    "field_type": "radio",
								    "required": true,
								    "field_options": {
								        "options": [{
								            "label": "Yes",
								            "checked": false
								        }, {
								            "label": "No",
								            "checked": false
								        }],
								        "include_other_option": true
								    },
								    "cid": "c10"
								  },
								  {
								    "label": "Medical history",
								    "field_type": "file",
								    "required": true,
								    "field_options": {},
								    "cid": "c14"
								  }
							]
							*/
						});
						formHome.loadFormIcon();
						
						//渲染任务信息
						//console.log(result.obj.FlowFormEntity.logo);
						//result.obj.FlowFormEntity.isSaveEdit = 0;
						$(".fb-main").attr("id",result.obj.FlowFormEntity.id);													//任务ID
						$(".fb-main").attr("data-status",result.obj.FlowFormEntity.status);										//任务状态
						$("#templateName").val(result.obj.FlowFormEntity.name);													//任务名称
						$("#templateDescription").val(result.obj.FlowFormEntity.description);  									//任务描述
						$("#relateTemplate").attr("data-id",result.obj.FlowFormEntity.parentId);  								//父任务ID
						$("#relateTemplate").val(result.obj.moduleName);  														//父任务名称
						$("#templateIcon li[name='"+result.obj.FlowFormEntity.logo+"']").addClass("selected");					//任务图标
						//$("#templateIsEdit").attr("checked", result.obj.FlowFormEntity.isEdit == 0 ? false : true);			//应用直接进入新增
						//$("#templateSaveIsEdit").attr("checked", result.obj.FlowFormEntity.isSaveEdit == 0 ? false : true);	//保存后可编辑
						$("#isStartAssign").attr("checked", result.obj.FlowFormEntity.isStartAssign == 0 ? false : true);		//动态选择审批人
						$("#notifyType").attr("checked", result.obj.FlowFormEntity.notifyType == 0 ? false : true);				//动态选择审批人
						$("#isSharefolder").attr("checked", result.obj.FlowFormEntity.isSharefolder == 0 ? false : true);				//发布到共享文件夹
						$("#templateVersion").text(result.obj.FlowFormEntity.version);											//版本
						
						formHome.formSaveEvtBind(fb_update);
						
						if(result.obj.FlowFormEntity.isStartAssign == 0 ? false : true){
							$("#aproPermPersonDiv").hide();
						}else{
							$("#aproPermPersonDiv").show();
						}
						
						//渲染任务使用人
						/*
						$("#templateModal").data("tpltData", {"userList": []});
						$.each(result.obj.AppFormUser, function(i, n){
							var avatarUrl = "";
				    		if(n.type == "user"){
				    			avatarUrl = ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png"
				    		}else{
				    			avatarUrl = selUser.getKindIcon(n.type);
				    		}
							$("#templateModal").data("tpltData").userList.push({
								"id": n.id, 
				    			"avatarImgUrl": avatarUrl, 
				    			"name": n.name || "张三",
				    			"orderby": n.orderby || i,
				    			"type": n.type || "user"
							});
						}); 
						*/
						//result.obj.AppFormUser = [{"id":"402881b2525972590152597458b10000","type":"role"},{"id":"f898202150caf28e0150cb02aa710007","type":"user"},{"id":"f898202152540a3c0152543355d20006","type":"org"},{"id":"f898202152531b4301525381daac0088","type":"org"}];
						$("#tpltPermPerson > ul > li").not(".add-item").remove();
				    	selUser.dataToLi(result.obj.AppFormUser, $("#tpltPermPerson > ul"), "prepend");
				    	$("#templateModal").data("tpltData", {"userList": result.obj.AppFormUser});
						//$userLiList.clone().prependTo($("#tpltPermPerson > ul"));
						
						//如果是关联任务，则屏蔽任务使用人的编辑功能，同时显示主任务的名称
						if(result.obj.FlowFormEntity.parentId != -1){
							//$("#field_tempUser").addClass("hidden");
							$("#tpltPermSetBtn").removeAttr("data-toggle")
								.removeAttr("data-target")
								.addClass("hidden");
							$("#tpltPermPerson li").off();
							
							$("#field_relatedTemp").removeClass("hidden");
						}else{
						}
				    	
						//渲染审批人
						/*
						$("#templateModal").data("aproData", {"userList": []});
						$.each(result.obj.AppFormApproveUser, function(i, n){
							var avatarUrl = "";
				    		if(n.type == "user"){
				    			avatarUrl = ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png"
				    		}else{
				    			avatarUrl = selUser.getKindIcon(n.type);
				    		}
							$("#templateModal").data("aproData").userList.push({
								"id": n.id, 
				    			"avatarImgUrl": avatarUrl, 
				    			"name": n.name || "张三",
				    			"orderby": n.orderby || i,
				    			"type": n.type || "user"
							});
						}); 
						*/
						$("#aproPermPerson > ul > li").not(".add-item").remove();
						selUser.dataToLi(result.obj.AppFormApproveUser, $("#aproPermPerson > ul"), "prepend");
						$("#templateModal").data("aproData", {"userList": result.obj.AppFormApproveUser});
				    	//$userLiList.clone().prependTo($("#aproPermPerson > ul"));
						
				    	
						//渲染数据权限
						/*
						$("#templateModal").data("dtData", {"userList": []});
						$.each(result.obj.AppFormUserData, function(i, n){
							var avatarUrl = "";
				    		if(n.type == "user"){
				    			avatarUrl = ("${attachForeRequest }" + n.portrait) || "basic/img/avatars/avatar_80.png"
				    		}else{
				    			avatarUrl = selUser.getKindIcon(n.type);
				    		}
							$("#templateModal").data("dtData").userList.push({
								"id": n.id,
				    			"avatarImgUrl": avatarUrl, 
				    			"name": n.name || "张三",
				    			"orderby": n.orderby || i
							});
						}); 
						*/
						
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
			formHome.formSave(payload, false);
	    });
		obj.on('deploy', function(payload){
			formHome.formSave(payload, true);
	    });
	},
	//表单保存方法
	formSave: function(payload, isDeploy){
		//console.log(typeof payload);
		//console.log(typeof JSON.parse(payload));
		//debugger;
		home.handleLoading(true, top.$("body"));
		var saveData = {},
			saveFormButton = $(".fb-main").find(".js-save-form").attr("disabled", true),
	    	deployFormButton = $(".fb-main").find("#btn-deploy").attr("disabled", true);
		
		//saveFormButton.attr("disabled", true);
		//deployFormButton.attr("disabled", true);
		
		//处理payload的层级关系
		payload = JSON.parse(payload);
		//console.info(payload);
		if(isDeploy == "1"){
			$.each(payload.fields, function(i, item) {
				if(item.label == ""){
					//$("#templateName").testRemind("请输入有效的手机号码！");
					alert("要素名称不能为空");
					saveFormButton.attr("disabled", false);
					deployFormButton.attr("disabled", false);
					home.handleLoading(false, top.$("body"));
					return false;
				}
			});
		}
		if($("#templateName").val() == ""){
			alert("任务名称不能为空！");
			saveFormButton.attr("disabled", false);
			deployFormButton.attr("disabled", false);
			home.handleLoading(false, top.$("body"));
			return false;
		}
		if(!/^[^&%]*$/.test($("#templateName").val())){
			alert("任务名称不能包含“%”或“&”符号！");
		}
		
		/*
		var fieldsTmp = [];
		$.each(payload.fields, function(){
			var _this = this;
			if(_this.pcid){
				var parentDetail = $.grep(payload.fields, function(n, i){return n.cid == _this.pcid});
				//console.log(parentDetail);
				parentDetail[0].fields = [];
				//$.grep(payload.fields, function(n, i){return n.cid == _this.pcid}).fields = [];
				parentDetail[0].fields.push(_this);
				//$.extend();
				console.log(parentDetail);
				
			}
		}); 
		*/
		//console.log(payload);
		
	    //任务信息
	    saveData.FlowFormEntity = {
    		"id": $(".fb-main").attr("id") || "",
    		"status": $(".fb-main").attr("data-status") || 0,
    		"name": $("#templateName").val(),
    		"description": $("#templateDescription").val(),
    		"parentId": $("#relateTemplate").attr("data-id") == "" ? "-1": $("#relateTemplate").attr("data-id"), 
    		"logo": $("#templateIcon li.selected").attr("name"), 
    		//"isEdit": $("#templateIsEdit").is(":checked") == true ? 1 : 0,
    		//"isSaveEdit": $("#templateSaveIsEdit").is(":checked") == true ? 1 : 0,
    		"isStartAssign": $("#isStartAssign").is(":checked") == true ? 1 : 0,
    		"notifyType": $("#notifyType").is(":checked") == true ? 1 : 0,
    		"isSharefolder": $("#isSharefolder").is(":checked") == true ? 1 : 0,
    		"version": 0
	    };
	    //console.log("savedata");
	    //console.log(saveData);
	    //任务字段
	    saveData.AppFormField = payload;
	    //任务权限
	    if($("#templateModal").data("tpltData")){
		    saveData.AppFormUser = {"userList": $.map($("#templateModal").data("tpltData").userList, function(n, i){
		    	//console.log(n);
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
 	    //console.log(saveData);
	    //console.log(JSON.stringify(saveData));
	    
		//debugger;
		//console.log(JSON.stringify(saveData));
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
				console.log(msg);
				home.handleLoading(false, top.$("body"));
				setTimeout(function(){
					saveFormButton.attr("disabled", false);
					deployFormButton.attr("disabled", false);
				}, 1000);
			},
			success : function(result) {
				if(result.success){
					alert(result.msg);
					home.handleLoading(false, top.$("body"));
					isDeploy = isDeploy ? 1 : 0;
					if(isDeploy == 1){
						top.$(".menu-pannel-body").load("loginController.do?appSetting&load=1");
					}else{
						$(".fb-main").attr("id",result.formId);
					}
				}else{
					alert("保存失败");
					home.handleLoading(false);
				} 
				setTimeout(function(){
					saveFormButton.attr("disabled", false);
					deployFormButton.attr("disabled", false);
				}, 1000);
			}
		});
	},
	
	//获取所有任务
	getTplList : function(){
		//debugger;
		var formId = "${formId}";
		if(typeof(formIbd)=="undefined"){
			formId="";
		}
	    $.ajax({
			url : 'flowFormController.do?queryAllMyFlowFormList&formId='+formId,
			type : 'post',
			data: '',
			dataType: "json",
			success : function(result) {
				if(result.success){
					$("#tplList").empty();
					$.each(result.obj, function(i, item) {
						//console.log(item);
						var templi = "<li id='"+item.id+"' name='"+item.name+"'><div class='name'><p>" + (item.name == "" ? "未定义" : item.name) + "</p></div><div class='status'><i class='glyphicon glyphicon-ok'></i><i class='glyphicon glyphicon-remove'></i></div></li>";
									
						$("#tplList").append(templi);
						
						//选择关联任务点击事件
						$(".person-list.all li").on("click", function() {
							var $this = $(this), 
								$thisStatus = $this.find(".status");
							
							$this.addClass("selected");
							$this.siblings("li").removeClass("selected");
							$thisStatus.addClass("ok");
							$this.siblings("li").find(".status").removeClass("ok");
							
							$("#tpltConfirm_t").removeAttr("disabled");
						});
					});
				}
			}
		});
	},
	//加载图标
	loadFormIcon: function(){
		var imgURL = '${attachForeRequest}';
/* 	    $.ajax({
			url : 'formLogoController.do?queryLogoList',
			type : 'post',
			data: '',
			async: false,
			dataType: "json",
			success : function(result) {
				if(result.success){
					var templi = "<ul class='list-style-none-h'>";
					$.each(result.obj, function(i, item) {
						templi = templi+"<li name='"+item.logo+"'><img class='icon' src='"+imgURL+item.logo+"' /><i class='fa fa-check check-flag'></i></li>";
					});
					templi = templi+"</ul>";
					 $(".form-field .icon-box").append(templi);
					 
					//绑定图标点击事件
				    $(".form-field .icon-box li").on("click", function(){
				    	$(this).addClass("selected");
				    	$(this).siblings("li").removeClass("selected");
				    });
				}
			}
		}); */
		
 		//加载图标列表
	    $(".form-field .icon-box").append("<ul class='list-style-none-h'>"+
	    		"<li name='cheliangxinxi'><img class='icon' src='basic/img/logo/cheliangxinxi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='daishoukuaidi'><img class='icon' src='basic/img/logo/daishoukuaidi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='dashuju'><img class='icon' src='basic/img/logo/dashuju.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='dingcanguanli'><img class='icon' src='basic/img/logo/dingcanguanli.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='dingdan'><img class='icon' src='basic/img/logo/dingdan.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='fawennigao'><img class='icon' src='basic/img/logo/fawennigao.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='feiji'><img class='icon' src='basic/img/logo/feiji.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='gerenzhongxinwodeqianbao'><img class='icon' src='basic/img/logo/gerenzhongxinwodeqianbao.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='gongwenbao'><img class='icon' src='basic/img/logo/gongwenbao.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='icon'><img class='icon' src='basic/img/logo/icon.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='icon06'><img class='icon' src='basic/img/logo/icon06.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='jiagebiaoqian'><img class='icon' src='basic/img/logo/jiagebiaoqian.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='jituankehutiyanshiyebu'><img class='icon' src='basic/img/logo/jituankehutiyanshiyebu.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='plane'><img class='icon' src='basic/img/logo/plane.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='qingjiashi'><img class='icon' src='basic/img/logo/qingjiashi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='rizhi'><img class='icon' src='basic/img/logo/rizhi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='shenpi'><img class='icon' src='basic/img/logo/shenpi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='shenpibaoxiao'><img class='icon' src='basic/img/logo/shenpibaoxiao.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='shujutongji'><img class='icon' src='basic/img/logo/shujutongji.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='tongji'><img class='icon' src='basic/img/logo/tongji.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='tongxunlu'><img class='icon' src='basic/img/logo/tongxunlu.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='tongzhigonggao'><img class='icon' src='basic/img/logo/tongzhigonggao.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmu'><img class='icon' src='basic/img/logo/xiangmu.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmu1'><img class='icon' src='basic/img/logo/xiangmu1.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmuleixing'><img class='icon' src='basic/img/logo/xiangmuleixing.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmushenli'><img class='icon' src='basic/img/logo/xiangmushenli.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmushezhi'><img class='icon' src='basic/img/logo/xiangmushezhi.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiangmutongji'><img class='icon' src='basic/img/logo/xiangmutongji.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xiaoxihezi2'><img class='icon' src='basic/img/logo/xiaoxihezi2.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xinfeng'><img class='icon' src='basic/img/logo/xinfeng.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xingyeyingyong'><img class='icon' src='basic/img/logo/xingyeyingyong.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='xinwendongtai'><img class='icon' src='basic/img/logo/xinwendongtai.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='yingyong'><img class='icon' src='basic/img/logo/yingyong.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='yinxingqia'><img class='icon' src='basic/img/logo/yinxingqia.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='yonghua'><img class='icon' src='basic/img/logo/yonghua.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='zhishishujuji'><img class='icon' src='basic/img/logo/zhishishujuji.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='zijinanquan'><img class='icon' src='basic/img/logo/zijinanquan.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='zijinguanli'><img class='icon' src='basic/img/logo/zijinguanli.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"<li name='ziyuanguanli'><img class='icon' src='basic/img/logo/ziyuanguanli.png' /><i class='fa fa-check check-flag'></i></li>"+
	    		"</ul>"); 
		
	    //绑定图标点击事件
	    $(".form-field .icon-box li").on("click", function(){
	    	$(this).addClass("selected");
	    	$(this).siblings("li").removeClass("selected");
	    });
	},
	
    //初始化formbuilder
    initFormbuilder: function(fieldsData){
    	fb = new Formbuilder({
  	      selector: '.fb-main',
  	      bootstrapData: fieldsData
  	      /* 
  	      */
  	    });
  	    //formbuilder保存事件
  	    formHome.formSaveEvtBind(fb);
  	  	
  	    formHome.loadFormIcon();
  	    
        home.loadSlimScroll([ {
    		obj : $('.person-box .slim-div'),
    		//obj : $('.person-box'),
    		width: "100%"
    	}]);
        
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
    	//选人界面点击确认关闭时，保存右侧已选择的人员列表
        $("#tpltConfirm").on("click", function(e){
        	return;
        	var $userLiList = $("#templateModal .person-list.approval-flow li"),
        		$this = $("#templateModal");
        	
        	if($this.data("modalType") == "tplt"){
        		//任务权限
    	    	var tpltData = {"userList": []}; 
    	    	
    	    	tpltData.userList = selUser.liToData($userLiList, tpltData.userList);
    	    	$("#tpltPermPerson > ul > li").not(".add-item").remove();
    	    	$userLiList.clone().prependTo($("#tpltPermPerson > ul"));
    	    	
    	    	$this.data("tpltData", tpltData);
        	}else if($this.data("modalType") == "apro"){
        		//审批权限
    	    	var aproData = {"userList": []}; 
    	    	
    	    	aproData.userList = selUser.liToData($userLiList, aproData.userList);
    	    	$("#aproPermPerson > ul > li").not(".add-item").remove();
    	    	$userLiList.clone().prependTo($("#aproPermPerson > ul"));
    	    	$this.data("aproData", aproData);
        	}else{
        		//数据权限
    	    	var dtData = {"userList": []}; 
    	    	
    	    	dtData.userList = selUser.liToData($userLiList, dtData.userList);
    	    	$this.data("dtData", dtData);
        	}
        	
        });
    	
        //选人弹框，全选、全部移除事件
        $(".select-all").on("click", function(){
        	//userList
        	$("#userList > li").each(function(){
    	    	var $thisLi = $("<li id='"+this.id+"'>"+
    	    			"<div class='item-person'>"+
    	    			$(this).html()+
    	    			"</div>"+
    	    			"<i class='glyphicon glyphicon-remove'></i>"+
    	    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
    	    			"</li>").click(function(){
    	    				$(this).closest("li").remove();
    	    				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    	    			});
    			
    	    	
    	    	if($("#templateModal").data("modalType") == "tplt" && $(".person-list.approval-flow ul").find("li[id='"+this.id+"']").length > 0){
    	    		return;
    	    	}
    	    	$(".person-list.approval-flow ul").append($thisLi);
    	    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
        	});
        });
        $(".remove-all").on("click", function(){
        	//selectedUser
        	$("#selectedUser").empty();
        	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
        });
        
        //搜索用户
    	$("#searchUser").on("input propertychange",function(){
    		var flag = 0;
    		clearTimeout(timer);
    		timer=setTimeout(function(){
    		var imgURL = '${attachForeRequest }';
    		var key = $("#searchUser").val();
    		if(key == null || key==""){
    			flag = 1;
    			$("#treeUserList").parent(".person-box").children(".pb-item").show();
    			$("#searchUserDiv").remove();
    		}else{
    			flag = 0;
    		}
    		var data1 = {"key" : key};
    		if(flag == 0){
    		 $.ajax({
    				url : 'orgGroupController.do?queryUsersByLikeKey',
    				type : 'post',
    				data: data1,
    				dataType: "json",
    				success : function(result) {
    					//$("#searchUserUl").show();
    					$("#treeUserList").parent(".person-box").children(".pb-item").hide();
    					if(result.success){
    						//$("#searchUserUl").empty();
    						var templi = "<div class='person-list all' id='searchUserDiv'>"
										+"<ul class='list-style-none' id='searchUserUl'>";
    						$.each(result.obj, function(i, item) {
    							//console.log(item);
    							templi = templi+"<li id='"+item.id+"'>"
    										+"<div class='avatar'><img src='"+imgURL+item.portrait+"'></div>"
    										+"<div class='name'><p class='text-overflow' title='"+item.name+"'>"+item.name+"</p></div>"
    										+"</li>";
    						});
    						templi = templi+"</ul></div>";
    						$("#treeUserList").parent(".person-box").append(templi);
    					    //选人弹框中，左侧所有人列表项点击添加到右侧
    					    $(".person-list.all li").on("click",function(){
    					    	var $thisLi = $("<li id='"+this.id+"'>"+
    					    			"<div class='item-person'>"+
    					    			$(this).html()+
    					    			"</div>"+
    					    			"<i class='glyphicon glyphicon-remove'></i>"+
    					    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
    					    			"</li>").click(function(){
    					    				$(this).closest("li").remove();
    					    				$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    					    			});
    							
    					    	if($("#templateModal").data("modalType") == "tplt" && $(".person-list.approval-flow ul").find("li[id='"+this.id+"']").length > 0){
    					    		return;
    					    	}
    					    	
    					    	$(".person-list.approval-flow ul").append($thisLi);
    					    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    					    });
    					    $(".person-list.approval-flow li .glyphicon-remove").on("click", function(){
    					    	$(this).closest("li").remove();
    					    	//console.log($(".person-list.approval-flow ul > li").length);
    					    	$(".selected-box .selected-count").text($(".person-list.approval-flow ul > li").length);
    					    });
    					}
    				}
    			});
    		}
    		},500);
        });
        
    	//选任务弹框点击确认关闭时，触发事件
        $("#tpltConfirm_t").on("click", function(e){
        	var $selectedTpl = $("#sltTmpModal .person-list.all li.selected"),
        		relatedInfo = formHome.getRelatedInfo($selectedTpl.attr("id"));

        	//formHome.getRelatedInfo($selectedTpl.attr("id"));
        	formHome.initFormbuilder(relatedInfo.fieldJson.fields);
        	
        	$("#relateTemplate").val($selectedTpl.attr("name"));
        	$("#relateTemplate").attr("data-id",$selectedTpl.attr("id"));
        	
        	$("#selectFormType").addClass("hidden");
        	$(".fb-main").removeClass("hidden");
        	$("#field_relatedTemp").removeClass("hidden");
        	//$("#field_tempUser").addClass("hidden");
        	
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
        	selUser.dataToLi($("#tpltPermSetBtn").data("tpltData").userList, $("#tpltPermPerson > ul"), "prepend");
        	$("#tpltPermPerson li").off();
        });
        
        //点击选择“普通任务”按钮，触发事件
        $("#formtype-normal").on("click", function(){
        	formHome.initFormbuilder([]);
        	
        	$("#selectFormType").addClass("hidden");
        	$(".fb-main").removeClass("hidden");
        });
    },
    
    //初始化绑定formbuilder里的事件
    initFormEvent: function(){
    	
    	//选人界面打开时，初始化右侧已选择的人员列表
        $(".add-item").on("click", function(e){
        	var type = $(this).attr("data-type"),
        		option = {};
       		
        	option.type = type;
        	
        	if(type == "tplt"){
        		option.title = "任务权限";
        		option.selected = selUser.liToData($("#tpltPermPerson > ul > li").not(".add-item"), []);  //$("#tpltPermPerson > ul")
        	}else if(type == "apro"){
        		option.title = "审批权限";
        		option.selected = selUser.liToData($("#aproPermPerson > ul > li").not(".add-item"), []);  //$("#tpltPermPerson > ul")
        	}
       		selUser.selectUser(option, function(t){
       			var $userLiList = $("#templateModal .person-list.approval-flow li"),
	        		$modal = $("#templateModal");
	        	
	        	if(t == "tplt"){
	        		//任务权限
	    	    	var tpltData = {"userList": []}; 
	    	    	
	    	    	tpltData.userList = selUser.liToData($userLiList, tpltData.userList);
	    	    	$("#tpltPermPerson > ul > li").not(".add-item").remove();
	    	    	$userLiList.clone().prependTo($("#tpltPermPerson > ul"));
	    	    	
	    	    	$modal.data("tpltData", tpltData);
	        	}else if(t == "apro"){
	        		//审批权限
	    	    	var aproData = {"userList": []}; 
	    	    	
	    	    	aproData.userList = selUser.liToData($userLiList, aproData.userList);
	    	    	$("#aproPermPerson > ul > li").not(".add-item").remove();
	    	    	$userLiList.clone().prependTo($("#aproPermPerson > ul"));
	    	    	$modal.data("aproData", aproData);
	        	}
       		});
        	/*
        	var $targetBtn = $(this),
        		$this = $("#templateModal"),
        		$modalTitle = $this.find(".modal-title"),
        		$userList = $(".person-list.approval-flow > ul").empty();
        	
        	if(this.id == "tpltPermSetBtn"){
    	    	$this.find(".modal-dialog").addClass("with-select-all");
        	}else if(this.id == "aproPermSetBtn"){
    	    	$this.find(".modal-dialog").removeClass("with-select-all");
        	}
        	
        	//调用获取用户列表方法
    		formHome.getUserList();
    		formHome.getRoleList();
    		
    		$("#searchUser").val("");
        	if($targetBtn.attr("id") == "tpltPermSetBtn"){
        		//debugger;
    	    	$modalTitle.text("任务权限");
    	    	$this.data("modalType","tplt");
    	    	//console.log($this.data("tpltData"));
    	    	if($this.data("tpltData")){
    		    	selUser.dataToLi($this.data("tpltData").userList, $userList);
    		    	$(".selected-box .selected-count").text($this.data("tpltData").userList.length);
    	    	}else{
    	    		$(".selected-box .selected-count").text("0");
    	    	}
        	}else if($targetBtn.attr("id") == "aproPermSetBtn"){
    	    	$modalTitle.text("审批权限");
    	    	$this.data("modalType","apro");
    	    	if($this.data("aproData")){
    		    	selUser.dataToLi($this.data("aproData").userList, $userList);
    		    	$(".selected-box .selected-count").text($this.data("aproData").userList.length);
    	    	}else{
    	    		$(".selected-box .selected-count").text("0");
    	    	}
        	}else{
    	    	$modalTitle.text("数据权限");
    	    	$this.data("modalType","data");
    	    	if($this.data("dtData")){
    		    	selUser.dataToLi($this.data("dtData").userList, $userList);
    		    	$(".selected-box .selected-count").text($this.data("dtData").userList.length);
    	    	}else{
    	    		$(".selected-box .selected-count").text("0");
    	    	}
        	}
        	*/
        });
        
        //任务名称不能以数字开头
        $("#templateName").keyup(function(){
        	//console.log("test");
        	var regStr = /^[0-9]*$/;
        	if(regStr.test($(this).val().charAt(0))){
        		$(this).val("");
        	}
        });
        
        //判断是否勾选动态选择审批人
        $("#isStartAssign").on("click",function(){
        	if($("#isStartAssign").is(':checked')){
        		$("#aproPermPersonDiv").hide();
        	}else{
        		$("#aproPermPersonDiv").show();
        	}
        });
    }
  	
};	

var timer;

$(function(){
	formHome.initPageEvent();
	
	//获取所有任务列表
    formHome.getTplList();
	
	if("${formId}".length > 0){
		//$("#selectFormType").addClass("hidden");
    	$(".fb-main").removeClass("hidden");
		formHome.loadTemplate();
	    formHome.initFormEvent();
	}else{
		$("#selectFormType").removeClass("hidden");
    	//$(".fb-main").addClass("hidden");
	    //formHome.initFormbuilder();
	}
	

});
</script>

