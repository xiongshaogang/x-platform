package com.xplatform.base.system.statistics.stat.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.stat.entity.StatisticsEntity;


/**
 * 
 * description : 分类统计service
 * @version 1.0
 * @createtime : 2014-07-03 10:42:37
 * 
 */
public interface StatisticsService{
	
 	/**
	 * 新增分类统计
	 */
	public String save(StatisticsEntity statistics) throws Exception;
	
	/**
	 * 删除分类统计
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除分类统计
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新分类统计
	 */
	public void update(StatisticsEntity statistics) throws Exception;
	
	/**
	 * 查询一条分类统计记录
	 */
	public StatisticsEntity get(String id);
	
	/**
	 * 查询分类统计列表
	 */
	public List<StatisticsEntity> queryList();
	
	/**
	 * 通过属性查询分类统计列表
	 */
	public List<StatisticsEntity> queryListByPorperty(String propertyName,String value) throws Exception;
	
	/**
	 * hibernate分类统计分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);

	public List<Map<String,Object>> queryDataList(Map<String, Object> params);

	public List<StatisticsEntity> findByPropertys(String id,
			Map<String, String> params);

	public List<Map<String, Object>> queryStatisticsList(
			Map<String, Object> params);
}
