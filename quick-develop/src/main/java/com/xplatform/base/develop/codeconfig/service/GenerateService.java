package com.xplatform.base.develop.codeconfig.service;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.develop.codeconfig.entity.ConBaseFormEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateEntity;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.TreeNode;

public interface GenerateService {

	public  void delete(GenerateEntity entity);

	public <T> Serializable save(T entity);

	public <T> void saveOrUpdate(T entity);

	<T> T getEntity(Class<T> class1, String id);

	GenerateEntity get(Class<GenerateEntity> class1, String id);

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	public List<ConBaseFormEntity> queryModelData(String id);

	public List<TreeNode> transformTreeNode(
			List<ConBaseFormEntity> conBaseFormEntityList);

	public void update(GenerateEntity f);

}
