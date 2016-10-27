<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<link href="basic/css/colorbox.css" rel="stylesheet" />
<script type="text/javascript">
	var dataManage = {
		onBeforeLoad : function(node, param) {
			if (node) {
				param.level = node.level;
			} else {
				param.level = 0;
			}
		},
		clickPreFun : function(node) {
			var menuType=$("#menuType").val();
			if(menuType=="work"){
				if(node.state=="open"){
					//点击后刷新文件视图
					if(node.level==2){
						$("#formCode").val(node.id);
					}else if(node.level==3){
						$("#formCode").val(node.id);
						$("#titleBusinessKey").val(node.attributes.titleBusinessKey);
					}
				}
			}else if(menuType=="org"){
				$("#dataOrgId").val(node.id);
				$("#parentTypeId").val("");
			}
			reloadView();
		},
		createFolder:function(){
			var parentTypeId=$("#parentTypeId").val();
			var menuType=$("#menuType").val();
			var isPublic=$("#isPublic").val();
			var dataOrgId=$("#dataOrgId").val();
			$.messager.prompt('提示信息', '请输入文件夹名称：', function(input){
				if (input){
					ajaxTip("attachController.do?createFolder&isPublic="+isPublic+"&dataOrgId="+dataOrgId+"&parentTypeId="+parentTypeId+"&menuType="+menuType+"&typeName="+input,function(){
						reloadView();
					});
				}
			});
		},
		fileNameFormatter:function(value, rec, index) {
			if (rec.fileFlag == "0") {
				return "<a href=\"#\" onclick=\"dataManage.forwardOtherDir('" + rec.id + "','" + rec.isPublic + "','" + rec.fileFlag + "','" + rec.name
						+ "')\"><img style='width:20px;height:20px' src='"+getFileIcon(rec.iconType)+"' />  " + value + "</a>";
			} else if (rec.fileFlag == "1") {
				return "<span><img style='width:20px;height:20px' src='"+getFileIcon(rec.iconType,'${attachThumbnailRequest}'+rec.id)+"' />  " + value + "</span>";
			}
		},
		//跳转到其他文件夹
		forwardOtherDir:function(parentTypeId,isPublic) {
			$("#parentTypeId").val(parentTypeId);
			$("#isPublic").val(isPublic);
// 			if (isBack != 'Y') {
				//fileNavigation(id, fileFlag, fileName);
// 			}
			reloadView();
		},
		finishUploadCallback:function () {
			reloadView();
		},
		move : function(id, fileFlag) {
			alert("功能实现中")
		},
		rename : function(id, name) {
			var dataOrgId = $("#dataOrgId").val();
			$.messager.prompt('提示信息', '请输入新名称：', function(input) {
				if (input) {
					ajaxTip("attachController.do?createFolder&isPublic=" + isPublic + "&dataOrgId=" + dataOrgId + "&parentTypeId=" + parentTypeId
							+ "&menuType=" + menuType + "&typeName=" + input, function() {
						reloadView();
					});
				}
			});
		},
		//文件夹授权 
		folderAuthority : function(id,orgId) {
			var dataOrgId = $("#dataOrgId").val();
			ajax("attachController.do?queryOrgTypeAuthority&typeId=" + id, function(data) {
				goOrgMulSelectPage({
					title : "文件夹授权",
					needOrg : true,
					orgId : nulls(orgId),
					containSelf : true,
					needBtnSave : true,
					multiples : true,
					hiddenValue : nulls(JSON.stringify(data.obj)),
					saveUrl : "attachController.do?saveOrgTypeAuthority&typeIds=" + id
				});
			});
		}
	};
