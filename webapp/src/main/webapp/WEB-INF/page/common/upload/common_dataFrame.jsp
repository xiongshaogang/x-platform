<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div id="${uploadId}" class="easyui-layout" fit="true">
	<input id="common_attachType" type="hidden" /> <input id="common_viewFlag" type="hidden" /> <input
		id="common_businessKey" type="hidden" value="${businessKey}" /> <input id="common_businessType" type="hidden"
		value="${businessType}" /> <input id="common_businessExtra" type="hidden" value="${businessExtra}" /> <input
		id="common_otherKey" type="hidden" value="${otherKey}" /> <input id="common_otherKeyType" type="hidden"
		value="${otherKeyType}" /><input id="common_isDownload" type="hidden" value="${isDownload}" /> <input
		id="common_isEdit" type="hidden" value="${isEdit}" /> <input id="common_isView" type="hidden" value="${isView}" /> <input
		id="common_isDelete" type="hidden" value="${isDelete}" /> <input id="common_gridUrl" type="hidden" value="${gridUrl}" />
	<input id="common_thumbnailUrl" type="hidden" value="${thumbnailUrl}" /><input id="common_defaultTypeCode"
		type="hidden" value="${defaultTypeCode}" />
	<c:if test="${isFileTypeFilter}">
		<div region="west" split="true" style="width: 150px;" title="资料分类">
			<div class="thumbnail-left list-group">
				<a href="#" flag="" class="list-group-item active"><i class="awsm-icon-file-alt bigger-160"></i><span>全部</span></a>
				<a href="#" flag="img" class="list-group-item"><i class="awsm-icon-picture bigger-160"></i><span>图片</span></a> <a
					href="#" flag="doc" class="list-group-item"><i class="awsm-icon-file-text-alt bigger-160"></i><span>文档</span></a> <a
					href="#" flag="video" class="list-group-item"><i class="awsm-icon-facetime-video bigger-160"></i><span>音频/视频</span></a>
				<a href="#" flag="other" class="list-group-item"><i class="awsm-icon-ellipsis-horizontal bigger-160"></i><span>其他</span></a>
			</div>
		</div>
	</c:if>
	<div region="center" style="padding: 0px;">
		<div class="common-dataViewDiv-top">
			<div class="common-dataViewDiv-button">
				<span class="grid_button_span"> <c:if test="${isUpload}">
						<a id="uploadFileHref" href="#" style="margin-right: 5px;display:none">
							<button type="button" class="btn btn-xs btn-white">
								<i class="awsm-icon-plus bigger-110 green"></i>上传
							</button>
						</a>
					</c:if> <c:if test="${isMulDelete}">
						<a id="batchDeleteFileHref" href="#" style="margin-right: 5px;display:none">
							<button type="button" class="btn btn-xs btn-white">
								<i class="awsm-icon-remove bigger-110 red"></i>批量删除
							</button>
						</a>
					</c:if>
				</span>
			</div>
			<div class="common-dataViewDiv-viewtype">
				<button id="data_datagrid_btn" class="btn btn-xs btn-white data_datagrid_btn changeView" data-view="datagridView">
					<i class="awsm-icon-list bigger-130"></i>
				</button>
				<button id="data_thumbnail_btn" class="btn btn-xs btn-white data_thumbnail_btn changeView" data-view="thumbnailView">
					<i class="awsm-icon-th-large bigger-130"></i>
				</button>
			</div>
		</div>
		<div id="common_dataViewDiv" style="border: 0px" class="easyui-panel dataViewDiv"
			data-options="border:false,cache:false,fit:true,noheader:true"></div>
	</div>
