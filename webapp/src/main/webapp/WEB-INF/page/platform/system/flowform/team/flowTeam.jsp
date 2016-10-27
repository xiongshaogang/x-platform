<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <script type="text/javascript">
 
 $(function(){
	 $.parser.onComplete = function() {
		 $('#fieldList').panel({   
			href:'flowTableController.do?getData&tableId=${flowTable.id }',   
			onLoad:function(){   
				loadData();
	    	}   
		}); 
	     $.parser.onComplete = mainComplete;
	  };
 });
 
 function loadData(){
	 var team = '${flowTable.team}';
	 if(team == ''){
		 addTeam();
	 }else{
	 var d = $.parseJSON(team);
	 for(var i=0;i<d.team.length;i++){
		 addTeam();
		 var index = parseInt($("#index").val())-1;
		 $("#teamName"+index).val(d.team[i].teamName);
		for(var j =0;j<d.team[i].teamField.length;j++){
			 $("#teamField"+index).append("<option value='"+d.team[i].teamField[j].fieldDesc+"'>"+d.team[i].teamField[j].fieldName+"</option>");
			 $("#"+d.team[i].teamField[j].fieldName).attr("style","color:red;");
		 } 
	 }
	 }
 }
 function selectField(fieldName,fieldDes){
	 var id = $("#table_id").val();
	 if(id==""){
		 alertTip("请选择分组");
		 return false;
	 }
	 var num = id.replace("team","");
	 
	 var selectArr = [];
	 $("#teamFormobj").find("select").each(function(){
		 $(this).find("option").each(function(){
			 selectArr.push(this.value);
		 });
	 });
	 if($.inArray(fieldDes, selectArr) != -1){
		 alertTip("该字段已被添加到分组，请另选择");
		 return false;
	 }
	 $("#teamField"+num).append("<option value='"+fieldDes+"'>"+fieldName+"</option>"); 
	 $("#"+fieldName).attr("style","color:red;");
 }
 function addTeam(){
	 var index = $("#index").val();
	 var str = '<table id="team'+index+'" cellpadding="0" cellspacing="1" class="formtable" style="margin-right: 250px;" onclick="setMouse(this)">';
	 str +='<tr><td class="td_title"><label class="Validform_label"><span style="color:red">*</span>分组名称:</label></td><td class="value" colspan="1" style="width: 300px;">';
	 str +='<input id="teamName'+index+'" name="teamName'+index+'" type="text" class="inputxt"   /><span ><a href="#" style="float: right;" onclick="removeTeam(\'team'+index+'\');">删除</a></span>';
	 str +='</td></tr><tr><td class="td_title"><label class="Validform_label"><span style="color:red">*</span>分组字段:</label></td><td class="value" colspan="1" ><div style="float: left;">';
     str +='<select size="10" style="width:200px;height:100px;display:inline-block;" id="teamField'+index+'" name="teamField'+index+'" ondblclick="removeOpt(this)"  ></select>';
     str +='</div></td></tr></table>';
     $("#teamFormobj").append(str);
     $("#index").val(parseInt(index)+1);
 }
 
 function removeTeam(id){
	 $("#"+id).remove();
 }
 
 function removeOpt(obj){
	 var text = $("#"+obj.id+" option[value='"+obj.value+"']").text();
	 $("#"+obj.id+" option[value='"+obj.value+"']").remove();
	 $("#"+text).attr("style","");
 }
 
 function setMouse(obj){
	 $("#table_id").val(obj.id);
	 $("#teamFormobj").find("table").each(function(){
		 $(this).find("select").attr("style","width:200px;height:100px;display:inline-block;");
	 });
	 $("#"+obj.id).find("select").attr("style","width:200px;height:100px;display:inline-block;border-color: red;");
 }
 
 function saveData(){
	 var flag = true;
	 $("#teamFormobj").find("table").each(function(){
		 if($(this).find("input[name^='teamName']").val() == '' || $(this).find("option").length == 0){
			 flag = false;
		 }
	 });
	 if(!flag){
		 alertTip("请把数据填写完整！");
	 }else{
		 var map = [];
		 $("#teamFormobj").find("table").each(function(){
			 var t = {};
			 t['teamName'] =  $(this).find("input[name^='teamName']").val();
			 var arr = [];
			 var tableid = this.id;
			 var index = tableid.replace("team","");
			 $(this).find("option").each(function(){
				 var option = {};
				 option['fieldName'] = $("#teamField"+index+" option[value='"+this.value+"']").text();
				 option['fieldDesc'] = this.value;
				 arr.push(option);
			 });
			 t['teamField'] = arr;
			 map.push(t);
		 });
		 var s ={};
		 s['team']= map
		 $("#team").val(JSON.stringify(s));
	 }
	 return flag;
 }
 </script>
	<div  id="teamFormLayout" class="easyui-layout" style="width:99%;height:99%;margin:4px auto 0px auto" onload="">
	  <div region="west" split="true" title="表字段" style="width: 150px; padding: 1px;" id="fieldList"  ></div>
	  <div region="center" style="padding:0px;" split="true">
	 <t:formvalid formid="teamFormobj"  action="flowTableController.do?saveData"  gridId="flowTableList"  refresh="false" beforeSubmit="saveData()">
	   <input id="id" name="id" type="hidden" value="${flowTable.id}">
	   <input id="index" name="index" value="0" type="hidden"/>
	   <input id="table_id" name="table_id"  type="hidden" style="color: red;"/>
	   <input id="team" name="team"  type="hidden" value=""/>
	   <div >
	  <span class="search_button_area" style="margin-left: 20px;">
	  <a href="#" class="easyui-linkbutton" iconCls="awsm-icon-plus" onclick="addTeam();">添加分组</a>
	  </span>
	  </div>
   </t:formvalid>
	  </div>
	</div>
