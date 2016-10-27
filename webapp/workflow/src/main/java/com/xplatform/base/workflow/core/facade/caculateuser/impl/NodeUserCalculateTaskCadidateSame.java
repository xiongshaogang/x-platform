package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.task.service.TaskUserService;

public class NodeUserCalculateTaskCadidateSame implements NodeUserCalculate {

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		TaskUserService taskUserService=ApplicationContextUtil.getBean("taskUserService");
		Set<TaskExecutor> candidateUsers = null;
		try {
			candidateUsers = taskUserService.getCandidateExecutors(nodeUser.getAssignIds());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<TaskExecutor> list=new ArrayList<TaskExecutor>();
		list.addAll(candidateUsers);
		return list;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "与某一个任务节点相同的任务候选人";
	}

}