</script>
<div class="easyui-layout" fit="true" id="dataManage_layout">
	<input id="menuType" type="hidden" /><input id="titleBusinessKey" type="hidden" /><input id="formCode" type="hidden" /><input id="dataOrgId" type="hidden" />
	<input id="isPublic" type="hidden" />
	<input id="attachType" type="hidden" /> <input id="viewFlag"
		type="hidden" /> <input id="parentTypeId" type="hidden" /> <input
		id="backParentFileFlag" type="hidden" /> <input id="backParentName"
		type="hidden" /> <input id="backParentTypeId" type="hidden" />
	

	<div region="west" split="true" style="width: 200px;" title="资料分类">
		<div id="menuAccordion" class="easyui-accordion" data-options="fit:true,border:false">
			<div title="个人" style="overflow: auto; padding: 10px;"></div>
			<div title="工作" style="overflow: auto; padding: 10px;">
				<t:tree id="workTree" showOptMenu="false" url="attachController.do?queryWorkTree"  onBeforeLoad="dataManage.onBeforeLoad(node, param)" clickPreFun="dataManage.clickPreFun(node)" onlyLeafClick="true">
  				</t:tree>
			</div>
			<div title="共享">
				<t:tree id="orgTree" showOptMenu="false" url="attachController.do?queryOrgTree" clickPreFun="dataManage.clickPreFun(node)" onlyLeafClick="true">
  				</t:tree>
			</div>
		</div>
		
<!-- 		<div class="thumbnail-left list-group"> -->
<!-- 			<a href="#" onclick="findDataByType('');" -->
<!-- 				class="list-group-item active"><i -->
<!-- 				class="awsm-icon-file-alt bigger-160"></i><span>全部</span></a> <a -->
<!-- 				href="#" onclick="findDataByType('img');" class="list-group-item"><i -->
<!-- 				class="awsm-icon-picture bigger-160"></i><span>图片</span></a> <a href="#" -->
<!-- 				onclick="findDataByType('doc');" class="list-group-item"><i -->
<!-- 				class="awsm-icon-file-text-alt bigger-160"></i><span>文档</span></a> <a -->
<!-- 				href="#" onclick="findDataByType('video');" class="list-group-item"><i -->
<!-- 				class="awsm-icon-facetime-video bigger-160"></i><span>音频/视频</span></a> <a -->
<!-- 				href="#" onclick="findDataByType('other');" class="list-group-item"><i -->
<!-- 				class="awsm-icon-ellipsis-horizontal bigger-160"></i><span>其他</span></a> -->
<!-- 		</div> -->
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
				<i class="fa fa-list icon-color"></i>
			</button>
			<button id="data_thumbnail_btn"
				class="btn btn-xs btn-white data_thumbnail_btn"
				data-view="datasThumbnailView">
				<i class="fa fa-th-large icon-color"></i>
			</button>
		</div>
		<div class="datagrid-toolbar dataframe-toolbar">
			<span class="grid_button_span"> 
			<a id="attachManager_upload_other" href="#" style="margin-right: 5px;">
				<button type="button" onclick="goUploadFilePage();" class="btn btn-xs btn-white">
					<i class="fa fa-upload green"></i>上传
				</button>
			</a>
			<a id="attachManager_createFolder_other" onclick="dataManage.createFolder();" href="#" style="margin-right: 5px;">
				<button type="button" class="btn btn-xs btn-white">
					<i class="fa fa-folder-o green"></i>创建文件夹
				</button>
			</a>
			<a id="attachManager_delete_other" onclick="alert('开发中')" href="#" style="margin-right: 5px;">
				<button type="button" class="btn btn-xs btn-white">
					<i class="fa fa-remove red"></i>删除
				</button>
			</a>
			<a id="attachManager_rename_other" onclick="alert('开发中')" href="#" style="margin-right: 5px;">
				<button type="button" class="btn btn-xs btn-white">
					<i class="fa fa-pencil-square-o icon-color"></i>重命名
				</button>
			</a>
			<a id="attachManager_download_other" onclick="alert('开发中')" href="#" style="margin-right: 5px;">
				<button type="button" class="btn btn-xs btn-white">
					<i class="fa fa-download icon-color"></i>下载
				</button>
			</a>
			<a id="attachManager_move_other" onclick="alert('开发中')" href="#" style="margin-right: 5px;">
				<button type="button" class="btn btn-xs btn-white">
					<i class="fa fa-files-o icon-color"></i>移动
				</button>
			</a>
