package com.xplatform.base.develop.codeconfig.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.develop.codeconfig.dao.GenerateFieldDao;
import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateFieldService;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;

@Service("generateFieldService")
public class GenerateFieldServiceImpl implements GenerateFieldService {
	@Resource
	private GenerateFieldDao generateFieldDao;


	@Override
	public void delete(GenerateFieldEntity entity) {
		generateFieldDao.delete(entity);
	}

	@Override
	public Serializable save(GenerateFieldEntity entity) {

		return generateFieldDao.save(entity);
	}

	@Override
	public void saveOrUpdate(GenerateFieldEntity entity) {
		generateFieldDao.saveOrUpdate(entity);
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		generateFieldDao.getDataGridReturn(cq, b);
	}

	@Override
	public GenerateFieldEntity getEntity(Class<GenerateFieldEntity> class1, String id) {
		return generateFieldDao.getEntity(class1, id);
	}

	@Override
	public List<GenerateFieldEntity> findFormFieldEntityListByEntityId(
			String entityId) {
		// TODO Auto-generated method stub
		String hql = "FROM FormFieldEntity WHERE formEntityId='" + entityId
				+ "'";
		return generateFieldDao.findByQueryString(hql);
	}

	/*
	 * 注释 by lxt 20150507 
	 * 注释原因：删除FormRuleEntity类
	@Override
	public List<FormRuleEntity> findAllFormRuleEntityList() {
		// TODO Auto-generated method stub
		String hql = "FROM FormRuleEntity";
		return generateFieldDao.findHql(hql, null);
	} */

	@Override
	@Action(description = "表字段信息保存", detail = "代码生成器表字段${fieldName}保存成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public void saveOrUpdateFromPage(GenerateFieldEntity newEntity)
			throws Exception {
		GenerateFieldEntity executeEntity = newEntity;
		if (StringUtil.isNotEmpty(newEntity.getId())) {
			GenerateFieldEntity oldEntity = generateFieldDao.getEntity(
					GenerateFieldEntity.class, newEntity.getId());
			MyBeanUtils.copyBeanNotNull2Bean(newEntity, oldEntity);
			executeEntity = oldEntity;
		}
		generateFieldDao.saveOrUpdate(executeEntity);
	}
}