package com.xplatform.base.orgnaization.user.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.dao.UserOrgDao;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;

@SuppressWarnings("unchecked")
@Repository("userJobDao")
public class UserOrgDaoImpl extends CommonDao implements UserOrgDao {

	@Override
	public String addUserJob(UserOrgEntity UserOrgEntity) {
		return (String) this.save(UserOrgEntity);
	}

	@Override
	public void deleteUserJob(String id) {
		this.deleteEntityById(UserOrgEntity.class, id);
	}

	@Override
	public void updateUserJob(UserOrgEntity UserOrgEntity) {
		this.merge(UserOrgEntity);
	}

	@Override
	public UserOrgEntity getUserJob(String id) {
		return (UserOrgEntity) this.get(UserOrgEntity.class, id);
	}

	@Override
	public List<UserOrgEntity> queryUserJobList() {
		return this.findByQueryString("from UserOrgEntity");
	}
	
	@Override
	public List<UserOrgEntity> queryUserJobListByUserId(String UserId){
		String sql = "from UserOrgEntity where userId = ?";
		List<UserOrgEntity> UserJobList = this.findHql(sql, new Object[]{UserId});
		return UserJobList;
	}
	
	@Override
	public List<UserOrgEntity> queryUserJobListByJobId(String jobId){
		String sql = "from UserOrgEntity where orgId = ?";
		List<UserOrgEntity> UserJobList = this.findHql(sql, new Object[]{jobId});
		return UserJobList;
	}
	
	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		this.getDataGridReturn(cq, false);
	}

}
