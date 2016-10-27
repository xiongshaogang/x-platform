<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
${content}
<c:if test="${from.messageType == 'annunciate'}">
<c:set var="index" value="0"></c:set>
<span style="margin-left: 70px;">&nbsp;&nbsp;附件：</span>
<c:forEach var="item" items="${dataVoList }">
<c:set var="index" value="${index + 1 }"></c:set>
<span id="${item.id}"><a onclick="common_downloadFile('${item.id}')" href="#">${item.name };</a>&nbsp;&nbsp;</span>
</c:forEach>
<c:if test="${index == 0 }">
无
</c:if>
</c:if>