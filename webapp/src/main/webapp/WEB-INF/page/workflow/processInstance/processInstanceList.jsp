<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<c:choose>
	<c:when test="${defId!=null}">
		<c:set var="actionUrl" value="processInstanceController.do?datagrid&defId=${defId}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="actionUrl" value="processInstanceController.do?datagrid"></c:set>
	</c:otherwise>
</c:choose>

<t:datagrid name="processInstanceList" title="流程实例管理" checkbox="true" fitColumns="true" actionUrl="${actionUrl}" idField="id" fit="true" queryMode="separate">
	<t:dgCol title="扩展id" field="id" hidden="false" ></t:dgCol>
	<t:dgCol title="实例id" field="actInstId" hidden="false" ></t:dgCol>
	<t:dgCol title="实例名称" query="true" width="20" field="title"></t:dgCol>
	<t:dgCol title="创建人" query="true" width="20" field="createUserName"></t:dgCol>
	<t:dgCol title="创建时间" query="true" width="15" field="createTime" ></t:dgCol>
	<t:dgCol title="结束时间" query="true"  width="15" field="endTime" ></t:dgCol>
	<t:dgCol title="持续时间"  width="15" field="duration" ></t:dgCol>
	<t:dgCol title="启用状态" replace="完成_2,运行中_1,禁用_0" width="15" field="status" ></t:dgCol>
	
	<t:dgCol title="操作" width="20" field="opt" ></t:dgCol>
	<t:dgDelOpt title="删除" icon="awsm-icon-trash red"  url="processInstanceController.do?delete&id={id}" />
 	   		<t:dgOpenOpt title="详细信息" icon="awsm-icon-zoom-in green" url="processInstanceController.do?processInstanceEdit&id={id}" exParams="{optFlag:'detail'}" preinstallWidth="1" height="400" ></t:dgOpenOpt>
  		<t:dgOpenOpt title="流程图" icon="awsm-icon-zoom-in green" url="processInstanceController.do?processImage&actInstId={actInstId}"  width="840"  height="500" ></t:dgOpenOpt>
  		<t:dgOpenOpt title="审批历史" icon="awsm-icon-zoom-in green" url="definitionController.do?showTaskOpinions&actInstId={actInstId}" width="700" height="500" ></t:dgOpenOpt>
	
    <t:dgToolBar title="删除"  icon="awsm-icon-remove" url="processInstanceController.do?batchDelete" funname="deleteALLSelect"></t:dgToolBar>
</t:datagrid>
<script type="text/javascript">
	 $(function(){
		 redrawEasyUI($("#page_content"));
	 });
 </script>
