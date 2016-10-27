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

public class NodeUserCalculateTaskExecutorSame implements NodeUserCalculate{

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
		TaskOpinionService taskOpinionService=ApplicationContextUtil.getBean("taskOpinionService");
		String actInstId = paramCalcVars.getActInstId();
		List<UserEntity> users = new ArrayList<UserEntity>();
		String nodeId = nodeUser.getAssignIds();
		TaskOpinionEntity taskOpinion =taskOpinionService.getLatestTaskOpinion(actInstId, nodeId);
		if(taskOpinion != null && StringUtil.isNotEmpty(taskOpinion.getExeUserId())){
	       UserEntity user = userService.getUserById(taskOpinion.getExeUserId());
	       users.add(user);
	    }
		return users;
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
		return "任务节点相同审批人";
	}

}
