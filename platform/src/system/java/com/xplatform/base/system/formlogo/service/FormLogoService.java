package com.xplatform.base.system.formlogo.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.formlogo.entity.FormLogoEntity;

public interface FormLogoService extends BaseService<FormLogoEntity>{

	public String saveFormLogo(FormLogoEntity FormLogo) throws BusinessException;
	
	public void updateFormLogo(FormLogoEntity FormLogo) throws BusinessException;
	
	public String deleteFormLogo(String id) throws BusinessException;
	
	public List<FormLogoEntity> queryList() throws BusinessException;
	
	public FormLogoEntity getFormLogo(String id) throws BusinessException;
	
	public FormLogoEntity getFormLogoByName(String name) throws BusinessException;
	
	public String getLogoByName(String name) throws BusinessException;
	
	/**
	 * 获取系统分类分页列表
	 * 
	 * @author luoheng
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
/*	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b);*/
	
}
