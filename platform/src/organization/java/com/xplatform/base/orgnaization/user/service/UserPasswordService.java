package com.xplatform.base.orgnaization.user.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;

public interface UserPasswordService {

	/**
	 * 保存用户密码信息
	 * @author luoheng
	 * @param userPassword
	 * @return
	 * @throws BusinessException
	 */
	public String save(UserPasswordEntity userPassword) throws BusinessException;
	
	/**
	 * 修改密码信息
	 * @author luoheng
	 * @param userPassword
	 * @throws BusinessException
	 */
	public void update(UserPasswordEntity userPassword) throws BusinessException;
	
	/**
	 * 逻辑删除密码信息
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void deleteUserPassword(String id) throws BusinessException;
	
	/**
	 * 根据ID获取密码信息
	 * @author luoheng
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public UserPasswordEntity getUserPassword(String id) throws BusinessException;
	
	/**
	 * 根据用户ID查找有效密码
	 * @author luoheng
	 * @param userId
	 * @return
	 */
	public UserPasswordEntity getUserPasswordByUserId(String userId);
	
	/**
	 * 根据属性获取密码信息
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public List<UserPasswordEntity> queryListByPorperty(String propertyName, String value);
	
	/**
	 * 查找该用户所有密码并进行时间排序
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<UserPasswordEntity> queryList(String userId) ;
	
	/**
	 * 查找该用户id获取最后一次使用的密码
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public UserPasswordEntity getLastPass(String userId) ;
}
