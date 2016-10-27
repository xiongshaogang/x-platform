package com.xplatform.base.orgnaization.orggroup.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

public interface OrgGroupService extends BaseService<OrgGroupEntity> {

	/**
	 * 保存群组,并且处理环信群组请求
	 * 
	 * @param orgGroup
	 * @return
	 * @throws BusinessException
	 */
	public AjaxJson saveAndProcessHX(OrgGroupEntity orgGroup,int workGroup) throws Exception;
	
	/**
	 * 保存群组,并且处理环信群组请求(含添加成員，根據傳入的phone或者ids或者phones与ids一并添加成員)
	 * 
	 * @param orgGroup
	 * @return
	 * @throws BusinessException
	 * @param orgGroup (phones or ids) or(phones and ids)
	 */
	public AjaxJson saveAndProcessHX(OrgGroupEntity orgGroup,String phones,String ids,int workGroup) throws Exception;

	/**
	 * 更新群组信息,并且处理环信群组请求
	 * 
	 * @param orgGroup
	 * @throws Exception
	 */
	public void updateAndProcessHX(OrgGroupEntity orgGroup) throws Exception;

	/**
	 * 删除群组,并且处理环信群组请求
	 * 
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteAndProcessHX(String groupId) throws Exception;
	
	/**
	 * 删除群组,并且处理环信群组请求
	 * 
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteAndProcessHXByRoleId(String roleId) throws Exception;

	/**
	 * 为群组添加用户(单个/多个均可),并且处理环信群组请求
	 * 
	 * @param groupId
	 * @param emGroupId
	 * @param userIds
	 * @throws Exception
	 */
	public AjaxJson addUser(String groupId, String userIds) throws Exception;
	
	/**
	 * 为群组添加用户(单个/多个均可),并且处理环信群组请求
	 * 
	 * @param groupId
	 * @param emGroupId
	 * @param userIds
	 * @throws Exception
	 */
	public AjaxJson addUser(String groupId, String phones,boolean flag) throws Exception;

	/**
	 * 为群组删除用户(单个),并且处理环信群组请求
	 * 
	 * @param groupId
	 * @param emGroupId
	 * @param userIds
	 * @throws Exception
	 */
	public AjaxJson deleteUser(String emGroupId, String userId) throws Exception;

	/**
	 * 查询一个用户所有群组
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryUserGroupsByPage(String userId,int row,int page) throws Exception;
	
	/**
	 * 查询一个用户所有群组
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryUserGroups(String userId) throws Exception;
	
	/**
	 * 查询一个用户可以联系的所有群组联系人
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryUsers(String userId);
	
	/**
	 * 查询一个用户可以联系的所有工作组的工作组好友
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryUsersByWork(String userId);
	
	/**
	 * 通过用户名(name字段)模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public List<Map<String,Object>> queryUsersByLikeKey(String userId,String key);
	
	/**
	 * 查询一个群组信息并附带群成员信息
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public Map<String,Object> getUserOrg(String groupId);
	
	/**
	 * 通过搜索关键词来查询某个群里边的人员
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryUserFromGroup(String searchKey,String groupId) throws Exception;

}
