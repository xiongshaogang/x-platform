package com.xplatform.base.system.statistics.stat.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.statistics.stat.entity.StatisticsEntity;

public interface StatisticsDao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String addStatistics(StatisticsEntity statistics);
	
	/**
	 * @Decription 删除
	 */
	public void deleteStatistics(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void updateStatistics (StatisticsEntity statistics);
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public StatisticsEntity getStatistics(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<StatisticsEntity> queryStatisticsList();
	
}
