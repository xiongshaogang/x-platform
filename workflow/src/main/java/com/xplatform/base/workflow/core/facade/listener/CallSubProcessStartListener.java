package com.xplatform.base.workflow.core.facade.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public class CallSubProcessStartListener extends BaseNodeEventListener {
	private static final long serialVersionUID = 1L;

	protected void execute(DelegateExecution execution, String actDefId,String nodeId) {
		String processInstanceId = execution.getProcessInstanceId();
		Map<String,Object> flowVars = TaskThreadService.getVariables();
		if (execution.getVariable("outPassVars") == null) {
			execution.setVariable("outPassVars", flowVars);
			TaskThreadService.clearVariables();
		}
		Integer completeInstance = (Integer) execution.getVariable("nrOfCompletedInstances");
		//还没有完成一个外部子流程实例
		if (completeInstance == null) {
			TaskNodeStatusService taskNodeStatusService =ApplicationContextUtil.getBean("taskNodeStatusService");
			TaskThreadService.clearNewTasks();
			TaskThreadService.cleanExtSubProcess();
			taskNodeStatusService.saveOrUpdte(actDefId, processInstanceId, nodeId,TaskOpinionEntity.STATUS_CHECKING.toString());
		}
	}

	protected String getScriptType() {
		return BpmConst.StartScript;
	}
}