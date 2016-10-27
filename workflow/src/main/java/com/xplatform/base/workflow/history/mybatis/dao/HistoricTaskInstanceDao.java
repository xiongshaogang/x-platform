package com.xplatform.base.workflow.history.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;

public interface HistoricTaskInstanceDao extends SimpleSqlDao<HistoricTaskInstanceEntity, String> {
	public List<HistoricTaskInstanceEntity> getByInstanceIdAndNodeId(Map<String,String> param);
}
