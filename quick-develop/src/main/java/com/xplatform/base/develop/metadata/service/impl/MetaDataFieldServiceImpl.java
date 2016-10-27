package com.xplatform.base.develop.metadata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.io.Serializable;

import com.xplatform.base.develop.metadata.dao.MetaDataFieldDao;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.develop.metadata.service.MetaDataFieldService;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;


@Service("metaDataFieldService")
public class MetaDataFieldServiceImpl implements MetaDataFieldService {

    private static final Logger logger = Logger.getLogger(MetaDataFieldServiceImpl.class);

    @Resource
	private MetaDataFieldDao MetaDataFieldDao;
	
	@Resource
	private BaseService baseService;

	public void setMetaDataFieldDao(MetaDataFieldDao MetaDataFieldDao) {
		this.MetaDataFieldDao = MetaDataFieldDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(description = "元数据字段保存", detail = "元数据表${tableName}字段数据保存成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public String save(MetaDataFieldEntity formField) throws Exception {
		String pk="";
		try {
			pk=this.MetaDataFieldDao.addFormField(formField);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段保存失败");
		}
		logger.info("表字段保存成功");
		return pk;
	}

	@Override
	@Action(description = "元数据字段删除", detail = "元数据表${tableName}字段数据删除成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.MetaDataFieldDao.deleteFormField(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段删除失败");
		}
		logger.info("表字段删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("表字段批量删除成功");
	}

	@Override
	@Action(description = "元数据字段更新", detail = "元数据表${tableName}字段数据更新成功", execOrder = ActionExecOrder.AFTER, moduleCode = "formHeadManager")
	public void update(MetaDataFieldEntity formField) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.MetaDataFieldDao.updateFormField(formField);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段更新失败");
		}
		logger.info("表字段更新成功");
	}

	@Override
	public MetaDataFieldEntity get(String id){
		// TODO Auto-generated method stub
		MetaDataFieldEntity formField=null;
		try {
			formField=this.MetaDataFieldDao.getFormField(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段获取失败");
		}
		logger.info("表字段获取成功");
		return formField;
	}

	@Override
	public List<MetaDataFieldEntity> queryList(){
		// TODO Auto-generated method stub
		List<MetaDataFieldEntity> formFieldList=new ArrayList<MetaDataFieldEntity>();
		try {
			formFieldList=this.MetaDataFieldDao.queryFormFieldList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段获取列表失败");
		}
		logger.info("表字段获取列表成功");
		return formFieldList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.MetaDataFieldDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("表字段获取分页列表失败");
		}
		logger.info("表字段获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(MetaDataFieldEntity.class, param, propertyName);
	}

}