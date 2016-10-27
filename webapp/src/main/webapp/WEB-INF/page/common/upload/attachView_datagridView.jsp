<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:datagrid  name="${uploadId}_grid" checkbox="true" fitColumns="true" autoLoadData="false" actionUrl="${gridUrl}"
	idField="id" fit="true" pagination="false" onLoadSuccess="${uploadId}_loadSuccess">
	<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
	<t:dgCol title="文件名" field="name" width="320" myFormatter="return onlyFileNameFormatter(value,rec,index);"></t:dgCol>
	<t:dgCol title="大小" field="attachSizeStr" width="100"></t:dgCol>
	<t:dgCol title="修改日期" field="updateTime" formatter="yyyy-MM-dd HH:mm" width="150"></t:dgCol>
	<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd HH:mm" width="150"></t:dgCol>
	<t:dgCol title="下载路径" field="absolutePath" hidden="false"></t:dgCol>
	<t:dgCol title="存储路径" field="storageType" hidden="false"></t:dgCol>
	<t:dgCol title="下载权限" field="downloadAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="编辑权限" field="editAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="删除权限" field="deleteAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="查看权限" field="viewAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="后缀名" field="ext" hidden="false"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="120"></t:dgCol>

	<t:dgFunOpt exp="downloadAuthority#eq#Y" funname="common_downloadFile(id)" icon="awsm-icon-download blue" title="下载" />
	<t:dgOpenOpt exp="editAuthority#eq#Y" preinstallWidth="1" url="attachController.do?updateDataEdit&id={id}"
		icon="awsm-icon-edit blue" exParams="{optFlag:'update'}" height="400" title="编辑" />
	<t:dgDelOpt exp="deleteAuthority#eq#Y" title="删除" icon="awsm-icon-trash red"
		url="attachController.do?deleteFile&aId={id}" />
	<t:dgOpenOpt exp="viewAuthority#eq#Y&&ext#eq#.doc,.docx,.xls,.xlsx,.ppt,.pptx,.vsd,.wps,.dps,.et,.pdf" title="在线查看"
		icon="awsm-icon-zoom-in green" exParams="{optFlag:'detail',isIframe:true,noheader:true}"
		url="attachController.do?viewDataEdit&id={id}&ext={ext}" width="100%" height="100%" />
</t:datagrid>
<script type="text/javascript">
	$(function() {
		var queryParams = $("#${uploadId}_grid",${uploadId}).datagrid('options').queryParams;
		queryParams.attachType = $("#common_attachType",${uploadId}).val();
		queryParams.businessKey = $("#common_businessKey",${uploadId}).val();
		queryParams.businessType = $("#common_businessType",${uploadId}).val();
		queryParams.businessExtra = $("#common_businessExtra",${uploadId}).val();
		queryParams.otherKey = $("#common_otherKey",${uploadId}).val();
		queryParams.otherKeyType = $("#common_otherKeyType",${uploadId}).val();
		queryParams.optFlag = $("#common_optFlag",${uploadId}).val();
		queryParams.isDownload = $("#common_isDownload",${uploadId}).val();
		queryParams.isEdit = $("#common_isEdit",${uploadId}).val();
		queryParams.isDelete = $("#common_isDelete",${uploadId}).val();
		queryParams.isView = $("#common_isView",${uploadId}).val();
		$('#${uploadId}_grid',${uploadId}).datagrid('load');
	});
</script>