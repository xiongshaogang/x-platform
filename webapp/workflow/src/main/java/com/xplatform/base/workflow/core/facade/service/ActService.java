package com.xplatform.base.workflow.core.facade.service;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;


public interface ActService {
	public ExecutionEntity getExecution(String executionId);
	public abstract void endProcessByTaskId(String paramString);
}
