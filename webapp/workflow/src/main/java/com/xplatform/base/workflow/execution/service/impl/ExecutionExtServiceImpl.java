package com.xplatform.base.workflow.execution.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.execution.mybatis.dao.ExecutionDao;
import com.xplatform.base.workflow.execution.mybatis.dao.ExecutionExtDao;
import com.xplatform.base.workflow.execution.mybatis.vo.ProcessExecution;
import com.xplatform.base.workflow.execution.service.ExecutionExtService;

@Service("executionExtService")
public class ExecutionExtServiceImpl implements ExecutionExtService {
	@Resource
	private ExecutionExtDao executionExtDao;
	@Resource
	private ExecutionDao executionDao;
	
	public void insert(ProcessExecution execution){
		this.executionExtDao.save(execution);
	}
	public void update(ProcessExecution execution){
		this.executionExtDao.update(execution);
	}
	public ExecutionEntity get(String id){
		return this.executionDao.get(id);
	}
	
	public void updateMainThread(String executionId, String nodeId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("nodeId", nodeId);
		params.put("executionId", executionId);
		this.executionExtDao.updateMainThread(params);
	}

	public void delNotMainThread(String procInstId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("procInstId", procInstId);
		this.executionExtDao.delNotMainThread(params);
	}

	public void updateTaskToMainThreadId(String executionId, String taskId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("executionId", executionId);
		params.put("taskId", taskId);
		this.executionExtDao.updateTaskToMainThreadId(params);
	}

	public void delLoopAssigneeVars(String executionId) {
		this.executionExtDao.delAssigneeByExecutionId(executionId);
		this.executionExtDao.delLoopCounterByExecutionId(executionId);
	}

	public void delExecutionById(String executionId) {
		this.executionExtDao.delete(executionId);
	}

	public void delTokenVarByTaskId(String taskId, String token) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("taskId", taskId);
		params.put("name", token);
		this.executionExtDao.delTokenVarByTaskId(params);
	}

	public void delVariableByProcInstId(String procInstId) {
		this.executionExtDao.delVariableByProcInstId(procInstId);
	}

	public void delExecutionByProcInstId(String procInstId) {
		this.executionExtDao.delExecutionByProcInstId(procInstId);
	}

	public void delSubExecutionByProcInstId(String procInstId) {
		this.executionExtDao.delSubExecutionByProcInstId(procInstId);
	}


	public void delVarsByExecutionId(String executionId) {
		this.executionExtDao.delVarsByExecutionId(executionId);
	}

}
