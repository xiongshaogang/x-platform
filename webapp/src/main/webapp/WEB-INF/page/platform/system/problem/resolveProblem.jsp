<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:formvalid formid="formobj" gridId="problemList" action="problemController.do?resolveProblem">
		<input id="id" name="id" type="hidden" value="${problem.id }">
		<table cellpadding="0" cellspacing="1" class="formtable" style="margin-left:30px;">
			<tr>
				<td class="td_title" valign="top">
					<label class="Validform_label">
						<span style="color:red">*</span>解决方案:
					</label>
				</td>
				<td>
				    <textarea id="resolveSolution" name="resolveSolution" datatype="*" class="input_area">${problem.resolveSolution}</textarea>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>是否已解决:
					</label>
				</td>
				<td class="value">                       
				    <input id="resolveFlag" name="resolveFlag" type="radio" class="inputext" value="N" ${problem.problemState!="2"?"checked":"" }>未解决
				    <input id="resolveFlag" name="resolveFlag" type="radio" class="inputext" value="Y" ${problem.problemState=="2"?"checked":"" }>已解决&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
	</table>
</t:formvalid>
