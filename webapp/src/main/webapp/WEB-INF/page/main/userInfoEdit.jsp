<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
</style>
<script type="text/javascript">
	function portraitUpload() {
		createwindow("头像上传", "employeeController.do?portraitUpload", 390, 330, null);
	}
</script>
<t:formvalid formid="form_userInfoEdit" action="employeeController.do?userInfoSimpleSave" afterSaveClose="false">
	<input type="hidden" id="userInfoEdit_id" name="id" class="inputxt" value="${employee.id}" />
	<div style="margin: 0 auto; width: 518px">
		<div style="width:80px;height:80px;padding:5px;border:1px dashed #d5d5d5;">
		<img id="portrait80" src="${employee.portrait80}"
			style="width: 80px; height: 80px;<c:if test="${employee.portrait80==null or employee.portrait80==''}">display:none</c:if>" />
		</div>
		<span style="margin-top:5px;width:76px;"
			class="btn btn-xs btn-warn fileinput-button" onclick="portraitUpload()"> <i class="awsm-icon-plus-search"></i>修改头像
		</span>
	</div>
	<hr>
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label">姓名</label></td>
			<td class="value"><input type="text" id="name" name="name" class="inputxt" value="${employee.name}"
				readonly="readonly" /></td>
			<td class="td_title"><label class="Validform_label">工作邮箱</label></td>
			<td class="value"><input type="text" id="jobEmail" name="jobEmail" class="inputxt" value="${employee.jobEmail}"
				datatype="e" ignore="ignore" readonly="readonly" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">员工编号</label></td>
			<td class="value"><input type="text" id="code" name="code" class="inputxt" value="${employee.code}"
				readonly="readonly" /></td>
			<td class="td_title"><label class="Validform_label">座机</label></td>
			<td class="value"><input type="text" id="jobPhone" name="jobPhone" class="inputxt" value="${employee.jobPhone}"
				readonly="readonly" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">手机号码</label></td>
			<td class="value"><input type="text" id="phone" name="phone" class="inputxt" value="${employee.phone}"
				datatype="m" ignore="ignore" /></td>
			<td class="td_title"><label class="Validform_label">QQ</label></td>
			<td class="value"><input type="text" id="qq" name="qq" class="inputxt" value="${employee.qq}" datatype="qq"
				ignore="ignore" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">MSN</label></td>
			<td class="value"><input type="text" id="msn" name="msn" class="inputxt" value="${employee.msn}" datatype="e"
				ignore="ignore" /></td>
			<td class="td_title"><label class="Validform_label">传真号</label></td>
			<td class="value"><input type="text" id="jobFax" name="jobFax" class="inputxt" value="${employee.jobFax}"
				datatype="tel" ignore="ignore" readonly="readonly" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">个人邮箱</label></td>
			<td class="value"><input type="text" id="email" name="email" class="inputxt" value="${employee.email}"
				datatype="e" ignore="ignore" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">联系地址</label></td>
			<td class="value" colspan="3"><input type="text" id="address" name="address" class="inputxt"
				style="width: 99.9%" value="${employee.address}" /></td>
		</tr>
	</table>
</t:formvalid>

