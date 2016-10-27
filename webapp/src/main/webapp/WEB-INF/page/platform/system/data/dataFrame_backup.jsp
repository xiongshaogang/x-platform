<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true" id="dataManage_layout">
	<input id="attachType" type="hidden" /> <input id="viewFlag"
		type="hidden" /> <input id="parentTypeId" type="hidden" /> <input
		id="backParentFileFlag" type="hidden" /> <input id="backParentName"
		type="hidden" /> <input id="backParentTypeId" type="hidden" />
	<div region="west" split="true" style="width: 200px;" title="资料分类">
		<div class="thumbnail-left list-group">
			<a href="#" onclick="findDataByType('');"
				class="list-group-item active"><i
				class="awsm-icon-file-alt bigger-160"></i><span>全部</span></a> <a
				href="#" onclick="findDataByType('img');" class="list-group-item"><i
				class="awsm-icon-picture bigger-160"></i><span>图片</span></a> <a href="#"
				onclick="findDataByType('doc');" class="list-group-item"><i
				class="awsm-icon-file-text-alt bigger-160"></i><span>文档</span></a> <a
				href="#" onclick="findDataByType('video');" class="list-group-item"><i
				class="awsm-icon-facetime-video bigger-160"></i><span>音频/视频</span></a> <a
				href="#" onclick="findDataByType('other');" class="list-group-item"><i
				class="awsm-icon-ellipsis-horizontal bigger-160"></i><span>其他</span></a>
		</div>
	</div>
	<div region="center" style="padding: 0px;">
		<div class="dataViewDiv-top">
			<div style="margin-left: 10px; float: left;">
				<a id="backRoot"
					onclick="backRoot('${typeEntity.id }','0','${typeEntity.name }');"
					href="#">总目录</a>&nbsp;|&nbsp;
			</div>
			<div id="fileNavigation" style="float: left;">
				<a id="backParent" onclick="backParent();" href="#">返回上一级</a>
			</div>
			<button id="data_datagrid_btn"
				class="btn btn-xs btn-white data_datagrid_btn"
				data-view="datasDatagridView">
				<i class="awsm-icon-list bigger-130"></i>
			</button>
			<button id="data_thumbnail_btn"
				class="btn btn-xs btn-white data_thumbnail_btn"
				data-view="datasThumbnailView">
				<i class="awsm-icon-th-large bigger-130"></i>
			</button>
		</div>
		<div class="datagrid-toolbar dataframe-toolbar">
			<span class="grid_button_span"> <a id="uploadFileHref"
				href="#" style="margin-right: 5px;">
					<button type="button" onclick="goUploadFilePage();"
						class="btn btn-xs btn-white">
						<i class="awsm-icon-plus bigger-110 green"></i>上传
					</button>
			</a><a id="batchDeleteFileHref" onclick="mulDeleteFile();" href="#"
				style="margin-right: 5px;">
					<button type="button" class="btn btn-xs btn-white">
						<i class="awsm-icon-remove bigger-110 red"></i>批量删除
					</button>
			</a><a id="batchFileAuthority" onclick="fileAuthority();" href="#"
				style="margin-right: 5px;">
					<button type="button" class="btn btn-xs btn-white">
						<i class="awsm-icon-remove bigger-110 red"></i>文件授权
					</button>
			</a>
			</span>
		</div>
		<div id="dataViewDiv" style="border: 0px"
			class="easyui-panel dataViewDiv"
			data-options="border:false,cache:false,fit:true,noheader:true"></div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
		$(".thumbnail-left a").on("click", function() {
			$(".thumbnail-left a").removeClass("active");
			$(this).addClass("active");
		});

		var typeId = '${typeId}';
		$("#parentTypeId").val(typeId);

		$(".dataViewDiv-top button").on("click", function() {
			$("#viewFlag").val($(this).attr("data-view"));

			var attachType = $("#attachType").val();
			var parentTypeId = $("#parentTypeId").val();

			$("#dataViewDiv").panel("refresh", "attachController.do?" + $(this).attr("data-view") + "&typeId=" + parentTypeId + "&attachType=" + attachType);
		});

		setTimeout('$("#data_datagrid_btn").click()', 0);
	});

	//返回根目录
	function backRoot(id, fileFlag, fileName) {
		$("#backParentTypeId").val("");
		$("#backParentName").val("");
		$("#backParentFileFlag").val("");

		$("#fileNavigation").html('<a id="backParent" onclick="backParent();" href="#">返回上一级</a>');

		var viewFlag = $("#viewFlag").val();
		if (viewFlag == 'datagridView') {
			/**
			id 编码
			fileFlag 文件标识
			isBack 是否返回上级
			 **/
			forwardOtherDir(id, fileFlag, fileName, 'Y');
		} else {
			/**
			id 编码
			fileFlag 文件标识
			isInit 是否初始化
			isBack 是否返回上级
			 **/
			findFileById(id, fileFlag, fileName, 'N', 'Y');
		}
	}

	//返回父级菜单
	function backParent() {
		//获取文件ID、名称、标识
		var viewFlag = $("#viewFlag").val();
		var backParentTypeId = $("#backParentTypeId").val();
		var backParentName = $("#backParentName").val();
		var backParentFileFlag = $("#backParentFileFlag").val();

		//将获取的文件ID、名称、标识分隔成数组
		var backParentTypeIds = backParentTypeId.split(',');
		var backParentNames = backParentName.split(',');
		var backParentFileFlags = backParentFileFlag.split(',');

		//获取fileNavigation DIV下的所有A标签
		var fileNavigationTags = document.getElementById("fileNavigation");
		var child = fileNavigationTags.getElementsByTagName("a");

		//判断是否只有一个《返回上一级》的a标签
		if (child.length > 1) {
			//获取返回上级的父级 文件ID、名称、标识
			var id = backParentTypeIds[backParentTypeIds.length - 2];
			var fileFlag = backParentFileFlags[backParentFileFlags.length - 2];
			var fileName = backParentNames[backParentNames.length - 2];

			//去除最后一级文件ID、名称、标识
			var backParentTypeId1 = backParentTypeId.substring(0, backParentTypeId.indexOf("," + backParentTypeIds[backParentTypeIds.length - 1]));
			var backParentName1 = backParentName.substring(0, backParentName.indexOf("," + backParentNames[backParentNames.length - 1]));
			var backParentFileFlag1 = backParentFileFlag.substring(0, backParentFileFlag.indexOf("," + backParentFileFlags[backParentFileFlags.length - 1]));

			//重新赋值
			$("#backParentTypeId").val(backParentTypeId1);
			$("#backParentName").val(backParentName1);
			$("#backParentFileFlag").val(backParentFileFlag1);

			//如果fileNavigation DIV下只有一个路径菜单（返回上一级不算）
			if (child.length <= 2) {
				$("#fileNavigation").html('<a id="backParent" onclick="backParent();" href="#">返回上一级</a>');
			} else {
				//去除该ID的路径菜单 并重新生成菜单
				$("#dh" + backParentTypeIds[backParentTypeIds.length - 1]).remove();
				var fileNavigationHTML = $("#fileNavigation").html();
				fileNavigationHTML = fileNavigationHTML.substring(0, fileNavigationHTML.lastIndexOf("</a>") + 4);
				$("#fileNavigation").html(fileNavigationHTML);
			}

			if (viewFlag == 'datagridView') {
				/**
				id 编码
				fileFlag 文件标识
				isBack 是否返回上级
				 **/
				forwardOtherDir(id, fileFlag, fileName, 'Y');
			} else {
				/**
				id 编码
				fileFlag 文件标识
				isInit 是否初始化
				isBack 是否返回上级
				 **/
				findFileById(id, fileFlag, fileName, 'N', 'Y');
			}
		}
	}

	//按类型查找
	function findDataByType(attachType) {
		$("#attachType").val(attachType);
		var viewFlag = $("#viewFlag").val();
		var parentTypeId = $("#parentTypeId").val();
		if (viewFlag == 'datagridView') {
			$("#dataViewDiv").panel("refresh", "attachController.do?datagridView&typeId=" + parentTypeId + "&attachType=" + attachType);
		} else {
			$("#dataViewDiv").panel("refresh", "attachController.do?thumbnailView&typeId=" + parentTypeId + "&attachType=" + attachType);
		}
	}

	//路径生成导航
	function fileNavigation(id, fileFlag, fileName) {
		if (id != '') {
			var backParentTypeId = $("#backParentTypeId").val();
			var backParentFileFlag = $("#backParentFileFlag").val();
			var backParentName = $("#backParentName").val();

			backParentTypeId = backParentTypeId + ',' + id;
			backParentFileFlag = backParentFileFlag + ',' + fileFlag;
			backParentName = backParentName + ',' + fileName;

			$("#parentTypeId").val(id);

			$("#backParentTypeId").val(backParentTypeId);
			$("#backParentName").val(backParentName);
			$("#backParentFileFlag").val(backParentFileFlag);

			var fileNavigationHTML = $("#fileNavigation").html();
			var contextHTML = '';

			contextHTML = '&gt;&nbsp;<a id="dh' + id + '" onclick="functionNameSel(\'' + id + '\',\'' + fileFlag + '\',\'' + fileName + '\')" href="#">'
					+ fileName + '</a>&nbsp;';
			if (fileNavigationHTML.indexOf(contextHTML) == -1) {
				fileNavigationHTML += contextHTML;
				$("#fileNavigation").html(fileNavigationHTML);
			} else {
				contextHTML = '&gt;&nbsp;<a id="dh' + id + '" onclick="functionNameSel(\'' + id + '\',\'' + fileFlag + '\',\'' + fileName + '\')" href="#">'
						+ fileName + '</a>&nbsp;';
				fileNavigationHTML = fileNavigationHTML.substring(0, fileNavigationHTML.indexOf(contextHTML));
				fileNavigationHTML += contextHTML;
				$("#fileNavigation").html(fileNavigationHTML);
			}
		}
	}

	function functionNameSel(id, fileFlag, fileName) {
		//获取文件ID、名称、标识
		var viewFlag = $("#viewFlag").val();
		var backParentTypeId = $("#backParentTypeId").val();
		var backParentName = $("#backParentName").val();
		var backParentFileFlag = $("#backParentFileFlag").val();

		backParentTypeId = backParentTypeId.substring(0, backParentTypeId.indexOf("," + id));
		backParentName = backParentName.substring(0, backParentName.indexOf("," + id));
		backParentFileFlag = backParentFileFlag.substring(0, backParentFileFlag.indexOf("," + id));

		$("#backParentTypeId").val(backParentTypeId);
		$("#backParentName").val(backParentName);
		$("#backParentFileFlag").val(backParentFileFlag);

		if (viewFlag == 'datagridView') {
			forwardOtherDir(id, fileFlag, fileName);
		} else {
			findFileById(id, fileFlag, fileName, 'N');
		}
	}

	/* 进入上传文件页面 */
	function goUploadFilePage() {
		var typeId = $("#parentTypeId").val();
		commonPageUpload({
			uploadId : "dataManageUpload",
			defaultTypeId : typeId,
			noBusinessKey : true,
			isChangeType : false,
			isLoadData : false,
			isReloadGrid : false,
			finishUploadCallback : "dataManage_finishUploadCallback()"
		});
	}
	function dataManage_finishUploadCallback() {
		var dataManage_layout = $("#dataManage_layout");
		var viewFlag = $("#viewFlag", dataManage_layout).val();
		if (viewFlag == "datagridView") {
			$(".data_datagrid_btn", dataManage_layout).click();
		} else if (viewFlag == "thumbnailView") {
			$(".data_thumbnail_btn", dataManage_layout).click();
		}
	}
	/* 执行批量删除方法 */
	function mulDeleteFile() {
		var url = "attachController.do?mulDeleteFile";
		var viewFlag = $("#viewFlag").val();
		if (viewFlag == "datagridView") {
			var aIds = [];
			var rows = $("#dataVoList").datagrid('getSelections');
			if (rows.length > 0) {
				$.messager.confirm('提示信息', '确定删除所有所选资料?', function(r) {
					if (r) {
						for (var i = 0; i < rows.length; i++) {
							if (rows[i].fileFlag == 0) {
								tip("您勾选项中包含了文件夹,无法进行批量删除,请检查");
								return;
							}
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
									$("#dataVoList").datagrid('unselectAll');
									$("#dataVoList").datagrid('reload');
									aIds = '';
								}
							}
						});
					}
				});
			} else {
				tip("请选择需要删除的资料");
			}
		} else if (viewFlag == "thumbnailView") {
			tip("请切换到列表视图进行删除");
		}

	}

	//判断上传、批量删除权限
	function loadSuccess(data) {
		var exParam = data.exParam;
		var operationCodes = exParam.operationCodes;
		var isAdmin = '${isAdmin}';
		if (isAdmin == 'true') {
			$("#uploadFileHref").show();
			$("#batchDeleteFileHref").show();
		} else {
			if (operationCodes.indexOf('dataManager_upload_other') != -1) {
				$("#uploadFileHref").show();
			} else {
				$("#uploadFileHref").hide();
			}

			if (operationCodes.indexOf('dataManager_mulDelete_batchDelete') != -1) {
				$("#batchDeleteFileHref").show();
			} else {
				$("#batchDeleteFileHref").hide();
			}
			
			if (operationCodes.indexOf('dataManager_batchFileAuthority_other') != -1) {
				$("#batchFileAuthority").show();
			} else {
				$("#batchFileAuthority").hide();
			}
		}
	}

	//文件批量授权
	function fileAuthority() {
		var viewFlag = $("#viewFlag").val();
		if (viewFlag == "datagridView") {
			var aIds = [];
			var rows = $("#dataVoList").datagrid('getSelections');
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					if (rows[i].fileFlag == 0) {
						tip("您勾选项中包含了文件夹,本功能为文件授权,请检查");
						return;
					}
					aIds.push(rows[i].id);
				}
				goRoleSavePage({
					title:"文件角色授权",
					saveUrl : "attachController.do?saveFileAuthority&aIds=" + aIds.join(',')
				});
			} else {
				tip("请选择需授权的资料");
			}
		} else if (viewFlag == "thumbnailView") {
			tip("请切换到列表视图进行删除");
		}
	}
</script>