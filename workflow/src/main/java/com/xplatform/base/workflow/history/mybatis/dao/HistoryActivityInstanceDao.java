package com.xplatform.base.workflow.history.mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;

public interface HistoryActivityInstanceDao extends SimpleSqlDao<HistoricActivityInstanceEntity,String>{

	public List<HistoricActivityInstanceEntity> getByInstanceId(Map<String,String> param);

	public void updateAssignee(HistoricActivityInstanceEntity hisActEnt);

	public void updateIsStart(Map<String,String> param);

	public List<HistoricActivityInstanceEntity> getByFilter(Map<String, Object> params);

	public List<HistoricActivityInstanceEntity> getByExecutionId(Map<String,String> param);
}
