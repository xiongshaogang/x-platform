package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.task.dao.TaskNodeStatusDao;
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;

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
@Service("taskNodeStatusService")
public class TaskNodeStatusServiceImpl implements TaskNodeStatusService {
	private static final Logger logger = Logger.getLogger(TaskNodeStatusServiceImpl.class);
	@Resource
	private TaskNodeStatusDao processInsStatusDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="ProcessInsStatusManager",description="流程实例新增",detail="流程实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskNodeStatusEntity ProcessInsStatus) throws BusinessException {
		String pk="";
		try {
			pk=this.processInsStatusDao.addProcessInsStatus(ProcessInsStatus);
		} catch (Exception e) {
			logger.error("流程实例保存失败");
			throw new BusinessException("流程实例保存失败");
		}
		logger.info("流程实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ProcessInsStatusManager",description="流程实例删除",detail="流程实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.processInsStatusDao.deleteProcessInsStatus(id);
		} catch (Exception e) {
			logger.error("流程实例删除失败");
			throw new BusinessException("流程实例删除失败");
		}
		logger.info("流程实例删除成功");
	}

	@Override
	@Action(moduleCode="ProcessInsStatusManager",description="流程实例批量删除",detail="流程实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
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
	@Action(moduleCode="ProcessInsStatusManager",description="流程实例修改",detail="流程实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskNodeStatusEntity ProcessInsStatus) throws BusinessException {
		try {
			TaskNodeStatusEntity oldEntity = get(ProcessInsStatus.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ProcessInsStatus, oldEntity);
			this.processInsStatusDao.updateProcessInsStatus(oldEntity);
		} catch (Exception e) {
			logger.error("流程实例更新失败");
			throw new BusinessException("流程实例更新失败");
		}
		logger.info("流程实例更新成功");
	}

	@Override
	public TaskNodeStatusEntity get(String id){
		TaskNodeStatusEntity ProcessInsStatus=null;
		try {
			ProcessInsStatus=this.processInsStatusDao.getProcessInsStatus(id);
		} catch (Exception e) {
			logger.error("流程实例获取失败");
			//throw new BusinessException("流程实例获取失败");
		}
		logger.info("流程实例获取成功");
		return ProcessInsStatus;
	}

	@Override
	public List<TaskNodeStatusEntity> queryList() throws BusinessException {
		List<TaskNodeStatusEntity> ProcessInsStatusList=new ArrayList<TaskNodeStatusEntity>();
		try {
			ProcessInsStatusList=this.processInsStatusDao.queryProcessInsStatusList();
		} catch (Exception e) {
			logger.error("流程实例获取列表失败");
			throw new BusinessException("流程实例获取列表失败");
		}
		logger.info("流程实例获取列表成功");
		return ProcessInsStatusList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.processInsStatusDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程实例获取分页列表失败");
			throw new BusinessException("流程实例获取分页列表失败");
		}
		logger.info("流程实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskNodeStatusEntity.class, param, propertyName);
	}
	
	public TaskNodeStatusEntity getByInstNodeId(String processInstanceId,String nodeId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actInstId", processInstanceId);
		param.put("nodeId", nodeId);
		List<TaskNodeStatusEntity> list=this.processInsStatusDao.findByPropertys(TaskNodeStatusEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void saveOrUpdte(String actDefId,String actInstanceId,String nodeId,String status){
		TaskNodeStatusEntity taskNodeStatus = getByInstNodeId(actInstanceId,nodeId);
		if (taskNodeStatus == null)  {
			Map<String,FlowNode> mapNode = NodeCache.getByActDefId(actDefId);
			taskNodeStatus = new TaskNodeStatusEntity();
			taskNodeStatus.setActDefId(actDefId);
			taskNodeStatus.setActInstId(actInstanceId);
			taskNodeStatus.setNodeId(nodeId);
			taskNodeStatus.setStatus(status);
			FlowNode flowNode = (FlowNode) mapNode.get(nodeId);
			taskNodeStatus.setNodeName(flowNode.getNodeName());
			try {
				save(taskNodeStatus);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			taskNodeStatus.setActDefId(actDefId);
			taskNodeStatus.setActInstId(actInstanceId);
			taskNodeStatus.setNodeId(nodeId);
			taskNodeStatus.setStatus(status);
			try {
				update(taskNodeStatus);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setProcessInsStatusDao(TaskNodeStatusDao processInsStatusDao) {
		this.processInsStatusDao = processInsStatusDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.processInsStatusDao.executeHql("delete from TaskNodeStatusEntity t where t.actDefId='"+actDefId+"'");
	}
	
	@Override
	public List<TaskNodeStatusEntity>  getByActInstanceId(String instanceId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actInstId", instanceId);
		List<TaskNodeStatusEntity> list=this.processInsStatusDao.findByPropertys(TaskNodeStatusEntity.class, param);
		return list;
	}

	@Override
	public Map<String, String> getStatusByInstanceId(String instanceId) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		List<TaskNodeStatusEntity> list = getByActInstanceId(instanceId);
		Map<String,String> statusColorMap=ApplicationContextUtil.getBean("statusColorMap");
		for (TaskNodeStatusEntity obj : list) {
			String color = statusColorMap.get(obj.getStatus());
			map.put(obj.getNodeId(), color);
		}
		return map;
	}
}
