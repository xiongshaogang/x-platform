package com.xplatform.base.system.flowform.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;

public interface FlowTableDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addFlowTable(FlowTableEntity flowTable);
	
	/**
	 * @Decription 删除
	 */
	public void deleteFlowTable(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateFlowTable (FlowTableEntity flowTable);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public FlowTableEntity getFlowTable(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<FlowTableEntity> queryFlowTableList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
