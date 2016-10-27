<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
	<div id="thumbnailData" class="thumbnail-data" region="center"
		style="padding: 0px; border-left: 0px; border-right: 0px; overflow-y: auto;"></div>
</div>
<script type="text/javascript">
	$(function() {
		loadThumbnail();
	});

	/**
	id 附件Id
	**/
	function loadThumbnail() {
		var businessKey = $("#common_businessKey", ${uploadId}).val();
		var businessType = $("#common_businessType", ${uploadId}).val();
		var businessExtra = $("#common_businessExtra", ${uploadId}).val();
		var otherKey = $("#common_otherKey", ${uploadId}).val();
		var otherKeyType = $("#common_otherKeyType", ${uploadId}).val();
		var attachType = $("#common_attachType",${uploadId}).val();
		var optFlag = $("#common_optFlag", ${uploadId}).val();
		
		var uParam = "&businessKey=" + businessKey + "&businessType=" + businessType + "&businessExtra=" + businessExtra
		+ "&otherKey=" + otherKey + "&otherKeyType=" + otherKeyType + "&attachType=" + attachType+ "&optFlag=" + optFlag;
		
		var isDownload = $("#common_isDownload",${uploadId}).val();
		var isEdit = $("#common_isEdit",${uploadId}).val();
		var isDelete = $("#common_isDelete",${uploadId}).val();
		var isView = $("#common_isView",${uploadId}).val();
		var thumbnailUrl = "${thumbnailUrl}";
		$.ajax({
			url : thumbnailUrl+ uParam,
			data : {
				attachType : attachType,
				isEdit : isEdit,
				isDelete : isDelete,
				isView : isView,
				isDownload : isDownload
			},
			type : 'post',
			cache : false,
			success : function(data) {
				var divHTML;
				var thumbnailData = data.thumbnailData;
				var backicon = "";
				//判断上次、批量删除权限
				${uploadId}_loadSuccess(data);
				divHTML = "<ul class='ace-thumbnails'>";
				for (var i = 0; i < thumbnailData.length; i++) {
					backicon = getFileIcon(thumbnailData[i].ext);
					var storageType = thumbnailData[i].storageType;
					var aId = thumbnailData[i].id;

					var attachName = thumbnailData[i].name;
					var attachType = thumbnailData[i].attachType;
					var colorboxViewHref = "#";
					var datarel = "";
					var thumbnailRePath;

					var backdiv;

					//如果是图片类型
					if (attachType == "img") {
						thumbnailRePath = thumbnailRePath = "attachController.do?getThumbnailImage&aId=" + aId;
						colorboxViewHref = "attachController.do?getPlainImage&aId=" + aId;
						datarel = "colorbox";
						backdiv = '<img alt="图片加载失败！" src="' + (thumbnailRePath || "#") + '" />';
					} else {
						backdiv = '<div class="background-div"><i class="' + backicon + ' awsm-icon-7x" /></div>'
					}
					divHTML += "<li>";
					divHTML += '<a id=\"' + aId + '\" href=\"' + colorboxViewHref
							+ '\" data-rel=\"' + datarel + '\">' + backdiv
							+ '<div class=\"text\">' + '<div class=\"inner\">' + attachName + '</div>' + '</div>'
							+ '<div class=\"name\">' + attachName + '</div>' + '</a>';

					divHTML += '<div class=\"tools tools-bottom\">';

					//判断权限控制显示按钮
					var downloadAuthority = thumbnailData[i].downloadAuthority;
					var viewAuthority = thumbnailData[i].viewAuthority;
					var editAuthority = thumbnailData[i].editAuthority;
					var deleteAuthority = thumbnailData[i].deleteAuthority;

					if (downloadAuthority == 'Y') {//下载
						divHTML += '<a href="#" onclick="common_downloadFile(\'' + aId
								+ '\')"><i class="awsm-icon-cloud-download" title="下载"></i></a>';
					}

					if (thumbnailData[i].viewAuthority == 'Y'&&(thumbnailData[i].ext=='.doc'||thumbnailData[i].ext=='.docx'||thumbnailData[i].ext=='.xls'
						||thumbnailData[i].ext=='.xlsx'||thumbnailData[i].ext=='.ppt'||thumbnailData[i].ext=='.pptx'||thumbnailData[i].ext=='.vsd')
						||thumbnailData[i].ext=='.wps'||thumbnailData[i].ext=='.dps'||thumbnailData[i].ext=='.et'||thumbnailData[i].ext=='.pdf') {//查看
						divHTML += '<a href="#" onclick="common_viewFile(\'' + aId
								+ '\',\'' + thumbnailData[i].ext + '\')"><i class="awsm-icon-search" title="查看"></i></a>';
					}

					if (editAuthority == 'Y') {//修改
						divHTML += '<a href="#" onclick="common_updateFile(\'' + aId
								+ '\')"><i class="awsm-icon-pencil" title="修改"></i></a>';
					}

					if (deleteAuthority == 'Y') {//删除
						divHTML += '<a onclick="${uploadId}_deleteFile(\'' + aId
								+ '\')" href="#"><i class="awsm-icon-remove red" title="删除"></i></a>';
					}

					divHTML += '</div>';
					divHTML += "</li>";
				}
				divHTML += "</ul>";
				$("#thumbnailData",${uploadId}).html(divHTML);

				$(".thumbnail-data .tools-bottom i",${uploadId}).hover(function() {
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
					maxWidth : '100%',
					maxHeight : '100%',
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

				$('.ace-thumbnails [data-rel="colorbox"]',${uploadId}).colorbox(colorbox_params);
			}
		});
	}
</script>