<!-- 			<a id="batchDeleteFileHref" onclick="mulDeleteFile();" href="#" style="margin-right: 5px;"> -->
<!-- 					<button type="button" class="btn btn-xs btn-white"> -->
<!-- 						<i class="awsm-icon-remove bigger-110 red"></i>批量删除 -->
<!-- 					</button> -->
<!-- 			</a> -->
<!-- 			<a id="batchTypeAuthority" onclick="orgTypeAuthority();" href="#" style="margin-right: 5px;"> -->
<!-- 					<button type="button" class="btn btn-xs btn-white"> -->
<!-- 						<i class="awsm-icon-remove bigger-110 red"></i>文件夹授权 -->
<!-- 					</button> -->
<!-- 			</a> -->
<!-- 			<a id="batchFileAuthority" onclick="fileAuthority();" href="#" style="margin-right: 5px;"> -->
<!-- 					<button type="button" class="btn btn-xs btn-white"> -->
<!-- 						<i class="awsm-icon-remove bigger-110 red"></i>文件授权 -->
<!-- 					</button> -->
<!-- 			</a>  -->
			</span>
		</div>
		<div id="datagridView" style="height:80%">
			<t:datagrid name="dataVoList" onLoadSuccess="loadSuccess" autoLoadData="false" actionUrl="attachController.do?datagrid"
				pagination="false">
				<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
				<t:dgCol title="父文件夹Id" field="parentTypeId" hidden="false"></t:dgCol>
				<t:dgCol title="图标类型" field="iconType" hidden="false"></t:dgCol>
				<t:dgCol title="是否公共文件夹" field="isPublic" hidden="false"></t:dgCol>
				<t:dgCol title="文件夹归属机构Id" field="orgId" hidden="false"></t:dgCol>
				<t:dgCol title="下载权限" field="downloadAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="上传权限" field="uploadAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="重命名权限" field="renameAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="删除权限" field="deleteAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="移动权限" field="moveAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="创建文件夹权限" field="createFolderAuthority" hidden="false"></t:dgCol>
				<t:dgCol title="文件夹授权权限" field="folderAuthority" hidden="false"></t:dgCol>
				
				<t:dgCol title="文件名" field="name" width="320" myFormatter="return dataManage.fileNameFormatter(value,rec,index);"></t:dgCol>
				<t:dgCol title="大小" field="attachSizeStr" width="120"></t:dgCol>
				<t:dgCol title="修改日期" field="updateTime" formatter="yyyy-MM-dd HH:mm" width="120"></t:dgCol>
				<t:dgCol title="创建人" field="createUserName" width="120"></t:dgCol>
				<t:dgCol title="文件标识" field="fileFlag" hidden="false"></t:dgCol>
				<t:dgCol title="下载路径" field="absolutePath" hidden="false"></t:dgCol>
				<t:dgCol title="存储路径" field="storageType" hidden="false"></t:dgCol>
				
				<t:dgCol title="后缀名" field="ext" hidden="false"></t:dgCol>
				<t:dgCol title="操作" field="opt" width="70"></t:dgCol>
			
				<t:dgFunOpt exp="downloadAuthority#eq#1" icon="fa fa-download icon-color" funname="common_downloadFile(id)" title="下载" />
				<t:dgOpenOpt exp="renameAuthority#eq#1" icon="fa fa-pencil-square-o icon-color" preinstallWidth="1" url="attachController.do?goRename&id={id}&fileFlag={fileFlag}&name={name}&parentTypeId={parentTypeId}" exParams="{optFlag:'update'}" height="150" title="重命名" />
				<t:dgDelOpt exp="deleteAuthority#eq#1"  icon="fa fa-times icon-color" title="删除" url="attachController.do?delete&id={id}&fileFlag={fileFlag}" />
				<t:dgFunOpt exp="moveAuthority#eq#1"  icon="fa fa-files-o icon-color" funname="dataManage.move()" title="移动" />
				<t:dgFunOpt exp="folderAuthority#eq#1"  icon="fa fa-key icon-color" funname="dataManage.folderAuthority(id,orgId)" title="文件夹授权" />
				
