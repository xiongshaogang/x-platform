package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.List;

import com.hotent.core.util.StringUtil;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

public class NodeUserCalculateTaskExecutorDirectLeader implements NodeUserCalculate {

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
		TaskOpinionService taskOpinionService=ApplicationContextUtil.getBean("taskOpinionService");
		String actInstId = paramCalcVars.getActInstId();
		String nodeId = nodeUser.getAssignIds();
		TaskOpinionEntity taskOpinion =taskOpinionService.getLatestTaskOpinion(actInstId, nodeId);
		if(taskOpinion == null || StringUtil.isEmpty(taskOpinion.getExeUserId())){
			return null;
	    }
		//通过员工获取
		return userService.getDirectLeaderByUserId(taskOpinion.getExeUserId());
	}

	@Override
	public List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		List<UserEntity> userList=getExecutor(nodeUser,paramCalcVars);
		List<TaskExecutor> executorList=new ArrayList<TaskExecutor>();
		if(userList!=null && userList.size()>0){
			for(UserEntity user:userList){
				executorList.add(TaskExecutor.getTaskUser(user.getId(),user.getName()));
			}
		}
		return executorList;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "任务节点务审批人的直接领导";
	}

}
