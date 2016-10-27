package com.xplatform.base.form.service;

import java.util.List;

import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;

public interface FlowInstanceUserService extends BaseService<FlowInstanceUserEntity>{

	public AjaxJson saveOrDeleteFIU(String assignResult,String businessKey) throws BusinessException;
	
	public List<FlowInstanceUserEntity> queryFIUListByBus(String businessKey) throws BusinessException;
	
	public FlowInstanceUserEntity queryFIUListByStatus(String businessKey,String taskNodeId) throws BusinessException;
}
