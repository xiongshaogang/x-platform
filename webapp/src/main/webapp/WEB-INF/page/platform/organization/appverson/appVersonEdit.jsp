<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript">
$(document).ready(function(){
	redrawEasyUI($(".easyui-layout"));
});

//uniquemsg='当前版本已存在' oldValue='${version.versionNumber }' entityName='com.xplatform.base.orgnaization.appversion.entity.AppVersionEntity' ajaxurl="commonController.do?checkUnique"



</script>	
<div class="easyui-layout" fit="true">

	<div region="center" style="padding: 0px; border: 0px;">
			<h4 style="margin-left: 20px">
				上传更新软件(当前的版本名称:<span id="vnameid">${oversion.versionName }</span>,版本号:<span
					id="vnumberid">${oversion.versionNumber }</span>)
			</h4>

			<div style="margin-left: 20px" id="div22">
				<t:formvalid formid="formobj"
					gridId="appList" callback="appVerson.appupdateCallback()" beforeSubmit="appVerson.appupdateBefore()"
					action="appVersonController.do?saveApp"
					tiptype="5">
					<table>
						<tr>
							<td><input type="hidden" id="aid" name="id"
								value="${version.id }"> <label class="Validform_label">
									<span style="color: red">*</span>新版本名称：
							</label></td>
							<td><input class="inputxt" datatype="*" name="versionName" type="text" id="versionName" value="${version.versionName }">
								
								<input type="hidden" name="attachMentId" id="attachMentId">
							</td>
						</tr>
	
						<tr>
							<td><label class="Validform_label"> <span
									style="color: red">*</span>新版本号：
							</label></td>
							<td><input class="inputxt" datatype="*" name="versionNumber" type="text" id="versionNumber" value="${version.versionNumber }" ></td>
						</tr>

						<tr>
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>是否强制更新:
								</label>
							</td>
							<td class="value">
								<t:comboBox name="isForcedUpdate" datatype="*" id="isForcedUpdate" data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]' value="${version.isForcedUpdate}" ></t:comboBox>
							</td>
						</tr>
						
						<tr>
							<td class="td_title">
								<label class="Validform_label">
									<span style="color:red">*</span>选择软件类型:
								</label>
							</td>
							<td class="value">
								<t:comboBox name="type" datatype="*" id="type" data='[{"id":"0","text":"android"},{"id":"1","text":"ios"}]' value="${version.type}" ></t:comboBox>
							</td>
						</tr>
						
						<tr>
							<td><label class="Validform_label"> <span
									style="color: red"></span>下载链接：
							</label></td>
							<td><input class="inputxt" name="url" type="text" id="url" value="${version.url }" ></td>
						</tr>
							
						<tr>
							<td><label class="Validform_label"> <span
									style="color: red">*</span>版本更新提示：
							</label></td>
							<td><textarea rows="4" datatype="*" id="versionDescrition" name="versionDescrition" cols="35">${version.versionDescrition }</textarea></td>
							
						</tr>
						
						<tr>
							<td><a id="version" class="easyui-linkbutton"
								onclick='appVerson.uploadApp()'>上传软件</a></td>
						</tr>
	
					</table>
				</t:formvalid>
			</div>
</div>
</div>