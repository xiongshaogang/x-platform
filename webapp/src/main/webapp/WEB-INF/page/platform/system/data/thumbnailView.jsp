<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
	<div id="thumbnailData" class="thumbnail-data" region="center"
		style="padding: 0px; border-left: 0px; border-right: 0px; overflow-y: auto;"></div>
</div>
<script type="text/javascript">
	$(function() {
		findFileById('${typeId}', '', '${attachType}', 'Y', 'N');
	});

	/**
	id 编码
	fileFlag 文件标识
	isInit 是否初始化
	isBack 是否返回上级
	**/
	

	//删除
	function removeFile(id) {
		var url = "attachController.do?deleteFile&aId=" + id;
		reqConfirm(null, "确定删除所有所选资料?", url, null, function() {
			var viewFlag = $("#viewFlag").val();
			if (viewFlag == "datagridView") {
				$("#data_datagrid_btn").click();
			} else if (viewFlag == "thumbnailView") {
				$("#data_thumbnail_btn").click();
			}
		});
	}
</script>