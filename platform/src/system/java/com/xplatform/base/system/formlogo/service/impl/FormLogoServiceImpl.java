package com.xplatform.base.system.formlogo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.dao.BaseDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.orggroup.dao.OrgGroupDao;
import com.xplatform.base.system.formlogo.dao.FormLogoDao;
import com.xplatform.base.system.formlogo.entity.FormLogoEntity;
import com.xplatform.base.system.formlogo.service.FormLogoService;

@Service("formLogoService")
public class FormLogoServiceImpl extends BaseServiceImpl<FormLogoEntity> implements FormLogoService{
	
	@Resource
	private FormLogoDao formLogoDao;
	

	@Resource
	public void setBaseDao(FormLogoDao formLogoDao) {
		super.setBaseDao(formLogoDao);
	}

	@Override
	public String saveFormLogo(FormLogoEntity FormLogo) throws BusinessException {
		return (String)this.formLogoDao.save(FormLogo);
	}

	@Override
	public void updateFormLogo(FormLogoEntity FormLogo) throws BusinessException {
		FormLogoEntity oldEntity = formLogoDao.get(FormLogo.getId());
		if(oldEntity != null){
			MyBeanUtils.copyBeanNotNull2Bean(FormLogo,oldEntity);
			this.formLogoDao.updateEntitie(oldEntity);
		}
		
	}

	@Override
	public String deleteFormLogo(String id) throws BusinessException {
		String message = "删除成功";
		this.formLogoDao.deleteEntityById(id);
		return message;
	}

	@Override
	public List<FormLogoEntity> queryList() throws BusinessException {
		String hql = "from FormLogoEntity where name Not IN('app_task.png','app_apply.png')";
		return this.formLogoDao.findByQueryString(hql);
	}

	@Override
	public FormLogoEntity getFormLogo(String id) throws BusinessException {
		return this.formLogoDao.get(id);
	}

	@Override
	public FormLogoEntity getFormLogoByName(String name) throws BusinessException {
		String hql = "from FormLogoEntity where name = ?";
		return this.formLogoDao.findUniqueByHql(hql, name);
	}

	@Override
	public String getLogoByName(String name) throws BusinessException {
		String logo="";
		String hql = "from FormLogoEntity where name = ?";
		FormLogoEntity formLogoEntity = this.formLogoDao.findUniqueByHql(hql, name);
		if(formLogoEntity != null){
			logo = formLogoEntity.getLogo();
		}
		return logo;
	}
	

}
