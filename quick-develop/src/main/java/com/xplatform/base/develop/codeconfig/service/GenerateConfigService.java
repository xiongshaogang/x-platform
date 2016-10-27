package com.xplatform.base.develop.codeconfig.service;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.develop.codeconfig.entity.GenerateConfigEntity;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;

public interface GenerateConfigService {

	public <T> void delete(T entity);

	public <T> Serializable save(T entity);

	public <T> void saveOrUpdate(T entity);

	GenerateConfigEntity getEntity(Class<GenerateConfigEntity> class1, String id);

	GenerateConfigEntity get(Class<GenerateConfigEntity> class1, String id);

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月15日 上午11:33:41
	 * @Decription 生成页面模型记录主表ID
	 *
	 * @param formTypeID
	 * @return
	 */

	public List<GenerateConfigEntity> findBaseFormEntityListByFormTypeID(
			String formTypeID);

	public GenerateConfigEntity getBaseFormEntityByConId(String con_Id, String type_id);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月19日 下午3:53:32
	 * @Decription 查找表单Id下是否已存在字段表记录
	 *
	 * @param entityId
	 */

	public boolean isExsitsFormFieldEntity(String entityId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月19日 下午3:39:59
	 * @Decription 删除表单实体Id下的所有字段记录
	 *
	 * @param entityId
	 */

	public Integer deleteFieldsByEntityId(String entityId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月15日 下午6:44:06
	 * @Decription 通过自动生成实体类Id查找实体类的字段
	 *
	 * @param entityId
	 *            CgFormHeadEntity的Id
	 * @return
	 */
	public List<MetaDataFieldEntity> findCgFormFieldEntityListByEntityId(
			String entityId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月19日 下午3:50:14
	 * @Decription 通过CgFormFieldEntity的列表自动产生FormFieldEntity的列表
	 *             (把CgFormFieldEntity的一部分值set到FormFieldEntity中)
	 *
	 * @param cgFormFieldEntityList
	 */

	public void saveInitFormFieldEntitys(
			List<MetaDataFieldEntity> cgFormFieldEntityList,String baseFormEntityId);
	

	
	/**
	 * @author xiaqiang
	 * @createtime 2014年5月15日 下午9:10:28
	 * @Decription 通过表单实体Id查找生成实体Id
	 *
	 * @param formEntityId 表单实体Id
	 * @return
	 */
		
	public String findCgEntityIdByFormEntityId(String formEntityId);
}
