package com.xplatform.base.orgnaization.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.orgnaization.user.dao.UserPasswordDao;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;
import com.xplatform.base.orgnaization.user.service.UserPasswordService;

@Service("userPasswordService")
public class UserPasswordServiceImpl implements UserPasswordService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserPasswordDao userPasswordDao;
	
	/**
	 * 保存用户密码信息
	 * @author luoheng
	 * @param userPassword
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String save(UserPasswordEntity userPassword) throws BusinessException {
		String pk = "";
		try {
			userPassword.setUpdatePasswordTime(new Date(System.currentTimeMillis()));
			userPassword.setFlag("Y");
			pk = userPasswordDao.addUserPassword(userPassword);
			logger.error("密码保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("密码保存失败");
			throw new BusinessException("密码保存失败");
		}
		return pk;
	}
	
	/**
	 * 修改密码信息
	 * @author luoheng
	 * @param userPassword
	 * @throws BusinessException
	 */
	@Override
	public void update(UserPasswordEntity userPassword) throws BusinessException {
		try {
			//更新原密码
			UserPasswordEntity userPasswordEntity = this.getUserPasswordByUserId(userPassword.getUser().getId());
			if(userPasswordEntity != null){
				userPasswordEntity.setUpdatePasswordTime(new Date(System.currentTimeMillis()));
				userPasswordEntity.setFlag("N");
				userPasswordDao.updateUserPassword(userPasswordEntity);
			}
			
			//保存新密码
			this.save(userPassword);
			
			logger.error("密码修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("密码修改失败");
			throw new BusinessException("密码修改失败");
		}
	}
	
	/**
	 * 逻辑删除密码信息
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void deleteUserPassword(String id) throws BusinessException {
		try {
			userPasswordDao.deleteUserPassword(id);
			logger.error("密码删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("密码删除失败");
			throw new BusinessException("密码删除失败");
		}
	}
	
	/**
	 * 根据ID获取密码信息
	 * @author luoheng
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public UserPasswordEntity getUserPassword(String id) throws BusinessException {
		UserPasswordEntity userPassword = null;
		try {
			userPassword = userPasswordDao.getUserPassword(id);
			logger.error("获取密码信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取密码信息失败");
			throw new BusinessException("获取密码信息失败");
		}
		return userPassword;
	}
	
	/**
	 * 根据用户ID查找有效密码
	 * @author luoheng
	 * @param userId
	 * @return
	 */
	public UserPasswordEntity getUserPasswordByUserId(String userId)  {
		UserPasswordEntity userPassword = null;
		userPassword = userPasswordDao.getUserPasswordByUserId(userId);
		return userPassword;
	}
	
	/**
	 * 查找该用户所有密码并进行时间排序
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<UserPasswordEntity> queryList(String userId) {
		List<UserPasswordEntity> userPasswordList = userPasswordDao.queryList(userId);
		return userPasswordList;
	}
	
	/**
	 * 根据属性获取密码信息
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<UserPasswordEntity> queryListByPorperty(String propertyName, String value){
		List<UserPasswordEntity> userPasswordList = new ArrayList<UserPasswordEntity>();
		userPasswordList = userPasswordDao.findByProperty(UserPasswordEntity.class, propertyName, value);
		return userPasswordList;
	}

	public void setUserPasswordDao(UserPasswordDao userPasswordDao) {
		this.userPasswordDao = userPasswordDao;
	}

	@Override
	public UserPasswordEntity getLastPass(String userId) {
		String hql = "from UserPasswordEntity where userId=? and flag='Y' ";
		return this.userPasswordDao.findUniqueByHql(hql, userId);
	}
}
