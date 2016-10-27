package com.xplatform.base.orgnaization.appversion.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.appversion.entity.AppVersionEntity;



public interface AppVersionService extends BaseService<AppVersionEntity>{
	
	public DataGridReturn getDataGridReturn(CriteriaQuery cq,boolean flag);

	public void saveAppVersion(AppVersionEntity appVersonEntity) throws BusinessException;
	
	public List<AppVersionEntity> queryAppVersonList() throws BusinessException;
	
	public List<AppVersionEntity> getNewApp(String versionNumber,int type) throws BusinessException;
	
	
	/**
	 * 获得最新的版本实体
	 * @return
	 */
	public AppVersionEntity getLatestApp(int type);
}
