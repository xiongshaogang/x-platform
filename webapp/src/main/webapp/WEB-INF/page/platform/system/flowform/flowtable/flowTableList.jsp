<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:datagrid name="flowTableList"   checkbox="true" fitColumns="true" title="流程表单定义" actionUrl="flowTableController.do?datagrid" idField="id" fit="true"  queryMode="group"    pagination="true" >
   <t:dgCol title="id"  field="id" hidden="false"  width="120"  ></t:dgCol>
   <t:dgCol title="表名"  field="tableName" hidden="true" query="true" width="120"  ></t:dgCol>
   <t:dgCol title="注释"  field="content" hidden="true"  width="120"  ></t:dgCol>
   <t:dgCol title="是否生成"  field="ispublished" hidden="true"  width="120"  replace="已生成_1,未生成_0"></t:dgCol>
   <t:dgCol title="生成方式"  field="generateType" hidden="true"  width="120" replace="表单生成_1,数据库生成_0" ></t:dgCol>
   <t:dgCol title="是否主表"  field="isMainTable" hidden="true"  width="120"  replace="主表_1,子表_0"></t:dgCol>
   <t:dgCol title="主表"  field="mainTable" hidden="true"  width="120"  ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   
   <t:dgFunOpt title="发布" icon="awsm-icon-refresh green" funname="doPublish(id)"/>
   <t:dgFunOpt title="分组" icon="awsm-icon-group green" funname="groupTeam(id)"/>
   <t:dgDelOpt title="删除" url="flowTableController.do?delete&id={id}" callback="reflash"/>
   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="flowTableController.do?flowTableEdit" funname="add" buttonType="GridAdd" width="1024" height="620" exParams="{formId:'flowTableFormobj'}"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="awsm-icon-edit" url="flowTableController.do?flowTableEdit" funname="update" buttonType="GridUpdate" width="1024" height="620" exParams="{formId:'flowTableFormobj'}"></t:dgToolBar>
   <t:dgToolBar title="数据库表生成表单" icon="awsm-icon-plus" url="flowTableController.do?addTableData" funname="addTableData" width="1024" height="620" buttonType="GridAdd" exParams="{formId:'addTableFormobj'}"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="flowTableController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflash"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="awsm-icon-search" url="flowTableController.do?flowTableEdit" funname="detail" buttonType="GridDetail" width="1024" height="620" exParams="{formId:'flowTableFormobj'}"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#flowTableList").datagrid("reload");
  }
 
 function doPublish(id){
	 $.messager.confirm('提示信息', '确认同步数据到数据库?', function(r) {
			if (r) {
				$.ajax({
					url : "flowTableController.do?doPublish",
					type : 'post',
					data : {
						id : id
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							tip(d.msg);
						}
					}
				});
			}
		});
 }
 
 function groupTeam(id){
	 createwindow('分组','flowTableController.do?groupTeam&id='+id,800,600,null,{formId:'teamFormobj',optFlag:'add'});
 }
 
 function addTableData(title, url, gridID, width, height, preinstallWidth, exParams) {
		var defaultOptions = {
			optFlag : 'add'
		};
		url += '&optFlag=add';
		var options = $.extend({}, defaultOptions, exParams);
		createwindow(title, url, width, height, preinstallWidth, options);
	}
 </script>