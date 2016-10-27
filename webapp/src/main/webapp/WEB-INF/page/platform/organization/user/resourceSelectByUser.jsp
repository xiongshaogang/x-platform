<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
input.checkbox_vertical {
	vertical-align: -2px;
	margin-right: 5px;
}

span.checkbox_span {
	display: inline-block;
	width: 110px;
	text-align: left
}
</style>
<c:forEach items="${resourceList}" var="resource1">
	<c:if test="${fn:contains(resourceAuthorityIds, resource1.id)}">
		<span class="checkbox_span"><input class="checkbox_vertical" type="checkbox" name="resourceCheckbox"
			value="${resource1.id}" checked="checked" />${resource1.name}</span>
	</c:if>
	<c:if test="${!fn:contains(resourceAuthorityIds, resource1.id)}">
		<span class="checkbox_span"><input class="checkbox_vertical" type="checkbox" name="resourceCheckbox"
			value="${resource1.id}" />${resource1.name}</span>
	</c:if>
	<br>
</c:forEach>
<script type="text/javascript">
	function resourceSubmit() {
		var typeId = "${typeId}";
		var userId = "${userId}";
		var resourceIds = "";
		$("input[name='resourceCheckbox']").each(function(i) {
			if (this.checked) {
				resourceIds += this.value + ",";
			}
		});
		resourceIds = escape(resourceIds);
		doSubmit("typeController.do?updateUserFileTypeAuthority&typeId=" + typeId + "&userId=" + userId
				+ "&resourceIds=" + resourceIds);
	}
</script>
