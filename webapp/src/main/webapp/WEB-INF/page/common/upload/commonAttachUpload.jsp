<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<div class="upload_main_div">
	<form id="commonAttachFileupload" action="attachController.do?uploadFiles" enctype="multipart/form-data">
		<input id="storageType" name="storageType" type="hidden" value="${storageType}" /> <input id="businessKey"
			name="businessKey" type="hidden" value="${businessKey}" /> <input id="businessType" name="businessType"
			type="hidden" value="${businessType}" /> <input id="businessExtra" name="businessExtra" type="hidden"
			value="${businessExtra}" /> <input id="otherKey" name="otherKey" type="hidden" value="${otherKey}" /> <input
			id="otherKeyType" name="otherKeyType" type="hidden" value="${otherKeyType}" /> <input id="isNeedToType"
			name="isNeedToType" type="hidden" value="${isNeedToType}" /><input id="parentTypeCode" name="parentTypeCode"
			type="hidden" value="${parentTypeCode}" /><input id="defaultTypeCode" name="defaultTypeCode" type="hidden"
			value="${defaultTypeCode}" /><input id="autoCreateType" name="autoCreateType" type="hidden"
			value="${autoCreateType}" /><input id="defaultTypeId" name="defaultTypeId" type="hidden" value="${defaultTypeId}" />
			<input id="isShowType" name="isShowType" type="hidden" value="${isShowType}" />
		<!-- 总操作按钮栏 -->
		<div class="fileupload-buttonbar">
			<div class="pull-left">
				<span class="btn btn-sm btn-info fileinput-button"> <i class="awsm-icon-plus-sign"></i> <span>添加文件</span> <input
					type="file" name="files[]" multiple />
				</span>
				<!-- 额外用户手控按钮暂时不启用 -->
				<!--<button type="submit" class="btn btn-primary start">
					<i class="awsm-icon-cloud-upload"></i> <span>全部上传</span>
				</button>
				 <button type="reset" class="btn btn-warning cancel">
					<i class="awsm-icon-minus-sign"></i> <span>取消全部</span>
				</button> 
				<button type="button" class="btn btn-danger delete">
					<i class="awsm-icon-trash"></i> <span>删除全部</span>
				</button>
				<input type="checkbox" class="toggle">-->

				<!-- 文件保存位置信息框 -->
				<c:if test="${isShowType}">
					<div class="upload-locinfo">
						<span>上传至:</span> <span> <c:choose>
								<c:when test="${isChangeType}">
									<t:combotree id="typeId" name="typeId" url="${typeTreeUrl}" value="${defaultTypeId}" />
								</c:when>
								<c:otherwise>
						${defaultTypeName}
						<input id="typeId" name="typeId" type="hidden" value="${defaultTypeId}" />
								</c:otherwise>
							</c:choose>
						</span>
					</div>
				</c:if>
				<!-- 总进度条 The global file processing state -->
				<span class="fileupload-process"></span>
			</div>
			<!-- 总进度相关参数 The global progress state -->
			<div class="pull-left fileupload-progress fade">
				<!-- The global progress bar -->
				<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
					<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
				</div>
				<!-- The extended global progress state -->
				<div class="progress-extended">&nbsp;</div>
			</div>
		</div>
		<!-- 放置上传于下载列表的table The table listing the files available for upload/download -->
		<div class="upload-list-div">
			<table role="presentation" class="table table-striped">
				<tbody class="files"></tbody>
			</table>
		</div>
		<div class="upload-bottom-div">
			<button id="finishUpload" type="button" class="btn btn-sm btn-info start">
				<i class="awsm-icon-ok-circle"></i> <span>完成上传</span>
			</button>
		</div>
	</form>
</div>

