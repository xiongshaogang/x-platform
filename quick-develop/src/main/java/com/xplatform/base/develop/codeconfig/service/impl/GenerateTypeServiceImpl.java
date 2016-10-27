package com.xplatform.base.develop.codeconfig.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.develop.codeconfig.dao.GenerateTypeDao;
import com.xplatform.base.develop.codeconfig.entity.GenerateTypeEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateTypeService;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.util.TreeMapper;

@Service("generateTypeService")
public class GenerateTypeServiceImpl implements GenerateTypeService {

	@Resource
	private GenerateTypeDao gnerateTypeDao;


	public <T> void delete(T entity) {
		gnerateTypeDao.delete(entity);
	}

	public <T> Serializable save(T entity) {
		return gnerateTypeDao.save(entity);
	}

	public <T> void saveOrUpdate(T entity) {
		gnerateTypeDao.saveOrUpdate(entity);
	}

	/**
	 * 默认按钮-sql增强-新增操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doAddSql(GenerateTypeEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-更新操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doUpdateSql(GenerateTypeEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-删除操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doDelSql(GenerateTypeEntity t) {
		return true;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		gnerateTypeDao.getDataGridReturn(cq, b);
	}

	@Override
	public GenerateTypeEntity getEntity(Class<GenerateTypeEntity> class1, String id) {
		return gnerateTypeDao.getEntity(class1, id);
	}

	@Override
	public GenerateTypeEntity get(Class<GenerateTypeEntity> class1, String id) {
		// TODO Auto-generated method stub
		return gnerateTypeDao.get(class1, id);
	}

	@Override
	public List<TreeNode> transformTreeNode(
			List<GenerateTypeEntity> modelTypeEntityList) {
		// TODO Auto-generated method stub

		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isleaf");
		return TreeMapper.buildJsonTree(modelTypeEntityList, propertyMapping);
	}

	@Override
	public List<GenerateTypeEntity> findChildrenByParentID(String parentID) {
		// TODO Auto-generated method stub
		String hql = "";
		if (parentID == null) {
			hql = "FROM GenerateTypeEntity WHERE parentid is null";
		} else {
			hql = "FROM GenerateTypeEntity WHERE parentid ='" + parentID + "'";
		}

		return this.gnerateTypeDao.findByQueryString(hql);
	}
}