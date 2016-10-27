package com.xplatform.base.develop.codeconfig.service;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.develop.codeconfig.entity.GenerateTypeEntity;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.TreeNode;

public interface GenerateTypeService {

	public <T> void delete(T entity);

	public <T> Serializable save(T entity);

	public <T> void saveOrUpdate(T entity);

	public boolean doAddSql(GenerateTypeEntity t);

	public boolean doUpdateSql(GenerateTypeEntity t);

	public boolean doDelSql(GenerateTypeEntity t);

	GenerateTypeEntity getEntity(Class<GenerateTypeEntity> class1, String id);

	GenerateTypeEntity get(Class<GenerateTypeEntity> class1, String id);

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月13日 上午11:34:12
	 * @Decription
	 *
	 * @param modelTypeEntityList
	 * @return
	 */

	public List<TreeNode> transformTreeNode(
			List<GenerateTypeEntity> modelTypeEntityList);

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月13日 上午11:34:15
	 * @Decription 通过父ID查找子记录
	 *
	 * @param parentID
	 * @return
	 */
	public List<GenerateTypeEntity> findChildrenByParentID(String parentID);
}
