<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid formid="formobj" gridId="timerList" action="timerController.do?saveTimer">
	<input id="id" name="id" type="hidden" value="${dictType.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						任务名称:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt" value='${dictType.name}'>
				
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						任务类:
					</label>
				</td>
				<td class="value">
				     <input id="className" name="className" type="text" class="inputxt" value='${dictType.name}'/>
				</td>
			</tr>
			<%-- <tr>
				<td class="td_title">
					<label class="Validform_label">
						任务参数:
					</label>
				</td>
				<td class="value">
					<textarea name="parameterJson" type="text" class="input_area">${dictType.description}</textarea>
				</td>
			</tr> --%>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						描述:
					</label>
				</td>
				<td class="value">
					<textarea name="description" type="text" class="input_area">${dictType.description}</textarea>
				</td>
			</tr>
	</table>
</t:formvalid>