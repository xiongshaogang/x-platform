package com.xplatform.base.workflow.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;





import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;

public interface TaskActService {

	/**
	 * 通过任务id和用户id判断有没有任务权限
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:34:09
	 * @Decription
	 *
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public boolean getHasRightsByTask(String taskId, String userId);
	
	/**
	 * 通过流程实例获取流程任务集合
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:51:07
	 * @Decription
	 *
	 * @param actInstanceId
	 * @return
	 */
	public List<ProcessTask> getTaskByInstId(String actInstanceId);
	
	public void updTaskExecution(String taskId) ;

	public Page<ProcessTask> getMyTasks(Page<ProcessTask> page);
	
	public Page<ProcessTask> getMyEndTaskList(Page<ProcessTask> page);
	
	public Page<ProcessTask> getMyCompleteTaskList(Page<ProcessTask> page);
	
	public Page<ProcessTask> getAll(Page<ProcessTask> page);
	/*
	public List<?> getMyTasksCount(Long userId) {
		
		return getBySqlKey("getMyTaskCount", userId);
	}

	public List<TaskEntity> getMyMobileTasks(QueryFilter filter) {
		String statmentName = "getMyMobileTask";
		return getBySqlKey(statmentName, filter);
	}*/


	public List<ProcessTask> getMyEvents(Map param);

	public int setDueDate(String taskId, Date dueDate);

	public void insertTask(ProcessTask task);

	public List<ProcessTask> getReminderTask() ;

	public List<ProcessTask> getTimeReminderTask() ;

	public List<ProcessTask> getTasksByRunId(String runId);

	public void updateTaskAssignee(String taskId, String userId);

	public void updateTaskDescription(String description, String taskId) ;

	public void updateTaskAssigneeNull(String taskId);

	public void updateTaskOwner(String taskId, String userId);

	public ProcessTask getByTaskId(String taskId);

	public List<ProcessTask> getByParentTaskIdAndDesc(String parentTaskId,
			String description);

	public List<ProcessTask> getByInstanceIdTaskDefKey(String instanceId,
			String taskDefKey) ;

	public List<ProcessTask> getListByInstanceIdTaskDefKey(String instanceId,
			String taskDefKey);

	public List<ProcessTask> getByInstanceId(String instanceId) ;

	public void delByInstanceId(String instanceId) ;

	public void delCandidateByInstanceId(String instanceId) ;

	public void updateNewTaskDefKeyByInstIdNodeId(String newTaskDefKey,
			String oldTaskDefKey, String actInstId);

	public void updateOldTaskDefKeyByInstIdNodeId(String newTaskDefKey,
			String oldTaskDefKey, String actInstId) ;

	public List<Map<String,Object>> getHasCandidateExecutor(String taskIds) ;
;

	public void delByParentId(String parentId) ;
	public void updateTask(String taskId, String userId, String description);

	public void delCommuTaskByInstNodeUser(Long instInstId, String nodeId,
			Long userId) ;

	public void delCommuTaskByParentTaskId(String parentTaskId) ;

	public void delTransToTaskByParentTaskId(String parentTaskId);



	public List<ProcessTask> getHisTaskByInstId(String actInstId);



	public boolean hasRead(Long taskId, Long userId) ;

	public List<ProcessTask> getTaskByActDefId(String actDefId, int num) ;

	public boolean getHisByInstanceidAndUserId(Long actInstId, Long userId);

	public List<ProcessTask> getByTaskNameOrTaskIds(String userId,String taskName, String taskIds);
	
	public void delDelegateUser(String taskId);
	
	public void setTaskUser(Map<String,Object> params);
}
