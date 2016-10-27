package com.xplatform.base.platform.common.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;

public interface SysUserService {

	/**
	 * 用户用户信息
	 * 
	 * @param userId
	 * @return
	 */
	/*public UserEntity getUserInfoById(String userId);*/

	/**
	 * 通过机构或者岗位id取得用户员工列表
	 * 
	 * @param orgIds
	 * @return
	 */
	public List<UserEntity> getUserByOrgIds(String orgIds);

	/**
	 * 通过机构或者岗位id取得用户员工列表
	 * 
	 * @param orgIds
	 * @return
	 */
	public List<UserEntity> getUserByCurrentOrgIds(String orgIds);

	/**
	 * 通过机构id取得不同用户员工列表
	 * 
	 * @param orgIds
	 * @return
	 */
	public List<UserEntity> getDistinctUserByOrgIds(String orgIds);

	/**
	 * 通过机构id取得不同用户员工列表
	 * 
	 * @param orgIds
	 * @return
	 */
	public List<UserEntity> getDistinctUserByCurrentOrgIds(String orgIds);

	/**
	 * 通过机构id取得用户员工列表(分页列表)
	 * 
	 * @param page
	 * @return
	 */
	public Page<UserEntity> getUserByOrgIdsByPage(Page<UserEntity> page);

	/**
	 * 通过机构id取得用户员工列表(分页列表)
	 * 
	 * @param page
	 * @return
	 */
	public Page<UserEntity> getUserByCurrentOrgIdsByPage(Page<UserEntity> page);


	/**
	 * 通过角色id取得去重用户员工列表
	 * 
	 * @param roles
	 * @return
	 */
	public List<UserEntity> getDistinctUserByRoles(String roles);

	/**
	 * 通过角色id查找相关联的所有部门/岗位
	 * 
	 * @param roleIds
	 * @return
	 */
	public List<OrgnaizationEntity> getOrgsByRoles(String roleIds);

	/**
	 * 通过机构角色id取得用户员工列表(分页列表)
	 * 
	 * @author xiaqiang
	 * @createtime 2014年8月26日
	 */
	public Page<UserEntity> getUserByRolesByPage(Page<UserEntity> page);

	/**
	 * 获得所有用户员工列表(分页列表)
	 * 
	 * @author xiaqiang
	 * @createtime 2014年8月26日
	 */
	public Page<UserEntity> getAllUserByPage(Page<UserEntity> page);

	/**
	 * 获得 可管理机构下有权管理的部门的 用户员工列表(分页列表)
	 * 
	 * @author xiaqiang
	 * @createtime 2014年8月26日
	 */
	/*
	 * public Page<UserEntity> getAllUserInAuthorityByPage(Page<UserEntity>
	 * page);
	 */

	/**
	 * 根据用户id得到用户实体
	 * 
	 * @author xiehs
	 * @createtime 2014年9月11日 下午6:26:25
	 * @Decription
	 *
	 * @param userId
	 * @return
	 */
	public UserEntity getUserById(String userId);

	/**
	 * 获取系统所有用户员工
	 * 
	 * @return
	 */
	public List<UserEntity> getQueryListAll();

	/**
	 * 根据机构id获取机构name
	 * 
	 * @param orgIds
	 * @return
	 */
	public String getOrgNameByIds(String orgIds);

	/**
	 * 获取角色下的所有用户
	 * 
	 * @param roleId
	 * @return
	 */
	/*public List<UserEntity> getUserByRoleId(String roleId);*/

	/**
	 * 获取用户角色集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<RoleEntity> getRoleListByUserId(String userId);

	/**
	 * 通过全机构类型ids获取相关角色
	 * 
	 * @param allOrgIds
	 * @return
	 */
	/*public List<RoleEntity> getRolesByAllOrgIds(String allOrgIds);*/

