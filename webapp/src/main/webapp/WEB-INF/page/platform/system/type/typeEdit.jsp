<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:formvalid formid="formobj" gridId="sysTypeList" callback="refreshTypeTree" action="typeController.do?saveOrUpdate" tiptype="5">
		<input id="id" name="id" type="hidden" value="${typePage.id }">
		<input id="parent.id" name="parent.id" type="hidden" class="inputxt" value='${typePage.parent.id}'>
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>名称:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" onblur="wirteCode();" datatype="*1-50" type="text" class="inputxt" value='${typePage.name}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>编码:
					</label>
				</td>
				<td class="value">
				    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.system.type.entity.TypeEntity" oldValue='${typePage.code}' value='${typePage.code}'>
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td class="td_title"> -->
<!-- 					<label class="Validform_label"> -->
<!-- 						<span style="color:red">*</span>类型: -->
<!-- 					</label> -->
<!-- 				</td> -->
<!-- 				<td class="value"> -->
<%-- 				    <t:comboBox id="sysType" name="sysType" multiple="false" dictCode="systemType" value="${typePage.sysType}"></t:comboBox> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						描述:
					</label>
				</td>
				<td class="value">
					<textarea id="remark" name="remark" style="width: 96%;" rows="5" >${typePage.remark}</textarea>
				</td>
			</tr>
	</table>
</t:formvalid>
 <script type="text/javascript">
 	$(function(){
 		var typeParentType = $("#typeParentType").val();
 		if(typeParentType != ''){
 			$.parser.onComplete = function(){
 				$('#sysType').combobox('select', typeParentType);
 			} 
 		}
 		var typeParentId = $("#typeParentId").val();
 		var id = '${typePage.id}';
 		if(typeParentId != '' && id == ''){
 			document.getElementById("parent.id").value = typeParentId;
 		}
 	});
 	
 	function wirteCode(){
 		var name = $("#name").val();
 		$.ajax({
			url : 'typeController.do?wirteCode&name='+name,
			type : 'post',
			data : {
				name : name
			},
			cache : false,
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					var msg = d.msg;
					$("#code").val(msg);
				}
			}
		});
 	}
 </script>
  
  