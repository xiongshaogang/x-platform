<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">

formLogoEdit = {
		
		 uploadFormLogo : function(result) {
				console.info(result);
				if (result.length > 1) {
					alert("只能上传单张图片");
				}
			
				if (result.length != 0) {
			
					$("#logo").val(result[0].id);
					$("#name").val(result[0].name);
					if ($("#imgbtd:has(img)").length > 0) {
						$("#formLogoImg").attr(
								"src",
								"attachController.do?getThumbnailImage&aId="
										+ result[0].id);
					} else {
						$("#logo").after(
								"<img id='commidityImg' src=attachController.do?getThumbnailImage&aId="
										+ result[0].id + " >");
					}
			
				} else {
					$("#logo").val("");
					$("#logo").next().remove();
				}
			
			},
			uploadFormLogoImg : function() {
				var id = $("#id").val();
				commonPageUpload({
					businessKey : id,
					businessType : "formLogoEntity",
					businessExtra : "logo",
					isNeedToType : false,
					finishUploadCallback : "formLogoEdit.uploadFormLogo(allFiles)",
					exParams : {
						imageMaxWidth : 720,
						imageMaxHeight : 777,
						maxFileSize : 30222200
					}

				});
			}
};

$(function(){
	<c:if test="${optFlag == 'add'}">
	$("#formLogodetail").hide();
	</c:if>
	
	<c:if test="${optFlag == 'update'}">
	$("#formLogodetail").hide();
	</c:if>
	
	<c:if test="${optFlag == 'detail'}">
	$("#formLogoImg").hide();
	$("#uploadBigBtn").hide();
	</c:if>
});	
</script>
<t:formvalid gridId="formLogoList" action="formLogoController.do?saveOrUpdate">
	<input id="id" name="id" type="hidden" value="${formLogo.id }">
	<input id="optFlag" name="optFlag" type="hidden" value="${optFlag}">
	<table cellpadding="0" cellspacing="1" class="formtable" style="width: 100%">
	
	
		<tr>
			
			<td class="td_title"><label class="Validform_label"> 表单logo：</label></td>
			<td class="value" id="imgbtd"><input name="logo" id="logo" type="hidden" value="${formLogo.logo }"> 
					
						<!-- add和update时显示 -->
						<img id="formLogoImg" name="formLogoImg" src='attachController.do?getThumbnailImage&aId=${formLogo.logo }' style="width: 100px;height: 100px;"/>
						<a id="uploadBigBtn" class="easyui-linkbutton" onclick='formLogoEdit.uploadFormLogoImg()'>上传</a>
						<!-- add和update时显示 -->
						
						<!-- detail时显示 -->
						<img id="formLogodetail" src='attachController.do?getThumbnailImage&aId=${formLogo.logo }' style="width: 100px;height: 100px;" />
						<!-- detail时显示 -->
						

				</td>
				
		</tr>

		<tr>
		
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span> 文件名称：
			</label></td>
			<td class="value"><input class="inputxt"  name="name" type="text" id="name" value="${formLogo.name}"></td>
<%-- 			
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span> 文件编码：
			</label></td>
			<td class="value"><input class="inputxt"  datatype="*" name="code" type="text" id="code" value="${formLogo.code}"></td> --%>
		
		</tr>
		

		
		
		
		

	</table>
</t:formvalid>