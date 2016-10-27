<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<form id="nodeUserChoiceData">
<table class="borderTable" style="width: 99.9%">
		<thead>
		<tr>
			<th height="28" width="30%">执行路径</th>
			<c:if test="${canChoicePath}">
				<th height="28" width="30%">选择同步条件后的执行路径</th>
			</c:if>
			<th width="40%">目标任务</th>
		</tr>
		</thead>		
		<c:forEach items="${nodeTranUserList}" var="nodeTranUser" varStatus="i">
			<tr>
				<td height="28" width="18%" nowrap="nowrap">
					<c:if test="${selectPath==1 }">
						<input type="radio" name="destTask" value="${nodeTranUser.nodeId}" <c:if test="${i.count==1}">checked="checked"</c:if> />
					</c:if>
					${nodeTranUser.nodeName}<!-- 跳转的目标节点 -->
				</td>
				<c:if test="${canChoicePath}">
				<td>
					<c:forEach items="${nodeTranUser.nextPathMap}" var="nextPath">
						<div>
							<label><input type="checkbox" name="nextPathId" value="${nextPath.key}"/>
							${nextPath.value}</label>
						</div>
					</c:forEach>
				</td>
				</c:if>
				<td>
					<c:forEach items="${nodeTranUser.nodeUserMapSet}" var="nodeUserMap">
						<div>
							${nodeUserMap.nodeName}
							<input type="hidden" name="lastDestTaskId" value="${nodeUserMap.nodeId}"/>
							<c:set var="userTranId" value=""></c:set>
							<c:set var="userTranName" value=""></c:set>
							<c:forEach items="${nodeUserMap.taskExecutors}" var="executor">
								<c:choose>
									<c:when test='${empty userTranId}'>
										<c:set var="userTranId" value="${executor.executeId}^^${executor.executor}^^${executor.type}"></c:set>
									</c:when>
									<c:otherwise>
										<c:set var="userTranId" value="${userTranId},${executor.executeId}^^${executor.executor}^^${executor.type}"></c:set>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test='${empty userTranName}'>
										<c:set var="userTranName" value="${executor.executor}"></c:set>
									</c:when>
									<c:otherwise>
										<c:set var="userTranName" value="${userTranName},${executor.executor}"></c:set>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<div style="margin:1px">
								<t:orgMulSelect displayName="userTypeName" hiddenName="${nodeUserMap.nodeId}_userId" multiples="true" displayValue="${userTranName}" hiddenValue="${userTranId}"></t:orgMulSelect>
							</div>
						</div>
					</c:forEach>
				</td> 
			</tr>
		</c:forEach>
	</table>
</form>