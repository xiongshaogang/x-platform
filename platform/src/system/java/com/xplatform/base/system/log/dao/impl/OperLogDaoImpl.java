package com.xplatform.base.system.log.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.log.dao.OperLogDao;
import com.xplatform.base.system.log.entity.OperLogEntity;


@Repository("operLogDao")
public class OperLogDaoImpl extends CommonDao implements OperLogDao {
    @Override
	public String addOperLog(OperLogEntity OperLog) {
		// TODO Auto-generated method stub
		return (String) this.save(OperLog);
	}

	@Override
	public void deleteOperLog(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(OperLogEntity.class, id);
	}

	@Override
	public void updateOperLog(OperLogEntity OperLog) {
		// TODO Auto-generated method stub
		this.updateEntitie(OperLog);
	}

	@Override
	public OperLogEntity getOperLog(String id) {
		// TODO Auto-generated method stub
		return (OperLogEntity) this.get(OperLogEntity.class, id);
	}

	@Override
	public List<OperLogEntity> queryOperLogList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from OperLogEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
