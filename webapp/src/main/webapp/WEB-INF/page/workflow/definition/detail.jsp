<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<style type="text/css">
	.definition_detail td {
		height:27px;
		border:0px solid #f1f1f1;
	}
	.title{
		background-color:#f5f5f5;
	}
	a{
		/*color:red;*/
	}
</style>
<table class="definition_detail" cellpadding="0" width="99%" cellspacing="1" style="">
	<tr>
		<td class="title" width="18%" align="right">分类:</td>
		<td>${definition.type.name}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">流程标题:</td>
		<td>${definition.name}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">流程定义Key:</td>
		<td>${definition.code}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">任务标题生成规则:</td>
		<td>${definition.taskNameRule}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">流程描述:</td>
		<td>${definition.description}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">创建人:</td>
		<td><f:userName userId="${definition.createUserName}"/></td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">创建时间:</td>
		<td><fmt:formatDate value="${definition.createTime}" pattern="yyyy-MM-dd HH:mm"/> </td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">更新人:</td>
		<td><f:userName userId="${definition.updateUserName}"/></td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">更新原因:</td>
		<td>${definition.reason}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">更新时间:</td>
		<td><fmt:formatDate value="${definition.updateTime}" pattern="yyyy-MM-dd HH:mm"/> </td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">流程状态:</td>
		<td>
			<c:choose>
				<c:when test='${definition.status=="Y"}'>
					<font color='green'>启用</font>
				</c:when>
				<c:when test='${definition.status=="N"}'>
					<font color='red'>禁用</font>
				</c:when>								
				<c:otherwise><font color='red'>未知
				</font></c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">版本号:</td>
		<td>${definition.version}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">activiti流程定义ID:</td>
		<td>${definition.actId}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">act流程定义Key:</td>
		<td>${definition.actKey}</td>
	</tr>
	<tr>
		<td class="title" width="18%" align="right">流程定义xml:</td>
		<td><a href="#" onclick="bpmnXml()"><font color='green'>查看</font></a></td>
	</tr>
</table>
<script type="text/javascript">
	function bpmnXml() {
		var url = "definitionController.do?bpmnXml&id=${definition.id}";
		createwindow('xml查看', url, "95%", 580, null, {optFlag :'null',isIframe:true});
	}
</script>

  