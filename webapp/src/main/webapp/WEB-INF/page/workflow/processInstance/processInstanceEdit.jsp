<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:formvalid formid="formobj" gridId="jobList" action="jobController.do?saveJob">
		<input id="id" name="id" type="hidden" value="${job.id }">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						实例名称
					</label>
				</td>
				<td class="value">
				    ${processInstance.title}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						流程定义id
					</label>
				</td>
				<td class="value">
				    ${processInstance.actDefId}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						流程定义名称
					</label>
				</td>
				<td class="value">
				    ${processInstance.defName}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						流程实例id:
					</label>
				</td>
				<td class="value">
				    ${processInstance.actInstId}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						业务编码
					</label>
				</td>
				<td class="value">
				    ${processInstance.businessKey}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						业务名称
					</label>
				</td>
				<td class="value">
				    ${processInstance.businessName}
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						持续时间:
					</label>
				</td>
				<td class="value">
				    ${processInstance.duration}
				</td>
			</tr>
		</table>
	</t:formvalid>
