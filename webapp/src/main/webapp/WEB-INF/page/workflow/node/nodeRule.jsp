<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.formtable .td_title{
	width:100px;
	height:30px;
	text-align:right;
	/*padding-left:5px;*/
}
td{
	border:0px;
}
</style>
<script type="text/javascript">
$(function(){
	var myButton = {
			id : 'add',
			text : '新增',
			iconCls : 'awsm-icon-plus',
			operationCode : "definitionManager_nodeRule_other",
			handler : function() {
				$("#ruleStatus").val("1");
				$("#name").val("");
				$("#id").val("");
				$("#condition").val("");
				InitMirror.setValue("condition","");
				$("#targetNodeId").val("");
				$("#description").val("");
			}
		};
	addButton(getD($("#nodeRuleDiv")),myButton,1);
	setTimeout('InitMirror.initId("condition")', InitMirror.options.initDelay);
});
function addNodeRule(data){
	if($("#ruleStatus").val()==1 || '${NodeRule}' == ''){
		var spanContent = '<span style="margin-left: 10px;" id="'+ data.attributes.NodeRule.id +'">' +  
		'<a href="#" onclick="checkNodeRule(\''+ data.attributes.NodeRule.id +'\');">' + data.attributes.NodeRule.name+'</a>'
		+ '<a href="#1" style="float: right;" onclick="removeNodeRule(\'' + data.attributes.NodeRule.id
		+ '\');"><i style="margin-right: 10px;" class="awsm-icon-remove red"></i></a></span>';
		$("#nodeRuleList").append(spanContent);
	}else{
		$("#nodeRuleList span").filter("#" + data.obj.NodeRule.id).find("a").eq("0").html(data.obj.NodeRule.name);
	}
	$("#ruleStatus").val("0");
	$("#id").val(data.obj.NodeRule.id);
}
//删除触发方法
function removeNodeRule(nodeRuleId) {
	$.messager.confirm('提示信息', '确认删除该记录?', function(r) {
		if (r) {
			$.ajax({
				url : "nodeRuleController.do?delete",
				type : 'post',
				data : {
					id : nodeRuleId
				},
				cache : false,
				success : function(data) {
					var d = $.parseJSON(data);
					var msg = d.msg;
					$.messager.show({
						title : '提示信息',
						msg : msg,
						timeout : 1000 * 6
					});
				}
			});
			$("#nodeRuleList span").filter("#" + nodeRuleId).remove();
		}
	});
}
function checkNodeRule(nodeRuleId){
	$.messager.progress({
		text : '数据加载中....',
		interval : 200
	});
	$.ajax({
		url : "nodeRuleController.do?getNodeRule",
		type : 'post',
		data : {
			id : nodeRuleId
		},
		cache : false,
		success : function(data) {
			var data = $.parseJSON(data);
			$("#ruleStatus").val("0");
			$("#id").val(data.obj.NodeRule.id);
			$("#name").val(data.obj.NodeRule.name);
			$("#condition").val(data.obj.NodeRule.condition);
			InitMirror.setValue("condition",data.obj.NodeRule.condition)
			$("#targetNodeId").val(data.obj.NodeRule.targetNodeId);
			$("#description").val(data.obj.NodeRule.description);
		}
	});
	$.messager.progress('close');
}

function saveNodeRuleScript(){
	$("#condition").val(InitMirror.getValue("condition"));
}
</script>
<div id="nodeRuleDiv" class="easyui-layout" style="width:98%;height:100%;margin:0px auto 0px auto">
	<div region="east" split="true" title="规则列表" style="width:140px;" id="nodeRuleList">
		<c:forEach var="nodeRule" items="${NodeRuleList}">
			<span id="${nodeRule.id}" style="margin-left: 10px;"><a href="#" onclick="checkNodeRule('${nodeRule.id }')">${nodeRule.name}</a> <a style="float: right;" href="#1"
				onclick="removeNodeRule('${nodeRule.id}');"><i style="margin-right: 10px;" class="awsm-icon-remove red"></i></a></span><br/>
		</c:forEach>
	</div>
	<div region="center" split="true"  style="overflow: auto;">
	<t:formvalid formid="formobj" action="nodeRuleController.do?saveNodeRule" tiptype="5" callback="addNodeRule" afterSaveClose="false" beforeSubmit="saveNodeRuleScript">
	  <input type="hidden" name="ruleStatus" value="0" id="ruleStatus"/>
	  <input type="hidden" name="id" value="${NodeRule.id}" id="id"/>
	  <input type="hidden" name="actDefId" value="${param.actDefId}" />
	  <input type="hidden" name="nodeId" value="${param.nodeId}" />
	  <input type="hidden" name="defId" value="${param.defId}" />
	  <table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td class="td_title"><label class="Validform_label"> 当前节点名称：</label></td>
			<td class="value" >
				${nodeName}
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 后续节点</label></td>
			<td class="value" >
				<c:forEach items="${nextNodes}" var="node" varStatus="status">
					${node.nodeName } (${node.nodeId })
					<c:if test="${! status.last }">,</c:if>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 规则名称</label></td>
			<td class="value" >
				<input type="text" class="inputxt" id="name" name="name" value="${NodeRule.name }" datatype="*">
			</td>
		</tr>
		<tr>	
			<td class="td_title"><label class="Validform_label"> 跳转节点名称</label></td>
			<td class="value" >
				<select name="targetNodeId" id="targetNodeId">
						<c:forEach items="${activityList}" var="item">
							<optgroup label="${item.key}">
								<c:forEach items="${item.value}" var="node">
									<option value="${node.key},${node.value}">${node.value}</option>
								</c:forEach>
							</optgroup>
						</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 规则表达式</label></td>
			<td class="value" >
				<div style="background: #F7F7F7;">
					<button type="button" class="btn btn-primary btn-xs" onclick="openScriptDialog('condition')">常用脚本</button>
					<textarea codemirror="true" mirrorheight="90px" id="condition" name="condition" class="input_area">${NodeRule.condition }</textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td class="td_title"><label class="Validform_label"> 备注</label></td>
			<td class="value" >
				<textarea name="description" id="description" class="input_area" style="height:50px">${NodeRule.description }</textarea>
			</td>
		</tr>
		</table>
    </t:formvalid>
	</div>
	</div>
