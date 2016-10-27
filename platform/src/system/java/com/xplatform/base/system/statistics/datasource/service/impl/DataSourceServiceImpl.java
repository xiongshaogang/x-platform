package com.xplatform.base.system.statistics.datasource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.mybatis.engine.condition.CriterionBuilder;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.system.statistics.datasource.dao.DataSourceDao;
import com.xplatform.base.system.statistics.datasource.entity.DataSourceEntity;
import com.xplatform.base.system.statistics.datasource.service.DataSourceService;


@Service("dataSourceService")
public class DataSourceServiceImpl implements DataSourceService {

    private static final Logger logger = Logger.getLogger(DataSourceServiceImpl.class);

    @Resource
	private DataSourceDao dataSourceDao;
	
	@Resource
	private BaseService baseService;

	public void setdataSourceDao(DataSourceDao dataSourceDao) {
		this.dataSourceDao = dataSourceDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(moduleCode="dataSourceManager",description="数据源保存",detail="数据源保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(DataSourceEntity dataSource) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.dataSourceDao.addDataSource(dataSource);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源保存失败");
		}
		logger.info("数据源保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源删除",detail="数据源删除成功", execOrder = ActionExecOrder.AFTER)
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.dataSourceDao.deleteDataSource(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源删除失败");
		}
		logger.info("数据源删除成功");
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源批量删除",detail="数据源批量删除成功", execOrder = ActionExecOrder.AFTER)
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("数据源批量删除成功");
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源更新",detail="数据源更新成功", execOrder = ActionExecOrder.AFTER)
	public void update(DataSourceEntity dataSource) throws Exception {
		// TODO Auto-generated method stub
		try {
			DataSourceEntity old = dataSourceDao.getDataSource(dataSource.getId());
			MyBeanUtils.copyBeanUpdateBean(old, dataSource);
			this.dataSourceDao.updateDataSource(dataSource);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源更新失败");
		}
		logger.info("数据源更新成功");
	}

	@Override
	public DataSourceEntity get(String id){
		// TODO Auto-generated method stub
		DataSourceEntity dataSource=null;
		try {
			dataSource=this.dataSourceDao.getDataSource(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源获取失败");
		}
		logger.info("数据源获取成功");
		return dataSource;
	}

	@Override
	public List<DataSourceEntity> queryList(){
		// TODO Auto-generated method stub
		List<DataSourceEntity> dataSourceList=new ArrayList<DataSourceEntity>();
		try {
			dataSourceList=this.dataSourceDao.queryDataSourceList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源获取列表失败");
		}
		logger.info("数据源获取列表成功");
		return dataSourceList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.dataSourceDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源获取分页列表失败");
		}
		logger.info("数据源获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(DataSourceEntity.class, param, propertyName);
	}

	@Override
	public List<DataSourceEntity> getList(UserEntity user) {
		// TODO Auto-generated method stub
		List<DataSourceEntity> list =null;
		if(StringUtils.equals(user.getId(), "1")){
			list = dataSourceDao.queryDataSourceList();
		}else{
			list = dataSourceDao.findByProperty(DataSourceEntity.class, "createUserId", user.getId());
		}
		return list;
	}

	@Override
	public String buildRelations(CriterionBuilder builder) {
		List<Criterion> relationList=builder.getRelationList();
		/*List<Criterion> formCriterionList = Lists.newArrayList(); 
		StringBuffer relationSql = new StringBuffer();
		for(Relation relation:relationList){
			Map<String, List<Criterion>> criterionMap = relation.getCriterions();
			Set<String> keySet = criterionMap.keySet();
			
			for(String key: keySet){
				formCriterionList.addAll(criterionMap.get(key));
			}
			
		}*/
	    StringBuffer relationSql = new StringBuffer();
	    if(relationList!=null && relationList.size()>0){
	    	for(Criterion c:relationList){
	    		String sql=c.toSqlString();
	    		int start=sql.indexOf("#{")+2;
	    		/*if(start>3){
		    		StringBuffer b = new StringBuffer(sql);
		    		b.insert(start, "parameter.");
		    		sql=b.toString();
	    		}*/
	    		while(start>3){
	    			StringBuffer b = new StringBuffer(sql);
		    		b.insert(start, "parameter.");
		    		sql=b.toString();
		    		start=sql.indexOf("#{", start)+2;
	    		}
	    			
	    			
				relationSql.append(" AND ").append(sql);
	    	}
	    }
		return relationSql.toString();

	}

}