<%-- 				<t:dgOpenOpt exp="viewAuthority#eq#Y&&ext#eq#.doc,.docx,.xls,.xlsx,.ppt,.pptx,.vsd,.wps,.dps,.et,.pdf" title="在线查看" icon="awsm-icon-zoom-in green" --%>
<%-- 					exParams="{optFlag:'detail',isIframe:true,noheader:true}" url="attachController.do?viewDataEdit&id={id}&ext={ext}" width="100%" height="100%" /> --%>
			</t:datagrid>
		</div>
		<div id="thumbnailView" style="display:none"></div>
	</div>
</div>
<script type="text/javascript" src="basic/js/jquery.colorbox-min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		redrawEasyUI($(".easyui-layout"));
		//初始化首屏画面参数
		var typeId = '${typeId}';
		$("#parentTypeId").val(typeId);
		$("#viewFlag").val("datasDatagridView");//默认显示列表视图
		$("#menuType").val("personal");//默认显示个人类别
		$("#isPublic").val("0");//默认非公共
		reloadView();
		
		$("#menuAccordion").accordion("options").onSelect=function(title,index){
			var menuType;
			if(index==0){
				menuType="personal";
				$("#menuType").val(menuType);
				reloadView();
			}else if(index==1){
				menuType="work";
			}else if(index==2){
				menuType="org";
			}
			$("#menuType").val(menuType);
		}
		
		$(".thumbnail-left a").on("click", function() {
			$(".thumbnail-left a").removeClass("active");
			$(this).addClass("active");
		});

		$(".data_datagrid_btn,.data_thumbnail_btn").on("click", function() {
			var viewFlag=$(this).attr("data-view");
			//修改当前视图值
			$("#viewFlag").val(viewFlag);
			reloadView();
		});
	});
	
	function reloadThumbnail(id, fileFlag, fileName, isInit, isBack) {
		$("#thumbnailView").empty();
		
		var parentTypeId = $("#parentTypeId").val();
		var menuType = $("#menuType").val();
		var formCode=$("#formCode").val();
		var viewFlag=$("#viewFlag").val();
		var titleBusinessKey=$("#titleBusinessKey").val();
		var dataOrgId=$("#dataOrgId").val();
		var isPublic=$("#isPublic").val();
		//生成导航
		if (id != '' && isInit != 'Y' && isBack != 'Y') {
			//fileNavigation(id, fileFlag, fileName);
		}
		var url="attachController.do?datagrid";
		ajax(url,function(data) {
			var divHTML;
			var thumbnailData=data.obj.result;
			var operationCodes=data.obj.exParam.operationCodes;
			//进行权限控制
			controllButton(nulls(operationCodes));
			
			var backicon = "";
			//判断上次、批量删除权限
			//loadSuccess(data);
			divHTML = "<ul class='ace-thumbnails'>";
			for (var i = 0; i < thumbnailData.length; i++) {
				backicon = getFileIcon(thumbnailData[i].ext);
				var fileFlag = thumbnailData[i].fileFlag;
				if (fileFlag == "0") {
					backicon = "awsm-icon-folder-close orange2";
				}
				var storageType = thumbnailData[i].storageType;
				var id = thumbnailData[i].id;
				var name = thumbnailData[i].name;
				var attachType = thumbnailData[i].attachType;
				var isPublic = thumbnailData[i].isPublic;
				var orgId = thumbnailData[i].orgId;
				var iconType = thumbnailData[i].iconType;
				var fileFlag = thumbnailData[i].fileFlag;
				var colorboxViewHref = "#";
				var datarel = "";
				var thumbnailRePath,backdiv,clickEvent;
				
				//如果是图片类型
				if (attachType == "img") {
					thumbnailRePath = '${attachThumbnailRequest}'+ id;
					colorboxViewHref = '${attachImgRequest}' + id;
					datarel = "colorbox";
					backdiv = '<img alt="图片加载失败！" src="' + (thumbnailRePath || "#") + '" />';
					clickEvent='href="' + colorboxViewHref + '"';
				} else { 
					backdiv = '<div class="background-div"><img style="width:80px;height:80px" src="'+getFileIcon(iconType)+'" /></div>'
					clickEvent='onclick="dataManage.forwardOtherDir(\''+ id + '\',\''+ isPublic + '\',\'' + fileFlag + '\',\'' + name + '\')"';
				}
				
				divHTML += "<li>";
				divHTML += '<a id="' + id + '" '+clickEvent+' data-rel="'
						+ datarel + '">' + backdiv + '<div class="text">' + '<div class="inner">'
						+ name + '</div>' + '</div>' + '<div class="name">' + name + '</div>'
						+ '</a>';

				divHTML += '<div class="tools tools-bottom">';

// 				if (thumbnailData[i].viewAuthority == 'Y'&&(thumbnailData[i].ext=='.doc'||thumbnailData[i].ext=='.docx'||thumbnailData[i].ext=='.xls'
// 					||thumbnailData[i].ext=='.xlsx'||thumbnailData[i].ext=='.ppt'||thumbnailData[i].ext=='.pptx'||thumbnailData[i].ext=='.vsd')
// 					||thumbnailData[i].ext=='.wps'||thumbnailData[i].ext=='.dps'||thumbnailData[i].ext=='.et'||thumbnailData[i].ext=='.pdf') {//查看
// 					divHTML += '<a href="#" onclick="common_viewFile(\'' + aId
// 							+ '\',\'' + thumbnailData[i].ext + '\')"><i class="awsm-icon-search" title="查看"></i></a>';
// 				}
				//判断权限控制显示按钮
				if (thumbnailData[i].downloadAuthority == '1') {
					//下载
					divHTML += '<a href="#" onclick="common_downloadFile(\'' + id + '\')"><i class="fa fa-download" title="下载"></i></a>';
				}
				if (thumbnailData[i].deleteAuthority == '1') {
					//删除
					divHTML += '<a onclick="delObj(\'attachController.do?delete&id=' + id + '&fileFlag='+fileFlag+'\',null,reloadView)" href="#"><i class="fa fa-times" title="删除"></i></a>';
				}
				if (thumbnailData[i].moveAuthority == '1') {
					//移动
					divHTML += '<a onclick="dataManage.move(\''+ id +'\',\''+ fileFlag +'\')" href="#"><i class="fa fa-files-o" title="移动"></i></a>';
				}
				if (thumbnailData[i].renameAuthority == '1') {
					//重命名
					divHTML += '<a onclick="createwindow(\'重命名\',\'attachController.do?goRename&id='+ id +'&fileFlag='+fileFlag+'&name='+name+'\',null,150,1,{optFlag:\'update\'})" href="#"><i class="fa fa-pencil-square-o" title="重命名"></i></a>';
				}
				if (thumbnailData[i].folderAuthority == '1') {
					//文件夹授权
					divHTML += '<a onclick="dataManage.folderAuthority(\'' + id + '\',\'' + orgId + '\')" href="#"><i class="fa fa-key" title="文件夹授权"></i></a>';
				}
				divHTML += '</div>';
				divHTML += "</li>";
			}
			divHTML += "</ul>";
			$("#thumbnailView").html(divHTML);

			$(".thumbnail-data .tools-bottom i").hover(function() {
				showTooltip({
					obj : this,
					msg : this.title,
					placement : "top",
					trg : "hover",
					show : false,
					destroy : false
				});
			});
			
			var colorbox_params = {
				reposition : true,
				scalePhotos : true,
				photo : true,
				scrolling : false,
				previous : '<i class="awsm-icon-arrow-left"></i>',
				next : '<i class="awsm-icon-arrow-right"></i>',
				close : '&times;',
				current : '{current} of {total}',
				maxWidth : '85%',
				maxHeight : '85%',
				onOpen : function() {
					document.body.style.overflow = 'hidden';
				},
				onClosed : function() {
					document.body.style.overflow = 'auto';
				},
				onComplete : function() {
					$.colorbox.resize();
				}
			};

			$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
		},{
			data:{
				parentTypeId: parentTypeId,
			 	menuType:menuType,
				formCode:formCode,
				viewFlag:viewFlag,
				titleBusinessKey:titleBusinessKey,
				dataOrgId:dataOrgId,
				isPublic:isPublic
			}
		});
	}
	
	//根据各参数,刷新整体视图
	function reloadView(){
		var viewFlag=$("#viewFlag").val();
		if(viewFlag=="datasDatagridView"){
			reloadDatagrid();
			$("#datagridView").show();
			$("#thumbnailView").hide();
		}else if(viewFlag=="datasThumbnailView"){
			reloadThumbnail();
			$("#datagridView").hide();
			$("#thumbnailView").show();
		}
	}
	
	function reloadDatagrid(){
		var queryParams = $("#dataVoList").datagrid('options').queryParams;
		queryParams.parentTypeId = $("#parentTypeId").val();
		queryParams.menuType = $("#menuType").val();
		queryParams.formCode=$("#formCode").val();
		queryParams.viewFlag=$("#viewFlag").val();
		queryParams.titleBusinessKey=$("#titleBusinessKey").val();
		queryParams.dataOrgId=$("#dataOrgId").val();
		queryParams.isPublic=$("#isPublic").val();
		$('#dataVoList').datagrid('load');
	}

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
			dataManage.forwardOtherDir(id, fileFlag, fileName, 'Y');
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
				 dataManage.forwardOtherDir(id, fileFlag, fileName, 'Y');
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
			$("#dataViewDiv").panel("refresh", "AttachController.do?datasDatagridView&typeId=" + parentTypeId + "&attachType=" + attachType);
		} else {
			$("#dataViewDiv").panel("refresh", "AttachController.do?datasThumbnailView&typeId=" + parentTypeId + "&attachType=" + attachType);
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
			dataManage.forwardOtherDir(id, fileFlag, fileName);
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
			finishUploadCallback : "dataManage.finishUploadCallback()"
		});
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
		var operationCodes = exParam?exParam.operationCodes:null;
