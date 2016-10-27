package com.xplatform.base.workflow.core.facade.service.cmd;

import java.io.Serializable;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class GetExecutionCmd implements Command<ExecutionEntity>, Serializable {
	private static final long serialVersionUID = 1L;
	private String executionId = "";

	public GetExecutionCmd(String executionId) {
		this.executionId = executionId;
	}

	public ExecutionEntity execute(CommandContext commandContext) {
		if (this.executionId == null) {
			throw new ActivitiException("executionId is null");
		}
		ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(this.executionId);

		if (execution == null) {
			throw new ActivitiException("execution " + this.executionId
					+ " doesn't exist");
		}
		execution.getProcessInstance();

		execution.getVariables();
		execution.getVariablesLocal();

		return execution;
	}
}