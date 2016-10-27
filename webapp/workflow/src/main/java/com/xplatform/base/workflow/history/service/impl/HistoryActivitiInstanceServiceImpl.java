package com.xplatform.base.workflow.history.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.history.mybatis.dao.HistoryActivityInstanceDao;
import com.xplatform.base.workflow.history.service.HistoryActivitiInstanceService;

@Service("historyActivitiInstanceService")
public class HistoryActivitiInstanceServiceImpl implements
		HistoryActivitiInstanceService {

	private HistoryActivityInstanceDao historyActivityInstanceDao;
	
	public void save(HistoricActivityInstanceEntity historicActivityInstance){
		this.historyActivityInstanceDao.save(historicActivityInstance);
	}
	
	public void update(HistoricActivityInstanceEntity historicActivityInstance){
		this.historyActivityInstanceDao.update(historicActivityInstance);
	}

	public List<HistoricActivityInstanceEntity> getByInstanceId(String actInstId,
			String nodeId) {
		Map <String,String>params = new HashMap<String,String>();
		params.put("actInstId", actInstId);
		params.put("nodeId", nodeId);
		return this.historyActivityInstanceDao.getByInstanceId(params);
	}

	public void updateAssignee(HistoricActivityInstanceEntity hisActEnt) {
		this.updateAssignee(hisActEnt);
	}

	public void updateIsStart(String actInstId, String nodeId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("processInstanceId", actInstId);
		params.put("activityId", nodeId);
		this.historyActivityInstanceDao.updateIsStart(params);
	}

	public List<HistoricActivityInstanceEntity> getByFilter(
			Map<String, Object> params) {
		return this.getByFilter(params);
	}

	public List<HistoricActivityInstanceEntity> getByExecutionId(
			String executionId, String nodeId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("executionId", executionId);
		params.put("nodeId", nodeId);
		return this.historyActivityInstanceDao.getByExecutionId(params);
	}

	@Autowired
	public void setHistoryActivityInstanceDao(
			HistoryActivityInstanceDao historyActivityInstanceDao) {
		this.historyActivityInstanceDao = historyActivityInstanceDao;
	}
}
