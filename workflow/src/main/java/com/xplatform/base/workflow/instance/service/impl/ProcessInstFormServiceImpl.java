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
import com.xplatform.base.workflow.instance.dao.ProcessInstFormDao;
import com.xplatform.base.workflow.instance.entity.ProcessInstFormEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstFormService;

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
@Service("processInstFormService")
public class ProcessInstFormServiceImpl implements ProcessInstFormService {
	private static final Logger logger = Logger.getLogger(ProcessInstFormServiceImpl.class);
	@Resource
	private ProcessInstFormDao processInsFormDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="ProcessInsFormManager",description="流程实例新增",detail="流程实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ProcessInstFormEntity ProcessInsForm) throws BusinessException {
		String pk="";
		try {
			pk=this.processInsFormDao.addProcessInsForm(ProcessInsForm);
		} catch (Exception e) {
			logger.error("流程实例保存失败");
			throw new BusinessException("流程实例保存失败");
		}
		logger.info("流程实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ProcessInsFormManager",description="流程实例删除",detail="流程实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.processInsFormDao.deleteProcessInsForm(id);
		} catch (Exception e) {
			logger.error("流程实例删除失败");
			throw new BusinessException("流程实例删除失败");
		}
		logger.info("流程实例删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsFormManager",description="流程实例批量删除",detail="流程实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
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
	@Action(moduleCode="ProcessInsFormManager",description="流程实例修改",detail="流程实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ProcessInstFormEntity ProcessInsForm) throws BusinessException {
		try {
			ProcessInstFormEntity oldEntity = get(ProcessInsForm.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ProcessInsForm, oldEntity);
			this.processInsFormDao.updateProcessInsForm(oldEntity);
		} catch (Exception e) {
			logger.error("流程实例更新失败");
			throw new BusinessException("流程实例更新失败");
		}
		logger.info("流程实例更新成功");
	}

	@Override
	public ProcessInstFormEntity get(String id){
		ProcessInstFormEntity ProcessInsForm=null;
		try {
			ProcessInsForm=this.processInsFormDao.getProcessInsForm(id);
		} catch (Exception e) {
			logger.error("流程实例获取失败");
			//throw new BusinessException("流程实例获取失败");
		}
		logger.info("流程实例获取成功");
		return ProcessInsForm;
	}

	@Override
	public List<ProcessInstFormEntity> queryList() throws BusinessException {
		List<ProcessInstFormEntity> ProcessInsFormList=new ArrayList<ProcessInstFormEntity>();
		try {
			ProcessInsFormList=this.processInsFormDao.queryProcessInsFormList();
		} catch (Exception e) {
			logger.error("流程实例获取列表失败");
			throw new BusinessException("流程实例获取列表失败");
		}
		logger.info("流程实例获取列表成功");
		return ProcessInsFormList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInsFormDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程实例获取分页列表失败");
			throw new BusinessException("流程实例获取分页列表失败");
		}
		logger.info("流程实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ProcessInstFormEntity.class, param, propertyName);
	}
	
	public void delByInstanceId(String actInstId){
		this.processInsFormDao.executeSql("DELETE from t_flow_instance_form  where act_inst_id="+actInstId);
	}
	
	public void setProcessInsFormDao(ProcessInstFormDao processInsFormDao) {
		this.processInsFormDao = processInsFormDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		//this.processInsFormDao.executeSql("delete from t_flow_instance_form t where t.act_id=?", actDefId);
		this.processInsFormDao.executeHql("delete from ProcessInstFormEntity t where t.actDefId='"+actDefId+"'");
	}
}
