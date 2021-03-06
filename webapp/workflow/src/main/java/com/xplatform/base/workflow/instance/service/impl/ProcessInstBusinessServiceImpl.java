package com.xplatform.base.workflow.instance.service.impl;

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
import com.xplatform.base.workflow.instance.dao.ProcessInstBusinessDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstBusinessEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstBusinessService;

/**
 * 
 * description :流程业务实例service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("processInstBusinessService")
public class ProcessInstBusinessServiceImpl implements ProcessInstBusinessService {
	private static final Logger logger = Logger.getLogger(ProcessInstBusinessServiceImpl.class);
	@Resource
	private ProcessInstBusinessDao processInstBusinessDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="processInstBusinessManager",description="流程业务实例新增",detail="流程业务实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ProcessInstBusinessEntity processInstBusiness) throws BusinessException {
		String pk="";
		try {
			pk=this.processInstBusinessDao.addProcessInstBusiness(processInstBusiness);
		} catch (Exception e) {
			logger.error("流程业务实例保存失败");
			throw new BusinessException("流程业务实例保存失败");
		}
		logger.info("流程业务实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="processInstBusinessManager",description="流程业务实例删除",detail="流程业务实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.processInstBusinessDao.deleteProcessInstBusiness(id);
		} catch (Exception e) {
			logger.error("流程业务实例删除失败");
			throw new BusinessException("流程业务实例删除失败");
		}
		logger.info("流程业务实例删除成功");
	}

	@Override
	@Action(moduleCode="processInstBusinessManager",description="流程业务实例批量删除",detail="流程业务实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("流程业务实例批量删除成功");
	}

	@Override
	@Action(moduleCode="processInstBusinessManager",description="流程业务实例修改",detail="流程业务实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ProcessInstBusinessEntity processInstBusiness) throws BusinessException {
		try {
			ProcessInstBusinessEntity oldEntity = get(processInstBusiness.getId());
			MyBeanUtils.copyBeanNotNull2Bean(processInstBusiness, oldEntity);
			this.processInstBusinessDao.updateProcessInstBusiness(oldEntity);
		} catch (Exception e) {
			logger.error("流程业务实例更新失败");
			throw new BusinessException("流程业务实例更新失败");
		}
		logger.info("流程业务实例更新成功");
	}

	@Override
	public ProcessInstBusinessEntity get(String id) throws BusinessException {
		ProcessInstBusinessEntity processInstBusiness=null;
		try {
			processInstBusiness=this.processInstBusinessDao.getProcessInstBusiness(id);
		} catch (Exception e) {
			logger.error("流程业务实例获取失败");
			throw new BusinessException("流程业务实例获取失败");
		}
		logger.info("流程业务实例获取成功");
		return processInstBusiness;
	}

	@Override
	public List<ProcessInstBusinessEntity> queryList() throws BusinessException {
		List<ProcessInstBusinessEntity> processInstBusinessList=new ArrayList<ProcessInstBusinessEntity>();
		try {
			processInstBusinessList=this.processInstBusinessDao.queryProcessInstBusinessList();
		} catch (Exception e) {
			logger.error("流程业务实例获取列表失败");
			throw new BusinessException("流程业务实例获取列表失败");
		}
		logger.info("流程业务实例获取列表成功");
		return processInstBusinessList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInstBusinessDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程业务实例获取分页列表失败");
			throw new BusinessException("流程业务实例获取分页列表失败");
		}
		logger.info("流程业务实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ProcessInstBusinessEntity.class, param, propertyName);
	}
	
	public void delByInstanceId(String actInstId){
		this.processInstBusinessDao.executeSql("DELETE from t_flow_instance_form  where act_inst_id="+actInstId);
	}
	
	public void setprocessInstBusinessDao(ProcessInstBusinessDao processInstBusinessDao) {
		this.processInstBusinessDao = processInstBusinessDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		//this.processInstBusinessDao.executeSql("delete from t_flow_instance_form t where t.act_id=?", actDefId);
		this.processInstBusinessDao.executeHql("delete from ProcessInstBusinessEntity t where t.actDefId='"+actDefId+"'");
	}
}
