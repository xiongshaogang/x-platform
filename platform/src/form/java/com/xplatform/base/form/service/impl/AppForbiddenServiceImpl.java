package com.xplatform.base.form.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppForbiddenDao;
import com.xplatform.base.form.entity.AppForbiddenEntity;
import com.xplatform.base.form.service.AppForbiddenService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;

@Service("appForbiddenService")
public class AppForbiddenServiceImpl extends BaseServiceImpl<AppForbiddenEntity> implements AppForbiddenService{

	@Resource
	AppForbiddenDao appForbiddenDao;
	
	@Resource
	public void setBaseDao(AppForbiddenDao appForbiddenDao) {
		super.setBaseDao(appForbiddenDao);
	}
	
	@Override
	public AppForbiddenEntity getAppForbidden(String formCode, String userId, String orgId) throws BusinessException {
		String hql = "from AppForbiddenEntity where formCode=? and userId = ? and orgId = ?";
		return this.appForbiddenDao.findUniqueByHql(hql, formCode,userId,orgId);
	}

	@Override
	public void deleteAppForbidden(String formCode, String userId, String orgId) throws BusinessException {
		String sql = "delete from t_app_form_forbidden where formCode=? and userId=? and orgId=?";
		this.appForbiddenDao.executeSql(sql, formCode,userId,orgId);
	}

}
