package com.xplatform.base.develop.codeconfig.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.develop.codeconfig.dao.GenerateDao;
import com.xplatform.base.develop.codeconfig.entity.ConBaseFormEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateService;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.TreeMapper;

@Service("generateService")
public class GenerateServiceImpl implements GenerateService {
	@Resource
	private GenerateDao generateDao;

	@Action(description = "代码生成器模板删除", detail = "代码生成器模板${params[0].name}删除成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public void delete(GenerateEntity entity) {
		// super.delete(entity);
		// 执行删除操作配置的sql增强
		// this.doDelSql((TBaseModelTypeEntity)entity);

		generateDao.delete(entity);
	}

	@Action(description = "代码生成器模板新增", detail = "代码生成器模板${name}新增成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public <T> Serializable save(T entity) {

		return generateDao.save(entity);
	}

	@Action(description = "代码生成器模板更新", detail = "代码生成器模板${name}更新成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public <T> void saveOrUpdate(T entity) {
		// super.saveOrUpdate(entity);
		// 执行更新操作配置的sql增强
		// this.doUpdateSql((TBaseModelTypeEntity)entity);

		generateDao.saveOrUpdate(entity);
	}
	
	@Override
	@Action(description = "代码生成器模板更新", detail = "代码生成器模板${name}更新成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public void update(GenerateEntity f) {
		// TODO Auto-generated method stub
		generateDao.updateEntitie(f);
	}


	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		generateDao.getDataGridReturn(cq, b);
	}

	@Override
	public <T> T getEntity(Class<T> class1, String id) {
		return generateDao.getEntity(class1, id);
	}

	@Override
	public GenerateEntity get(Class<GenerateEntity> class1, String id) {
		// TODO Auto-generated method stub
		return generateDao.get(class1, id);
	}

	@Override
	public List<ConBaseFormEntity> queryModelData(String id) {
		// TODO Auto-generated method stub
		String hql = "FROM ConBaseFormEntity WHERE base_model_id ='" + id + "'";

		return this.generateDao.findByQueryString(hql);
	}

	@Override
	public List<TreeNode> transformTreeNode(
			List<ConBaseFormEntity> conBaseFormEntityList) {
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isleaf");
		return TreeMapper.buildJsonTree(conBaseFormEntityList, propertyMapping);
	}

}