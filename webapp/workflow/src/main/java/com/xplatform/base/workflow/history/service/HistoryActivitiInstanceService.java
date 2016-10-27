package com.xplatform.base.workflow.history.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

public interface HistoryActivitiInstanceService {
	
	public void save(HistoricActivityInstanceEntity historicActivityInstance);
	
	public void update(HistoricActivityInstanceEntity historicActivityInstance);

	public List<HistoricActivityInstanceEntity> getByInstanceId(String actInstId,String nodeId) ;
	
	public void updateAssignee(HistoricActivityInstanceEntity hisActEnt);

	public void updateIsStart(String actInstId, String nodeId);

	public List<HistoricActivityInstanceEntity> getByFilter(Map<String, Object> params) ;

	public List<HistoricActivityInstanceEntity> getByExecutionId(String executionId, String nodeId);
}
