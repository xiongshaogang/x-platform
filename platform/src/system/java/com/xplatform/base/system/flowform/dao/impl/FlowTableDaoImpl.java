package com.xplatform.base.system.flowform.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.flowform.dao.FlowTableDao;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;


@Repository("flowTableDao")
public class FlowTableDaoImpl extends CommonDao implements FlowTableDao {
    @Override
	public String addFlowTable(FlowTableEntity FlowTable) {
		// TODO Auto-generated method stub
		return (String) this.save(FlowTable);
	}

	@Override
	public void deleteFlowTable(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(FlowTableEntity.class, id);
	}

	@Override
	public void updateFlowTable(FlowTableEntity FlowTable) {
		// TODO Auto-generated method stub
		this.updateEntitie(FlowTable);
	}

	@Override
	public FlowTableEntity getFlowTable(String id) {
		// TODO Auto-generated method stub
		return (FlowTableEntity) this.get(FlowTableEntity.class, id);
	}

	@Override
	public List<FlowTableEntity> queryFlowTableList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from FlowTableEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
