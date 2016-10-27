package com.xplatform.base.develop.metadata.dao;

import java.util.List;

import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;

public interface MetaDataFieldDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addFormField(MetaDataFieldEntity formField);
	
	/**
	 * @Decription 删除
	 */
	public void deleteFormField(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateFormField (MetaDataFieldEntity formField);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public MetaDataFieldEntity getFormField(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<MetaDataFieldEntity> queryFormFieldList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
}
