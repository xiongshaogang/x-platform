<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript">
	$(function() {
		if (!$("#parentId").val()) {
			$("#parentId").val($("#pparentId").val());
		}
		if (!$("#typeId").val()) {
			$("#typeId").val($("#ptypeId").val());
		}
	});
</script>
<t:formvalid callback="refreshTree" formid="formobj" gridId="dictValueList"
	action="dictValueController.do?saveDictValue">
	<input id="id" name="id" type="hidden" value="${dictValue.id }">
	<input id="parentId" name="parentId" type="hidden" value="${dictValue.parent.id }">
	<input id="typeId" name="typeId" type="hidden" value="${dictValue.dictType.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>字典值名称:
			</label></td>
			<td class="value"><input id="name" name="name" datatype="*1-50" type="text" class="inputxt"
				value='${dictValue.name}'></td>
		</tr>
		<!-- 		<tr> -->
		<!-- 			<td class="td_title"> -->
		<!-- 				<label class="Validform_label"> -->
		<!-- 					<span style="color:red">*</span>字典值编码: -->
		<!-- 				</label> -->
		<!-- 			</td> -->
		<!-- 			<td class="value"> -->
		<%-- 			    <input id="code" name="code" type="text" class="inputxt" datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.system.dict.entity.DictValueEntity" oldValue='${dictValue.code}' value='${dictValue.code}'> --%>
		<!-- 			</td> -->
		<!-- 		</tr> -->
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>字典值:
			</label></td>
			<td class="value"><input id="value" name="value" datatype="*1-32" type="text" class="inputxt"
				value='${dictValue.value}' ajaxurl="dictValueController.do?checkUnique&dictTypeId=${typeId}" /></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> <span style="color: red">*</span>排序号:
			</label></td>
			<td class="value"><input id="orderby" name="orderby" datatype="n1-32" type="text" class="inputxt"
				value='${dictValue.orderby}'></td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 扩展属性1:</label></td>
			<td class="value"><input id="extend1" name="extend1" type="text" class="inputxt" value='${dictValue.extend1}'>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 扩展属性2:</label></td>
			<td class="value"><input id="extend2" name="extend2" type="text" class="inputxt" value='${dictValue.extend2}'>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 扩展属性3:</label></td>
			<td class="value"><input id="extend3" name="extend3" type="text" class="inputxt" value='${dictValue.extend3}'>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 描述: </label></td>
			<td class="value"><textarea name="description" type="text" class="input_area">${dictValue.description}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>
