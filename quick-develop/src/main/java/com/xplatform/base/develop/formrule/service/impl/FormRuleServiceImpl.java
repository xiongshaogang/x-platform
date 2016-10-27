package com.xplatform.base.develop.formrule.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.develop.formrule.dao.FormRuleDao;
import com.xplatform.base.develop.formrule.entity.FormRuleEntity;
import com.xplatform.base.develop.formrule.service.FormRuleService;
import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;


@Service("formRuleService")
public class FormRuleServiceImpl implements FormRuleService {

    @Resource
	private FormRuleDao formRuleDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private UcgCacheManager ucgCacheManager;
	
	private UcgCache ucgCache;
	
	public void setUcgCacheManager(UcgCacheManager ucgCacheManager) {
		this.ucgCacheManager = ucgCacheManager;
	}

	public void setFormRuleDao(FormRuleDao formRuleDao) {
		this.formRuleDao = formRuleDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	
	public void initCache(){
		ucgCache=ucgCacheManager.getDictCacheBean();
		ucgCache.put("system_setting", "word");
		System.out.print(ucgCache.get("dict"));
	}

	
 	public <T> void delete(String id) {
 		FormRuleEntity formRule = this.getEntity(FormRuleEntity.class, id);
		formRuleDao.delete(formRule);
 	}
 	
 	public <T> Serializable save(T entity) {
 		return formRuleDao.save(entity);
 	}
 	
 	public <T> void update(T entity) {
 		formRuleDao.merge(entity);		
 	}
 	
 	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
 		formRuleDao.getDataGridReturn(cq, b);
	}

	@Override
	public FormRuleEntity getEntity(Class<FormRuleEntity> class1, String id) {
		return formRuleDao.getEntity(class1, id);
	}

	@Override
	public List<FormRuleEntity> queryAll() {
		// TODO Auto-generated method stub
		String hql = "FROM FormRuleEntity";
		return this.formRuleDao.findByQueryString(hql);
	}
 	
}