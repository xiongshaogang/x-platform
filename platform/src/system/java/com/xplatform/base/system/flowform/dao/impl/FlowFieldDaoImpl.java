package com.xplatform.base.system.flowform.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.flowform.dao.FlowFieldDao;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;


@Repository("flowFieldDao")
public class FlowFieldDaoImpl extends CommonDao implements FlowFieldDao {
    @Override
	public String addFlowField(FlowFieldEntity FlowField) {
		// TODO Auto-generated method stub
		return (String) this.save(FlowField);
	}

	@Override
	public void deleteFlowField(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(FlowFieldEntity.class, id);
	}

	@Override
	public void updateFlowField(FlowFieldEntity FlowField) {
		// TODO Auto-generated method stub
		this.updateEntitie(FlowField);
	}

	@Override
	public FlowFieldEntity getFlowField(String id) {
		// TODO Auto-generated method stub
		return (FlowFieldEntity) this.get(FlowFieldEntity.class, id);
	}

	@Override
	public List<FlowFieldEntity> queryFlowFieldList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from FlowFieldEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
