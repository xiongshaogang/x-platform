package com.xplatform.base.workflow.approve.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.approve.dao.ApproveItemDao;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;
import com.xplatform.base.workflow.approve.service.ApproveItemService;

/**
 * 
 * description :审批常用语service实现
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
@Service("approveItemService")
public class ApproveItemImpl implements ApproveItemService {
	private static final Logger logger = Logger.getLogger(ApproveItemImpl.class);
	@Resource
	private ApproveItemDao approveItemDao;
	
	@Resource
	private BaseService baseService;

	@Override
	@Action(moduleCode="ApproveItemManager",description="审批常用语新增",detail="审批常用语${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ApproveItemEntity ApproveItem) throws BusinessException {
		String pk="";
		try {
			pk=this.approveItemDao.addApproveItem(ApproveItem);
		} catch (Exception e) {
			logger.error("审批常用语保存失败");
			throw new BusinessException("审批常用语保存失败");
		}
		logger.info("审批常用语保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ApproveItemManager",description="审批常用语删除",detail="审批常用语${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.approveItemDao.deleteApproveItem(id);
		} catch (Exception e) {
			logger.error("审批常用语删除失败");
			throw new BusinessException("审批常用语删除失败");
		}
		logger.info("审批常用语删除成功");
	}

	@Override
	@Action(moduleCode="ApproveItemManager",description="审批常用语批量删除",detail="审批常用语${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("审批常用语批量删除成功");
	}

	@Override
	@Action(moduleCode="ApproveItemManager",description="审批常用语修改",detail="审批常用语${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ApproveItemEntity ApproveItem) throws BusinessException {
		try {
			ApproveItemEntity oldEntity = get(ApproveItem.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ApproveItem, oldEntity);
			this.approveItemDao.updateApproveItem(oldEntity);
		} catch (Exception e) {
			logger.error("审批常用语更新失败");
			throw new BusinessException("审批常用语更新失败");
		}
		logger.info("审批常用语更新成功");
	}

	@Override
	public ApproveItemEntity get(String id) throws BusinessException {
		ApproveItemEntity ApproveItem=null;
		try {
			ApproveItem=this.approveItemDao.getApproveItem(id);
		} catch (Exception e) {
			logger.error("审批常用语获取失败");
			throw new BusinessException("审批常用语获取失败");
		}
		logger.info("审批常用语获取成功");
		return ApproveItem;
	}

	@Override
	public List<ApproveItemEntity> queryList() throws BusinessException {
		List<ApproveItemEntity> ApproveItemList=new ArrayList<ApproveItemEntity>();
		try {
			ApproveItemList=this.approveItemDao.queryApproveItemList();
		} catch (Exception e) {
			logger.error("审批常用语获取列表失败");
			throw new BusinessException("审批常用语获取列表失败");
		}
		logger.info("审批常用语获取列表成功");
		return ApproveItemList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.approveItemDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("审批常用语获取分页列表失败");
			throw new BusinessException("审批常用语获取分页列表失败");
		}
		logger.info("审批常用语获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ApproveItemEntity.class, param, propertyName);
	}
	
	@Override
	public ApproveItemEntity getTaskApproval(String actDefId, String nodeId,
			String isGrobal) {
		// TODO Auto-generated method stub
		Map<String,String> param=new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		param.put("isGlobal", isGrobal);
		List<ApproveItemEntity> list=this.approveItemDao.findByPropertys(ApproveItemEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public ApproveItemEntity getFlowApproval(String actDefId, String isGrobal) {
		// TODO Auto-generated method stub
		Map<String,String> param=new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("isGlobal", isGrobal);
		List<ApproveItemEntity> list=this.approveItemDao.findByPropertys(ApproveItemEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public List<ApproveItemEntity> getByActDefId(String actDefId) {
		// TODO Auto-generated method stub
		return this.approveItemDao.findByProperty(ApproveItemEntity.class, "actDefId", actDefId);
	}
	
	public void setApproveItemDao(ApproveItemDao approveItemDao) {
		this.approveItemDao = approveItemDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public List<String> getApprovalByActDefId(String actDefId, String nodeId) {
		// TODO Auto-generated method stub
		List<String> taskAppItemsList = new ArrayList<String>();
		List<ApproveItemEntity> taskAppItems=new ArrayList<ApproveItemEntity>();
		
		Map<String,String> globalParam=new HashMap<String,String>();
		globalParam.put("isGlobal", "Y");
		globalParam.put("actDefId", actDefId);
		List<ApproveItemEntity> globalList=this.approveItemDao.findByPropertys(ApproveItemEntity.class, globalParam);
		
		Map<String,String> nodeParam=new HashMap<String,String>();
		nodeParam.put("isGlobal", "N");
		nodeParam.put("actDefId", actDefId);
		nodeParam.put("nodeId", nodeId);
		List<ApproveItemEntity> nodeList =this.approveItemDao.findByPropertys(ApproveItemEntity.class, nodeParam);
	    
		taskAppItems.addAll(globalList);
		taskAppItems.addAll(nodeList);
		if (BeanUtils.isNotEmpty(taskAppItems)) {
	    	for (ApproveItemEntity taskAppItem : taskAppItems) {
	    		String[] itemArr = taskAppItem.getContent().split("\r\n");
	    		for (String item : itemArr) {
	    			taskAppItemsList.add(item);
	    		}
	    	}
	    }

	    return taskAppItemsList;
	}

	

	
}
