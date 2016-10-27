<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <script type="text/javascript">
  //编写自定义JS代码
  $(function(){
   $.parser.onComplete = function() {
	 var queryParams = $("#flowFieldList").datagrid('options').queryParams;
	 queryParams["flowTable_id"] = '${flowTable.id}';
	 $("#flowFieldList").datagrid("reload");
	 setMainTable();
     $.parser.onComplete = mainComplete;
   };
  });	
  function reflash(data){
	  $("#flowTableList").datagrid("reload");
	  //获得列表的查询参数
	  var queryParams=$("#flowFieldList").datagrid('options').queryParams;
	  queryParams["flowTable_id"] = data.obj;
	  $("#id").val(data.obj);
	  $("#flowFieldList").datagrid("reload");
  }
  
  function reflashFlowField(){
	  var queryParams = $("#flowFieldList").datagrid('options').queryParams;
	  queryParams["flowTable_id"] = $("#id").val();
	  $("#flowFieldList").datagrid("reload");
  }
  function checkMain(){
	 if($("#id").val() == ""){
		 tip('请先保存主表信息');
		 return false;
	 }else{
		 return true;
	 } 
  }
  
  function addFlowField(title, addurl, gridID, width, height, preinstallWidth, exParams) {
	    if(!checkMain()){
	    	return ;
	    }
		var defaultOptions = {
			optFlag : 'add'
		};
		addurl += '&optFlag=add&flowTableId='+$("#id").val();
		var options = $.extend({}, defaultOptions, exParams);
		createwindow(title, addurl, width, height, preinstallWidth, options);
  }
  
  function setMainTable(){
	  var val = $("input[name='isMainTable']:checked").val();
	  if(val == '0'){
		  $(".main").show();
	  }else{
		  $("#mainTable").combobox("setValue","");
		  $("#relation").val("");
		  $(".main").hide();
	  }
  }
  </script>
 <div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;width:100%;height:100%;border:0px;overflow: hidden;">
  <t:formvalid formid="flowTableFormobj"  action="flowTableController.do?saveFlowTable"  gridId="flowTableList" callback="reflash"  refresh="false" afterSaveClose="false">
	   <input id="id" name="id" type="hidden" value="${flowTable.id }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
		 <tr>
		    <td class="td_title"><label class="Validform_label"><span style="color:red">*</span>表名:</label></td>
			<td class="value" colspan="1" >
				<input id="tableName" name="tableName" type="text" class="inputxt"  datatype="*"  value="${flowTable.tableName}"   />
			</td>
			<td class="td_title"><label class="Validform_label"><span style="color:red">*</span>注释:</label></td>
			<td class="value" colspan="1" >
				<input id="content" name="content" type="text" class="inputxt"  tableName  value="${flowTable.content}"   />
			</td>
		</tr>
		 <tr>
		    <td class="td_title"><label class="Validform_label"><span style="color:red">*</span>是否主表:</label></td>
			<td class="value" colspan="1" >
				<label class="Validform_label"> <input type="radio" name="isMainTable" value="1" onclick="setMainTable()" <c:if test="${flowTable.isMainTable == '1' || empty flowTable.isMainTable}">checked="checked"</c:if>/></label>主表
				<label class="Validform_label"> <input type="radio" name="isMainTable" value="0" onclick="setMainTable()" <c:if test="${flowTable.isMainTable == '0' }">checked="checked"</c:if>/></label>从表
			</td>
			<td class="td_title main"><label class="Validform_label"><span style="color:red">*</span>主表:</label></td>
			<td class="value main" colspan="1" >
				<t:comboBox url="flowTableController.do?getMainTable" textField="tableName" valueField="tableName"
							value="${flowTable.mainTable}" name="mainTable" id="mainTable"></t:comboBox>
			</td>
		</tr>
		<tr>
			<td class="td_title main"><label class="Validform_label"><span style="color:red">*</span>外键:</label></td>
			<td class="value main" colspan="1" >
			<input id="relation" name="relation" type="text" class="inputxt"  tableName  value="${flowTable.relation}"   />
			</td>
		</tr>
	  </table>
   </t:formvalid>
   </div>
   <div region="south" style="padding: 1px;width:100%;height:400px;padding:4px 4px 0px 4px;border:0px;overflow: hidden;">
        <t:datagrid name="flowFieldList"   checkbox="true" fitColumns="true" title="字段列表" actionUrl="flowFieldController.do?datagrid" idField="id" fit="true"  queryMode="group"    pagination="true"  autoLoadData="false" >
	   <t:dgCol title="id"  field="id" hidden="false"  width="120"></t:dgCol>
	   <t:dgCol title="列名"  field="fieldName" hidden="true"  width="120"></t:dgCol>
	   <t:dgCol title="注释"  field="filedDes" hidden="false"  width="120"></t:dgCol>
	   <t:dgCol title="注释"  field="team" hidden="false"  width="120"></t:dgCol>
	   <t:dgCol title="类型"  field="type" hidden="true"  width="120"></t:dgCol>
	   <t:dgCol title="是否必填"  field="isrequired" hidden="true"  width="120" replace="是_1,否_0"></t:dgCol>
	   <t:dgCol title="是否列表显示"  field="isList" hidden="true"  width="120" replace="是_1,否_0"></t:dgCol>
	   <t:dgCol title="是否流程变量"  field="isflowvar" hidden="true"  width="120" replace="是_1,否_0"></t:dgCol>
	   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	   <t:dgDelOpt title="删除" url="flowFieldController.do?delete&id={id}" callback="reflashFlowField" />
	   <t:dgToolBar title="录入" icon="awsm-icon-plus" url="flowFieldController.do?flowFieldEdit" funname="add" buttonType="GridAdd" width="800" height="600" exParams="{formId:'flowFieldFormobj'}" onclick="addFlowField('录入','flowFieldController.do?flowFieldEdit','flowFieldList',800,600,null,{formId:'flowFieldFormobj'})"></t:dgToolBar>
	   <t:dgToolBar title="编辑" icon="awsm-icon-edit" url="flowFieldController.do?flowFieldEdit" funname="update" buttonType="GridUpdate" width="800" height="600" exParams="{formId:'flowFieldFormobj'}"></t:dgToolBar>
	   <t:dgToolBar title="批量删除"  icon="awsm-icon-remove" url="flowFieldController.do?batchDelete" funname="deleteALLSelect" buttonType="GridDelMul" callback="reflashFlowField"></t:dgToolBar>
	   <t:dgToolBar title="查看" icon="awsm-icon-search" url="flowFieldController.do?flowFieldEdit" funname="detail" buttonType="GridDetail" width="800" height="600" exParams="{formId:'flowFieldFormobj'}"></t:dgToolBar>
	  </t:datagrid>
   </div>
   </div>
