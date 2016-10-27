package com.xplatform.base.orgnaization.user.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;

public interface UserPasswordDao extends ICommonDao {

	/**
	 * 保存用户密码信息
	 * @author luoheng
	 * @param userPassword
	 * @return
	 */
	public String addUserPassword(UserPasswordEntity userPassword);
	
	/**
	 * 修改密码信息
	 * @author luoheng
	 * @param userPassword
	 */
	public void updateUserPassword(UserPasswordEntity userPassword);
	
	/**
	 * 逻辑删除密码信息
	 * @author luoheng
	 * @param id
	 */
	public void deleteUserPassword(String id);
	
	/**
	 * 根据ID获取密码信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public UserPasswordEntity getUserPassword(String id);
	
	/**
	 * 根据用户ID查找有效密码
	 * @author luoheng
	 * @param userId
	 * @return
	 */
	public UserPasswordEntity getUserPasswordByUserId(String userId);
	
	/**
	 * 查找该用户所有密码并进行时间排序
	 * @param userId
	 * @return
	 */
	public List<UserPasswordEntity> queryList(String userId);
}
