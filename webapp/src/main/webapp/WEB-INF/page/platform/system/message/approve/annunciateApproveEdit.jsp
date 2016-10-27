<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.formtable {
	width: 80%; 
	/* background-color: #B8CCE2; */
	margin:5px auto;
}

.formtable .td_title{
	width:15%;
	height:30px;
	text-align:right;
	/*padding-left:5px;*/
}

.formtable .value {
	/*background-color: #FFFFFF;*/
	width:85%;
	height:30px;
	padding:4px;
}
</style>
<div class="easyui-layout" fit="true" style="border: 0;">
  <div region="center"  style="padding:0px;border: 0;height:330px;" href="messageController.do?innerMsgView&id=${param.id }&type=approve" border="false" split="true">
  </div>
  <div region="south" split="true" style="padding:0px;border: 0;height:220px;">
   <t:formvalid formid="formobj" gridId="annunciateApproveList" action="messageController.do?saveAnnApprove">
		<input id="id" name="id" type="hidden" value="${param.id }">
		<input id="roleCode" name="roleCode" type="hidden" value="${param.roleCode }">
		<table cellpadding="0" cellspacing="1" class="formtable" >
		<c:forEach begin="0" var="approve" items="${approveList }" varStatus="status">
		<tr>
				<td class="td_title" >
					<label class="Validform_label">
						<span style="color: green;">${approve.createUserName }</span>&nbsp;&nbsp;&nbsp;<br/>审核意见:
					</label>
				</td>
				<td >
				<textarea id="content" name="content" rows="4" disabled="disabled">${approve.content }</textarea>
				</td>
			</tr>
			<tr>
			<td class="td_title">
					<label class="Validform_label">
						审核结果:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" name="status_${status.index }" value="1" disabled="disabled" <c:if test="${approve.status == '1' }">checked="checked"</c:if>/></label>通过
				   <label class="Validform_label"> <input type="radio" name="status_${status.index }" value="0" disabled="disabled" <c:if test="${approve.status == '0' }">checked="checked"</c:if>/></label>不通过
 				</td>
		</tr>
		</c:forEach>
		<c:if test="${param.type == 'approve' }">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>审核意见:
					</label>
				</td>
				<td >
				<textarea id="content" name="content" rows="4">${approve.content }</textarea>
				</td>
			</tr>
			<tr>
			<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>审核结果:
					</label>
				</td>
				<td class="value">
				   <label class="Validform_label"> <input type="radio" name="status" value="1" <c:if test="${approve.status == '1' }">checked="checked"</c:if>/></label>通过
				   <label class="Validform_label"> <input type="radio" name="status" value="0" <c:if test="${approve.status == '0' }">checked="checked"</c:if>/></label>不通过
 				</td>
			</tr>
		</c:if>
		</table>
	</t:formvalid>
  </div>  
</div>
 
