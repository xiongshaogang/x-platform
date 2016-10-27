package com.xplatform.base.system.flowform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.io.Serializable;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.flowform.dao.FlowFieldDao;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;
import com.xplatform.base.system.flowform.service.FlowFieldService;


@Service("flowFieldService")
public class FlowFieldServiceImpl implements FlowFieldService {

    private static final Logger logger = Logger.getLogger(FlowFieldServiceImpl.class);

    @Resource
	private FlowFieldDao flowFieldDao;
	
	@Resource
	private BaseService baseService;

	public void setFlowFieldDao(FlowFieldDao flowFieldDao) {
		this.flowFieldDao = flowFieldDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(moduleCode="flowFormdefinition",description="保存",detail="保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(FlowFieldEntity flowField) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.flowFieldDao.addFlowField(flowField);
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
			this.flowFieldDao.deleteFlowField(id);
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
	public void update(FlowFieldEntity flowField) throws Exception {
		// TODO Auto-generated method stub
		try {
		    FlowFieldEntity oldEntity = this.get(flowField.getId());
			MyBeanUtils.copyBeanNotNull2Bean(flowField, oldEntity);
			this.flowFieldDao.updateFlowField(oldEntity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新失败");
		}
		logger.info("更新成功");
	}

	@Override
	public FlowFieldEntity get(String id){
		// TODO Auto-generated method stub
		FlowFieldEntity flowField=null;
		try {
			flowField=this.flowFieldDao.getFlowField(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取失败");
		}
		logger.info("获取成功");
		return flowField;
	}

	@Override
	public List<FlowFieldEntity> queryList(){
		// TODO Auto-generated method stub
		List<FlowFieldEntity> flowFieldList=new ArrayList<FlowFieldEntity>();
		try {
			flowFieldList=this.flowFieldDao.queryFlowFieldList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取列表失败");
		}
		logger.info("获取列表成功");
		return flowFieldList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.flowFieldDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取分页列表失败");
		}
		logger.info("获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(FlowFieldEntity.class, param, propertyName);
	}

	@Override
	public List<FlowFieldEntity> queryListByTableId(String tableId) {
		// TODO Auto-generated method stub
		return this.flowFieldDao.findByProperty(FlowFieldEntity.class, "table.id", tableId);
	}
}