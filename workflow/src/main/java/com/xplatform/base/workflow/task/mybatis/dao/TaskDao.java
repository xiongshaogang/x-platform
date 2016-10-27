package com.xplatform.base.workflow.task.mybatis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;





import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;

public interface TaskDao extends SimpleSqlDao<ProcessTask, String> {
	
	public void updTaskExecution(String taskId);

	public List<ProcessTask> getAllMyTaskByPage(Page<ProcessTask> page);
	
	public List<ProcessTask> getMyEndTaskByPage(Page<ProcessTask> page);
	
	public List<ProcessTask> getMyCompleteTaskByPage(Page<ProcessTask> page);
	
	public List<ProcessTask> getAllByPage(Page<ProcessTask> page);

	public List<?> getMyTaskCount(String userId);

	public List<ProcessTask> getMyMobileTask(Map<String,String> params);

	public List<ProcessTask> getAllMyEvent(Map<String,Object> param); 

	public int setDueDate(Map<String,Object> params);
	
	public void add(ProcessTask task) ;

	public List<ProcessTask> getReminderTask(Date curDate);

	public List<ProcessTask> getTimeReminderTask(Date curDate);

	public List<ProcessTask> getTasksByRunId(String runId) ;

	public void updateTaskAssignee(Map<String,String> params);

	public void updateTaskDescription(Map<String,String> params);

	public void updateTaskAssigneeNull(String taskId);

	public void updateTaskOwner(Map<String,String> params);

	public ProcessTask getByTaskId(String taskId);

	public List<ProcessTask> getByParentTaskIdAndDesc(Map<String,String> params); 
	
	public List<ProcessTask> getByInstanceIdTaskDefKey(Map<String,String> params); 

	public List<ProcessTask> getListByInstanceIdTaskDefKey(Map<String,String> params);

	public List<ProcessTask> getByInstanceId(String instanceId);
	
	public void delByInstanceId(String instanceId);

	public void delCandidateByInstanceId(String instanceId) ;

	public void updateNewTaskDefKeyByInstIdNodeId(Map<String,String> params);

	public void updateOldTaskDefKeyByInstIdNodeId(Map<String,String> params);

	public List<Map<String,Object>> getHasCandidateExecutor(List<String> taskIds);

	public void delByParentId(String parentId);

	public void updateTask(Map<String,Object> params);

	public void delCommuTaskByInstNodeUser(Map<String,String> params);

	public void delCommuTaskByParentTaskId(String parentTaskId);

	public void delTransToTaskByParentTaskId(String parentTaskId) ;

	public List<ProcessTask> getTaskByInstId(String actInstId) ;

	public List<ProcessTask> getHisTaskByInstId(String actInstId);

	public Integer getHasRightsByTask(Map<String,Object> params);

	public Integer hasRead(Map<String,String> params);

	public List<ProcessTask> getTaskByActDefId(Map<String,Object> params);

	public Integer getHisByInstanceidAndUserId(Map<String,String> params);

	public List<ProcessTask> getByTaskNameOrTaskIds(Map<String,String> params);
	
	public void delDelegateUser(String taskId);
	
	public void setTaskUser(Map<String,Object> params);
	
	public void setRead(Map<String,Object> params);
	
}
