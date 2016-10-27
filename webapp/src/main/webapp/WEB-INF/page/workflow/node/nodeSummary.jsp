<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>
.green-set {
  color: green;
  font-size: large;
}
.red-set {
  color: red;
  font-size: large;
}
.normal-set {
  color: black;
  font-size: large;
}
</style>
<script type="text/javascript">
function startScript(nodeId,defId,actDefId){
	createwindow("流程事件脚本设置", "nodeScriptController.do?nodeScript&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId+"&type=startEvent", null, 350, 1,{optFlag:'add'});
}
function endScript(nodeId,defId,actDefId){
	createwindow("流程事件脚本设置", "nodeScriptController.do?nodeScript&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId+"&type=endEvent", null, 350, 1,{optFlag:'add'});
}

function taskScript(nodeId,defId,actDefId){
	createwindow("流程事件脚本设置", "nodeScriptController.do?nodeScript&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId+"&type=userTask", null, 350, 1,{optFlag:'add'});
}
function approveItem(nodeId,defId,actDefId){
	createwindow("常用语设置", "approveItemController.do?approveItem&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId, null, 300, 1,{optFlag:'add',formId:'taskApprovalItemsForm'});	
}
function nodeRule(nodeId,defId,actDefId,deployId,nodeName){
    createwindow("跳转规则设置", "nodeRuleController.do?NodeRule&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId+"&deployId="+deployId+"&nodeName="+nodeName, null, 500, 2,{optFlag:'add'});
}
function taskDue(nodeId,defId,actDefId){
createwindow("任务催办设置", "taskDueController.do?taskDue&nodeId="+nodeId+"&defId="+defId+"&actDefId="+actDefId, null, 500, 2,{optFlag:'add'});
}
function chooseForm(){
	$("#flowSetting").find("ul").find("li").eq("4").click();
}
</script>
	<form method="post" >
		<input type="hidden" name="defId" value="${defId}" />
		<input type="hidden" name="nodeId" value="${nodeId}" />
		<div style="width:99%;margin:0 auto;">
		<div class="panel-header" style="margin-top:5px;width:98%">
			图示：<span class="green-set">√</span>：表示已经设置 &nbsp;&nbsp;&nbsp;&nbsp;
			<span class="red-set" >×</span>：表示未设置&nbsp;&nbsp;&nbsp;&nbsp;
			<span class="normal-set" >-</span>：表示没有该功能&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" class="awsm-icon-edit" title="设置" ></a>：表示可以对该功能进行设置
		</div>
		<table class="datagrid-body" cellspacing="0" cellpadding="0" style="border:1px solid #ccc;border-top:none;margin-bottom:5px;width: 100%">
				<tr  style="border-top:none;border-right:1px solid #ccc;" align="center">
					<td class="datagrid-header"  align="center" rowspan="2">序号</td>
					<td class="datagrid-header" rowspan="2" >节点名称</td>
					<td class="datagrid-header" rowspan="2" colspan="2">人员设置</td>
					<td class="datagrid-header" rowspan="2" colspan="2">常用语设置</td>
					<td class="datagrid-header"   colspan="5">流程事件</td>
					<td class="datagrid-header"   rowspan="2" colspan="2">流程规则</td>
					<td class="datagrid-header"   rowspan="2" colspan="2">表单设置</td>
					<td class="datagrid-header"   rowspan="2" colspan="2">操作按钮</td>
					<td class="datagrid-header"   rowspan="2" colspan="2">催办设置</td>
				</tr>
				<tr>
					<td  style="text-align:center;" class="datagrid-header">前置</td>
					<td  style="text-align:center;" class="datagrid-header">后置</td>
					<td  style="text-align:center;" class="datagrid-header">分配</td>
				    <td  style="text-align:center;" class="datagrid-header" colspan="2">开始</br>结束</td>
				</tr>
				
				<!-- <tr>
					<td width="15px" nowrap="nowrap"  rowspan="2" style="text-align: center;font-weight: bold;" class="header"  >序号</td>
					<td rowspan="2" style="text-align: center;font-weight: bold;" class="header ">节点名称</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">人员设置</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">常用语设置</td>
					<td colspan="5" style="text-align: center;font-weight: bold;" class="header">流程事件</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">流程规则</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">表单设置</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">操作按钮</td>
					<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">催办设置</td>
				</tr> 
				<tr>
					<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">前置</td>
					<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">后置</td>
					<td nowrap="nowrap" style="text-align: center;font-weight: bold;"class="header">分配</td>
				    <td nowrap="nowrap" style="text-align: center;font-weight: bold;"class="header">开始</br>结束</td>
					<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">&nbsp;</td>
				</tr>-->
				<tr align="center">
					<td>-</td>
					<td>全局设置</td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td>
						<c:choose>
							<c:when test="${globalApprovalMap['global']}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set" >×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="approveItem('','${defId}','${actDefId}')"></a>
					</td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td>
						<c:choose>
							<c:when test="${formMap['global']}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td><a href="#" class="awsm-icon-edit" title="设置" onclick="chooseForm()"></a></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
				</tr>
				<tr align="center">
					<td>-</td>
					<td>开始节点</br>(${startFlowNode.nodeName})</td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td>
						<c:choose>
							<c:when test="${startScriptMap[startFlowNode.nodeId]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" 
						onclick="startScript('${startFlowNode.nodeId}','${defId}','${actDefId}')"></a>
					</td>
			
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td>
						<c:choose>
							<c:when test="${buttonMap[startFlowNode.nodeId]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td><a href="#" class="awsm-icon-edit" title="设置" onclick="BpmNodeButtonWindow({defId:'${defId}',nodeId:'',callback:function(){window.location.reload()}})"></a></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
				</tr>
				<c:forEach items="${endFlowNodeList}" var="endFlowNode">
				<tr align="center">
					<td>-</td>
					<td>结束节点</br>(${endFlowNode.nodeName})</td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td>
						<c:choose>
							<c:when test="${endScriptMap[endFlowNode.nodeId]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>	
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="endScript('${endFlowNode.nodeId}','${defId}','${actDefId}')"></a>
					</td>	
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
					<td><span class="normal-set">-</span></td>
				</tr>
				</c:forEach>
			<c:forEach items="${nodeSetList}" var="nodeSet" varStatus="i">
				<tr align="center">
					<td>${i.count}</td>
					<td>${nodeSet.nodeName}</br>(${nodeSet.nodeId})</td>
					
					<!-- 人员设置 -->
					<td>
						<c:choose>
							<c:when test="${nodeUserMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="UserSetWindow({defId:'${defId}',nodeId:'${nodeSet.nodeId}',callback:function(){window.location.reload()}});"></a>
					</td>
					<!-- 常用语 -->
					<td>
						<c:choose>
							<c:when test="${taskApprovalItemsMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="approveItem('${nodeSet.nodeId}','${defId}','${actDefId}')"></a>
					</td>
					
					<!-- 流程事件-前置脚本 -->
					<td>	
						<c:choose>
							<c:when test="${preScriptMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<!-- 流程事件-后置脚本 -->
					<td>	
						<c:choose>
							<c:when test="${afterScriptMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					
					<!-- 流程事件-分配脚本 -->
					<td>	
						<c:choose>
							<c:when test="${assignScriptMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<span class="normal-set">-</span>
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置"  onclick="taskScript('${nodeSet.nodeId}','${defId}','${actDefId}')"></a>
					</td>
					<!-- 流程规则 -->
					<td>	
						<c:choose>
							<c:when test="${nodeRulesMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="nodeRule('${nodeSet.nodeId}','${defId}','${nodeSet.actDefId}','${deployId}','${nodeSet.nodeName}')"></a>
					</td>
					<!-- 流程表单 -->
					<td>	
						<c:choose>
							<c:when test="${bpmFormMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="chooseForm()"></a>
					</td>
					
					<!-- 操作按钮-->
					<td>	
						<c:choose>
							<c:when test="${nodeButtonMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="BpmNodeButtonWindow({defId:'${defId}',nodeId:'${nodeSet.nodeId}',callback:function(){window.location.reload()}})"></a>
					</td>
					
					<!-- 催办设置-->
					<td>	
						<c:choose>
							<c:when test="${taskReminderMap[nodeSet.id]}">
								<span class="green-set">√</span>
							</c:when>
							<c:otherwise>
								<span class="red-set">×</span>
							</c:otherwise>
						</c:choose> 
					</td>
					<td>
						<a href="#" class="awsm-icon-edit" title="设置" onclick="taskDue('${nodeSet.nodeId}','${defId}','${actDefId}')"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		</div>
		<div style="height: 40px"></div>
	</form>
