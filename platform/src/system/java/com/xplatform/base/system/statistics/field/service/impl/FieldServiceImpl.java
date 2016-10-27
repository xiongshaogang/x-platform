package com.xplatform.base.system.statistics.field.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.io.Serializable;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.statistics.field.service.FieldService;
import com.xplatform.base.system.statistics.field.dao.FieldDao;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;


@Service("fieldService")
public class FieldServiceImpl implements FieldService {

    private static final Logger logger = Logger.getLogger(FieldServiceImpl.class);

    @Resource
	private FieldDao fieldDao;
	
	@Resource
	private BaseService baseService;

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(moduleCode="dataSourceManager",description="数据源字段保存",detail="数据源字段保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(FieldEntity field) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.fieldDao.addField(field);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段保存失败");
		}
		logger.info("数据源字段保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源字段删除",detail="数据源字段删除成功", execOrder = ActionExecOrder.AFTER)
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.fieldDao.deleteField(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段删除失败");
		}
		logger.info("数据源字段删除成功");
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源字段批量删除",detail="数据源字段批量删除成功", execOrder = ActionExecOrder.AFTER)
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("数据源字段批量删除成功");
	}

	@Override
	@Action(moduleCode="dataSourceManager",description="数据源字段更新",detail="数据源字段更新成功", execOrder = ActionExecOrder.AFTER)
	public void update(FieldEntity field) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.fieldDao.updateField(field);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段更新失败");
		}
		logger.info("数据源字段更新成功");
	}

	@Override
	public FieldEntity get(String id){
		// TODO Auto-generated method stub
		FieldEntity field=null;
		try {
			field=this.fieldDao.getField(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段获取失败");
		}
		logger.info("数据源字段获取成功");
		return field;
	}

	@Override
	public List<FieldEntity> queryList(){
		// TODO Auto-generated method stub
		List<FieldEntity> fieldList=new ArrayList<FieldEntity>();
		try {
			fieldList=this.fieldDao.queryFieldList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段获取列表失败");
		}
		logger.info("数据源字段获取列表成功");
		return fieldList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.fieldDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据源字段获取分页列表失败");
		}
		logger.info("数据源字段获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(FieldEntity.class, param, propertyName);
	}

	@Override
	public void insertDataSourceField(String sql,String id) throws SQLException{
		// TODO Auto-generated method stub
		Connection conn;
		FieldEntity fe =null;
			conn = SessionFactoryUtils.getDataSource(fieldDao.getSession().getSessionFactory()).getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			for(int i=1;i<=metaData.getColumnCount();i++){
				fe = new FieldEntity();
				fe.setDatasourceId(id);
				fe.setName(metaData.getColumnName(i).toLowerCase());
				fe.setNum(i);
				fe.setIsshow("N");
				fe.setIssum("N");
				fe.setIssearch("N");
				fe.setIsx("N");
				fe.setIsy("N");
				fieldDao.save(fe);
			}
	}

	@Override
	public void deleteDataSourceField(String id) {
		// TODO Auto-generated method stub
		List<FieldEntity> list = fieldDao.findByProperty(FieldEntity.class, "datasourceId", id);
		for(FieldEntity f : list){
			fieldDao.delete(f);
		}
	}

	@Override
	public List<FieldEntity> queryByDatasourceIdList(String datasourceId) {
		// TODO Auto-generated method stub
			return this.fieldDao.queryByDatasourceIdList(datasourceId);
	}

	@Override
	public boolean judenSqlValid(String sql) {
		    Connection conn;
		    try{
			conn = SessionFactoryUtils.getDataSource(fieldDao.getSession().getSessionFactory()).getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
		    }catch(SQLException e){
		    	return false;
		    }
			
			return true;
	}
}