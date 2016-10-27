package com.xplatform.base.workflow.history.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.history.mybatis.dao.HistoricProcessInstanceDao;
import com.xplatform.base.workflow.history.service.HistoryProcessInstanceService;

@Service("historyProcessInstanceService")
public class HistoryProcessInstanceServiceImpl implements HistoryProcessInstanceService {
	
	private HistoricProcessInstanceDao historicProcessInstanceDao;
	
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(String actInstanceId, String nodeId){
	    Map<String,String> map = new HashMap<String,String>();
	    map.put("actInstanceId", actInstanceId);
	    map.put("nodeId", nodeId);
	    return this.historicProcessInstanceDao.getByInstanceIdAndNodeId(map);
	}
	
	public void update(HistoricProcessInstanceEntity historicProcessInstance){
		this.historicProcessInstanceDao.update(historicProcessInstance);
	}

	@Autowired
	public void setHistoricProcessInstanceDao(
			HistoricProcessInstanceDao historicProcessInstanceDao) {
		this.historicProcessInstanceDao = historicProcessInstanceDao;
	}
	
	
}
