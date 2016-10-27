package com.xplatform.base.workflow.history.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.workflow.history.mybatis.dao.HistoricTaskInstanceDao;
import com.xplatform.base.workflow.history.mybatis.dao.HistoricTaskInstanceExtDao;
import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;
import com.xplatform.base.workflow.history.service.HistoryTaskInstanceService;

@Service("historyTaskInstanceService")
public class HistoryTaskInstanceServiceImpl implements HistoryTaskInstanceService {
	private HistoricTaskInstanceExtDao historicTaskInstanceExtDao;
	private HistoricTaskInstanceDao historicTaskInstanceDao;
	
	public void save(ProcessTaskHistory taskHistory){
		this.historicTaskInstanceExtDao.save(taskHistory);
	}
	
	public ProcessTaskHistory get(String id){
		return this.historicTaskInstanceExtDao.get(id);
	}
	
	public List<HistoricTaskInstanceEntity> getByInstanceIdAndNodeId(Map<String,String> param){
		return this.historicTaskInstanceDao.getByInstanceIdAndNodeId(param);
	}
	
	@Autowired
	public void setHistoricTaskInstanceExtDao(
			HistoricTaskInstanceExtDao historicTaskInstanceExtDao) {
		this.historicTaskInstanceExtDao = historicTaskInstanceExtDao;
	}
	
	@Autowired
	public void setHistoricTaskInstanceDao(
			HistoricTaskInstanceDao historicTaskInstanceDao) {
		this.historicTaskInstanceDao = historicTaskInstanceDao;
	}
}
