package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.workflow.node.entity.NodeMessageEntity;

public interface NodeMessageService {

	NodeMessageEntity getEntity(Map<String, String> params);

	void save(NodeMessageEntity n);

	void update(NodeMessageEntity nme);

	List<NodeMessageEntity> getNodeMessageList(Map<String, String> params);
	
	public void deleteByActDefId(String actDefId) throws BusinessException;

}
