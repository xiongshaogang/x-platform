package com.xplatform.base.system.statistics.field.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;

public interface FieldDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addField(FieldEntity field);
	
	/**
	 * @Decription 删除
	 */
	public void deleteField(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateField (FieldEntity field);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public FieldEntity getField(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<FieldEntity> queryFieldList();
	
	/**
	 * 
	 * @Decription hibernate分页查询
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);

	public List<FieldEntity> queryByDatasourceIdList(String datasourceId);
}
