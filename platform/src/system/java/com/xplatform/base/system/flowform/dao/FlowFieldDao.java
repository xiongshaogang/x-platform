package com.xplatform.base.system.flowform.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;

public interface FlowFieldDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addFlowField(FlowFieldEntity flowField);
	
	/**
	 * @Decription 删除
	 */
	public void deleteFlowField(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateFlowField (FlowFieldEntity flowField);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public FlowFieldEntity getFlowField(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<FlowFieldEntity> queryFlowFieldList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
