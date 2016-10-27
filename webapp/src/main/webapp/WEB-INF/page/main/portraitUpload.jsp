<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
</style>
<script type="text/javascript">
	var previewWidth = 80;
	var previewHeight = 80;
	var maxWidth = 230;
	var maxHeight = 230;
	var aspectRatio = 1;
	$(function() {
		//设置提示语句
		yitip($("#useImg"), "建议上传容量小于100KB的头像文件");

		$('#form_portaitUpload').fileupload({
			uploadTemplateId : null,
			downloadTemplateId : null,
			add : function(e, data) {
				$("#useImg").off().on("click", function() {
					data.submit();
				});
			},
			send : function(e, data) {
			},
			done : function(e, data) {
				if (data.textStatus == "success") {
					var result = data.result;
					if (result && result.success) {
						//给对应预览图赋值
						$("#index-portrait30").attr("src", result.obj);
						$("#index-portrait80").attr("src", result.obj);
						//关闭上传页
						closeD(getD($("#form_portaitUpload")));
					}
					tip(result.msg);
				} else {
					tip("头像上传失败,请重新上传!");
				}
			}
		});

		var jcrop_api, boundx, boundy;
		FileReader = window.FileReader;
		$("#selectImg").change(function() {
			var image = $("<img>");
			if (jcrop_api != null) {
				jcrop_api.destroy();
			}
			if (FileReader) {
				//Firefox
				var reader = new FileReader();
				var file = this.files[0];
				reader.readAsDataURL(file);
				reader.onload = function(e) {
					image.attr("src", this.result);
					image.attr("id", "sourceImg");
					image.hide();
					image.on("load", function() {
						//缩放尺寸
						resizeImg(this, maxWidth, maxHeight);
						image.show();
						//初始化裁剪工具
						initJcrop();
					});
					$("#preview").attr("src", this.result);
					$(".editBox").html(image);
				};
			} else {
				//IE
				var path = $(this).val();
				image.src = path;
				image.id = "sourceImg";
				$("#preview").attr("src", path);
				$(".editBox").html(image);
				$(image).Jcrop({
					onChange : updatePreview,
					onChange : updateCoords,
					onSelect : updateCoords,
					onSelect : updatePreview,
					onRelease : clearCoords
				}, function() {
					jcrop_api = this;
				});
				//设置长宽比
				width = image.width;
				height = image.height;
				while (width > MAXWIDTH || height > MAXHEIGHT) {
					var rat;
					if (width > MAXWIDTH) {
						rat = MAXWIDTH / width;
						width = MAXWIDTH;
						height = height * rat;
					}
					if (height > MAXHEIGHT) {
						rat = MAXHEIGHT / height;
						height = MAXHEIGHT;
						width = width * rat;
					}
				}
				$(image).css('width', width);
				$(image).css('height', height);
				$("#sourceImg").css({
					width : width,
					height : height
				});
			}
		});
	});

	function updateCoords(c) {
		$('#portaitUpload_x').val(Math.round(c.x));
		$('#portaitUpload_y').val(Math.round(c.y));
		$('#portaitUpload_w').val(Math.round(c.w));
		$('#portaitUpload_h').val(Math.round(c.h));
	}

	function initJcrop() {
		$('#sourceImg').Jcrop({
			//大小改变事件
			onChange : function(c) {
				showPreview(c);
				updateCoords(c);
			},
			//选择事件
			onSelect : function(c) {
				showPreview(c);
				updateCoords(c);
			},
			aspectRatio : 1,
			allowSelect : false,
			allowResize : true,
			addClass : 'jcrop-dark',
			bgOpacity : 0.5
		}, function() {
			jcrop_api = this;
			//jcrop_api.ui.selection.addClass('jcrop-selection');
			jcrop_api.setSelect([ 0, 0, 80, 80 ]);
			jcrop_api.setOptions({
				bgFade : true
			});
			jcrop_api.ui.holder.css("margin-top", $("#sourceImg").css("margin-top"));
			jcrop_api.ui.holder.css("margin-left", $("#sourceImg").css("margin-left"));
			$(".sourceImg").css("position", "static");
		});
	}

	function showPreview(coords) {
		var rx = previewWidth / (coords.w || 1);
		var ry = previewHeight / (coords.h || 1);
		var boundx = $("#sourceImg").width();
		var boundy = $("#sourceImg").height();
		$('#preview').css({
			width : Math.round(rx * boundx) + 'px',
			height : Math.round(ry * boundy) + 'px',
			marginLeft : '-' + Math.round(rx * coords.x) + 'px',
			marginTop : '-' + Math.round(ry * coords.y) + 'px'
		});
	}
	function resizeImg(ImgD, rWidth, rHeight) {
		var image = new Image();
		image.src = ImgD.src;
		if (image.width > 0 && image.height > 0) {
			if (image.width / image.height >= rWidth / rHeight) {
				if (image.width > rWidth) {
					ImgD.width = rWidth;
					ImgD.height = image.height * (rWidth / image.width);
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
				} else {
					ImgD.width = image.width;
					ImgD.height = image.height;
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
				}
			} else {
				if (image.height > rHeight) {
					ImgD.height = rHeight;
					ImgD.width = image.width * (rHeight / image.height);
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
				} else {
					ImgD.width = image.width;
					ImgD.height = image.height;
					$(ImgD).css("margin-top", (rHeight - ImgD.height) / 2);
					$(ImgD).css("margin-left", (rWidth - ImgD.width) / 2);
				}
			}
			ImgD.title = image.width + "×" + image.height;
		}
	}
