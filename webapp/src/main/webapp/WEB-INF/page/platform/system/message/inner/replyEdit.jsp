<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:formvalid formid="formobj" gridId="innerList" action="messageController.do?saveReply" refresh="true">
		<input id="id" name="id" type="hidden" value="${msgTo.id }">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td ><span class="Validform_label">回复内容:</span></td>
				<td >
					<textarea id="content" name="content" cols="70" rows="15" >${msgTo.replyContent }</textarea>
				</td>
			</tr>
		</table>
	</t:formvalid>