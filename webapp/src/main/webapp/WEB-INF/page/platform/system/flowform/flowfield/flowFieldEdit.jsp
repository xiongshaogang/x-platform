<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <script type="text/javascript">
  //编写自定义JS代码
  $(function(){
     if($("#flowtable").val() == ""){
        $("#flowtable").val("${param.flowTableId}");
     }
     setTimeout('InitMirror.initId("jscript")', InitMirror.options.initDelay);
     setTimeout('InitMirror.initId("idScript")', InitMirror.options.initDelay);
     
     $.parser.onComplete = function(){
    	 if('${flowField.valueFrom}' == ''){
        	 $("#valueFrom").combobox("setValue","1");
         }
         if('${flowField.controlType}' == ''){
        	 $("#controlType").combobox("setValue","1");
         }
	$.parser.onComplete=mainComplete;				
	} 
     selectValue();
  });
  
  function selectValue(){
	  var fval = $("#valueFrom").val();
	  var cval = $("#controlType").val();
	  if(fval == '1'){
		  if(cval == '1' || cval =='2'){
			  $("#control").show();
			  $("#verify").show();
			  $("#dcode").hide();
			  $("#script1").hide();
			  $("#script2").hide();
			  $(".sn").hide();
			  
			  $("#dictCode").val("");
			  $("#idScript").val("");
			  $("#jscript").val("");
			  $("#serialNumber").val("");
		  }else if(cval == '3' || cval =='13'){
			  $("#control").show();
			  $("#verify").hide();
			  $("#dcode").show();
			  $("#script1").hide();
			  $("#script2").hide();
			  $(".sn").hide();
			  
			  $("#verifyType").combobox("setValue","");
			  $("#idScript").val("");
			  $("#jscript").val("");
			  $("#serialNumber").val("");
		  }else{
			  $("#control").show();
			  $("#verify").hide();
			  $("#dcode").hide();
			  $("#script1").hide();
			  $("#script2").hide();
			  $(".sn").hide();
			  
			  $("#dictCode").val("");
			  $("#verifyType").combobox("setValue","");
			  $("#idScript").val("");
			  $("#jscript").val("");
			  $("#serialNumber").val("");
		  }
	  }
	  
	  if(fval == '2' || fval == '3'){
		  if(cval == '4' || cval == '5' || cval =='6' || cval == '7' || cval == '8' || cval == '9' || cval == '10' || cval == '11' || cval == '18'){
			  $("#control").show();
			  $("#verify").hide();
			  $("#dcode").hide();
			  $("#script1").show();
			  $("#script2").show();
			  $(".sn").hide();
			  
			  $("#dictCode").val("");
			  $("#verifyType").combobox("setValue","");
			  $("#serialNumber").val("");
		  }else{
			  $("#control").show();
			  $("#verify").hide();
			  $("#dcode").hide();
			  $("#script1").show();
			  $("#script2").hide();
			  $(".sn").hide();
			  
			  $("#dictCode").val("");
			  $("#verifyType").combobox("setValue","");
			  $("#idScript").val("");
			  $("#serialNumber").val("");
		  }
	  }
	  
	  if(fval == '4'){
		  $("#control").hide();
		  $("#verify").hide();
		  $("#dcode").hide();
		  $("#script1").hide();
		  $("#script2").hide();
		  $(".sn").show();
		  
		  $("#controlType").combobox("setValue","");
		  $("#dictCode").val("");
		  $("#verifyType").combobox("setValue","");
		  $("#idScript").val("");
		  $("#jscript").val("");
	  }
  }
  
  function saveScript(){
		$("#idScript").val(InitMirror.getValue("idScript"));
		$("#jscript").val(InitMirror.getValue("jscript"));
	}
  </script>
  <t:formvalid formid="flowFieldFormobj"  action="flowFieldController.do?saveFlowField"  gridId="flowFieldList" beforeSubmit="saveScript">
		<input id="id" name="id" type="hidden" value="${flowField.id }">
		<input id="flowtable" name="table.id" type="hidden" value="${flowField.table.id }">
	   <table  cellpadding="0" cellspacing="1" class="formtable">
	     <tr>
			<td class="td_title"><label class="Validform_label">字段名称:</label></td>
			<td class="value" colspan="1" >
				<input id="fieldName" name="fieldName" type="text" class="inputxt"  datatype="*"   value="${flowField.fieldName}"   />
			</td>
			<td class="td_title"><label class="Validform_label">字段描述:</label></td>
			<td class="value" colspan="1" >
				<input id="fieldDes" name="fieldDes" type="text" class="inputxt"  datatype="*"   value="${flowField.fieldDes}"   />
			</td>
		</tr>
		 <tr>
			<td class="td_title"><label class="Validform_label">字段类型:</label></td>
		    <td class="value" colspan="1">
		        <t:comboBox   multiple="false"  data='[{"id":"string","text":"String"},{"id":"int","text":"Integer"},{"id":"double","text":"Double"},{"id":"Date","text":"Date"},{"id":"BigDecimal","text":"BigDecimal"},{"id":"Text","text":"Text"},{"id":"Blob","text":"Blob"}]'  datatype="*" name="type" id="type" value="${flowField.type}"    ></t:comboBox>
		    </td>
		    <td class="td_title"><label class="Validform_label">长度:</label></td>
			<td class="value" colspan="1" >
				<input id="length" name="length" type="text" class="inputxt"  datatype="n"  value="${flowField.length}"   />
			</td>
		</tr>
		<tr>
		    <td class="td_title"><label class="Validform_label">小数点长度:</label></td>
			<td class="value" colspan="1" >
				<input id="pointLength" name="pointLength" type="text" class="inputxt" datatype="/^\d{0,10}$/"  value="${flowField.pointLength}"  errormsg="请填写数字!"  />
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">选项:</label></td>
			<td class="value" colspan="3" >
			    <label class="Validform_label"> <input type="checkbox" id="isrequired" name="isrequired"  value="1" <c:if test="${flowField.isrequired == '1'}">checked="checked"</c:if>/></label>是否必填&nbsp;&nbsp;&nbsp;
				<label class="Validform_label"> <input type="checkbox" id="islist" name="islist"  value="1" <c:if test="${flowField.islist == '1' || empty flowField.islist}">checked="checked"</c:if> /></label>是否列显示&nbsp;&nbsp;&nbsp;
				<label class="Validform_label"> <input type="checkbox" id="isquery" name="isquery"  value="1" <c:if test="${flowField.isquery == '1'}">checked="checked"</c:if>/></label>是否查询&nbsp;&nbsp;&nbsp;
				<%-- <label class="Validform_label"> <input type="checkbox" id="ishidden" name="ishidden"  value="1" <c:if test="${flowField.ishidden == '1'}">checked="checked"</c:if>/></label>是否隐藏&nbsp;&nbsp;&nbsp; --%>
				<label class="Validform_label"> <input type="checkbox" id="isflowvar" name="isflowvar"  value="1" <c:if test="${flowField.isflowvar == '1'}">checked="checked"</c:if>/></label>是否流程变量
				<%-- <input id="options" name="options" type="text" class="inputxt"    value="${flowField.options}"   /> --%>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label">值来源:</label></td>
			<td class="value" >
			    <t:comboBox   multiple="false"  dictCode="valueFrom"  datatype="*" name="valueFrom" id="valueFrom" value="${flowField.valueFrom}" onSelect="selectValue()"></t:comboBox>
			</td>
			<td class="td_title sn" style="display: none;"><label class="Validform_label">流水号:</label></td>
			<td class="value sn" style="display: none;">
			    <input id="serialNumber" name="serialNumber" type="text" class="inputxt"  value="${flowField.serialNumber}"   />
			</td>
		</tr> 
		 <tr id="control">
			<td class="td_title"><label class="Validform_label">要素类型:</label></td>
		    <td class="value" colspan="3">
		        <t:comboBox   multiple="false"  dictCode="controlType"  name="controlType" id="controlType" value="${flowField.controlType}" onSelect="selectValue()"></t:comboBox>
		    </td>
		</tr>
		<tr id="verify">
			<td class="td_title"><label class="Validform_label">验证规则:</label></td>
		    <td class="value" colspan="3">
		        <t:comboBox   multiple="false"  url="formRuleController.do?getAllRule"  valueField="regulation" textField="name" name="verifyType" id="verifyType" value="${flowField.verifyType}"    ></t:comboBox>
		    </td>
		</tr>
		 <tr id="dcode" style="display: none;">
			<td class="td_title"><label class="Validform_label">数据字典:</label></td>
			<td class="value" colspan="3" >
				<input id="dictCode" name="dictCode" type="text" class="inputxt"    value="${flowField.dictCode}"   />
			</td>
		</tr>
		<tr id="script1" style="display: none;">
			<td width="40px" align="right"><label class="Validform_label"> 脚本：</label></td>
			<td class="value" colspan="3" >
			<div style="background: #F7F7F7;width: 90%">
		    <button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('jscript')">常用脚本</button>
			<textarea codemirror="true" mirrorheight="90px" id="jscript" name="jscript" class="input_area">${flowField.jscript}</textarea>
			</div>
			</td>
		</tr>
		<tr id="script2" style="display: none;">
			<td width="40px" align="right"><label class="Validform_label"> ID脚本：</label></td>
			<td class="value" colspan="3" >
			<div style="background: #F7F7F7;width: 90%">
		    <button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('idScript')">常用脚本</button>
			<textarea codemirror="true" mirrorheight="90px" id="idScript" name="idScript" class="input_area">${flowField.idScript}</textarea>
			</div>
			</td>
		</tr>
	  </table>
   </t:formvalid>