	/**
	 * 获取下属员工
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEntity> getUnderUserByUserId(String userId);

	/**
	 * 获取员工直接领导
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEntity> getDirectLeaderByUserId(String userId);

	/**
	 * 获取用户岗位/部门/机构集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getAllOrganizationsByUserId(String userId);
	
	/**
	 * 获取所有机构
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getAllOrganization();

	/**
	 * 获取员工所有机构
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getOrganizationsByUserId(String userId);

	/**
	 * 获取用户所有部门
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getDeptsByUserId(String userId);

	/**
	 * 获取用户主部门经理用户实体
	 * 
	 * @param userId
	 * @return
	 */
	/* public UserEntity getDeptManagerUserByUserId(String userId); */

	/**
	 * 获取用户所有岗位
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getJobsByUserId(String userId);

	/**
	 * 获取员工所在各部门分管领导用户
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEntity> getBranchLeadersByUserId(String userId);

	/**
	 * 获取员工所在各部门负责人用户
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEntity> getDeptManagersByUserId(String userId);

	/**
	 * 通过员工Id获取员工可管理岗位(包含所在岗位)
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgnaizationEntity> getUnderJobsByUserId(String userId);

	/**
	 * 获取员工关联机构及上层机构（用于获取权限等）(由于岗位权限 不应该被继承，所以应该排除父岗位权限)
	 * 
	 * @param list
	 * @return
	 */
	public List<String> getUserOrgList(List<OrgnaizationEntity> list);

	/**
	 * 根据用户ids得到用户实体集合
	 * 
	 * @author xiaqiang
	 * @createtime 2015-9-23 18:23:24
	 *
	 * @param userIds
	 *            (","隔开的userIds)
	 * @return
	 */
	public List<UserEntity> getUserByIds(String userIds);

	/**
	 * 通过综合类型获得所包含去重用户
	 * 
	 * @createtime 2015-9-23 18:23:24
	 * @param receive
	 * @return
	 */
	public List<UserEntity> getDistinctUsersByMulType(String receive);

	/**
	 * 传入重复的userList,通过id获得去重的List
	 * 
	 * @createtime 2015-9-23 18:23:24
	 * @param users
	 * @return
	 */
	public List<UserEntity> getDistinctUsers(List<UserEntity> users);

	/**
	 * 传入重复的orgList,通过id获得去重的List
	 * 
	 * @createtime 2015-9-23 18:23:24
	 * @param orgs
	 * @return
	 */
	public List<OrgnaizationEntity> getDistinctOrgs(List<OrgnaizationEntity> orgs);

	/**
	 * 通过角色id获取去重后的关联组织集合
	 * 
	 * @createtime 2015-9-23 18:23:24
	 * @param roleIds
	 * @return
	 */
	public List<OrgnaizationEntity> getDistinctOrgsByRoles(String roleIds);

	/**
	 * 将orgList 转为 字符串集合
	 * 
	 * @createtime 2015-9-23 18:23:24
	 * @param roleIds
	 * @return
	 */
	public String parseOrgListToStr(List<OrgnaizationEntity> orgList);
	
	/**
	 * 获得无重复所在组织的上级组织(所有所在组织)
	 * @param userId
	 * @return
	 */
	public List<String> getDistinctUpOrgIds(String userId);
	
	/**
	 * 根据综合选择的json获得当时包含的所有用户
	 * @param finalValue
	 * @return
	 */
	public List<UserEntity> getDistinctMulUsers(String finalValue) throws BusinessException;
	
	/**
	 * 根据综合选择的json获得当时包含的所有用户Id
	 * @param finalValue
	 * @return
	 */
	public List<String> getDistinctMulUserIds(String finalValue) throws BusinessException;
	/**
	 * 根据综合选择的json获得当时包含的所有用户Id
	 * @param finalValue
	 * @return
	 */
	public List<UserTypeVo> getMulUsers(String finalValue) ;
	
	public List<UserEntity> getUserListByRoleId(String roleId);
	/**
	 * 获取所在的上级机构跟部门集合，用来查询，组装成逗号隔开
	 * @param userId
	 * @return
	 */
	public String getOrgIdsByUserId(String userId);
	
	/**
	 * 获取UserEntity中的UserId
	 * @param list
	 * @return
	 */
	public String getUserIds(List<UserEntity> list);
}
