package com.xplatform.base.workflow.execution.service;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.xplatform.base.workflow.execution.mybatis.vo.ProcessExecution;

public interface ExecutionExtService {
	public void insert(ProcessExecution execution);
	public void update(ProcessExecution execution);
	public ExecutionEntity get(String id);
	public void updateMainThread(String executionId, String nodeId) ;
	public void delNotMainThread(String procInstId) ;
	public void updateTaskToMainThreadId(String executionId, String taskId) ;
	public void delLoopAssigneeVars(String executionId) ;
	public void delExecutionById(String executionId);
	public void delTokenVarByTaskId(String taskId, String token);
	public void delVariableByProcInstId(String procInstId);
	public void delExecutionByProcInstId(String procInstId) ;
	public void delSubExecutionByProcInstId(String procInstId);
}