// 		var isAdmin = '${isAdmin}';
// 		if (isAdmin == 'true') {
// 			$("#attachManager_upload_other").show();
// 			$("#batchDeleteFileHref").show();
// 		} else {
			
// 		}
		controllButton(nulls(operationCodes));
	}
	
	//按钮权限控制
	function controllButton(operationCodes) {
		var authorityKeyArray = [ "attachManager_download_other", "attachManager_upload_other", "attachManager_rename_other", "attachManager_delete_other",
				"attachManager_move_other", "attachManager_createFolder_other" ];
		for (var i = 0; i < authorityKeyArray.length; i++) {
			if (operationCodes.indexOf(authorityKeyArray[i]) != -1) {
				$("#" + authorityKeyArray[i]).show();
			} else {
				$("#" + authorityKeyArray[i]).hide();
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
					title : "文件角色授权",
					saveUrl : "attachController.do?saveFileAuthority&aIds=" + aIds.join(',')
				});
			} else {
				tip("请选择需授权的资料");
			}
		} else if (viewFlag == "thumbnailView") {
			tip("请切换到列表视图进行删除");
		}
	}

	//文件批量授权
	function orgTypeAuthority() {
		var viewFlag = $("#viewFlag").val();
		if (viewFlag == "datasDatagridView") {
			var ids = [];
			var rows = $("#dataVoList").datagrid('getSelections');
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					if (rows[i].fileFlag == 1) {
						tip("您勾选项中包含了文件,本功能为文件夹授权,请检查");
						return;
					}
					ids.push(rows[i].id);
				}
				goOrgMulSelectPage({
					title : "文件夹授权",
					needRole : true,
					needOrg : true,
					saveUrl : "attachController.do?saveOrgTypeAuthority&typeIds=" + ids.join(',')
				});
			} else {
				tip("请选择需授权的资料");
			}
		} else if (viewFlag == "datasThumbnailView") {
			tip("请切换到列表视图进行删除");
		}
	}
</script>