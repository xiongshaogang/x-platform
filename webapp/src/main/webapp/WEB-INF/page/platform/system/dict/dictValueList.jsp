<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <script type="text/javascript">
	function clickTree(node){
		$("#pparentId").val(node.id);
	 	$('#dictValueTree').tree('select', node.target);
	 	$("#dictValueList").datagrid("reload",{parentId:node.id});
	 	$("#dictValueList").datagrid("getPanel").panel("setTitle",node.text);
	}
	function refreshTree(){
		$('#dictValueTree').tree('reload');
	}
 </script>
	<div  id="dictValueLayout" class="easyui-layout" style="width:100%;height:100%;">
	  <div region="west" split="true" title="字典值树" style="width:200px;">
	   	<t:tree id="dictValueTree" gridTreeFilter="parentId"
			url="dictValueController.do?tree&typeId=${typeId}" gridId="dictValueList"
			clickPreFun="clickTree(node)">
		</t:tree>
	  </div>
	  <div region="center" style="padding:0px;border:0px;" split="true">
		  <t:datagrid name="dictValueList" checkbox="true" fitColumns="true" title="数据字典值" actionUrl="dictValueController.do?datagrid&typeId=${typeId}" idField="id" fit="true" queryMode="group">
			   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
			   <t:dgCol title="字典值名称"  field="name" query="true"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
<%-- 			   <t:dgCol title="字典值编码"  field="code" query="true"  hidden="true"  queryMode="single"  width="80"></t:dgCol> --%>
			   <t:dgCol title="数据值"  field="value"  hidden="true"  queryMode="single"  width="100"></t:dgCol>
			   <t:dgCol title="排序号"  field="orderby"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
			   <t:dgCol title="描述"  field="description"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
			   <t:dgCol title="操作" field="opt" width="160"></t:dgCol>
		  
		  	   <t:dgOpenOpt operationCode="dictManager_editDictValue_edit" title="编辑" icon="glyphicon glyphicon-pencil icon-color" preinstallWidth="1" height="450" url="dictValueController.do?dictValueEdit&id={id}" exParams="{optFlag:'add'}"></t:dgOpenOpt>
	   		   <t:dgOpenOpt operationCode="dictManager_viewDictValue_view" title="查看" icon="glyphicon glyphicon-search icon-color" preinstallWidth="1" height="450" url="dictValueController.do?dictValueEdit&id={id}&optCode=detail" exParams="{optFlag:'detail'}" ></t:dgOpenOpt>
			   <t:dgDelOpt operationCode="dictManager_deleteDictValue_delete" title="删除" icon="glyphicon glyphicon-remove icon-color" callback="refreshTree"  url="dictValueController.do?delete&ids={id}" />
				
			   <t:dgToolBar operationCode="dictManager_addDictValue_add" preinstallWidth="1" height="450" title="新增" icon="glyphicon glyphicon-plus icon-color" url="dictValueController.do?dictValueEdit&typeId=${typeId}" funname="add"></t:dgToolBar>
			   <t:dgToolBar operationCode="dictManager_batchDeleteDictValue_add" title="批量删除" callback="refreshTree" icon="glyphicon glyphicon-trash icon-color" url="dictValueController.do?delete" funname="deleteALLSelect"></t:dgToolBar>
		  </t:datagrid>
	  </div>
	</div>
<input type="hidden" id="pparentId">
<input type="hidden" id="ptypeId" value="${typeId}">
