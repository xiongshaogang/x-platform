<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center"  style="padding:0px;border:0px;">
  <t:formvalid usePlugin="password"  layout="table" tiptype="5" action="fieldController.do?saveField"   beforeSubmit="setFieldData" refresh="false" afterSaveClose="false" >
	<input type="hidden" id="datagridList" name="datagridList" value=""/>
	<input type="hidden" id="updatedRows" name="updatedRows" value=""/>
</t:formvalid>
  <t:datagrid name="fieldList"   checkbox="true" fitColumns="false" actionUrl="fieldController.do?datagrid&datasourceId=${ datasourceId}"  queryMode="group" editable="true">
   <t:dgCol title="主键"  field="id" hidden="false"   ></t:dgCol>
   <t:dgCol title="字段名称"  field="name" hidden="true"  width="90"  editType="validatebox" editorParams='{required:true}'></t:dgCol>
   <t:dgCol title="显示名称"  field="showName" hidden="true"  width="90"  editType="validatebox" editorParams='{required:true}'></t:dgCol>
   <t:dgCol title="字段类型"  field="type" hidden="true"  width="90"  editType="combobox" editorParams='{data:[{"id":"string","text":"String"},{"id":"int","text":"Integer"},{"id":"double","text":"Double"},{"id":"Date","text":"Date"},{"id":"BigDecimal","text":"BigDecimal"},{"id":"Text","text":"Text"},{"id":"Blob","text":"Blob"}],valueField:"id",textField:"text"}'></t:dgCol>
   <t:dgCol title="排序号"  field="num" hidden="false"  width="90"  ></t:dgCol>
   <t:dgCol title="字段描述"  field="description" hidden="false"  width="90"  ></t:dgCol>
   <t:dgCol title="字段是否显示"  field="isshow" hidden="true"  width="90" editType="combobox"  dictCode="YNType" ></t:dgCol>
   <t:dgCol title="是否求和"  field="issum" hidden="false"  width="90"   editType="combobox"  dictCode="YNType"></t:dgCol>
   <t:dgCol title="是否查询"  field="issearch" hidden="true"  width="90"   editType="combobox"  dictCode="YNType"></t:dgCol>
   <t:dgCol title="字段查询要素"  field="searchActivex" hidden="true"  width="90"  editType="combobox"  dictCode="searchType" editorParams="{onSelect:function (record){chooseCondition(record);}}"></t:dgCol>
   <t:dgCol  title="查询条件"  field="searchCondition" hidden="true"  width="90"  editType="combobox"  dictCode="searchCondition"  ></t:dgCol>
   <t:dgCol title="数据字典值"  field="dictCode" hidden="true"  width="90"  editType="validatebox"  ></t:dgCol>
   <t:dgCol title="是否X轴"  field="isx" hidden="true"  width="90"  editType="combobox"  dictCode="YNType"></t:dgCol>
   <t:dgCol title="是否Y轴"  field="isy" hidden="true"  width="90"  editType="combobox"  dictCode="YNType"></t:dgCol>
   <t:dgCol title="数据源ID"  field="datasourceId" hidden="false"  width="90"  ></t:dgCol>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 function reflash(){
	  $("#fieldList").datagrid("reload");
  }
   $(function(){
	 redrawEasyUI($(".easyui-layout"));
 });
   
   function setFieldData(){
		  if(!getEditData("fieldList")){
			  return false;
		  }
		  $("#updatedRows").val(getEditData("fieldList").updatedRows);
		  $("#datagridList").val(JSON.stringify($("#fieldList").datagrid("getRows")));
	  }
   
   
   
   
   function chooseCondition(record){
	   
	   if(record.id =='combobox' || record.id=='combotree'){
		   $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"dictCode"}).target.validatebox({
			   required:true ,
			   missingMessage:"字典值Code不能为空"  
		  });
		  $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox("clear");
		  $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox("disable");
	   }else{
		   $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"dictCode"}).target.validatebox({
			   required:false ,
		  });
		  $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox("enable");
	   }
	   
	   
	   /* var fieldOption=$("#fieldList").datagrid("getColumnOption","searchCondition");
	   if(record.id =='text' || record.id=='datebox' || record.id=='numberbox'){
		 // console.info($("#fieldList").datagrid("options"));
		 // console.info($("#fieldList").datagrid("getColumnOption","searchCondition"));
		  $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox({
			    mode:'remote',
			    panelWidth:'auto',
			    panelHeight:'auto',
				url:'fieldController.do?getComboxData&dictCode=searchCondition' ,
				valueField:'id' ,
				textField:'text' ,
		  });
	   }else{
		   $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.validatebox({
			   required:true ,
			   missingMessage:"字典值Code不能为空"
		   });
	   } */
	   
	   
	   /* else if(record.id =='combobox'){
		   $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox({
			    mode:'remote',
			    panelHeight:'auto',
				url:'fieldController.do?getComboxData&dictCode=all&type=selected' ,
				valueField:'id' ,
				textField:'text' ,
		  });
	   }else if(record.id =='combotree'){
		   $("#fieldList").datagrid("getEditor",{index:fieldListeditIndex,field:"searchCondition"}).target.combobox({
			    mode:'remote',
			    panelHeight:'auto',
				url:'fieldController.do?getComboxData&dictCode=all&type=tree' ,
				valueField:'id' ,
				textField:'text' ,
		  });
	   } */
   }
 </script>