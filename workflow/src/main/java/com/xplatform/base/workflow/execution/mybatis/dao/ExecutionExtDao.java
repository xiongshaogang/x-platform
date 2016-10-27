package com.xplatform.base.workflow.execution.mybatis.dao;

import java.util.Map;


import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;
import com.xplatform.base.workflow.execution.mybatis.vo.ProcessExecution;

public interface ExecutionExtDao extends SimpleSqlDao<ProcessExecution, String> {
	
	public void delExecutionByProcInstId(String procInstId);
	
	public void delSubExecutionByProcInstId(String procInstId);
  
	public void delAssigneeByExecutionId(String exeuctionId);
	
	public void delLoopCounterByExecutionId(String exeuctionId);
  
	public void delVariableByProcInstId(String procInstId);
	
	public void delTokenVarByTaskId(Map<String,String> param);
  
	public void delVarsByExecutionId(String exeuctionId);
  
	public void delNotMainThread(Map<String,String> param);
  
	public void updateMainThread(Map<String,String> param);
	
	public void updateTaskToMainThreadId(Map<String,String> param);
}
