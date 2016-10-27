<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var nodeId = "${nodeId}",
		deployId = "${deployId}",
		defId = "${defId}",
		selectNodeId="${selectCanChoicePathNodeId}";
	$(function() {
		$("a[name='btnVars']").click(selectVar);
		$("#btnScript").click(selectScript);
		$("#canChoicePath").click(function(){
			var targ=$("#canChoicePathNodeId");
			if($(this).attr("checked")){
				targ.removeClass("hidden");
				InitMirror.each(function(e){
					e.setCode('NextPathId.contains("'+e.targetId+'")');
				});
			}
			else{
				targ.addClass("hidden");
				InitMirror.each(function(e){
					e.setCode('');
				});
			}
		});	
		if(selectNodeId){
			$("#canChoicePathNodeId").val(selectNodeId);			
		}
		$("a[name='signResult']").click(function() {
			addToTextarea($(this).attr("result"));
		});
		$("a.save").click(saveCondition);
	});
	var flowVarWindow;
	//选择变量
	function selectVar() {
		FlowVarWindow({
			deployId : deployId,
			nodeId : nodeId,
			callback : function(vars) {
				addToTextarea(vars);
			}
		});
	};
	//将条件表达式追加到脚本输入框内
	function addToTextarea(str){		
		InitMirror.editor.insertCode(str);		
	};
	
	function selectScript() {
		ScriptDialog({
			callback : function(script) {
				addToTextarea(script);
			}
		});
	};
	function handFlowVars(obj) {
		addToTextarea($(obj).val());
	};
	function saveCondition() {
		InitMirror.save();
		var tasks = [];
		var conditions = [];		
		$("tr.taskTr > td").each(function(){
			var condition=$("[name='condition']", $(this)).val();
			var task=$("[name='task']", $(this)).val(); 
			tasks.push(task);
			conditions.push(condition);
		});
		
		var canChoicePathNodeId=$("#canChoicePathNodeId:visible").val();
		
		var url = __ctx + "/platform/bpm/bpmDefinition/saveCondition.ht";
		var paras = {
			"defId" : defId,
			"nodeId" : nodeId,
			"tasks" : tasks.join('#split#'),
			"conditions" : conditions.join('#split#'),
			"canChoicePathNodeId":canChoicePathNodeId
		};

		$.post(url, paras, function(data) {
			var resultObj = new com.hotent.form.ResultMessage(data);
			if (resultObj.isSuccess()) {
				$.ligerDialog.success("编辑规则成功!","提示信息", function() {
					window.close();
				});
			} else {
				$.ligerDialog.warn("编辑规则失败,请检查条件表达式是否正确!","提示信息");
			}
		});
	};
</script>
<style>

</style>
<t:formvalid formid="formobj" beforeSubmit="setConditionValue" action="definitionController.do?saveCondition">
	  <input type="hidden" name="deployId" value="${deployId}" />
	  <input type="hidden" name="nodeId" value="${nodeId}" />
	  <input type="hidden" name="defId" value="${defId}" />
	  <input type="hidden" name="tasks" id="tasks"/>
	  <input type="hidden" name="conditions"  id="conditions"/>
		<table cellpadding="0" cellspacing="1" class="formtable" >
			<tr>
				<td class="td_title"><label class="Validform_label"> 条件表达式：</label></td>
				<td>
					<div style="margin: 8px 0; ">
						<a href="#" id="btnScript" class="link var" title="常用脚本">常用脚本</a>
						&nbsp;&nbsp;表单变量:
						<c:if test="${ifInclusiveGateway}">
							&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="checkbox" id="canChoicePath" <c:if test="${selectCanChoicePathNodeId!=null}">checked="checked"</c:if> />可以选择条件同步路径</label>&nbsp;
							<select  <c:if test="${selectCanChoicePathNodeId==null}">class="hidden"</c:if> id="canChoicePathNodeId">
								<c:forEach items="${incomeNodes}" var="incomeNode">
									<option value="${incomeNode.nodeId}">${incomeNode.nodeName}</option>
								</c:forEach>
							</select>
						</c:if>
					</div> 
					<c:forEach items="${incomeNodes}" var="inNode">
						<div style="padding: 4px;">
							<c:choose>
								<c:when test="${inNode.isMultiple==true}">
									<a href="#1" name="signResult"
										result='signResult_${inNode.nodeId}=="pass"'>[${inNode.nodeName}]投票通过</a>
												&nbsp;
												<a href="#2" name="signResult"
										result='signResult_${inNode.nodeId}=="refuse"'>[${inNode.nodeName}]投票不通过</a>
								</c:when>
								<c:otherwise>
									<a href="#1" name="signResult"
										result="approvalStatus_${inNode.nodeId}==1">[${inNode.nodeName}]-通过</a>
												&nbsp;
												<a href="#2" name="signResult"
										result="approvalStatus_${inNode.nodeId}==2">[${inNode.nodeName}]-反对</a>
								</c:otherwise>
							</c:choose><br>
							1.先选中下方的脚本输入框，然后再插入条件表达式。</br>
							2.表达式中不能有分号或return语句。
							
														
						</div>
					</c:forEach>							
				</td>
			</tr>
			<c:forEach items="${outcomeNodes}" var="outNode">
				<tr class="taskTr" style="padding:2px">
					<td class="td_title"><label class="Validform_label"> ${outNode.nodeName }：</label></td>
					<td class="value" style="margin:1px">
					<div style="background:#f7f7f7;">
						<input type="hidden" name="task" value="${outNode.nodeId}" />
						<textarea codemirror="true" mirrorheight="70px" id="${outNode.nodeId}" name="condition" style="width:300px">${outNode.condition}</textarea>
					</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	
</t:formvalid>
<script type="text/javascript">
	$(function(){
		setTimeout(InitMirror.init,InitMirror.options.initDelay);
	});
	function setConditionValue(){
		InitMirror.save();
		var tasks = [];
		var conditions = [];		
		$("tr.taskTr > td").each(function(){
			var condition=$("[name='condition']", $(this)).val();
			var task=$("[name='task']", $(this)).val(); 
			tasks.push(task);
			conditions.push(condition);
		});
		$("#tasks").val(tasks.join('#split#'));
		$("#conditions").val(conditions.join('#split#'));
	}
</script>
