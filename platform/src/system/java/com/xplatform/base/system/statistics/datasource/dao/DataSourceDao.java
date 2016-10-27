package com.xplatform.base.system.statistics.datasource.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.datasource.entity.DataSourceEntity;

public interface DataSourceDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addDataSource(DataSourceEntity dataSource);
	
	/**
	 * @Decription 删除
	 */
	public void deleteDataSource(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateDataSource (DataSourceEntity dataSource);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public DataSourceEntity getDataSource(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<DataSourceEntity> queryDataSourceList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
