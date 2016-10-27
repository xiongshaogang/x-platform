<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px;">
		<t:datagrid  name="innerList" title="系统消息列表" actionUrl="messageController.do?sysMsgDatagrid">
			<t:dgCol title="id" field="id" hidden="false" width="120"></t:dgCol>
			<t:dgCol title="标题" field="title" width="200"></t:dgCol>
			<t:dgCol title="接收时间" formatter="yyyy-MM-dd HH:mm:ss" field="receiveTime" width="100" query="true" queryMode="group" queryInputType="datetimebox"></t:dgCol>
			<t:dgCol title="发送人" field="sendUserName" width="100" query="true"></t:dgCol>
			<t:dgCol title="是否已读" replace="未读_0,已读_1" field="isRead" width="50"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			<t:dgOpenOpt width="500" height="350" icon="awsm-icon-zoom-in green" url="messageController.do?sysMsgView&id={id}"
				title="消息详细"></t:dgOpenOpt>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
</script>