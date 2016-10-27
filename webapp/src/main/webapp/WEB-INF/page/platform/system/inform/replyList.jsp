<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.formtable1 .td_title{
	width:80px;
	height:30px;
	text-align:right;
	/*padding-left:5px;*/
}

.formtable1 .value {
	/*background-color: #FFFFFF;*/
	width:500px;
	height:30px;
	padding:4px;
}

/* textarea文本域 */
.input_area1 {
	padding:0;
	width:460px;
	height:60px;
	border: 1px solid #D5D5D5;
	resize: none;
	/*font-size:12px;*/
	/*border: 1px solid #a5aeb6;*/
	/*padding: 3px 2px;*/
}
</style>
		<table cellpadding="0" cellspacing="1" class="formtable1" >
		<c:choose>
		<c:when test="${empty list}"><span class="Validform_label" style="margin-left: 2em;">暂无回复</span></c:when>
		<c:otherwise>
		<c:forEach begin="0" step="1" items="${list }" var="item">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color: green;">${item.receive_name }</span>:
					</label>
				</td>
				<td class="value">
				   <textarea class="input_area1">${item.reply_content }</textarea>
				</td>
			</tr>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		</table>
