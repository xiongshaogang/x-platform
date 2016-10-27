package com.xplatform.base.workflow.history.mybatis.dao;


import com.xplatform.base.framework.mybatis.dao.SimpleSqlDao;
import com.xplatform.base.workflow.history.mybatis.vo.ProcessTaskHistory;

public interface HistoricTaskInstanceExtDao extends SimpleSqlDao<ProcessTaskHistory, String> {

}
