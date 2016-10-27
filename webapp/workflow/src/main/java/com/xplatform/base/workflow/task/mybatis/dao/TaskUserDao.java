package com.xplatform.base.workflow.task.mybatis.dao;



import java.util.List;

import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;
import com.xplatform.base.workflow.task.mybatis.vo.TaskUser;

public interface TaskUserDao extends SimpleSqlDao<TaskUser, String> {
	
	public void add(TaskUser taskUser);
	
	public List<TaskUser> getByTaskId(String taskId);
	
	public void delByInstanceId(String instanceId);
	
}
