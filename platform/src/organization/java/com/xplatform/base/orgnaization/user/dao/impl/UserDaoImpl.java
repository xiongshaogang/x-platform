package com.xplatform.base.orgnaization.user.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
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
@Repository("userDao")
public class UserDaoImpl extends CommonDao implements UserDao {

	@Override
	public String addUser(UserEntity user) {
		// TODO Auto-generated method stub
		return (String) this.save(user);
	}

	@Override
	public void deleteUser(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(UserEntity.class, id);
	}

	@Override
	public void updateUser(UserEntity user) {
		// TODO Auto-generated method stub
		this.merge(user);
	}

	@Override
	public UserEntity getUser(String id) {
		// TODO Auto-generated method stub
		return (UserEntity) this.get(UserEntity.class, id);
	}

	@Override
	public List<UserEntity> queryUserList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from UserEntity");
	}

	@Override
	public List<UserRoleEntity> getUserRole(Map<String, String> param) {
		// TODO Auto-generated method stub
		return this.findHql("from UserRoleEntity ur where ur.user.id=?",param.get("userId"));
	}
	
	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	@Override
	public UserEntity getUser(String phone, boolean flag) {
		// TODO Auto-generated method stub
		String hql = "";
		if(flag){
			 hql = "from UserEntity t where t.phone = ?";
		}
		return (UserEntity)this.findUniqueByHql(hql, phone);
	}

	

}
