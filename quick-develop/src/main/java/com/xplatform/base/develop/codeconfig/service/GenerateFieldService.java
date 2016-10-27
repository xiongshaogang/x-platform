package com.xplatform.base.develop.codeconfig.service;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;

public interface GenerateFieldService {

	public void delete(GenerateFieldEntity entity);

	public Serializable save(GenerateFieldEntity entity);

	public void saveOrUpdate(GenerateFieldEntity entity);

	GenerateFieldEntity getEntity(Class<GenerateFieldEntity> class1, String id);

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月15日 下午4:38:13
	 * @Decription 通过表单实体Id查找表单实体字段列表
	 *
	 * @param entityId
	 *            表单实体Id
	 * @return
	 */

	public List<GenerateFieldEntity> findFormFieldEntityListByEntityId(
			String entityId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月23日 下午4:14:11
	 * @Decription 查找预设验证类型表(所有记录)
	 *
	 * @return
	 */

//注释 by lxt 20150507 注释原因 删除	FormRuleEntity类
//	public List<FormRuleEntity> findAllFormRuleEntityList();

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月26日 下午2:40:48
	 * @Decription 通过页面获得的实体,一个方法进行实体的增或改
	 *
	 * @param newEntity 从页面获得的实体
	 * @throws Exception
	 */
	public void saveOrUpdateFromPage(GenerateFieldEntity newEntity)
			throws Exception;
}
