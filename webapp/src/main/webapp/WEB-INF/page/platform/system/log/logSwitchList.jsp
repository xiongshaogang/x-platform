<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="logSwitchList" checkbox="true" autoLoadData="true" fitColumns="true" title="模块列表" actionUrl="moduleController.do?logdatagrid" idField="id" fit="true" queryMode="separate" treegrid="true" >
	<t:dgCol title="编号" field="id" hidden="false" treefield="id"></t:dgCol>
	<t:dgCol title="是否根节点" field="isLeaf" hidden="false" treefield="isLeaf"></t:dgCol>
	<t:dgCol title="模块名称" width="50" field="name"  query="true" treefield="text"></t:dgCol>
	<t:dgCol title="模块编码" width="50" field="code" query="true" treefield="code" ></t:dgCol>
	<t:dgCol title="模块路径" width="100" field="url" treefield="src"></t:dgCol>
	<t:dgCol title="是否开启日志信息" replace="是_1,否_0" width="40" field="status" treefield="status" align="center" hidden="false"></t:dgCol>
	<t:dgCol title="操作" width="80" field="opt" ></t:dgCol>
	<t:dgFunOpt exp="status#eq#0&&isLeaf#eq#1" title="开启" funname="changeStatus(id,status)" icon="awsm-icon-eye-open grey" operationCode="logSwitchManager_closeLogSwitch_other"></t:dgFunOpt>
	<t:dgFunOpt exp="status#eq#1&&isLeaf#eq#1" title="关闭" funname="changeStatus(id,status)" icon="awsm-icon-eye-close green" operationCode="logSwitchManager_closeLogSwitch_other"></t:dgFunOpt>
</t:datagrid>
<script type="text/javascript">
	$(document).ready(function(){
		redrawEasyUI($("#page_content"));
	});
	function changeStatus(id,status){
		var obj = new Object();
		obj.status=status;
		obj.id=id;
		doAction("moduleController.do?setStatus", "logSwitchList", "","treegrid",JSON.stringify(obj))
	}
</script>
