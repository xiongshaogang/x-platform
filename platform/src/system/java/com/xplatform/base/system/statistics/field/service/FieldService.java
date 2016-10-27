package com.xplatform.base.system.statistics.field.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;

public interface FieldService{
	
 	/**
	 * 新增数据源字段
	 */
	public String save(FieldEntity field) throws Exception;
	
	/**
	 * 删除数据源字段
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除数据源字段
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新数据源字段
	 */
	public void update(FieldEntity field) throws Exception;
	
	/**
	 * 查询一条数据源字段记录
	 */
	public FieldEntity get(String id);
	
	/**
	 * 查询数据源字段列表
	 */
	public List<FieldEntity> queryList();
	
	/**
	 * hibernate数据源字段分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	public void insertDataSourceField(String sql, String id) throws SQLException;

	public void deleteDataSourceField(String id);

	public List<FieldEntity> queryByDatasourceIdList(String datasourceId);

	public boolean judenSqlValid(String value);
}
