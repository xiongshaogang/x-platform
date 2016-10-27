<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid formid="formobj" gridId="problemList" action="problemController.do?save">
	<input id="problemId" name="id" type="hidden" value="${problem.id }">
	<input id="problemEdit_optFlag" name="optFlag" type="hidden" value="${optFlag}">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>问题类型:
			</label></td>
			<td class="value"><input id="problemType" name="problemType" type="radio" value="bug"
				${problem.problemType=="bug"?"checked":"" }>bug问题&nbsp;&nbsp;&nbsp; <input id="problemType"
				name="problemType" type="radio" value="experience"
				${problem.problemType=="experience"?"checked":problem.problemType==null?"checked":"" }>体验问题</td>
		</tr>
		<tr>
			<td class="td_title" valign="top"><label class="Validform_label"> <span style="color: red">*</span>反馈内容:
			</label></td>
			<td class="value"><textarea id="content" name="content" datatype="s" class="input_area" style="height: 150px;width:99%"
					placeholder="欢迎您提出在系统使用中遇到的问题或宝贵建议，感谢您对合创成长的支持。">${problem.content}</textarea></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>联系方式:
			</label></td>
			<td class="value"><input id="contactInformation" name="contactInformation" datatype="e" type="text"
				class="inputxt" value='${email==null?problem.contactInformation:email}'></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>问题截图:
			</label></td>
			<td class="value"><a id="problemEdit_uploadBtn" class="easyui-linkbutton" disabled=""
				onclick='${optFlag=="detail"?"problemEdit.uploadView()":"problemEdit.upload()"}'>${optFlag=="detail"?"查看":"上传"}</a>
			</td>
		</tr>
	</table>
</t:formvalid>
<script type="text/javascript">
	$(function() {
		var problemList=$("#problemList");
		var problemEdit_optFlag=$("#problemEdit_optFlag").val();
		if(problemList&&problemEdit_optFlag=="detail"){
			$("#problemList").datagrid("reload");
		}
		setTimeout(function() {
			$("#problemEdit_uploadBtn button").attr("disabled", false);
		}, 0);
	});
	var problemEdit = {
		upload : function() {
			var id = $("#problemId").val();
			commonPageUpload({
				businessKey : id,
				businessType : "ProblemEntity",
				isNeedToType : false
			});
		},
		uploadView : function() {
			var id = $("#problemId").val();
			commonUploadAndView({
				businessKey : id,
				businessType : "ProblemEntity",
				isFileTypeFilter : false,
				isMulDelete : false,
				isEdit : false,
				isUpload : false,
				isDelete : false
			})
		}
	};
</script>