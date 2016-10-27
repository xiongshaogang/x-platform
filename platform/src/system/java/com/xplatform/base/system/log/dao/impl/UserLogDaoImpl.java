package com.xplatform.base.system.log.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.dao.UserLogDao;
import com.xplatform.base.system.log.entity.UserLogEntity;


@Repository("userLogDao")
public class UserLogDaoImpl extends CommonDao implements UserLogDao {
    @Override
	public String addUserLog(UserLogEntity UserLog) {
		// TODO Auto-generated method stub
		return (String) this.save(UserLog);
	}

	@Override
	public void deleteUserLog(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(UserLogEntity.class, id);
	}

	@Override
	public void updateUserLog(UserLogEntity UserLog) {
		// TODO Auto-generated method stub
		this.updateEntitie(UserLog);
	}

	@Override
	public UserLogEntity getUserLog(String id) {
		// TODO Auto-generated method stub
		return (UserLogEntity) this.get(UserLogEntity.class, id);
	}

	@Override
	public List<UserLogEntity> queryUserLogList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from UserLogEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
