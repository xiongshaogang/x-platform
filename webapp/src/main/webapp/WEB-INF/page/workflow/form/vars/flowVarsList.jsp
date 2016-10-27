<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		redrawEasyUI($(".easyui-layout"));
	});
</script>


<div class="easyui-layout" style="width:98%;height:100%;margin:0px auto 0px auto">  
    <div region="center" style="padding:0px;border:0px;" split="true">
    	<t:datagrid name="flowVarList" checkbox="true" fitColumns="true" title="表单列表" actionUrl="flowVarsController.do?datagrid&formId=${flowForm.id}" idField="id" fit="true" queryMode="group">
			<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
			<t:dgCol title="变量名称" width="100" field="name" query="true" ></t:dgCol>
			<t:dgCol title="变量编码" width="100" field="code" query="true"></t:dgCol>
			<t:dgCol title="数据类型" width="100" field="dataType" ></t:dgCol>
			<t:dgCol title="默认值" width="100" field="defValue" ></t:dgCol>
			<t:dgCol title="备注" width="160" field="description" ></t:dgCol>
			<t:dgCol title="操作" width="160" field="opt" ></t:dgCol>
			
			<t:dgOpenOpt title="编辑" icon="awsm-icon-edit blue" preinstallWidth="1" height="400" url="flowVarsController.do?flowVarsEdit&id={id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
   			<t:dgOpenOpt title="查看" icon="awsm-icon-zoom-in green" preinstallWidth="1" height="400" url="flowVarsController.do?flowVarsEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" ></t:dgOpenOpt>
			<t:dgDelOpt title="删除" icon="awsm-icon-trash red"  url="flowVarsController.do?delete&id={id}" />
			<t:dgToolBar preinstallWidth="1" height="400" title="新增"  icon="awsm-icon-plus" url="flowVarsController.do?flowVarsEdit" funname="add"></t:dgToolBar>
		    <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="flowVarsController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
		</t:datagrid>
    </div>  
</div>
<input type="hidden" id="pformId" value="${flowForm.id}">
<input type="hidden" id="pformName" value="${flowForm.name}">