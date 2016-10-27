<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	function fileNameFormatter(value, rec, index) {
		if (rec.fileFlag == "0") {
			return "<a href=\"#\" onclick=\"forwardOtherDir('" + rec.id + "','" + rec.fileFlag + "','" + rec.name
					+ "')\"><i class='awsm-icon-folder-close orange2 bigger-120'></i>  " + value + "</a>";
		} else if (rec.fileFlag == "1") {
			return "<span><i class='" + getFileIcon(rec.ext) + " bigger-120'></i>  " + value + "</span>";
		}
	}
</script>
<t:datagrid name="dataVoList" checkbox="true" onLoadSuccess="loadSuccess" fitColumns="true" autoLoadData="false" actionUrl="attachController.do?datagrid"
	idField="id" fit="true" pagination="false">
	<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
	<t:dgCol title="文件名" field="name" width="320" myFormatter="return fileNameFormatter(value,rec,index);"></t:dgCol>
	<t:dgCol title="大小" field="attachSizeStr" width="120"></t:dgCol>
	<t:dgCol title="修改日期" field="updateTime" formatter="yyyy-MM-dd HH:mm" width="120"></t:dgCol>
	<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd HH:mm" width="120"></t:dgCol>
	<t:dgCol title="文件标识" field="fileFlag" hidden="false"></t:dgCol>
	<t:dgCol title="下载路径" field="absolutePath" hidden="false"></t:dgCol>
	<t:dgCol title="存储路径" field="storageType" hidden="false"></t:dgCol>
	<t:dgCol title="下载权限" field="downloadAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="编辑权限" field="editAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="删除权限" field="deleteAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="查看权限" field="viewAuthority" hidden="false"></t:dgCol>
	<t:dgCol title="后缀名" field="ext" hidden="false"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="70"></t:dgCol>

	<t:dgFunOpt exp="downloadAuthority#eq#Y" operationCode="dataManager_download_other" funname="common_downloadFile(id)" icon="awsm-icon-download blue" title="下载" />
	<t:dgOpenOpt exp="editAuthority#eq#Y" preinstallWidth="1" url="attachController.do?updateDataEdit&id={id}"
		icon="awsm-icon-edit blue" exParams="{optFlag:'update'}" height="400" title="编辑" />
	<t:dgDelOpt exp="deleteAuthority#eq#Y" title="删除" icon="awsm-icon-trash red"
		url="attachController.do?deleteFile&aId={id}" />
	<t:dgOpenOpt exp="viewAuthority#eq#Y&&ext#eq#.doc,.docx,.xls,.xlsx,.ppt,.pptx,.vsd,.wps,.dps,.et,.pdf"  title="在线查看"
		icon="awsm-icon-zoom-in green" exParams="{optFlag:'detail',isIframe:true,noheader:true}" url="attachController.do?viewDataEdit&id={id}&ext={ext}" width="100%"
		height="100%" />
</t:datagrid>
<script type="text/javascript">
	$(function() {
		var attachType = $("#attachType").val();
		var parentTypeId = $("#parentTypeId").val();
		var menuType = $("#menuType").val();
		var queryParams = $("#dataVoList").datagrid('options').queryParams;
		queryParams.parentTypeId = parentTypeId;
		queryParams.attachType = attachType;
		queryParams.menuType= menuType;
		$('#dataVoList').datagrid('load');
	});

	/* 跳转到其他分类目录  isBack是否返回上级*/
	function forwardOtherDir(id, fileFlag, fileName, isBack) {
		$("#parentTypeId").val(id);
		var attachType = $("#attachType").val();
		if (isBack != 'Y') {
			fileNavigation(id, fileFlag, fileName);
		}
		var queryParams = $("#dataVoList").datagrid('options').queryParams;
		queryParams.typeId = id;
		queryParams.attachType = attachType;
		$('#dataVoList').datagrid('load');

	}
</script>