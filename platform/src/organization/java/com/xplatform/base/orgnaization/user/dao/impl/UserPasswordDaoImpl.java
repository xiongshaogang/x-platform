package com.xplatform.base.orgnaization.user.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.orgnaization.user.dao.UserPasswordDao;
import com.xplatform.base.orgnaization.user.entity.UserPasswordEntity;

@Repository("userPasswordDao")
public class UserPasswordDaoImpl extends CommonDao implements UserPasswordDao {

	/**
	 * 保存用户密码信息
	 * @author luoheng
	 * @param userPassword
	 * @return
	 */
	@Override
	public String addUserPassword(UserPasswordEntity userPassword) {
		return (String) this.save(userPassword);
	}
	
	/**
	 * 修改密码信息
	 * @author luoheng
	 * @param userPassword
	 */
	@Override
	public void updateUserPassword(UserPasswordEntity userPassword){
		this.merge(userPassword);
	}
	
	/**
	 * 逻辑删除密码信息
	 * @author luoheng
	 * @param id
	 */
	@Override
	public void deleteUserPassword(String id){
		UserPasswordEntity userPassword = this.getUserPassword(id);
		userPassword.setFlag("N");
		this.merge(userPassword);
	}
	
	/**
	 * 根据ID获取密码信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public UserPasswordEntity getUserPassword(String id) {
		return (UserPasswordEntity) this.get(UserPasswordEntity.class, id);
	}
	
	/**
	 * 根据用户ID查找有效密码
	 * @author luoheng
	 * @param userId
	 * @return
	 */
	@Override
	public UserPasswordEntity getUserPasswordByUserId(String userId) {
		String hql = "from UserPasswordEntity where userId = ? and flag = 'Y'";
		List<UserPasswordEntity> userPasswordList = this.findHql(hql, new Object[]{userId});
		if(userPasswordList != null && userPasswordList.size() > 0){
			return userPasswordList.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 查找该用户所有密码并进行时间排序
	 * @param userId
	 * @return
	 */
	@Override
	public List<UserPasswordEntity> queryList(String userId){
		String hql = "from UserPasswordEntity where userId = ? order by updatePasswordTime desc";
		List<UserPasswordEntity> userPasswordList = this.findHql(hql, new Object[]{userId});
		return userPasswordList;
	}
}
