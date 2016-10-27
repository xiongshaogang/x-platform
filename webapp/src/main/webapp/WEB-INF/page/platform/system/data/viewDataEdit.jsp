<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:formvalid formid="formobj" action="attachController.do?saveOrUpdate" tiptype="5">
		<input id="id" name="id" type="hidden" value="${typePage.id }">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						文件名:
					</label>
				</td>
				<td class="value">
				    <input id="attachName" name="attachName" type="text" class="inputxt" value='${attachPage.attachName}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						文件大小:
					</label>
				</td>
				<td class="value">
				    <input id="attachSizeStr" attachSizeStr="attachSizeStr" type="text" class="inputxt" value='${attachPage.attachSizeStr}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						文件类型:
					</label>
				</td>
				<td class="value">
					<input id="attachType" attachSizeStr="attachType" type="text" class="inputxt" value='${attachPage.attachType}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						附件原始类型:
					</label>
				</td>
				<td class="value">
					<input id="attachContentType" attachSizeStr="attachContentType" type="text" class="inputxt" value='${attachPage.attachContentType}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						附件描述:
					</label>
				</td>
				<td class="value">
					<textarea id="attachRemark" name="attachRemark" class="input_area" >${attachPage.attachRemark}</textarea>
				</td>
			</tr>
	</table>
</t:formvalid>
 <script type="text/javascript">
 	
 </script>
  
  