<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

  <script type="text/javascript">
	  $(function(){
		   if(!$("#moduleId").val()){
			   $("#moduleId").val($("#pmoduleId").val());
		   }
		   $("#moduleCode").val($("#pmoduleCode").val());
	  });
  </script>

  <t:formvalid formid="formobj" gridId="resourceList" action="resourceController.do?saveResource" >
	<input id="id" name="id" type="hidden" value="${resource.id }">
	<input id="moduleId" name="moduleId" type="hidden" value="${resource.module.id }">
	<input id="moduleCode" name="moduleCode" type="hidden">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>资源名称:
				</label>
			</td>
			<td class="value">
			    <input name="name" dataType="zh" type="text" class="inputxt" value='${resource.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>资源编码:
				</label>
			</td>
			<td class="value">
			    <input name="code" dataType="*" type="text" class="inputxt" value='${resource.code}' uniquemsg='资源编码已存在' oldValue='${resource.code}' entityName="com.xplatform.base.orgnaization.resouce.entity.ResourceEntity" ajaxurl="commonController.do?checkUnique">
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>操作类型:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="optType" id="optType" dictCode="optType" datatype="*" value="${resource.optType}" ></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>操作url:
				</label>
			</td>
			<td class="value">
			    <input name="url" dataType="*" type="text" class="inputxt" value='${resource.url}' uniquemsg='公共资源url已存在' oldValue='${resource.url}' ajaxurl="resourceController.do?checkUnique"/>
			</td>
		</tr>
		<%-- <tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>资源类型:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="filterType" datatype="*" id="filterType" data='[{"id":"module","text":"模块"},{"id":"common","text":"公共"}]' value="${resource.filterType}" ></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>是否权限拦截:
				</label>
			</td>
			<td class="value">
				<t:comboBox datatype="*" name="isInterceptor" id="isInterceptor" data='[{"id":"Y","text":"是"},{"id":"N","text":"否"}]' value="${resource.isInterceptor}" ></t:comboBox>
			</td>
		</tr> --%>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value">
			    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${resource.description}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>