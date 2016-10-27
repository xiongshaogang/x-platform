package com.xplatform.base.workflow.core.facade.service.impl;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.core.facade.service.ActService;
import com.xplatform.base.workflow.core.facade.service.cmd.EndProcessCmd;
import com.xplatform.base.workflow.core.facade.service.cmd.GetExecutionCmd;

@Service("actService")
public class ActServiceImpl extends BaseProcessService implements ActService {

	@Override
	public void endProcessByTaskId(String taskId) {
		// TODO Auto-generated method stub
		EndProcessCmd cmd = new EndProcessCmd(taskId);
		this.commandExecutor.execute(cmd);
	}
	
	@Override
	public ExecutionEntity getExecution(String executionId) {
		return (ExecutionEntity) this.commandExecutor.execute(new GetExecutionCmd(executionId));
	}

}
