//加签
function showAddSignWindow(taskId) {
	var myButton = [ {
		text : '确定加签',
		iconCls : 'awsm-icon-save',
		handler : function() {
			$("#btn_sub",$("#form_addSign")).click();
		}
	}, {
		text : '取消',
		iconCls : 'awsm-icon-remove',
		handler : function() {
			closeD(getD($(this)));
		}
	} ];
	createwindow("加签处理", "definitionController.do?showAddSign&taskId="+taskId, null, 155, 1, {
		formId : "form_addSign",
		buttons : myButton
	});
}

// 自由流更改节点
function changeDestTask(sel, taskId) {
	var nodeId = sel.value;
	if (typeof nodeId == 'undefined') { // 对象不是用原始JS的，而是通过Jquery获取的对象
		nodeId = sel.val();
	}
	if (typeof nodeId == 'undefined' || nodeId == null || nodeId == "") {
		$('#lastDestTaskId').val("");
		return;
	}
	$('#lastDestTaskId').val(nodeId);
	var url = "taskController.do?getTaskUsers&taskId=" + taskId + "&nodeId=" + nodeId;
	$.getJSON(url, function(dataJson) {
		var data = eval(dataJson);
		var userIds = "";
		var userNames = "";
		for (var i = 0; i < data.length; i++) {
			var executor = data[i];
			if (typeof userIds == 'undefined' || userIds == null || userIds == "") {
				userIds = executor.executeId + "^^" + executor.executor + "^^" + executor.type;
			} else {
				userIds = userIds + "," + executor.executeId + "^^" + executor.executor + "^^" + executor.type;
			}

			if (typeof userNames == 'undefined' || userNames == null || userNames == "") {
				userNames = executor.executor
			} else {
				userNames = userNames + "," + executor.executor
			}
		}
		$("#freeUserSelectId").val(userIds);
		$("#freeUserSelectId").attr("name", nodeId + "_userId");
		$("#freeUserSelectName").val(userNames);
	});

}

// 选择路径1
function choosePath(taskId, jupmType,choosePathBefore,choosePathAfter) {
	createwindow("选择路径跳转", "taskController.do?showTaskSubmit&taskId=" + taskId + "&jumpType=" + jupmType, 600, 400,
			null, {
				optFlag : 'add',
				buttons : [ {
					text : '提交',
					handler : function() {
						if(typeof choosePathBefore=="function"){
							var result=choosePathBefore(taskId,jupmType);
							if(result!=undefined&&(!result)){
								return;
							}
						}
						var choiceData = "";
						var choice = $("input[name=sublujingChoice]:checked").val();
						if (choice == "1") {
							choiceData = $("#nodeUserChoiceData").serialize();
						} else if (choice == "2") {
							choiceData = $("#nodeUserChoiceData").serialize();
						} else if (choice == "3") {
							choiceData = $("#freeJumpData").serialize() + "&back=3";
						}
						var voteAgree = "";
						if($("#conclusionApprove").attr("class")!=undefined&&$("#conclusionApprove").attr("class").indexOf("easyui-combobox")!=-1){
							voteAgree = $("#conclusionApprove").combobox('getValue');// 同意，不同意，弃权
						}else{
							voteAgree = $("#conclusionApprove").val();// 同意，不同意，弃权
						}
						if (voteAgree == "" || voteAgree == null || voteAgree == "undefined") {
							alertTip('请填写结论');
							return;
						}
						if (voteAgree == "1") {// 同意
							if ($("#chkDirectComplete").length > 0) {
								if ($("#chkDirectComplete").attr("checked") == "checked") {
									voteAgree = "5";// 会签直接同意
								}
							}
						} else if (voteAgree == "2") {
							if ($("#chkDirectComplete").length > 0) {
								if (!$("#chkDirectComplete").attr("checked") == "checked") {
									voteAgree = "6";// 会签直接不同意
								}
							}
						}
						$.messager.confirm("流转提醒", "您是否确定提交？", function(r) {
							if (r) {
								$.ajax({
									cache : true,
									type : 'POST',
									url : "taskController.do?complete&taskId=" + taskId + "&voteAgree=" + voteAgree,// 请求的action路径
									data : $("#approveForm").serialize() + "&" + choiceData,
									async : false,
									success : function(data) {
										var d = $.parseJSON(data);
										if (d.success) {
											if(typeof choosePathAfter=="function"){
												var result=choosePathAfter(data);
												if(result!=undefined&&(!result)){
													return;
												}
											}
											var msg = d.msg;
											tip(msg);
											closeD(getD($("#taskSubmit_jumpView")));
											refreshRelateGrid();
										}
									}
								});
							}
						});
					}
				}, {
					text : '关闭',
					handler : function() {
						closeD(getD($(".taskSubmit_jumpView")));
					}
				} ]
			});
}

