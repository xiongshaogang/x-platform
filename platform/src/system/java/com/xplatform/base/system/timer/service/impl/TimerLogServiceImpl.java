package com.xplatform.base.system.timer.service.impl;

import java.util.ArrayList;
import java.util.List;


import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.timer.dao.TimerLogDao;
import com.xplatform.base.system.timer.entity.TimerLogEntity;
import com.xplatform.base.system.timer.service.TimerLogService;

@Service("timerLogService")
public class TimerLogServiceImpl implements TimerLogService {

	private static final Logger logger = Logger.getLogger(TimerLogServiceImpl.class);
	@Resource
	private TimerLogDao timerDao;
	
	@Resource
	private BaseService baseService;

	@Override
	public String save(TimerLogEntity TimerLog) throws BusinessException {
		String pk="";
		try {
			pk=this.timerDao.addTimerLog(TimerLog);
		} catch (Exception e) {
			logger.error("计划任务日志保存失败");
			throw new BusinessException("计划任务日志保存失败");
		}
		logger.info("计划任务日志保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		try {
			this.timerDao.deleteTimerLog(id);
		} catch (Exception e) {
			logger.error("计划任务日志删除失败");
			throw new BusinessException("计划任务日志删除失败");
		}
		logger.info("计划任务日志删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("计划任务日志批量删除成功");
	}

	@Override
	public void update(TimerLogEntity TimerLog) throws BusinessException {
		try {
			this.timerDao.updateTimerLog(TimerLog);
		} catch (Exception e) {
			logger.error("计划任务日志更新失败");
			throw new BusinessException("计划任务日志更新失败");
		}
		logger.info("计划任务日志更新成功");
	}

	@Override
	public TimerLogEntity get(String id) throws BusinessException {
		TimerLogEntity TimerLog=null;
		try {
			TimerLog=this.timerDao.getTimerLog(id);
		} catch (Exception e) {
			logger.error("计划任务日志获取失败");
			throw new BusinessException("计划任务日志获取失败");
		}
		logger.info("计划任务日志获取成功");
		return TimerLog;
	}

	@Override
	public List<TimerLogEntity> queryList() throws BusinessException {
		List<TimerLogEntity> TimerLogList=new ArrayList<TimerLogEntity>();
		try {
			TimerLogList=this.timerDao.queryTimerLogList();
		} catch (Exception e) {
			logger.error("计划任务日志获取列表失败");
			throw new BusinessException("计划任务日志获取列表失败");
		}
		logger.info("计划任务日志获取列表成功");
		return TimerLogList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		this.timerDao.getDataGridReturn(cq, true);
	}

	
	public void settimerDao(TimerLogDao timerDao) {
		this.timerDao = timerDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
}
