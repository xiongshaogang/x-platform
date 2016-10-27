package com.xplatform.base.workflow.task.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;
import com.xplatform.base.workflow.task.mybatis.vo.TaskVars;

public interface TaskVarsDao extends SimpleSqlDao<TaskVars, String> {
	//获取变量集合
	public List<TaskVars> getTaskVars(Map<String,Object> param);
	//删除流程定义的变量
	public void delVarsByActInstId(String actInstId);
}
