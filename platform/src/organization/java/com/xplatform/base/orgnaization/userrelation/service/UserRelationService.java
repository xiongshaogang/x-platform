package com.xplatform.base.orgnaization.userrelation.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.userrelation.entity.UserRelationEntity;

public interface UserRelationService extends BaseService<UserRelationEntity> {

	/**
	 * 添加好友(这里无需请求环信接口,因为环信加好友操作已经在客户端做了)
	 * 
	 * @param userRelation
	 * @throws Exception
	 */
	public void saveUserRelation(UserRelationEntity userRelation) throws Exception;

	/**
	 * 删除好友(这里无需请求环信接口,因为环信移除好友操作已经在客户端做了)
	 * 
	 * @param apply
	 * @param receive
	 * @return
	 * @throws BusinessException
	 */
	public Integer deleteUserRelation(String apply, String receive) throws Exception;

	/**
	 * 通过双方Id查询关系
	 * 
	 * @param first
	 * @param second
	 * @return
	 * @throws BusinessException
	 */
	public UserRelationEntity getUserRelation(String first, String second);

	/**
	 * 查询一个人的所有好友关系
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRelationEntity> queryAllMyFirendRelation(String userId);

	/**
	 * 查询一个人的所有好友关系
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEntity> queryAllMyFirend(String userId);
}
