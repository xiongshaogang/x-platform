package com.xplatform.base.system.statistics.datasource.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.mybatis.engine.condition.CriterionBuilder;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.system.statistics.datasource.entity.DataSourceEntity;

public interface DataSourceService{
	
 	/**
	 * 新增数据源
	 */
	public String save(DataSourceEntity category) throws Exception;
	
	/**
	 * 删除数据源
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除数据源
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新数据源
	 */
	public void update(DataSourceEntity category) throws Exception;
	
	/**
	 * 查询一条数据源记录
	 */
	public DataSourceEntity get(String id);
	
	/**
	 * 查询数据源列表
	 */
	public List<DataSourceEntity> queryList();
	
	/**
	 * hibernate数据源分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	public List<DataSourceEntity> getList(UserEntity user);

	public String buildRelations(CriterionBuilder builder);

}