<!-- 上传模板 The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) {var uploadFileIcon=getFileIcon(file.iconType,file.thumbnailUrl); %}
    <tr class="template-upload fade">
        <td>
			<span class="preview-cancel">
				<img height="80" width="80" src="{%=uploadFileIcon%}">
			</span>
        </td>
        <td>
			<p class="name"><p class="filename" title="{%=file.name%}" name="fileNames[]" >{%=file.name%}</p></p>
            <strong class="error text-danger"></strong>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
            <span>文件大小：</span><span class="size">上传中...</span>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-sm btn-primary start hide" disabled>
                    <i class="awsm-icon-cloud-upload"></i>
                    <span>上传</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-sm btn-warning cancel">
                    <i class="awsm-icon-minus-sign"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- 下载模板 The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) {
	var uploadFileIcon=getFileIcon(file.iconType,file.thumbnailUrl); 
%}
    <tr class="template-download fade">
        <td>
           	<span class="preview">
                 <a href="{%=file.downloadUrl%}" title="{%=file.name%}" data-gallery><img height="80" width="80" src="{%=uploadFileIcon%}"></a>
            </span>
        </td>
        <td>
			<input type="hidden" flag="attachId" value="{%=file.id%}"/>
            <p class="name">
                {% if (file.downloadUrl) { %}
                    <a class="filename" title="{%=file.name%}" href="{%=file.downloadUrl%}" title="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span class="filename" title="{%=file.name%}">{%=file.name%}</span>
                {% } %}
            </p>
			<p>
				<span class="size">{%=o.formatFileSize(file.size)%}</span>
			</p>
            {% if (file.info) { %}
                <div>上传信息：{%=file.info%}</div>
            {% } %}
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-sm btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="awsm-icon-trash"></i>
                    <span>删除</span>
                </button>
            {% } else { %}
                <button class="btn btn-sm btn-warning cancel">
                    <i class="awsm-icon-minus-sign"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>

<script type="text/javascript">
	$(function() {
		//查询出的已上传附件数据
		var loadResult=${result};
		var businessKey='${businessKey}';
		var businessType='${businessType}';
		var businessExtra='${businessExtra}';
		var otherKey='${otherKey}';
		var otherKeyType='${otherKeyType}';
		var isReloadGrid=${isReloadGrid};
		var isLoadData=${isLoadData};
		var allFiles;
		//利用点击事件刷新视图页面
		$("#finishUpload").on("click", function() {
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : "attachController.do?queryAttachUpload",// 请求的action路径
				data:{
					businessKey:businessKey,	
					businessType:businessType,	
					businessExtra:businessExtra,	
					otherKey:otherKey,	
					otherKeyType:otherKeyType	
				},
				error : function() {// 请求失败处理函数
				},
				success : function(data) {
					allFiles=data;
				}
			});
			${finishUploadCallback};
			//刷新页面
			if(isReloadGrid){
				var viewFlag = $("#common_viewFlag",$("#${uploadId}")).val();
				if (viewFlag == "datagridView") {
					$(".data_datagrid_btn",$("#${uploadId}")).click();
				} else if (viewFlag == "thumbnailView") {
					$(".data_thumbnail_btn",$("#${uploadId}")).click();
				}
			}
			closeD(getD($("#finishUpload")));
		});
		var jfExParams=${jfExParams};
		$('#commonAttachFileupload').fileupload(jfExParams);
		if(isLoadData){
			$('#commonAttachFileupload').fileupload('option', 'done').call($('#commonAttachFileupload'), $.Event('done'), {
				result : loadResult
			});
		}
		// Load existing files:
// 		$('#commonAttachFileupload').addClass('fileupload-processing');
		
// 		var loadFilesUrl="attachController.do?getFiles";
// 		loadFilesUrl+="&businessKey=1";
// 		loadFilesUrl+="&businessType=testEntity";
			
// 		$.ajax({
// 		    // Uncomment the following to send cross-domain cookies:
// 		    //xhrFields: {withCredentials: true},
// 		    url: loadFilesUrl,
// 		    dataType: 'json',
// 		    context: $('#commonAttachFileupload')[0]
// 		}).always(function () {
// 		    $(this).removeClass('fileupload-processing');
// 		}).done(function (result) {
// 			console.info(result);
// 		    $(this).fileupload('option', 'done')
// 		        .call(this, $.Event('done'), {result: result});
// 		}); 

	});
</script>