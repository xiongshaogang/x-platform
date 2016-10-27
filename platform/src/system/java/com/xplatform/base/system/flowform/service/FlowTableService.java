package com.xplatform.base.system.flowform.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;

public interface FlowTableService extends CommonService{
	
 	/**
	 * 新增
	 */
	public String save(FlowTableEntity flowTable) throws Exception;
	
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
	public void update(FlowTableEntity flowTable) throws Exception;
	
	/**
	 * 查询一条记录
	 */
	public FlowTableEntity get(String id);
	
	/**
	 * 查询列表
	 */
	public List<FlowTableEntity> queryList();
	
	/**
	 * hibernate分页列表
	 * @return 
	 */
    public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	/**取主表list*/
	public List<FlowTableEntity> getMainTableList();

	public boolean deployDb(FlowTableEntity flowTable) throws BusinessException;

	public void saveTable(FlowTableEntity flowTable, String string);
 	
}
