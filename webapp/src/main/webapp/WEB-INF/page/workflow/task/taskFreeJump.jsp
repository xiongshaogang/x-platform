<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<form id="freeJumpData">
	<table class="borderTable" style="width: 99.9%">
		<tr>
			<td width="15%">回退节点</td>
			<td width="35%">
				<div style="padding:1px;">
				<select id="destTask" name="destTask" onchange="changeDestTask(this,${taskId})">
					<option value="">请选择...</option>
					<c:forEach items="${jumpNodeMap}" var="item">
						<optgroup label="${item.key}">
							<c:forEach items="${item.value}" var="node">
								<option value="${node.key}">${node.value}</option>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</select>
				</div>
			</td>
			<td width="15%">节点执行人</td>
			<td width="35%">
				<input type="hidden" id="lastDestTaskId" name="lastDestTaskId" value="">
				<div style="padding:1px;">
				 	<t:orgMulSelect displayName="freeUserSelectName"  hiddenName="freeUserSelectId" multiples="true"></t:orgMulSelect>
				</div>
			</td>
		</tr>
		<c:if test="${not empty nodeSet.backType}">
		<tr>
			<td width="15%">回退方式</td>
			<td colspan="3">
				<c:if test="${fn:indexOf(nodeSet.backType,'1')!=-1}">
					<input name="backType" value="1" type="radio"/>回退
				</c:if>
				<c:if test="${fn:indexOf(nodeSet.backType,'2')!=-1}">
					<input name="backType" value="2"type="radio"/>反馈
				</c:if>
			</td>
		</tr>
		</c:if>
		<tr>
			<td width="15%">回退原因</td>
			<td class="value" colspan="3">
					<textarea id="back_reason" name="backReason" style="width:98%;border:0px" rows="5"></textarea>
				</td>
		</tr>
	</table>
</form>
