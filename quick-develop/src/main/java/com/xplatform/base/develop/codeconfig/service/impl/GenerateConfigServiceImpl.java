package com.xplatform.base.develop.codeconfig.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.develop.codeconfig.dao.GenerateConfigDao;
import com.xplatform.base.develop.codeconfig.entity.GenerateConfigEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateConfigService;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.StringUtil;

@Service("generateConfigService")
public class GenerateConfigServiceImpl implements GenerateConfigService {

	@Resource
	private GenerateConfigDao generateConfigDao;

	public <T> void delete(T entity) {
		generateConfigDao.delete(entity);
	}
	
	@Action(description = "模板表单实体配置保存", detail = "代码生成器模板表${cgFormHeadService.getTableName(entityId)}配置保存成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public <T> Serializable save(T entity) {
		return generateConfigDao.save(entity);
	}

	@Action(description = "模板表单实体配置修改", detail = "代码生成器模板表${cgFormHeadService.getTableName(entityId)}配置修改成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public <T> void saveOrUpdate(T entity) {
		generateConfigDao.saveOrUpdate(entity);
	}
	
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		generateConfigDao.getDataGridReturn(cq, b);
	}

	@Override
	public GenerateConfigEntity getEntity(Class<GenerateConfigEntity> class1, String id) {
		return generateConfigDao.getEntity(class1, id);
	}

	@Override
	public GenerateConfigEntity get(Class<GenerateConfigEntity> class1, String id) {
		// TODO Auto-generated method stub
		return generateConfigDao.get(class1, id);
	}

	@Override
	public GenerateConfigEntity getBaseFormEntityByConId(String con_Id, String type_id) {
		List<GenerateConfigEntity> BaseFormEntity = generateConfigDao.findByProperty(
				GenerateConfigEntity.class, "conBaseFormId", con_Id);
		for (GenerateConfigEntity b : BaseFormEntity) {
			if (StringUtil.equals(type_id, b.getFormTypeEntity().getId())
					&& StringUtil.isNotEmpty(type_id)) {
				return b;
			}
		}
		return null;
	}

	@Override
	public List<GenerateConfigEntity> findBaseFormEntityListByFormTypeID(
			String formTypeID) {
		// TODO Auto-generated method stub
		String hql = "FROM GenerateConfigEntity WHERE formTypeEntity='" + formTypeID
				+ "'";
		return generateConfigDao.findByQueryString(hql);
	}

	@Override
	public Integer deleteFieldsByEntityId(String entityId) {
		// TODO Auto-generated method stub
		String hql = "DELETE FROM GenerateFieldEntity WHERE formEntityId='"
				+ entityId + "'";
		return generateConfigDao.executeHql(hql);
	}

	public List<MetaDataFieldEntity> findCgFormFieldEntityListByEntityId(
			String entityId) {
		String hql = "FROM MetaDataFieldEntity WHERE table='" + entityId + "'";
		return generateConfigDao.findByQueryString(hql);
	}

	@Override
	public boolean isExsitsFormFieldEntity(String entityId) {
		// TODO Auto-generated method stub
		String hql = "FROM GenerateFieldEntity WHERE formEntityId='" + entityId
				+ "'";
		List<GenerateFieldEntity> formFieldEntityList = generateConfigDao
				.findByQueryString(hql);

		return formFieldEntityList.size() > 0 ? true : false;
	}
	
	@Override
	@Action(description = "同步表字段信息", detail = "代码生成器同步${cgFormHeadService.getTableName(entityId)}表字段信息进入表t_base_form_field成功", execOrder = ActionExecOrder.AFTER, moduleCode = "templateManager")
	public void saveInitFormFieldEntitys(
			List<MetaDataFieldEntity> cgFormFieldEntityList,
			String baseFormEntityId) {
		// TODO Auto-generated method stub
		List<GenerateFieldEntity> formFieldEntityList = new ArrayList<GenerateFieldEntity>();
		for (int i = 0; i < cgFormFieldEntityList.size(); i++) {
			GenerateFieldEntity formFieldEntity = new GenerateFieldEntity();
			MetaDataFieldEntity cgFormFieldEntity = cgFormFieldEntityList.get(i);
			// 构造BaseFormEntity实体
			GenerateConfigEntity baseFormEntity = new GenerateConfigEntity();
			baseFormEntity.setId(baseFormEntityId);
			formFieldEntity.setFormEntityId(baseFormEntity);

			formFieldEntity.setFieldEntityId(cgFormFieldEntity);
			formFieldEntity.setFieldName(cgFormFieldEntity.getFieldName());
			formFieldEntity.setFieldLabel(cgFormFieldEntity.getContent());
			formFieldEntity.setFieldType(cgFormFieldEntity.getType());
			formFieldEntity.setFieldLength(cgFormFieldEntity.getLength());
			formFieldEntity.setFieldPersion(cgFormFieldEntity.getPointLength());
			formFieldEntity.setIsUnique("N");
			formFieldEntity.setListShow("N");
			formFieldEntity.setQueryShow("N");
			formFieldEntity.setEditShow("N");
			formFieldEntity.setIsReadonly("N");
			formFieldEntity.setAlignDict(null);
			formFieldEntity.setGridIndex(i);
			formFieldEntity.setIsNullable("Y");
			formFieldEntity.setIsOrder("Y");
			formFieldEntity.setPageRowspan(1);
			formFieldEntity.setQueryShow("N");
			formFieldEntityList.add(formFieldEntity);
		}
		generateConfigDao.batchSave(formFieldEntityList);
	}

	@Override
	public String findCgEntityIdByFormEntityId(String formEntityId) {
		String hql = "SELECT entityId FROM GenerateConfigEntity t WHERE t.id='"
				+ formEntityId + "'";
		List<Object> objList = generateConfigDao.findByQueryString(hql);
		String objStr = null;
		if (objList.size() > 0) {
			objStr = objList.get(0).toString();
		}
		return objStr;
	}
}
