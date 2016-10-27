<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>


<form id="batchCompleteForm">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>结论:
				</label>
			</td>
			<td class="value">
			   <t:comboBox id="voteAgree" name="voteAgree" data="${conclusionData}"></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					审批意见:
				</label>
			</td>
			<td class="value">
				<textarea id="opinion" name="opinion" style="width: 97%;" rows="5"></textarea>
			</td>
		</tr>
	</table>
</form>