package com.xplatform.base.workflow.task.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.service.RoleService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.task.mybatis.dao.TaskUserDao;
import com.xplatform.base.workflow.task.mybatis.vo.TaskUser;
import com.xplatform.base.workflow.task.service.TaskUserService;

@Service("taskUserService")
public class TaskUserServiceImpl implements TaskUserService {
	
	private TaskUserDao taskUserDao;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private RoleService roleService;
	
	@Override
	public Set<UserEntity> getCandidateUsers(String taskId)
			throws BusinessException {
		// TODO Auto-generated method stub
		Set<UserEntity> taskUserSet = new HashSet<UserEntity>();
	    List<TaskUser> taskUsers = this.taskUserDao.getByTaskId(taskId);
	    if(taskUsers!=null && taskUsers.size()>0){
	    	for (TaskUser taskUser : taskUsers) {
	    		//用户
	  	      	if (taskUser.getUserId() != null) {
	  	      		UserEntity sysUser = this.sysUserService.getUserById(taskUser.getUserId());
	  	      		taskUserSet.add(sysUser);
	  	      	} else if (taskUser.getGroupId() != null) {
	  	      		String tmpId = taskUser.getGroupId();
	  	      		if ("org".equals(taskUser.getType())) {//机构部门
	  	      			List<UserEntity> userList = this.sysUserService.getUserByCurrentOrgIds(tmpId);
	  	      			taskUserSet.addAll(userList);
	  	      		} else if ("pos".equals(taskUser.getType())) {//岗位
	  	      			List<UserEntity> userList = this.sysUserService.getUserByCurrentOrgIds(tmpId);
	  	      			taskUserSet.addAll(userList);
	  	      		} else if ("role".equals(taskUser.getType())) {//角色
	  	      			List<UserEntity> userList = this.sysUserService.getUserListByRoleId(tmpId);
	  	      			taskUserSet.addAll(userList);
	  	      		}
	  	      	}
	  	    }
	    }
	    return taskUserSet;
	}

	@Override
	public Set<TaskExecutor> getCandidateExecutors(String taskId)
			throws BusinessException {
		// TODO Auto-generated method stub
		Set<TaskExecutor> taskUserSet = new HashSet<TaskExecutor>();
		List<TaskUser> taskUsers = this.taskUserDao.getByTaskId(taskId);
	    for (TaskUser taskUser : taskUsers) {
	    	if (taskUser.getUserId() != null) {
	    		UserEntity sysUser = this.sysUserService.getUserById(taskUser.getUserId());
	    		if (BeanUtils.isNotEmpty(sysUser)){
	    			taskUserSet.add(TaskExecutor.getTaskUser(taskUser.getUserId(), sysUser.getName()));
	    		}
	    	} else if (taskUser.getGroupId() != null) {
		        String tmpId = taskUser.getGroupId();
		        if ("org".equals(taskUser.getType())) {
		        	OrgnaizationEntity sysOrg = this.orgnaizationService.get(tmpId);
		        	taskUserSet.add(TaskExecutor.getTaskOrg(tmpId, sysOrg.getName()));
		        }else if ("role".equals(taskUser.getType())) {
		        	RoleEntity sysRole = this.roleService.get(tmpId);
		        	taskUserSet.add(TaskExecutor.getTaskRole(tmpId, sysRole.getName()));
		        }
	       }
	    }
	    return taskUserSet;
	}
	
	@Override
	public void saveTaskUser(TaskUser taskUser) {
		// TODO Auto-generated method stub
		this.taskUserDao.add(taskUser);
	}

	@Autowired
	public void setTaskUserDao(TaskUserDao taskUserDao) {
		this.taskUserDao = taskUserDao;
	}

	

}
