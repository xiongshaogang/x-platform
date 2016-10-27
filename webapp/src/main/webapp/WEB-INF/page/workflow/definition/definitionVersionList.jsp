<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="flowDefinitionVersionList" checkbox="true" fitColumns="true" actionUrl="definitionController.do?datagrid&isMain=0&code=${code}">
	<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
	<t:dgCol title="流程名称" width="20" field="name"></t:dgCol>
	<t:dgCol title="流程编码" width="20" field="code"></t:dgCol>
	<t:dgCol title="所属类型" width="15" field="type.name" ></t:dgCol>
	<t:dgCol title="版本号"  width="15" field="version" ></t:dgCol>
	<t:dgCol title="是否发布" replace="未发布_N,已发布_Y" width="15" field="published" ></t:dgCol>
	<t:dgCol title="状态" replace="启用_Y,禁用_N" width="15" field="status" ></t:dgCol>
	
	<t:dgCol title="操作" width="20" field="opt" ></t:dgCol>
	<t:dgOpenOpt title="流程设计" icon="glyphicon glyphicon-pencil icon-color"  height="99%" width="95%" url="definitionController.do?online&id={id}" exParams="{isIframe:true,optFlag:null}"></t:dgOpenOpt>
	<t:dgDelOpt title="删除" icon="glyphicon glyphicon-remove icon-color"  url="definitionController.do?delete&id={id}" />
	<t:dgFunOpt exp="published#eq#N" title="发布" icon="glyphicon glyphicon-forward icon-color" funname="doPublished(id)" />
	<t:dgFunOpt exp="published#eq#Y&&status#eq#N" title="启用" icon="glyphicon glyphicon-play icon-color" funname="doActiveVersion(id)" />
	<t:dgFunOpt exp="published#eq#Y&&status#eq#Y" title="禁用" icon="glyphicon glyphicon-stop icon-color" funname="doActiveVersion(id)" />
	<t:dgOpenOpt exp="published#eq#Y" title="流程配置" exParams="{noheader:true,isIframe:false,dialogID:'flowSettingDialog'}" height="500" width="900" icon="glyphicon glyphicon-cog icon-color" url="definitionController.do?flowSetting&id={id}" />
</t:datagrid>
<script type="text/javascript">
	function doActiveVersion(id){
		doSubmit("definitionController.do?doActive&id="+id,"flowDefinitionVersionList");
	}
</script>