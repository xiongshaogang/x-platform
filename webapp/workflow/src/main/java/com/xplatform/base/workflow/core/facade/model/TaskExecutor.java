package com.xplatform.base.workflow.core.facade.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;

/**
 * 
 * description :查询任务执行者
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月26日 下午5:48:06
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月26日 下午5:48:06
 *
 */
public class TaskExecutor implements Serializable {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;
	public static final int EXACT_NOEXACT = 0;
	public static final int EXACT_EXACT_USER = 1;
	public static final int EXACT_EXACT_SECOND = 2;
	public static final int EXACT_USER_GROUP = 3;
	
	public static final String USER_TYPE_USER = "user";
	public static final String USER_TYPE_ORG = "org";
	public static final String USER_TYPE_ROLE = "role";
	public static final String USER_TYPE_POS = "pos";
	public static final String USER_TYPE_USERGROUP = "group";
	private String type = "user";
	private String executeId = "";
	private String executor = "";

	public String mainOrgName;

	private int exactType = 0;

	public TaskExecutor() {
	}

	public TaskExecutor(String executeId) {
		SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
		UserEntity sysUser = sysUserService.getUserById(executeId);
		this.executeId = executeId;
		this.executor = sysUser.getUserName();
	}

	public TaskExecutor(String type, String executeId, String name) {
		this.type = type;
		this.executeId = executeId;
		this.executor = name;
		if ("group".equalsIgnoreCase(type)){
			this.exactType = 3;
		}
	}

	public static TaskExecutor getTaskUser(String executeId, String name) {
		return new TaskExecutor("user", executeId, name);
	}

	public static TaskExecutor getTaskOrg(String executeId, String name) {
		return new TaskExecutor("org", executeId, name);
	}

	public static TaskExecutor getTaskRole(String executeId, String name) {
		return new TaskExecutor("role", executeId, name);
	}

	public static TaskExecutor getTaskPos(String executeId, String name) {
		return new TaskExecutor("pos", executeId, name);
	}

	public static TaskExecutor getTaskUserGroup(String executeId, String name) {
		TaskExecutor ex = new TaskExecutor("group", executeId, name);
		ex.setExactType(3);
		return ex;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecuteId() {
		return this.executeId;
	}

	public void setExecuteId(String executeId) {
		this.executeId = executeId;
	}

	public String getExecutor() {
		return this.executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public int getExactType() {
		return this.exactType;
	}

	public void setExactType(int exactType) {
		this.exactType = exactType;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TaskExecutor)) {
			return false;
		}
		TaskExecutor tmp = (TaskExecutor) obj;

		return (this.type.equals(tmp.getType()))&& (this.executeId.equals(tmp.getExecuteId()));
	}

	public int hashCode() {
		String tmp = this.type + this.executeId;
		return tmp.hashCode();
	}

	//通过配置的执行人获取执行用户
	public Set<UserEntity> findSysUser() throws BusinessException {
		Set<UserEntity> sysUsers = new HashSet<UserEntity>();
		SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
		if ("user".equals(this.type)) {
			UserEntity user=sysUserService.getUserById(this.executeId);
			sysUsers.add(user);
		} else if ("org".equals(this.type)) {
			List<UserEntity> users= sysUserService.getUserByCurrentOrgIds(this.executeId);
			sysUsers.addAll(users);
		} else if ("pos".equals(this.type)) {
			List<UserEntity> users= sysUserService.getUserByCurrentOrgIds(this.executeId);
			sysUsers.addAll(users);
		} else if ("role".equals(this.type)) {
			List<UserEntity> users= sysUserService.getUserListByRoleId(this.executeId);
			sysUsers.addAll(users);
		}
		return sysUsers;
	}

	public String getMainOrgName() {
		return this.mainOrgName;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}
}
