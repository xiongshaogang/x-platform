package com.xplatform.base.develop.formrule.service;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.develop.formrule.entity.FormRuleEntity;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;

public interface FormRuleService{
	
 	public <T> void delete(String id);
 	
 	public <T> Serializable save(T entity);
 	
 	public <T> void update(T entity);
 	
 	FormRuleEntity getEntity(Class<FormRuleEntity> class1, String id);
 	
 	void getDataGridReturn(CriteriaQuery cq, boolean b);
 	
 	public void initCache();

	public List<FormRuleEntity> queryAll();
}