</script>
<style>
.avatorInfo {
	margin-top: 25px;
	width: 100%;
/* 	height: 235px; */
}

.editBox {
	border: 1px solid #f79263;
/* 	box-shadow: 0 1px 0 #b9b9b9; */
	height: 230px;
	margin-left: 25px;
	margin-right: 22px;
	width: 230px;
	float: left;
}

.priviewDiv {
	float: left;
	height:230px;
	position: relative;
}

.priviewBox {
	width: 80px;
	height: 80px;
	overflow: hidden;
	/* 	background: url("../images/avatar_default17ced3.png") no-repeat scroll center center rgba(0, 0, 0, 0); */
	border: 1px solid #f79263;
/* 	box-shadow: 0 1px 0 #b9b9b9; */
}

.sourceImg {
	/* 	width: 230px; */
	/* 	height: 230px; */
	
}

.priviewImg {
	/* 	width: 80px !important; */
	/* 	height: 80px !important; */
	
}
</style>
<form id="form_portaitUpload" method="post" action="userController.do?uploadPortraitFile"
	enctype="multipart/form-data">
	<!-- 	<table cellpadding="0" cellspacing="1" class="formtable"> -->
	<!-- 		<tr> -->
	<!-- 			<td class="td_title"><label class="Validform_label">图片上传<font color='red'>*.gif,*.jpg,*.png,*.bmp</font></label></td> -->
	<!-- 			<td class="value"></td> -->
	<!-- 		</tr> -->
	<!-- 	</table> -->
	<input type="hidden" id="portaitUpload_x" name="portaitUpload_x" /> <input type="hidden" id="portaitUpload_y"
		name="portaitUpload_y" /> <input type="hidden" id="portaitUpload_w" name="portaitUpload_w" /> <input type="hidden"
		id="portaitUpload_h" name="portaitUpload_h" /> <input type="hidden" id="portaitUpload_w" name="portaitUpload_w" />
	<div class="avatorInfo">
		<div class="editBox">
			<!-- 			<img class="sourceImg" src="webpage/main/sago.jpg" id="sourceImg" onload="resizeImg(this,230,230);initJcrop()" /> -->
		</div>
		<div class="priviewDiv">
			<div class="priviewBox">
				<img class="priviewImg" id="preview" />
			</div>
			<span id="selectImgSpan" class="btn btn-xs btn-warn fileinput-button" style="width:80px;position: absolute;bottom:30px"> <i class="awsm-icon-plus-search"></i>选择文件<input
				id="selectImg" type="file" name="files[]" multiple />
			</span><br /> <span id="useImg" class="btn btn-xs btn-warn fileinput-button" style="width:80px;position: absolute;bottom:0"> <i class="awsm-icon-plus-search"></i>使用头像
			</span>
		</div>
	</div>
</form>

