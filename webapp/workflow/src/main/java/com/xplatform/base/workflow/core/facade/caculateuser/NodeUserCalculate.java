package com.xplatform.base.workflow.core.facade.caculateuser;

import java.util.List;

import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;

public interface NodeUserCalculate {
	//获取执行人
	public abstract List<UserEntity> getExecutor(NodeUserEntity nodeUser, CalcVars paramCalcVars);
	//获取任务执行人
    public abstract List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser, CalcVars paramCalcVars);
    //计算用户标题
    public abstract String getTitle();
}
