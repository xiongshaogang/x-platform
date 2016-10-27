package com.xplatform.base.form.service;

import com.xplatform.base.form.entity.AppForbiddenEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.BaseService;

public interface AppForbiddenService extends BaseService<AppForbiddenEntity>{
	/**
	 * 根据下列参数获取AppForbiddenEntity
	 * @serialData 2016/01/27  早上9:26
	 * @author lixt
	 * @param formId
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws BusinessException
	 */
	public AppForbiddenEntity getAppForbidden(String formCode,String userId,String orgId) throws BusinessException;
	/**
	 * 根据下列参数删除AppForbiddenEntity
	 * @serialData 2016/01/27  早上9:26
	 * @author lixt
	 * @param formId
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws BusinessException
	 */
	public void deleteAppForbidden(String formCode,String userId,String orgId) throws BusinessException;
}
