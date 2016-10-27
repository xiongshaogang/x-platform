package com.xplatform.base.system.flowform.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.io.Serializable;

import com.sun.star.uno.RuntimeException;
import com.xplatform.base.develop.codegenerate.service.impl.config.util.DbTableUtil;
import com.xplatform.base.develop.codegenerate.util.PublicUtil;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.common.service.impl.CommonServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.flowform.config.FlowDbTableProcess;
import com.xplatform.base.system.flowform.dao.FlowTableDao;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;
import com.xplatform.base.system.flowform.service.FlowTableService;


@Service("flowTableService")
public class FlowTableServiceImpl extends CommonServiceImpl implements FlowTableService {

    private static final Logger logger = Logger.getLogger(FlowTableServiceImpl.class);

    @Resource
	private FlowTableDao flowTableDao;
	
	@Resource
	private BaseService baseService;

	public void setFlowTableDao(FlowTableDao flowTableDao) {
		this.flowTableDao = flowTableDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(moduleCode="flowFormdefinition",description="保存",detail="保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(FlowTableEntity flowTable) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.flowTableDao.addFlowTable(flowTable);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("保存失败");
		}
		logger.info("保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="flowFormdefinition",description="删除",detail="删除成功", execOrder = ActionExecOrder.AFTER)
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.flowTableDao.deleteFlowTable(id);
			List<FlowFieldEntity> listFlowField =  this.flowTableDao.findByProperty(FlowFieldEntity.class, "flowTable.id", id);
			for(FlowFieldEntity flowField : listFlowField){
				this.flowTableDao.delete(flowField);				
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("删除失败");
		}
		logger.info("删除成功");
	}

	@Override
	@Action(moduleCode="flowFormdefinition",description="批量删除",detail="批量删除成功", execOrder = ActionExecOrder.AFTER)
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("批量删除成功");
	}

	@Override
	@Action(moduleCode="flowFormdefinition",description="更新",detail="更新成功", execOrder = ActionExecOrder.AFTER)
	public void update(FlowTableEntity flowTable) throws Exception {
		// TODO Auto-generated method stub
		try {
		    FlowTableEntity oldEntity = this.get(flowTable.getId());
			MyBeanUtils.copyBeanNotNull2Bean(flowTable, oldEntity);
			this.flowTableDao.updateFlowTable(oldEntity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新失败");
		}
		logger.info("更新成功");
	}

	@Override
	public FlowTableEntity get(String id){
		// TODO Auto-generated method stub
		FlowTableEntity flowTable=null;
		try {
			flowTable=this.flowTableDao.getFlowTable(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取失败");
		}
		logger.info("获取成功");
		return flowTable;
	}

	@Override
	public List<FlowTableEntity> queryList(){
		// TODO Auto-generated method stub
		List<FlowTableEntity> flowTableList=new ArrayList<FlowTableEntity>();
		try {
			flowTableList=this.flowTableDao.queryFlowTableList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取列表失败");
		}
		logger.info("获取列表成功");
		return flowTableList;
	}

	@Override
	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		return  this.flowTableDao.getDataGridReturn(cq, true);
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(FlowTableEntity.class, param, propertyName);
	}

	@Override
	public List<FlowTableEntity> getMainTableList() {
		// TODO Auto-generated method stub
		String hql = "FROM FlowTableEntity where isMainTable = '1' and ispublished = '1'";
		return this.flowTableDao.findHql(hql, new Object[]{});
	}

	@Override
	public boolean deployDb(FlowTableEntity flowTable) throws BusinessException {
		try {
				//普通方式同步
				// step.1判断表是否存在
				if (judgeTableIsExit(flowTable.getTableName())) {
					// 更新表操作
					FlowDbTableProcess dbTableProcess = new FlowDbTableProcess(getSession());
					List<String> updateTable = dbTableProcess.updateTable(flowTable, getSession());
					for (String sql : updateTable) {
						if(StringUtils.isNotEmpty(sql)){
							flowTableDao.executeSql(sql);
						}
					}
				} else {
					// 不存在的情况下，创建新表
					try {
						FlowDbTableProcess.createOrDropTable(flowTable, getSession());
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
						throw new BusinessException("同步失败:创建新表出错");
					}
				}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BusinessException("同步失败:数据库不支持本次修改,如果不需要保留数据,请尝试强制同步");
		}
		return true;
	}

	
	public boolean judgeTableIsExit(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			String[] types = { "TABLE" };
			conn = SessionFactoryUtils.getDataSource(
					getSession().getSessionFactory()).getConnection();
			DatabaseMetaData dbMetaData = conn.getMetaData();
			rs = dbMetaData
					.getTables(
							null,
							null,
							DbTableUtil.getDataType(getSession()).equals(
									"ORACLE") ? tableName.toUpperCase()
									: tableName, types);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		}finally{//关闭连接
			try {
				if(rs!=null){rs.close();}
				if(conn!=null){conn.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重载创建表
	 * 
	 * @param cgFormHead
	 * @param a
	 */
	@Override
	public void saveTable(FlowTableEntity flowTable, String string) {
		flowTable.setId((String) this.getSession().save(flowTable));
		FlowFieldEntity column;
		for (int i = 0; i < flowTable.getColumns().size(); i++) {
			column = flowTable.getColumns().get(i);
			PublicUtil.judgeCheckboxValue(column,
					"isNull,isShow,isShowList,isQuery,isKey");
			column.setTable(flowTable);
			this.save(column);
		}
	}
}