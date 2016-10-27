package com.xplatform.base.system.timer.dao.impl;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.timer.dao.TimerLogDao;
import com.xplatform.base.system.timer.entity.TimerLogEntity;

@Repository("timerLogDao")
public class TimerLogDaoImpl extends CommonDao implements TimerLogDao {
	@Override
	public String addTimerLog(TimerLogEntity TimerLog) {
		// TODO Auto-generated method stub
		return (String) this.save(TimerLog);
	}

	@Override
	public void deleteTimerLog(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(TimerLogEntity.class, id);
	}

	@Override
	public void updateTimerLog(TimerLogEntity TimerLog) {
		// TODO Auto-generated method stub
		this.updateEntitie(TimerLog);
	}

	@Override
	public TimerLogEntity getTimerLog(String id) {
		// TODO Auto-generated method stub
		return (TimerLogEntity) this.get(TimerLogEntity.class, id);
	}

	@Override
	public List<TimerLogEntity> queryTimerLogList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TimerLogEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	
}
