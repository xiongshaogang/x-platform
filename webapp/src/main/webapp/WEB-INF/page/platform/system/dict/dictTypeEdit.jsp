<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<t:formvalid formid="formobj" gridId="dictTypeList" action="dictTypeController.do?saveDictType">
	<input id="id" name="id" type="hidden" value="${dictType.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
			<tr> 
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>类型名称:
					</label>
				</td>
				<td class="value">
				    <input id="name" name="name" datatype="*1-50" type="text" class="inputxt" value='${dictType.name}'>
				
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>类型编码:
					</label>
				</td>
				<td class="value">
				     <input id="code" name="code" type="text" <c:if test="${dictType.code!=null}">disabled="disabled"</c:if>  datatype="*1-32" ajaxurl="commonController.do?checkUnique" entityName="com.xplatform.base.system.dict.entity.DictTypeEntity" oldValue='${dictType.code}' class="inputxt" value='${dictType.code}'>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>字典类型:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="type" id="type" value="${dictType.type}" datatype="*" data='[{"id":"system","text":"系统"},{"id":"user","text":"用户"}]'></t:comboBox>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						<span style="color:red">*</span>显示类型:
					</label>
				</td>
				<td class="value">
					<t:comboBox name="valueType" id="valueType" datatype="*" value="${dictType.valueType}" data='[{"id":"selected","text":"下拉框"},{"id":"checkbox","text":"复选框"},{"id":"tree","text":"树结构"}]'></t:comboBox>
				</td>
			</tr>
			<tr>
				<td class="td_title">
					<label class="Validform_label">
						描述:
					</label>
				</td>
				<td class="value">
					<textarea name="description" type="text" class="input_area">${dictType.description}</textarea>
				</td>
			</tr>
	</table>
</t:formvalid>