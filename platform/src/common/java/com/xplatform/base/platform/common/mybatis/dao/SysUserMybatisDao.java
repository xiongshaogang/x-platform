package com.xplatform.base.platform.common.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

/**
 * description : 对应SysUser.xml里的sql
 *
 * @version 1.0
 * @author xiaqiang 
 * @createtime : 2014年10月29日 下午4:50:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年10月29日 下午4:50:13
 *
*/
public interface SysUserMybatisDao {

	/**
	 * 通过岗位查询用户
	 * @param param
	 * @return
	 */
	public List<UserEntity> getUserByCurrentOrgIds(Map<String, Object> param);

	/**
	 * 通过机构部门查询用户
	 * @param param
	 * @return
	 */
	public List<UserEntity> getUserByOrgIds(Map<String, Object> param);
	
	/**
	 * 通过角色查询用户
	 * @param param
	 * @return
	 */
	public List<UserEntity> getUserByRoles(Map<String, Object> param);
	
	/**
	 * 通过部门分页查询部门下的所有用户
	 * @param page
	 * @return
	 * @author lxt
	 */
	public List<UserEntity> getUserByCurrentOrgIdsByPage(Page<UserEntity> page);
	/**
	 * 通过角色分页查询用户
	 * @param page
	 * @return
	 */
	public List<UserEntity> getUserByRolesByPage(Page<UserEntity> page);
	
	/**
	 * 角色查询岗位
	 * @param param
	 * @return
	 */
	public List<OrgnaizationEntity> getOrgsByRoles(Map<String, Object> param);

	
	/**
	 * 通过机构部门分页查询用户
	 * @param page
	 * @return
	 */
	public List<UserEntity> getUserByOrgIdsByPage(Page<UserEntity> page);
	
	
	/**
	 * 分页查询所有用户
	 * @param page
	 * @return
	 */
	public List<UserEntity> getAllUserByPage(Page<UserEntity> page);
	
	/**
	 * 分页查询管理的用户
	 * @param page
	 * @return
	 *//*
	public List<UserEntity> getAllUserInAuthorityByPage(Page<UserEntity> page);*/

	/**
	 * 查询所有的用户
	 * @return
	 */
	public List<UserEntity> getQueryListAll();

	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	public UserEntity getUserInfoById(String userId);
	
}
