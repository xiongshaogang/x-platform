package com.xplatform.base.workflow.core.facade.listener;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;

public class CallSubProcessEndListener extends BaseNodeEventListener {
	
	private static final long serialVersionUID = 1L;

	TaskNodeStatusService taskNodeStatusService =ApplicationContextUtil.getBean("taskNodeStatusService");

	private RuntimeService runtimeService = ApplicationContextUtil.getBean("runtimeService");

	protected void execute(DelegateExecution execution, String actDefId,
			String nodeId) {
		String processInstanceId = execution.getProcessInstanceId();
		ExecutionEntity ent = (ExecutionEntity) execution;
		String varName = ent.getActivityId() + "_" + "subExtAssignIds";

		boolean rtn = BpmUtil.isMuiltiExcetion(ent);
		TaskNodeStatusEntity taskNodeStatus=new TaskNodeStatusEntity();
		taskNodeStatus.setActInstId(processInstanceId);
		taskNodeStatus.setActDefId(actDefId);
		taskNodeStatus.setStatus(TaskOpinionEntity.STATUS_AGREE.toString());
		if (rtn) {
			int completeInstance = ((Integer) execution.getVariable("nrOfCompletedInstances")).intValue();
			int nrOfInstances = ((Integer) execution.getVariable("nrOfInstances")).intValue();
			if (completeInstance == nrOfInstances) {
				try {
					taskNodeStatusService.update(taskNodeStatus);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				execution.removeVariable(varName);
				deleteInstanceBySupperInstanceId(processInstanceId);
			}
		} else {
			try {
				taskNodeStatusService.update(taskNodeStatus);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execution.removeVariable(varName);
			deleteInstanceBySupperInstanceId(processInstanceId);
		}
	}

	private void deleteInstanceBySupperInstanceId(String processInstanceId) {
		List<ProcessInstance> subInstances = this.runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
		for (ProcessInstance instance : subInstances){
			this.runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), "结束子流程");
		}
	}

	protected String getScriptType() {
		return BpmConst.EndScript;
	}
}