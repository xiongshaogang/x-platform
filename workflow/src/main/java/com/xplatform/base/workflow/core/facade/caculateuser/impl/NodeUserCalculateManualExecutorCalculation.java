package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;

public class NodeUserCalculateManualExecutorCalculation implements NodeUserCalculate {

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		GroovyScriptEngine groovyScriptEngine=ApplicationContextUtil.getBean("scriptEngine");
		String script=nodeUser.getAssignIds();
		List<UserEntity> userList=new ArrayList<UserEntity>();
		//组装参数
		String prevUserId = paramCalcVars.getPrevExecUserId();
		String startUserId = paramCalcVars.getStartUserId();
		Map<String,Object> grooVars = new HashMap<String,Object>();
		grooVars.put("prevUser", prevUserId);
		grooVars.put("startUser", startUserId.toString());
		if (paramCalcVars.getVars().size() > 0) {
			grooVars.putAll(paramCalcVars.getVars());
		}
		//执行脚本获取用户id集合
		Object result = groovyScriptEngine.executeObject(script, grooVars);
		if(BeanUtils.isEmpty(result)){
			return null;
		}else if(result instanceof UserEntity){
			userList.add((UserEntity)result);
		}else if(result instanceof java.util.List){
			userList=(List<UserEntity>)result;
		}
		//返回用户集合
		return userList;
	}

	@Override
	public List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		List<UserEntity> userList=getExecutor(nodeUser,paramCalcVars);
		List<TaskExecutor> executorList=new ArrayList<TaskExecutor>();
		if(userList!=null && userList.size()>0){
			for(UserEntity user:userList){
				executorList.add(TaskExecutor.getTaskUser(user.getId(),user.getUserName()));
			}
		}
		return executorList;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "脚本获取用户";
	}

}
