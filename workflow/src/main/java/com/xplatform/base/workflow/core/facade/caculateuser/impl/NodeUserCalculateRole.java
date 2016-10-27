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

/**
 * 
 * description :计算任务节点选择的角色
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月3日 下午6:57:18
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月3日 下午6:57:18
 *
 */
public class NodeUserCalculateRole implements NodeUserCalculate {

	@Override
	public List<UserEntity> getExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		/*SysUserService userService=ApplicationContextUtil.getBean("sysUserService");
		String ids=nodeUser.getAssignIds();
		String[] arrId=null;
		if(StringUtil.isNotBlank(ids)){
			arrId=ids.split(",");
		}
		List<UserEntity> userList=new ArrayList<UserEntity>();
		for(String id:arrId){
			List<UserEntity> user=userService.getUserByRoleId(id);
			userList.addAll(user);
		}
		return userList;*/
		return null;
	}

	@Override
	public List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		String ids=nodeUser.getAssignIds();
		String names=nodeUser.getAssignNames();
		String[] arrId=null;
		String[] arrName=null;
		if(StringUtil.isNotBlank(ids)){
			arrId=ids.split(",");
			arrName=names.split(",");
		}
		List<TaskExecutor> executorList=new ArrayList<TaskExecutor>();
		for(int i=0;i<arrId.length;i++){
			executorList.add(TaskExecutor.getTaskRole(arrId[i], arrName[i]));
		}
		return executorList;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "角色";
	}

}
