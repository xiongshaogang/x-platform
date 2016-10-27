package com.xplatform.base.workflow.execution.service;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;


public interface ExecutionService {
	public void insert(ExecutionEntity execution);
	public void update(ExecutionEntity execution);
	public ExecutionEntity get(String id);
	public void deleteById(String id);
}