// 加载会签数据
function loadTaskSign() {
	// $(".taskOpinion").load('${ctx}/platform/bpm/task/toSign.ht?taskId=${task.id}');
}

// 驳回1
function rejectTask(taskId,rejectTaskBefore,rejectTaskAfter) {
	if(typeof rejectTaskBefore=="function"){
		var result=rejectTaskBefore(taskId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	
	$.messager.confirm("驳回提醒", "您是否确定回退到上一步？", function(r) {
		if (r) {
			$.ajax({
				cache : true,
				type : 'POST',
				url : "taskController.do?complete&taskId=" + taskId + "&voteAgree=3&back=1",// 请求的action路径
				data : $("#approveForm").serialize(),
				async : false,
				success : function(data) {
					if(typeof rejectTaskAfter=="function"){
						var result=rejectTaskAfter(data);
						if(result!=undefined&&(!result)){
							return;
						}
					}
					var d = $.parseJSON(data);
					if (d.success) {
						var msg = d.msg;
						tip(msg);
						closeD(getD($("#conclusionApprove")));
						refreshRelateGrid();
					}
				}
			});
		}
	});
}

// 驳回到发起人1
function rejectTaskToStart(taskId,rejectTaskToStartBefore,rejectTaskToStartAfter) {
	if(typeof rejectTaskToStartBefore=="function"){
		var result=rejectTaskToStartBefore(taskId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	$.ajax({
		cache : true,
		type : 'POST',
		url : "taskController.do?complete&taskId=" + taskId + "&voteAgree=3&back=2",// 请求的action路径
		data : $("#approveForm").serialize(),
		async : false,
		success : function(data) {
			if(typeof rejectTaskToStartAfter=="function"){
				var result=rejectTaskToStartAfter(data);
				if(result!=undefined&&(!result)){
					return;
				}
			}
			var d = $.parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				tip(msg);
				closeD(getD($("#conclusionApprove")));
				refreshRelateGrid();
			}
		}
	});
}

// 任务转办1
function changeAssignee(taskId,changeAssigneeBefore,changeAssigneeAfter) {
	if(typeof changeAssigneeBefore=="function"){
		var result=changeAssigneeBefore(taskId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	createwindow("任务转办", "taskExeController.do?assignee&taskId=" + taskId, 400, 320, null, {
		optFlag : 'add',
		buttons : [ {
			text : '提交',
			handler : function() {
				var description = $("#taskExeAssigneeForm #description").val();
				var informType = $("#taskExeAssigneeForm #informType").combobox("getValues");
				var assignee = $("#taskExeAssigneeForm #exeUserId").val();
				if (assignee == "" || assignee == null || assignee == "undefined") {
					tip("任务接收人不能为空");
					return;
				}
				if (informType == "" || informType == null || informType == "undefined") {
					tip("通知方式不能为空");
					return;
				}
				if (description == "" || description == null || description == "undefined") {
					tip("转办原因不能为空");
					return;
				}
				$.ajax({
					cache : true,
					type : 'POST',
					url : "taskExeController.do?assignSave&taskId=" + taskId,// 请求的action路径
					data : $("#taskExeAssigneeForm").serialize(),
					async : false,
					success : function(data) {
						if(typeof changeAssigneeAfter=="function"){
							var result=changeAssigneeAfter(data);
							if(result!=undefined&&(!result)){
								return;
							}
						}
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							closeD(getD($("#conclusionApprove")));
							refreshRelateGrid();
							closeD(getD($("#taskExeAssigneeForm")));
						}
					}
				});
			}
		}, {
			text : '关闭',
			handler : function() {
				closeD(getD($("#taskExeAssigneeForm")));
			}
		} ]
	});

}

function handJumpOrClose() {
	// if(window.opener){
	// try{
	// window.opener.location.href=window.opener.location.href.getNewUrl();
	// }
	// catch(e){}
	// }
	// window.close();
}

function isTaskEnd(callBack) {
	
}

// 显示流程图
function showTaskUserDlg(actInstId) {
	createwindow("流程图查看", "processInstanceController.do?processImage&actInstId=" + actInstId, 840, 500);
	// TaskImageUserDialog({actInstId:${processRun.actInstId}});
}

// 显示审批历史
function showTaskOpinions(actInstId) {
	createwindow("审批历史", "definitionController.do?showTaskOpinions&actInstId=" + actInstId, 1000, 550, null);
	// var
	// url="${ctx}/platform/bpm/taskOpinion/list.ht?runId=${processRun.runId}&isOpenDialog=1";
	// url=url.getNewUrl();
	// window.open(url,"taskOpinion","toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
}

// 终止1
function endAllTask(taskId) {
	$.ajax({
		cache : true,
		type : 'POST',
		url : "taskController.do?endProcess&taskId=" + taskId,// 请求的action路径
		async : false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				tip(msg);
				closeD(getD($("#conclusionApprove")));
				refreshRelateGrid();
			}
		}
	});
}

// 终止一个任务，实际上是往后流转1
function endTask() {
	$.ajax({
		cache : true,
		type : 'POST',
		url : "taskController.do?end&taskId=" + taskId,// 请求的action路径
		async : false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				tip(msg);
				closeD(getD($("#conclusionApprove")));
				refreshRelateGrid();
			}
		}
	});
}
//1
function nextProcess(taskId,nextProcessBefore,nextProcessAfter) {
	if(typeof nextProcessBefore=="function"){
		var result=nextProcessBefore(taskId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	var voteAgree = "";
	
	if($("#conclusionApprove").attr("class")!=undefined&&$("#conclusionApprove").attr("class").indexOf("easyui-combobox")!=-1){
		voteAgree = $("#conclusionApprove").combobox('getValue');// 同意，不同意，弃权
	}else{
		voteAgree = $("#conclusionApprove").val();// 同意，不同意，弃权
	}
	
	if (voteAgree == "" || voteAgree == null || voteAgree == "undefined") {
		alertTip('请填写结论');
		return;
	}
	if (voteAgree == "1") {// 同意
		if ($("#chkDirectComplete").length > 0) {
			if ($("#chkDirectComplete").attr("checked") == "checked") {
				voteAgree = "5";// 会签直接同意
			}
		}
	} else if (voteAgree == "2") {
		if ($("#chkDirectComplete").length > 0) {
			if (!$("#chkDirectComplete").attr("checked") == "checked") {
				voteAgree = "6";// 会签直接不同意
			}
		}
	} else if (voteAgree == "0") {
		if ($("#chkDirectComplete").length > 0) {
			if (!$("#chkDirectComplete").attr("checked") == "checked") {
				voteAgree = "7";// 会签直接再议
			}
		}
	}
	$.messager.confirm("流转提醒", "您是否确定往下流转？", function(r) {
		if (r) {
			$.ajax({
				cache : true,
				type : 'POST',
				url : "taskController.do?complete&taskId=" + taskId + "&voteAgree=" + voteAgree,// 请求的action路径
				data : $("#approveForm").serialize(),
				async : false,
				success : function(data) {
					if(typeof nextProcessAfter=="function"){
						var result=nextProcessAfter(data);
						if(result!=undefined&&(!result)){
							return;
						}
					}
					var d = $.parseJSON(data);
					if (d.success) {
						var msg = d.msg;
						tip(msg);
						closeD(getD($("#conclusionApprove")));
						refreshRelateGrid();
					}
				}
			});
		}
	})
	
}

//1
function nextProcessOnly(taskId) {
	var voteAgree = "";
	
	if($("#conclusionApprove").attr("class")!=undefined&&$("#conclusionApprove").attr("class").indexOf("easyui-combobox")!=-1){
		voteAgree = $("#conclusionApprove").combobox('getValue');// 同意，不同意，弃权
	}else{
		voteAgree = $("#conclusionApprove").val();// 同意，不同意，弃权
	}
	
	if (voteAgree == "" || voteAgree == null || voteAgree == "undefined") {
		alertTip('请填写结论');
		return;
	}
	if (voteAgree == "1") {// 同意
		if ($("#chkDirectComplete").length > 0) {
			if ($("#chkDirectComplete").attr("checked") == "checked") {
				voteAgree = "5";// 会签直接同意
			}
		}
	} else if (voteAgree == "2") {
		if ($("#chkDirectComplete").length > 0) {
			if (!$("#chkDirectComplete").attr("checked") == "checked") {
				voteAgree = "6";// 会签直接不同意
			}
		}
	}
	$.ajax({
		cache : true,
		type : 'POST',
		url : "taskController.do?complete&taskId=" + taskId + "&voteAgree=" + voteAgree,// 请求的action路径
		data : $("#approveForm").serialize(),
		async : false,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				var msg = d.msg;
				tip(msg);
				closeD(getD($("#conclusionApprove")));
				refreshRelateGrid();
			}
		}
	});
}

