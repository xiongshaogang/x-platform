package com.xplatform.base.system.statistics.stat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.statistics.stat.dao.StatisticsDao;
import com.xplatform.base.system.statistics.stat.entity.StatisticsEntity;
import com.xplatform.base.system.statistics.stat.mybatis.dao.StatisticsMybatisDao;
import com.xplatform.base.system.statistics.stat.service.StatisticsService;

/**
 * 
 * description :分类统计service实现
 * @version 1.0
 * @createtime : 2014-07-03 10:42:37
 * 
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = Logger.getLogger(StatisticsServiceImpl.class);

    @Resource
	private StatisticsDao statisticsDao;
    
    private StatisticsMybatisDao statisticsMybatisDao;
	
	@Resource
	private BaseService baseService;

	public void setStatisticsDao(StatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	
 	public void setStatisticsMybatisDao(StatisticsMybatisDao statisticsMybatisDao) {
		this.statisticsMybatisDao = statisticsMybatisDao;
	}

	@Override
 	@Action(moduleCode="statisticsManager",description="分类统计保存",detail="分类统计保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(StatisticsEntity statistics) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.statisticsDao.addStatistics(statistics);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计保存失败");
		}
		logger.info("分类统计保存成功");
		return pk;
	}
	
	
	@Override
	@Action(moduleCode="statisticsManager",description="分类统计删除",detail="分类统计删除成功", execOrder = ActionExecOrder.AFTER)
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.statisticsDao.deleteStatistics(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计删除失败");
		}
		logger.info("分类统计删除成功");
	}


	@Override
	@Action(moduleCode="statisticsManager",description="分类统计批量删除",detail="分类统计批量删除成功", execOrder = ActionExecOrder.AFTER)
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("分类统计批量删除成功");
	}

	@Override
	@Action(moduleCode="statisticsManager",description="分类统计更新",detail="分类统计更新成功", execOrder = ActionExecOrder.AFTER)
	public void update(StatisticsEntity statistics) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			StatisticsEntity oldEntity = get(statistics.getId());
			MyBeanUtils.copyBeanUpdateBean(oldEntity,statistics);
			this.statisticsDao.merge(statistics);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计更新失败");
			throw new BusinessException("分类统计更新失败");
		}
		logger.info("分类统计更新成功");
	}

	@Override
	public StatisticsEntity get(String id){
		// TODO Auto-generated method stub
		StatisticsEntity statistics=null;
		try {
			statistics=this.statisticsDao.getStatistics(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计获取失败");
		}
		logger.info("分类统计获取成功");
		return statistics;
	}

	@Override
	public List<StatisticsEntity> queryList(){
		// TODO Auto-generated method stub
		List<StatisticsEntity> statisticsList=new ArrayList<StatisticsEntity>();
		try {
			statisticsList=this.statisticsDao.queryStatisticsList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计获取列表失败");
		}
		logger.info("分类统计获取列表成功");
		return statisticsList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.statisticsDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分类统计获取分页列表失败");
		}
		logger.info("分类统计获取分页列表成功");
	}
	
	@Override
	public List<StatisticsEntity> queryListByPorperty(String propertyName, String value) throws BusinessException {
		// TODO Auto-generated method stub
		return this.statisticsDao.findByProperty(StatisticsEntity.class, propertyName, value);
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(StatisticsEntity.class, param, propertyName);
	}

	@Override
	public List<Map<String,Object>> queryDataList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.statisticsMybatisDao.queryDataList(params);
	}

	@Override
	public List<StatisticsEntity> findByPropertys(String id,
			Map<String, String> params) {
		// TODO Auto-generated method stub
		params.put("type.id", id);
		return this.statisticsDao.findByPropertys(StatisticsEntity.class, params);
	}

	@Override
	public List<Map<String, Object>> queryStatisticsList(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.statisticsMybatisDao.queryStatisticsList(params);
	}
}