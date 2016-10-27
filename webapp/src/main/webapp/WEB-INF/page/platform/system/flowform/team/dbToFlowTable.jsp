<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
$(function(){
	$.parser.onComplete = function(){
		setMainTable();
	$.parser.onComplete=mainComplete;				
	} 
});
function setMainTable(){
	  var val = $("input[name='isMainTable']:checked").val();
	  if(val == '0'){
		  $("#mainTable").combobox({
			  disabled : false
		  });
		  $("#relation").attr("disabled",false);
	  }else{
		  $("#mainTable").combobox("setValue","");
		  $("#mainTable").combobox({
			  disabled : true
		  });
		  $("#relation").attr("disabled",true);
	  }
}
function saveData(){
	flag = true;
	var rowsData = $("#dbList").datagrid('getSelections');
	
	if (!rowsData || rowsData.length == 0) {
		alertTip('请选择生成表');
		flag = false;
	}
	if (rowsData.length > 1) {
		alertTip('请选择一条记录再生成');
		flag = false;
	}
	if(flag){
		$("#tableName").val(rowsData[0].id);
	}else{
		return false;
	}
}
</script>
<div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;width:100%;height:100%;border:0px;overflow: hidden;">
  <t:formvalid formid="addTableFormobj"  action="flowTableController.do?addTable"  gridId="flowTableList" beforeSubmit="saveData()">
	   <input id="tableName" name="tableName" type="hidden" value="">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		 <tr>
		    <td class="td_title"><label class="Validform_label"><span style="color:red">*</span>是否主表:</label></td>
			<td class="value" colspan="1" >
				<label class="Validform_label"> <input type="radio" name="isMainTable" value="1" onclick="setMainTable()" <c:if test="${flowTable.isMainTable == '1' || empty flowTable.isMainTable}">checked="checked"</c:if>/></label>主表
				<label class="Validform_label"> <input type="radio" name="isMainTable" value="0" onclick="setMainTable()" <c:if test="${flowTable.isMainTable == '0' }">checked="checked"</c:if>/></label>从表
			</td>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>主表:</label></td>
			<td class="value " colspan="1" >
				<t:comboBox url="flowTableController.do?getMainTable" textField="tableName" valueField="tableName"
							value="${flowTable.mainTable}" name="mainTable" id="mainTable"></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title "><label class="Validform_label"><span style="color:red">*</span>外键:</label></td>
			<td class="value" colspan="3">
			<input id="relation" name="relation" type="text" class="inputxt"  tableName  value="${flowTable.relation}" />
			</td>
		</tr>
	  </table>
   </t:formvalid>
   </div>
   <div region="south" style="padding: 1px;width:100%;height:400px;padding:4px 4px 0px 4px;border:0px;overflow: hidden;">
    <t:datagrid name="dbList" title="数据库表列表"  pagination="false" checkbox="true" fit="true" queryMode="group" actionUrl="flowTableController.do?dbdatagrid" idField="id" sortName="id" >
		<t:dgCol title="表名" field="id" query="true" width="300"></t:dgCol>
	</t:datagrid>
   </div>
   </div>
