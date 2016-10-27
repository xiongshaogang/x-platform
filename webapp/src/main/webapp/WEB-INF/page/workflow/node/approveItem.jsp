<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(function() {
		var nodeId ='${nodeId}';
		var isGlobal ='${isGlobal}';
		if(isGlobal=="Y"){
			checkIsCommon("N");
			$("#isGlobal").attr("checked",'true');
		}else{
			checkIsCommon("Y");
		}
	});
	
	// 设置是否全局
	function checkIsCommon(value){
		if(value=="Y"){//不是全局
			$('#trTaskNode').show();
			$('#isGlobal').attr('value',"N");
			var nodeExp = $('#tmpNodeExp').attr('value');//.replaceAll("{,}","\r\n");
			$('#approvalItem').val(nodeExp);
		}else{//是全局
			$('#trTaskNode').hide();
			$('#isGlobal').attr('value',"Y");
			var defExp = $('#tmpDefExp').attr('value');//.replaceAll("{,}","\r\n");
			$('#approvalItem').val(defExp);
		}
	}
	
	// 节点ID
	function changeNodeId(value){
		
		$('#nodeId').attr('value',value);
		var url="approveItemController.do?get";
	 	var para=$('#taskApprovalItemsForm').serialize();
    	$.post(url,para,function(result){
    			$('#tmpNodeExp').attr('value',result);
    			$('#approvalItem').val(result);
    		},"text"
    	);
    	
    	/* $.ajax({
  		   type: "POST",
  		   url: url,
  		   dataType: "text"
  		   data: para,
  		   success: function(message){
  			   $('#tmpNodeExp').attr('value',message);
     		   $('#approvalItem').val(message);
  		   }
  		}); */
	}
	
	$('#defNode').combobox({
		onChange: function(newValue, oldValue){
			changeNodeId(newValue);
		}
	});

</script>
<style>
	td{
		border:0px;
	}
</style>
<t:formvalid formid="taskApprovalItemsForm" action="approveItemController.do?saveApproveItem">
	<input type="hidden" id="tmpDefExp" name="tmpDefExp" value="${defExp}" />
	<input type="hidden" id="tmpNodeExp" name="tmpNodeExp" value="${nodeExp}" />
	<input type="hidden" id="actDefId" name="actDefId" value="${actDefId}" />
	<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" />
	<input type="hidden" id="setId" name="setId" value="${setId}" />
	
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					是否全局:
				</label>
			</td>
			<td class="value">
			    <input type="checkbox" id="isGlobal" name="isGlobal" value="N" onclick="checkIsCommon(value)"/>
			</td>
		</tr>
		<tr id="trTaskNode">
			<td class="td_title">
				<label class="Validform_label">
					任务节点:
				</label>
			</td>
			<td class="value">
				<t:comboBox id="defNode" name="defNode" data='${nodeMap}' value="${nodeId}"></t:comboBox>
			</td>
		</tr>
		
		<tr>
			<td class="td_title">
				<label class="Validform_label">
					常用语:
				</label>
			</td>
			<td class="value">
				<textarea rows="4" cols="50" id="approvalItem" name="approvalItem" 
					style="margin-top: 5px;margin-bottom: 5px;">${nodeExp}</textarea>
			</td>
		</tr>
	</table>
</t:formvalid>