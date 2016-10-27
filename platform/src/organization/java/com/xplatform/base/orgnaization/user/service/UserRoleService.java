package com.xplatform.base.orgnaization.user.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;

/**
 * 
 * description : 分组管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface UserRoleService {
	
	/**
	 * 新增分组
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param job
	 * @return
	 */
	public String save(UserRoleEntity UserRoleEntity) throws BusinessException ;
	
	public String save(UserRoleEntity UserRoleEntity,String userIds) throws BusinessException ;
	
	public void batchSave(List<UserRoleEntity> UserRoleList) throws BusinessException ;
	
	public void deleteByRoleId(String roleId) throws BusinessException ;
	
	/**
	 * 删除分组
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException ;
	
	/**
	 * 根据userId和roleId删除
	 * @author lixt
	 * @createtime 2016年01月15日 上午11:32:56
	 * @Decription
	 *
	 * @param userId,roleId
	 * @return
	 */
	public void deleteByUseAndRole(String userId,String roleId) throws BusinessException ;
	
	/**
	 * 批量删除分组
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception ;
	
	/**
	 * 更新分组
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(UserRoleEntity UserRoleEntity) throws BusinessException ;
	
	/**
	 * 查询一条分组记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public UserRoleEntity get(String id) throws BusinessException ;
	
	public UserRoleEntity getUserRole(String userId, String roleId) throws BusinessException ;
	
	/**
	 * 查询分组列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<UserRoleEntity> queryList() throws BusinessException ;
	
	public List<UserRoleEntity> queryUserRoleByUserIdList(String userId);
	
	public List<UserRoleEntity> queryUserRoleByRoleIdList(String RoleId) ;
	
	/**
	 * 根据用户ID删除用户所属角色
	 * @author luoheng
	 * @param userId
	 * @throws BusinessException
	 */
	public void deleteUserRoleByUserId(String userId) throws BusinessException;
	
	/**
	 * hibernate分组分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException ;
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	public void updateUserRole(String roleIds, String groupId) throws BusinessException ;
	
	public void updateRoleUser(String userIds, String roleId) throws BusinessException;
	
	public void deleteOrgManager(String userId,String orgId) throws BusinessException;
	
	/**
	 * 查询某个角色群组里的所有用户
	 * @author lixt
	 * @createtime 2016年1月19日 下午5:20:22
	 *
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> queryRoleUsers(String roleId) throws BusinessException;
	
	/**
	 * 查询角色的头像，最多四个
	 * @author lixt
	 * @createtime 2016年1月19日 下午5:20:22
	 *
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> queryRolePortrait(String roleId) throws BusinessException;
}
