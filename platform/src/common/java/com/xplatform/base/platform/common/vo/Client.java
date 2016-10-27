package com.xplatform.base.platform.common.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

/**
 * 
 * description :在线用户对象
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月20日 上午10:32:11
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年6月20日 上午10:32:11
 *
 */
public class Client implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private UserEntity user; // 登陆用户信息

	//private List<RoleEntity> roleList;// 用户角色

	private Map<String, ModuleTreeVo> modules; // 用户模块权限

	private Map<String, List<ResourceVo>> resources; // 用户模块权限

	//private List<OrgnaizationEntity> orgList;// 用户所具有的组织机构
	
	//private List<OrgnaizationEntity> managerOrgList;// 用户所具有的组织机构
	

	private String ip; // 用户登陆ip

	private Date logindatetime; // 用户登陆时间

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Map<String, ModuleTreeVo> getModules() {
		return modules;
	}

	public void setModules(Map<String, ModuleTreeVo> modules) {
		this.modules = modules;
	}

	public java.lang.String getIp() {
		return ip;
	}

	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}

	public Date getLogindatetime() {
		return logindatetime;
	}

	public void setLogindatetime(Date logindatetime) {
		this.logindatetime = logindatetime;
	}

	public Map<String, List<ResourceVo>> getResources() {
		return resources;
	}

	public void setResources(Map<String, List<ResourceVo>> resources) {
		this.resources = resources;
	}

	/*public List<RoleEntity> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleEntity> roleList) {
		this.roleList = roleList;
	}

	public List<OrgnaizationEntity> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrgnaizationEntity> orgList) {
		this.orgList = orgList;
	}

	public List<OrgnaizationEntity> getManagerOrgList() {
		return managerOrgList;
	}

	public void setManagerOrgList(List<OrgnaizationEntity> managerOrgList) {
		this.managerOrgList = managerOrgList;
	}
*/


}
