package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;

/**
 * 
 * description :计算选择的部门
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月3日 下午6:56:33
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月3日 下午6:56:33
 *
 */
public class NodeUserCalculateDept implements NodeUserCalculate {

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
			List<UserEntity> user=userService.getUserByDeptId(id);
			userList.addAll(user);
		}
		return userList;*/
		return null;
	}

	@Override
	public List<TaskExecutor> getTaskExecutor(NodeUserEntity nodeUser,
			CalcVars paramCalcVars) {
		// TODO Auto-generated method stub
		/*List<UserEntity> userList=getExecutor(nodeUser,paramCalcVars);
		List<TaskExecutor> executorList=new ArrayList<TaskExecutor>();
		if(userList!=null && userList.size()>0){
			for(UserEntity user:userList){
				executorList.add(TaskExecutor.getTaskUser(user.getId(),user.getUserName()));
			}
		}
		return executorList;*/
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
			OrgnaizationService orgnaizationService=ApplicationContextUtil.getBean("orgnaizationService");
			/*OrgnaizationEntity org=orgnaizationService.get(arrId[i]);
			if(org!=null && StringUtil.equals("job", org.getId())){//如果组织机构类型是岗位
				executorList.add(TaskExecutor.getTaskPos(arrId[i], arrName[i]));
			}else{*/
			executorList.add(TaskExecutor.getTaskOrg(arrId[i], arrName[i]));
			/*}*/
			
		}
		return executorList;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "部门";
	}

}
