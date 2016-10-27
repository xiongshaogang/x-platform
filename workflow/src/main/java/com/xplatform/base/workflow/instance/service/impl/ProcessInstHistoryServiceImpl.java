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
import com.xplatform.base.workflow.instance.dao.ProcessInstHistoryDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;

/**
 * 
 * description :流程实例service实现
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
@Service("processInsHistoryService")
public class ProcessInstHistoryServiceImpl implements ProcessInstHistoryService {
	private static final Logger logger = Logger.getLogger(ProcessInstHistoryServiceImpl.class);
	@Resource
	private ProcessInstHistoryDao processInsHistoryDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="ProcessInsHistoryManager",description="流程实例新增",detail="流程实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ProcessInstHistoryEntity ProcessInsHistory) throws BusinessException {
		String pk="";
		try {
			pk=this.processInsHistoryDao.addProcessInsHistory(ProcessInsHistory);
		} catch (Exception e) {
			logger.error("流程实例保存失败");
			throw new BusinessException("流程实例保存失败");
		}
		logger.info("流程实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ProcessInsHistoryManager",description="流程实例删除",detail="流程实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.processInsHistoryDao.deleteProcessInsHistory(id);
		} catch (Exception e) {
			logger.error("流程实例删除失败");
			throw new BusinessException("流程实例删除失败");
		}
		logger.info("流程实例删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsHistoryManager",description="流程实例批量删除",detail="流程实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("流程实例批量删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsHistoryManager",description="流程实例修改",detail="流程实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ProcessInstHistoryEntity ProcessInsHistory) throws BusinessException {
		try {
			ProcessInstHistoryEntity oldEntity = get(ProcessInsHistory.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ProcessInsHistory, oldEntity);
			this.processInsHistoryDao.updateProcessInsHistory(oldEntity);
		} catch (Exception e) {
			logger.error("流程实例更新失败");
			throw new BusinessException("流程实例更新失败");
		}
		logger.info("流程实例更新成功");
	}

	@Override
	public ProcessInstHistoryEntity get(String id){
		ProcessInstHistoryEntity ProcessInsHistory=null;
		try {
			ProcessInsHistory=this.processInsHistoryDao.getProcessInsHistory(id);
		} catch (Exception e) {
			logger.error("流程实例获取失败");
			//throw new BusinessException("流程实例获取失败");
		}
		logger.info("流程实例获取成功");
		return ProcessInsHistory;
	}
	
	@Override
	public ProcessInstHistoryEntity getByBusinessKey(String businessKey){
		List<ProcessInstHistoryEntity> list=null;
		try {
			list=this.processInsHistoryDao.findByProperty(ProcessInstHistoryEntity.class, "businessKey", businessKey);
		} catch (Exception e) {
			//throw new BusinessException("流程实例获取失败");
		}
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ProcessInstHistoryEntity> queryList() throws BusinessException {
		List<ProcessInstHistoryEntity> ProcessInsHistoryList=new ArrayList<ProcessInstHistoryEntity>();
		try {
			ProcessInsHistoryList=this.processInsHistoryDao.queryProcessInsHistoryList();
		} catch (Exception e) {
			logger.error("流程实例获取列表失败");
			throw new BusinessException("流程实例获取列表失败");
		}
		logger.info("流程实例获取列表成功");
		return ProcessInsHistoryList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInsHistoryDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程实例获取分页列表失败");
			throw new BusinessException("流程实例获取分页列表失败");
		}
		logger.info("流程实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ProcessInstHistoryEntity.class, param, propertyName);
	}
	
	@Override
	public ProcessInstHistoryEntity getByActInstanceId(String actInstId) {
		List<ProcessInstHistoryEntity> list = this.processInsHistoryDao.findByProperty(ProcessInstHistoryEntity.class,
				"actInstId", actInstId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public void setProcessInsHistoryDao(ProcessInstHistoryDao processInsHistoryDao) {
		this.processInsHistoryDao = processInsHistoryDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
