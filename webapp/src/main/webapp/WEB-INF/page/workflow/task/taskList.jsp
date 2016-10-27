<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%-- <t:base type="jquery,easyui,tools"></t:base> --%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px;">
	  <t:datagrid name="taskList" checkbox="true" fitColumns="true" title="任务列表" actionUrl="taskController.do?taskListDatagrid" idField="id" fit="true" queryMode="group">
	   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="项目名称"  field="businessName" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务名称"  field="name" query="true" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="实例名称"  field="subject"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="所属流程"  field="processName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="任务候选人"  field="orgName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="是否已读"  field="hasRead" replace="是_1,否_0"  hidden="true"  queryMode="single"  width="50"></t:dgCol>
	   <t:dgCol title="实例创建人"  field="creator"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd HH:mm:ss" hidden="true"  queryMode="single"  width="120"></t:dgCol>
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgOpenOpt title="任务处理" icon="awsm-icon-edit blue" exParams="{noheader:true}"  url="taskController.do?toStart&taskId={id}" width="1000" height="600" ></t:dgOpenOpt>

	   <t:dgToolBar title="批量处理" preinstallWidth="1"  height="300" icon="awsm-icon-plus" url="taskController.do?bacthCompleteEdit" funname="bacthComplete"></t:dgToolBar>
	  </t:datagrid>
  </div>
</div>
<script>
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
	function bacthComplete(){
		var rows = $("#taskList").datagrid('getSelections');
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
							$("#taskList").datagrid("reload");
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
