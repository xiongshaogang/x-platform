package com.xplatform.base.workflow.history.mybatis.dao;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;

public interface HistoricProcessInstanceDao extends SimpleSqlDao<HistoricProcessInstanceEntity, String> {
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(Map<String,String> param);
}
