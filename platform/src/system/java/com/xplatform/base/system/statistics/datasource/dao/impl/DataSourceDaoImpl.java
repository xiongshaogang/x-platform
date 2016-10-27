package com.xplatform.base.system.statistics.datasource.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.datasource.dao.DataSourceDao;
import com.xplatform.base.system.statistics.datasource.entity.DataSourceEntity;


@Repository("dataSourceDao")
public class DataSourceDaoImpl extends CommonDao implements DataSourceDao {
    @Override
	public String addDataSource(DataSourceEntity DataSource) {
		// TODO Auto-generated method stub
		return (String) this.save(DataSource);
	}

	@Override
	public void deleteDataSource(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(DataSourceEntity.class, id);
	}

	@Override
	public void updateDataSource(DataSourceEntity DataSource) {
		// TODO Auto-generated method stub
		this.merge(DataSource);
	}

	@Override
	public DataSourceEntity getDataSource(String id) {
		// TODO Auto-generated method stub
		return (DataSourceEntity) this.get(DataSourceEntity.class, id);
	}

	@Override
	public List<DataSourceEntity> queryDataSourceList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from DataSourceEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
