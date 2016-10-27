package com.xplatform.base.system.flowform.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;

public interface FlowFieldService{
	
 	/**
	 * 新增
	 */
	public String save(FlowFieldEntity flowField) throws Exception;
	
	/**
	 * 删除
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新
	 */
	public void update(FlowFieldEntity flowField) throws Exception;
	
	/**
	 * 查询一条记录
	 */
	public FlowFieldEntity get(String id);
	
	/**
	 * 查询列表
	 */
	public List<FlowFieldEntity> queryList();
	
	/**
	 * hibernate分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	public List<FlowFieldEntity> queryListByTableId(String tableId);
 	
}
