<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

 <script type="text/javascript">
  $(function(){
	   var moduleId=$("#id").val();
	   var parentId=$("#parentId").val();
	   //新增一级菜单的页面才出现子系统选择(现已取消)
// 	   if(parentId==""){
		   var subsystemTr=$("#subsystemTr");
		   subsystemTr.show();
// 	   }
  });
 </script>

 <t:formvalid callback="reflashTree" formid="formobj" action="moduleController.do?saveModule" gridId="moduleList">
	<input id="id" name="id" type="hidden" value="${module.id }">
	<input id="parentId" name="parentId" type="hidden" value="${parentId}">
	<table  cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>模块名称:
				</label>
			</td>
			<td class="value">
			    <input datatype="zh" name="name" type="text" class="inputxt" value='${module.name}'>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>模块编码:
				</label>
			</td>
			<td class="value">
			    <input datatype="s1-50" name="code" type="text" class="inputxt" value='${module.code}'  uniquemsg='模块编码已存在' oldValue='${module.code}' entityName="com.xplatform.base.orgnaization.module.entity.ModuleEntity" ajaxurl="commonController.do?checkUnique">
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>模块url:
				</label>
			</td>
			<td class="value">
			    <input name="url" type="text" class="inputxt" value='${module.url}'/>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>排序值:
				</label>
			</td>
			<td class="value">
			    <input name="orderby" datatype="n1-3" type="text" class="inputxt" value='${module.orderby}'/>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					<span style="color:red">*</span>是否框架:
				</label>
			</td>
			<td class="value">
				<t:comboBox name="isIframe" id="isIframe" datatype="*" data='[{"id":"Y","text":"是"},{"id":"N","text":"否"}]' value="${module.isIframe}" ></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					模块图标:
				</label>
			</td>
			<td class="value">
				<input name="iconCls" type="text" class="inputxt" value='${module.iconCls}'/>
			</td>
		</tr>
		<tr id="subsystemTr" style="display:none;">
			<td class="td_title">
				<label class="Validform_label">
					子系统选择:
				</label>
			</td>
			<td class="value">
				<t:comboBox id="subsystem" name="subsystem" dictCode="subsystem" value='${module.subsystem}' textname="subSystemName"></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					手机显示:
				</label>
			</td>
			<td class="value">
				<t:comboBox id="phoneShow" name="phoneShow" dictCode="YNType" value='${module.phoneShow}'></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					备注:
				</label>
			</td>
			<td class="value">
			    <textarea style="height:80px" id="description" name="description" type="text" class="input_area">${module.description}</textarea>
			</td>
		</tr>
				
	</table>
</t:formvalid>
