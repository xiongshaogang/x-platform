package com.xplatform.base.orgnaization.user.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.dao.UserRoleDao;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
/**
 * 
 * description :分组dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("userRoleDao")
public class UserRoleDaoImpl extends CommonDao implements UserRoleDao {

	@Override
	public String addUserRole(UserRoleEntity userRoleEntity) {
		return (String) this.save(userRoleEntity);
	}

	@Override
	public void deleteUserRole(String id) {
		this.deleteEntityById(UserRoleEntity.class, id);
	}

	@Override
	public void updateUserRole(UserRoleEntity userRoleEntity) {
		this.updateEntitie(userRoleEntity);
	}

	@Override
	public UserRoleEntity getUserRole(String id) {
		return (UserRoleEntity) this.get(UserRoleEntity.class, id);
	}
	
	@Override
	public UserRoleEntity getUserRole(String userId, String roleId){
		String hql = " from UserRoleEntity where userId = ? and roleId = ? ";
		Object obj = this.findUniqueByHql(hql, userId,roleId);
		UserRoleEntity userRole = null;
		if(obj != null){
			userRole = (UserRoleEntity)obj;
		}
		return userRole;
	}

	@Override
	public List<UserRoleEntity> queryUserRoleList() {
		return this.findByQueryString("from UserRoleEntity");
	}
	
	@Override
	public List<UserRoleEntity> queryUserRoleByUserIdList(String userId) {
		String hql = " from UserRoleEntity where user_id = ? ";
		return this.findHql(hql, new Object[]{userId});
	}
	
	@Override
	public List<UserRoleEntity> queryUserRoleByRoleIdList(String roleId) {
		String hql = " from UserRoleEntity where role_id = ? ";
		return this.findHql(hql, new Object[]{roleId});
	}
	
	/**
	 * 根据用户ID删除用户所属角色
	 * @author luoheng
	 * @param userId
	 */
	@Override
	public void deleteUserRoleByUserId(String userId){
		String hql = "delete from UserRoleEntity ur where ur.user.id = :userId";
		Query queryObject = this.getSession().createQuery(hql);
		queryObject.setParameter("userId", userId);
		queryObject.executeUpdate();
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		this.getDataGridReturn(cq, false);
	}

}
