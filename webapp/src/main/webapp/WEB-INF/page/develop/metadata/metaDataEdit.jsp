<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
  $(function(){
	   var typeId=$("#typeId").val();
 	   if(typeId==""){
 		  $("#typeId").val($("#ptypeId").val());
 	   }
  });
 </script>
<t:formvalid action="metaDataController.do?saveOrUpdata" callback="metadata_edit.reflash" afterSaveClose="false" beforeSubmit="metadata_edit.setFieldData">
	<input id="id" name="id" type="hidden" value="${metaData.id}">
	<input id="typeId" name="typeId" type="hidden" value="${metaData.typeId}">
	<input type="hidden" id="insertedRows" name="insertedRows" value="" />
	<input type="hidden" id="deletedRows" name="deletedRows" value="" />
	<input type="hidden" id="updatedRows" name="updatedRows" value="" />
	<t:collapseTitle id="div1" title="数据表配置" doSize="false">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="td_title"><label class="Validform_label"> 表名</label></td>
				<td class="value"><input class="inputxt" type="text" id="tableName" name="tableName" value="${metaData.tableName}"
					<c:if test="${not empty metaData.tableName}">readonly="readonly"</c:if> datatype="*" entityName="com.xplatform.base.develop.metadata.entity.MetaDataEntity"
					oldValue="${metaData.tableName}" ajaxurl="commonController.do?checkUnique" nullmsg="请输入表名！"></td>
				<td class="td_title"><label class="Validform_label"> 主键策略</label></td>
				<td class="value"><t:comboBox
						data='[{"id":"UUID","text":"UUID(36位唯一编码)"},{"id":"NATIVE","text":"NATIVE(自增长方式)"},{"id":"SEQUENCE","text":"SEQUENCE(序列方式)"}]'
						value="${metaData.jformPkType}" name="jformPkType" id="jformPkType"></t:comboBox></td>
			</tr>
			<tr>
				<td class="td_title" id="jformPkSequenceN" <c:if test="${metaData.jformPkType ne 'SEQUENCE'}">style="display: none;"</c:if>><label
					class="Validform_label"> 序列名</label></td>
				<td class="value" id="jformPkSequenceV" <c:if test="${metaData.jformPkType ne 'SEQUENCE'}">style="display: none;"</c:if>><input id="jformPkSequence"
					name="jformPkSequence" type="text" class="inputxt" value="${metaData.jformPkSequence}" /> <label class="Validform_label" style="display: none;">
						序列名</label></td>
			</tr>
			<tr>
				<td class="td_title"><label class="Validform_label"> 表描述 </label></td>
				<td class="value"><input type="text" class="inputxt" id="content" name="content" value="${metaData.content}" datatype="s2-100" ignore="ignore"></td>
				<td class="td_title"><label class="Validform_label">表类型</label></td>
				<td class="value"><t:comboBox dictCode="tableType" value="${metaData.jformType}" name="jformType" id="jformType"></t:comboBox></td>
			</tr>
			<c:if test="${metaData.jformType eq '2'}">
				<tr id="fb_tb">
					<td align="right"><label class="Validform_label">附表</label></td>
					<td class="value" colspan="3"><input class="inputxt" style="width: 440px" disabled="disabled" value="${metaData.subTableStr}"></td>
				</tr>
			</c:if>
			<tr>
				<td class="td_title"><label class="Validform_label"> 版本号</label></td>
				<td class="value"><input type="text" class="inputxt" id="jformVersion" name="jformVersion" value="${metaData.jformVersion}" readonly="readonly"></td>
			</tr>
		</table>
	</t:collapseTitle>
	<t:collapseTitle id="div2" title="表字段配置" >
		<t:datagrid name="metaDataFieldList" editable="true" fit="tue" border="0" defaultOpt="true" fitColumns="true"
			actionUrl="metaDataFieldController.do?datagrid&tableId=${metaData.id}" pagination="false">
			<t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="表id" field="table_id" hidden="false"></t:dgCol>
			<t:dgCol title="修改前字段名" field="oldFieldName" hidden="false"></t:dgCol>
			<t:dgCol title="主表id" field="mainField" hidden="false"></t:dgCol>
			<t:dgCol title="字段排序" field="orderNum" hidden="false"></t:dgCol>
			<t:dgCol title="主表名" field="mainTable" hidden="false"></t:dgCol>

			<t:dgCol title="字段名" field="fieldName" width="140" editType="validatebox" editorParams='{required:true}'></t:dgCol>
			<t:dgCol title="字段描述" field="content" width="140" editType="textarea"></t:dgCol>
			<t:dgCol title="字段长度" field="length" width="75" editType="validatebox"></t:dgCol>
			<t:dgCol title="字段精度" field="pointLength" width="75" editType="validatebox"></t:dgCol>
			<t:dgCol title="默认值" field="fieldDefault" width="75" editType="text"></t:dgCol>
			<t:dgCol title="字段类型" field="type" width="75" align="center" editType="combobox"
				editorParams='{required:true,data:[{"id":"string","text":"String"},{"id":"int","text":"Integer"},{"id":"double","text":"Double"},{"id":"Date","text":"Date"},{"id":"BigDecimal","text":"BigDecimal"},{"id":"Text","text":"Text"},{"id":"Blob","text":"Blob"}]}'></t:dgCol>
			<%-- <t:dgCol title="是否主键"  field="isKey"   width="75" align="center" editType="combobox" dictCode="YNType" editorParams='{required:true}'></t:dgCol> --%>
			<t:dgCol title="是否可为空" field="isNull" width="75" replace="是_Y,否_N" align="center" editType="combobox" dictCode="YNType" editorParams='{required:true}'></t:dgCol>
			<t:dgCol title="操作" field="opt" width="85" align="center"></t:dgCol>
			<t:dgDelOpt title="删除" url="cgFormFieldController.do?delete&id={id}" operationCode="metadataManager_formfieldDelete_delete" />
			<t:dgToolBar title="查看"  url="metaDataFieldController.do?metaDataFieldEdit" funname="detail" buttonType="GridDetail" height="400"
				width="700"></t:dgToolBar>
		</t:datagrid>
	</t:collapseTitle>
</t:formvalid>

<script type="text/javascript">
	$(function() {
		var queryParams = $("#metaDataFieldList").datagrid('options').queryParams;
		//添加上左树的ID进行过滤
		queryParams["tableId"] = '${metadata.id}';
		queryParams["jformVersion"] = '${metadata.jformVersion}';
		$("#metaDataFieldList").datagrid("reload");
	});
	var metadata_edit = {
		reflash : function(data) {
			$("#metadataList").datagrid("reload");
			//获得右列表的查询参数
			var queryParams = $("#metaDataFieldList").datagrid('options').queryParams;
			//添加上左树的ID进行过滤
			debugger;
			queryParams["tableId"] = data.obj.tableId;
			$("#id").val(data.obj.tableId);
			$("#jformVersion").val(data.obj.jformVersion);
			$("#metaDataFieldList").datagrid("reload");
		},
		setFieldData : function() {
			var editData = getEditData("metaDataFieldList");
			if (!editData) {
				return false;
			}
			$("#insertedRows").val(editData.insertedRows);
			$("#updatedRows").val(editData.updatedRows);
			$("#deletedRows").val(editData.deletedRows);
		}
	};
</script>
