package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;

public class NodeUserCalculateStartUserDirectLeader implements NodeUserCalculate {

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
		//获取流程发起人id
		String startUserId = paramCalcVars.getStartUserId();
		if(StringUtil.isBlank(startUserId)){return null;}
		//通过员工获取
		return userService.getDirectLeaderByUserId(startUserId);
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
		return "流程发起人的直接领导";
	}

}
