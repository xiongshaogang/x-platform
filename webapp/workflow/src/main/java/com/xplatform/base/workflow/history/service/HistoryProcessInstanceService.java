package com.xplatform.base.workflow.history.service;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

public interface HistoryProcessInstanceService {
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(String actInstanceId, String nodeId);
	public void update(HistoricProcessInstanceEntity historicProcessInstance);
}