function divertTask(actInstId) {
	createwindow("转发", "processInsCptoController.do?divertTask&actInstId=" + actInstId, 440, 310, null, {
		formId : "form_divertTask",
		optFlag : "add"
	});
}

function redoTask(actInstId,redoTaskBefore,redoTaskAfter) {
	if(typeof redoTaskBefore=="function"){
		var result=redoTaskBefore(actInstId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	createwindow("追回", "processInstanceController.do?redoOrRecoverTask&actInstId=" + actInstId+"&backToStart=0", 440, 270, null, {
		formId : "form_redoOrRecoverTask",
		optFlag : "add"
	});
}

function recoverTask(actInstId,recoverTaskBefore,recoverTaskAfter) {
	if(typeof recoverTaskBefore=="function"){
		var result=recoverTaskBefore(actInstId);
		if(result!=undefined&&(!result)){
			return;
		}
	}
	createwindow("撤销", "processInstanceController.do?redoOrRecoverTask&actInstId=" + actInstId+"&backToStart=1", 440, 270, null, {
		formId : "form_redoOrRecoverTask",
		optFlag : "add"
	});
}

function showTask(url) {
	createwindow('审批', url, 900, 600, null, {
		noheader : true
	})
}

function showCptoList(id) {
	var url = "processInsCptoController.do?showCptoList&id=" + id + "&type=" + type;
	createwindow('抄送人', url, 900, 600)
}

//刷新待办、已办、首页待办等相关datagrid,以保证流转等操作之后的数据正确性
function refreshRelateGrid() {
	// 刷新首页我的待办列表
	if ($("#myTaskListPage")[0]) {
		$("#myTaskListPage").datagrid("load");
	}
	// 刷新已办事宜列表
	if ($("#completeTaskList")[0]) {
		$("#completeTaskList").datagrid("load");
	}
	// 刷新待办事宜列表
	if ($("#myTaskList")[0]) {
		$("#myTaskList").datagrid("load");
	}
	// 刷新办结事宜列表
	if ($("#endTaskList")[0]) {
		$("#endTaskList").datagrid("load");
	}
	// 刷新我的请求列表
	if ($("#requestInstanceList")[0]) {
		$("#requestInstanceList").datagrid("load");
	}
	// 刷新我的办结列表
	if ($("#completeInstanceList")[0]) {
		$("#completeInstanceList").datagrid("load");
	}
	// 刷新流程任务列表(所有任务的列表,一般管理员使用的功能)
	if ($("#taskList")[0]) {
		$("#taskList").datagrid("load");
	}
}