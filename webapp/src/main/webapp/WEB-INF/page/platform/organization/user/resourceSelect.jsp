<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<c:forEach items="${resourceList}" var="resource">
	<c:if test="${fn:contains(resourceAuthorityIds, resource.id)}">
		<span class="icon group_add">&nbsp;</span>
		<input style="width: 20px;" type="checkbox" name="resourceCheckbox" value="${resource.id}" checked="checked" />${resource.name}
	 </c:if>
	<c:if test="${!fn:contains(resourceAuthorityIds, resource.id)}">
		<span class="icon group_add">&nbsp;</span>
		<input style="width: 20px;" type="checkbox" name="resourceCheckbox" value="${resource.id}" />${resource.name}
	 </c:if>
	<br>
</c:forEach>
<script type="text/javascript">
function submitresource() {
	var moduleId = "${moduleId}";
	var userId = $("#rid").val();
	var resourceIds = "";
	$("input[name='resourceCheckbox']").each(function(i){
		   if(this.checked){
			   resourceIds+=this.value+",";
		   }
	 });
	resourceIds=escape(resourceIds); 
	doSubmit("userController.do?updateResourceAuthority&moduleId=" + moduleId + "&userId=" + userId+"&resourceIds="+resourceIds);
}
</script>