</div>
<script type="text/javascript">
	var ${uploadId} = $("#${uploadId}");
	$(function() {
		$(".thumbnail-left a", ${uploadId}).on("click", function() {
			$(".thumbnail-left a", ${uploadId}).removeClass("active");
			$(this).addClass("active");
		});
		//给左侧文件类型li绑定点击事件
		$("a.list-group-item",${uploadId}).on("click",function(){
			var attachType=$(this).attr("flag");
			$("#common_attachType", ${uploadId}).val(attachType);
			var businessKey = $("#common_businessKey", ${uploadId}).val();
			var businessType = $("#common_businessType", ${uploadId}).val();
			var businessExtra = $("#common_businessExtra", ${uploadId}).val();
			var otherKey = $("#common_otherKey", ${uploadId}).val();
			var otherKeyType = $("#common_otherKeyType", ${uploadId}).val();
			
			var gridUrl = $("#common_gridUrl", ${uploadId}).val();
			var thumbnailUrl = $("#common_thumbnailUrl", ${uploadId}).val();
			var uParam = "&businessKey=" + businessKey + "&businessType=" + businessType + "&businessExtra=" + businessExtra
					+ "&otherKey=" + otherKey + "&otherKeyType=" + otherKeyType + "&attachType=" + attachType;

			var viewFlag = $("#common_viewFlag", ${uploadId}).val();
			if (viewFlag == 'datagridView') {
				$("#common_dataViewDiv", ${uploadId}).panel("refresh",
						"attachController.do?datagridView" + uParam + "&gridUrl=" + encodeURIComponent(gridUrl)+"&uploadId="+"${uploadId}");
			} else {
				$("#common_dataViewDiv", ${uploadId}).panel("refresh",
						"attachController.do?thumbnailView" + uParam + "&thumbnailUrl=" + encodeURIComponent(thumbnailUrl)+"&uploadId="+"${uploadId}");
			}
		})
		//给上传按钮绑定点击事件
		$("#uploadFileHref",${uploadId}).on("click",function(){
			var businessKey = $("#common_businessKey", ${uploadId}).val();
			var businessType = $("#common_businessType", ${uploadId}).val();
			var businessExtra = $("#common_businessExtra", ${uploadId}).val();
			var otherKey = $("#common_otherKey", ${uploadId}).val();
			var otherKeyType = $("#common_otherKeyType", ${uploadId}).val();
			var defaultTypeCode = $("#common_defaultTypeCode", ${uploadId}).val();
			
			commonPageUpload({
				uploadId:"${uploadId}",
				noBusinessKey : "${noBusinessKey}",
				storageType : "${storageType}",
				businessKey : businessKey,
				businessExtra : businessExtra,
				businessType : businessType,
				otherKey : otherKey,
				otherKeyType : otherKeyType,
				parentTypeCode : "${parentTypeCode}",
				defaultTypeCode : defaultTypeCode,
				defaultTypeId : "${defaultTypeId}",
				rootTreeCode : "${rootTreeCode}",
				onlyAuthority : "${onlyAuthority}",
				containSelf : "${containSelf}",
				isNeedToType : "${isNeedToType}",
				isChangeType : "${isChangeType}",
				isShowType : "${isShowType}",
				isReloadGrid : "${isReloadGrid}",
				isLoadData : "${isLoadData}",
				autoCreateType : "${autoCreateType}",
				finishUploadCallback : "${finishUploadCallback}",
				exParams : "${exParams}"
			});
		});
		//给视图切换按钮绑定点击事件
		$(".common-dataViewDiv-top button.changeView", ${uploadId}).on(
				"click",
				function() {
					var businessKey = $("#common_businessKey", ${uploadId}).val();
					var businessType = $("#common_businessType", ${uploadId}).val();
					var businessExtra = $("#common_businessExtra", ${uploadId}).val();
					var otherKey = $("#common_otherKey", ${uploadId}).val();
					var otherKeyType = $("#common_otherKeyType", ${uploadId}).val();
					
					var attachType = $("#common_attachType", ${uploadId}).val();
					var gridUrl = $("#common_gridUrl", ${uploadId}).val();
					var thumbnailUrl = $("#common_thumbnailUrl", ${uploadId}).val();
					var uParam = "&businessKey=" + businessKey + "&businessType=" + businessType + "&businessExtra=" + businessExtra
							+ "&otherKey=" + otherKey + "&otherKeyType=" + otherKeyType+ "&attachType=" + attachType;
					
					var viewFlag = $(this).attr("data-view");
					
					var requestFun;
					if (viewFlag == "datagridView") {
						requestFun = "datagridView";
					} else if (viewFlag == "thumbnailView") {
						requestFun = "thumbnailView";
					}
					$("#common_viewFlag", ${uploadId}).val(viewFlag);

					var url = "attachController.do?" + requestFun + "&gridUrl=" + encodeURIComponent(gridUrl)
							+ "&thumbnailUrl=" + encodeURIComponent(thumbnailUrl)+"&uploadId="+"${uploadId}";
					$("#common_dataViewDiv", ${uploadId}).panel("refresh", url);
		});
		//给批量删除按钮绑定点击事件
		$("#batchDeleteFileHref",${uploadId}).on("click",function(){
			${uploadId}_mulDeleteFile();
		});
		setTimeout('$("#data_datagrid_btn", ${uploadId}).click()', 0);
	});

	/* 执行批量删除方法 */
	function ${uploadId}_mulDeleteFile() {
		var url = "attachController.do?mulDeleteFile";
		var viewFlag = $("#common_viewFlag",${uploadId}).val();
		if (viewFlag == "datagridView") {
			var aIds = [];
			var rows = $("#${uploadId}_grid", ${uploadId}).datagrid('getSelections');
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					var deleteAuthority=rows[i].deleteAuthority;
					if(deleteAuthority&&deleteAuthority=="N"){
						tip("您选中的附件中包含无权删除的,请检查");
						return;
					}
				}
				$.messager.confirm('提示信息', '确定删除所有所选附件?', function(r) {
					if (r) {
						for (var i = 0; i < rows.length; i++) {
							aIds.push(rows[i].id);
						}
						$.ajax({
							url : url,
							type : 'post',
							data : {
								aIds : aIds.join(',')
							},
							cache : false,
							success : function(data) {
								var d = $.parseJSON(data);
								if (d.success) {
									var msg = d.msg;
									tip(msg);
									$("#${uploadId}_grid", ${uploadId}).datagrid('unselectAll');
									$("#${uploadId}_grid", ${uploadId}).datagrid('reload');
									aIds = '';
								}
							}
						});
					}
				});
			} else {
				tip("请选择需要删除的附件");
			}
		} else if (viewFlag == "thumbnailView") {
			tip("请切换到列表视图进行删除");
		}
	}
	
	//单个删除
	function ${uploadId}_deleteFile(id) {
		var url = "attachController.do?deleteFile&aId=" + id;
		reqConfirm(null, "确定删除所有所选附件?", url, null, function() {
			var viewFlag = $("#common_viewFlag",${uploadId}).val();
			if (viewFlag == "datagridView") {
				$("#data_datagrid_btn",${uploadId}).click();
			} else if (viewFlag == "thumbnailView") {
				$("#data_thumbnail_btn",${uploadId}).click();
			}
		});
	}
	
	//判断上传、批量删除权限
	function ${uploadId}_loadSuccess(data) {
		var exParam = data.exParam;
		var operationCodes = exParam.operationCodes;
		if (operationCodes.indexOf('isUpload') != -1) {
			$("#uploadFileHref",${uploadId}).show();
		} else {
			$("#uploadFileHref",${uploadId}).hide();
		}

		if (operationCodes.indexOf('isMulDelete') != -1) {
			$("#batchDeleteFileHref",${uploadId}).show();
		} else {
			$("#batchDeleteFileHref",${uploadId}).hide();
		}
	}
</script>