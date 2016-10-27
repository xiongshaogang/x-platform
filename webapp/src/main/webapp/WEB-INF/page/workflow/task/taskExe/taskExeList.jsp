<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="taskExeList" checkbox="true" fitColumns="true" title="转办代理实例列表" actionUrl="taskExeController.do?datagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="主键"  field="taskId"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="主键"  field="actInstId"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="项目名称"  field="businessName" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务名称"  field="taskName" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="实例名称"  field="subject"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="所属流程"  field="defName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd HH:mm:ss" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务所属人人"  field="ownerName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="转办（代理）人"  field="assigneeName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务执行人"  field="exeUserName" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="转办（代理）类型"  field="type" replace="代理_1,转办_2"  hidden="true"  queryMode="single"  width="50"></t:dgCol>
	   <t:dgCol title="执行时间"  field="exeTime" formatter="yyyy-MM-dd HH:mm:ss" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="状态"  field="status"  hidden="true" replace="代理中_0,自己完成_1,取消_2,代理人完成_3,退回_4"  queryMode="single"  width="120"></t:dgCol>
	   
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgDelOpt title="取消" icon="awsm-icon-trash red"  url="taskExeController.do?delete&id={id}" />
	   <t:dgOpenOpt url="processInstanceController.do?processImage&actInstId={actInstId}" icon="awsm-icon-zoom-in green" exParams="{optFlag:'detail'}" height="500" width="840" title="流程图"></t:dgOpenOpt>
	   <t:dgOpenOpt preinstallWidth="2" url="definitionController.do?showTaskOpinions&actInstId={actInstId}" icon="awsm-icon-zoom-in green" height="530" title="审批历史"></t:dgOpenOpt>
	   
	   <t:dgToolBar title="批量取消"  icon="awsm-icon-remove"  url="taskExeController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
	  </t:datagrid>
  </div>
</div>
<script>
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
	function bacthComplete(){
		var rows = $("#myTaskList").datagrid('getSelections');
		var ids=[];
		if (rows.length > 0) {
			for (var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		}else{
			tip("没有选择任务");
			return ;
		}
		var bacthCompleteButton = [ {
			text : '审批',
			iconCls : 'awsm-icon-save',
			handler : function() {
				var a=$("#batchCompleteForm");
				$.ajax({
					cache : true,
					type : 'POST',
					url : "taskController.do?batchComplete&taskIds="+ids,// 请求的action路径
					data : $("#batchCompleteForm").serialize(),
					async : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							closeD(getD($(this)));
							$("#myTaskList").datagrid("reload");
						}
					}
				});
				
			}
		}, {
			text : '关闭',
			iconCls : 'awsm-icon-remove',
			handler : function() {
				closeD(getD($(this)));
			}
		} ];
		createwindow("批量审批", "taskController.do?bacthCompleteEdit", null, 300, 1,{buttons:bacthCompleteButton});
	}
</script>
