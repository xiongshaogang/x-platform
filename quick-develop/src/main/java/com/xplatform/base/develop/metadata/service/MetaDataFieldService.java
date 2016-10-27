package com.xplatform.base.develop.metadata.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;

public interface MetaDataFieldService{
	
 	/**
	 * 新增表字段
	 */
	public String save(MetaDataFieldEntity formField) throws Exception;
	
	/**
	 * 删除表字段
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除表字段
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新表字段
	 */
	public void update(MetaDataFieldEntity formField) throws Exception;
	
	/**
	 * 查询一条表字段记录
	 */
	public MetaDataFieldEntity get(String id);
	
	/**
	 * 查询表字段列表
	 */
	public List<MetaDataFieldEntity> queryList();
	
	/**
	 * hibernate表字段分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
 	
}
