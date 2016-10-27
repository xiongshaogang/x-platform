package com.xplatform.base.system.statistics.stat.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.statistics.stat.dao.StatisticsDao;
import com.xplatform.base.system.statistics.stat.entity.StatisticsEntity;


@Repository("statisticsDao")
public class StatisticsDaoImpl extends CommonDao implements StatisticsDao {
    @Override
	public String addStatistics(StatisticsEntity Statistics) {
		// TODO Auto-generated method stub
		return (String) this.save(Statistics);
	}

	@Override
	public void deleteStatistics(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(StatisticsEntity.class, id);
	}

	@Override
	public void updateStatistics(StatisticsEntity Statistics) {
		// TODO Auto-generated method stub
		this.updateEntitie(Statistics);
	}

	@Override
	public StatisticsEntity getStatistics(String id) {
		// TODO Auto-generated method stub
		return (StatisticsEntity) this.get(StatisticsEntity.class, id);
	}

	@Override
	public List<StatisticsEntity> queryStatisticsList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from StatisticsEntity");
	}

}
