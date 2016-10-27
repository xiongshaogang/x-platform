<#if isManage==0 >
	<#if isSignTask&&isAllowDirectExecute >
	<div class="flow_button_group flow_button_check">
		特权：<input type="checkbox" value="1" id="chkDirectComplete" class="flow_button_checkbox">直接结束
	</div>
	</#if>
	<#if isTransfer><div class="flow_button_group"><a class="flow_button" href="#" onclick="nextProcess(\&apos;${taskId}\&apos;,${nextProcessBefore},${nextProcessAfter})">流转</a></div></#if>
	<div class="flow_button_group flow_button_more"><a href="javascript:void(0)" class="easyui-menubutton flow_button" data-options="menu:\&apos;#flowButtonMore\&apos;,iconCls:\&apos;icon-edit\&apos;,plain:true ">更多</a></div>
	<div id="flowButtonMore" style="width:120px;" >
		<#if isSignTask&&isAllowRetoactive>
		<div onclick="showAddSignWindow(\&apos;${taskId}\&apos;)">加签</div>
		</#if>
		<#if isCanAssignee>
		<div onclick="changeAssignee(\&apos;${taskId}\&apos;,${changeAssigneeBefore},${changeAssigneeAfter})">任务转办</div>
		</#if>
		<#if isCanJump>
		<div onclick="choosePath(\&apos;${taskId}\&apos;,\&apos;${jumpType}\&apos;)">选择路径</div>
		</#if>
		<#if isCanBack>
		<div onclick="rejectTask(\&apos;${taskId}\&apos;,${rejectTaskBefore},${rejectTaskAfter})">退回上一步</div>
		</#if>
		<#if isFinishedDivert>
		<div onclick="divertTask(\&apos;${actInstId}\&apos;)">转发</div>
		</#if>
		<#if isCanRedo>
		<div onclick="redoTask(\&apos;${actInstId}\&apos;,${redoTaskBefore},${redoTaskAfter})">任务追回</div>
		</#if>
		<#if isCanRecover>
		<div onclick="recoverTask(\&apos;${actInstId}\&apos;,${recoverTaskBefore},${recoverTaskAfter})">撤销</div>
		</#if>
		<div class="menu-sep"></div> 
		<div onclick="showTaskUserDlg(\&apos;${actInstId}\&apos;)">流程图</div>
		<div onclick="showTaskOpinions(\&apos;${actInstId}\&apos;)">审批记录</div>
	</div>
<#else>
	<#if isSignTask&&isAllowDirectExecute >
	<div class="flow_button_group flow_button_check">
		特权：<input type="checkbox" value="1" id="chkDirectComplete" class="flow_button_checkbox">直接结束
	</div>
	</#if>
	<#if isTransfer><div class="flow_button_group"><a class="flow_button" href="#" onclick="nextProcess(\&apos;${taskId}\&apos;,${nextProcessBefore},${nextProcessAfter})">流转</a></div></#if>
	<div class="flow_button_group flow_button_more"><a href="javascript:void(0)" class="easyui-menubutton flow_button" data-options="menu:\&apos;#flowButtonMore\&apos;,iconCls:\&apos;icon-edit\&apos;,plain:true">更多</a></div>
	<div id="flowButtonMore" style="width:120px;" >
		<#if isSignTask&&isAllowRetoactive>
		<div onclick="showAddSignWindow(\&apos;${taskId}\&apos;)">加签</div>
		</#if>
		<#if isCanBack>
		<div onclick="rejectTask(\&apos;${taskId}\&apos;,${rejectTaskBefore},${rejectTaskAfter})">退回上一步</div>
		<div onclick="rejectTaskToStart(\&apos;${taskId}\&apos;,${rejectTaskToStartBefore},${rejectTaskToStartAfter})">退回到发起人</div>
		</#if>
		<#if isFinishedDivert>
		<div onclick="divertTask(\&apos;${actInstId}\&apos;)">转发</div>
		</#if>
		<#if isCanRedo>
		<div onclick="redoTask(\&apos;${actInstId}\&apos;,${redoTaskBefore},${redoTaskAfter})">任务追回</div>
		</#if>
		<#if isCanRecover>
		<div onclick="recoverTask(\&apos;${actInstId}\&apos;,${recoverTaskBefore},${recoverTaskAfter})">撤销</div>
		</#if>
		<div onclick="endAllTask(\&apos;${taskId}\&apos;)">终止</div>
		<div onclick="choosePath(\&apos;${taskId}\&apos;,\&apos;1,2,3\&apos;)">选择路径</div>
		<div class="menu-sep"></div> 
		<div onclick="showTaskUserDlg(\&apos;${actInstId}\&apos;)">流程图</div>
		<div onclick="showTaskOpinions(\&apos;${actInstId}\&apos;)">审批记录</div>
	</div>
</#if>