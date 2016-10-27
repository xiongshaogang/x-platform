package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.task.dao.TaskOpinionDao;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

/**
 * 
 * description :审批意见service实现
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
@Service("taskOpinionService")
public class TaskOpinionServiceImpl implements TaskOpinionService {
	private static final Logger logger = Logger.getLogger(TaskOpinionServiceImpl.class);
	@Resource
	private TaskOpinionDao taskOpinionDao;
	
	@Resource
	private BaseService baseService;

	@Override
	public String save(TaskOpinionEntity taskOpinion) throws BusinessException {
		String pk="";
		try {
			Map<String,String> statusNameMap=ApplicationContextUtil.getBean("statusNameMap");
			taskOpinion.setCheckStatusStr(StringUtil.toString(statusNameMap.get(taskOpinion.getCheckStatus())));
			pk=this.taskOpinionDao.addTaskOpinion(taskOpinion);
		} catch (Exception e) {
			logger.error("审批意见保存失败");
			throw new BusinessException("审批意见保存失败");
		}
		logger.info("审批意见保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		try {
			this.taskOpinionDao.deleteTaskOpinion(id);
		} catch (Exception e) {
			logger.error("审批意见删除失败");
			throw new BusinessException("审批意见删除失败");
		}
		logger.info("审批意见删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("审批意见批量删除成功");
	}

	@Override
	public void update(TaskOpinionEntity taskOpinion) throws BusinessException {
		try {
			Map<String,String> statusNameMap=ApplicationContextUtil.getBean("statusNameMap");
			taskOpinion.setCheckStatusStr(StringUtil.toString(statusNameMap.get(taskOpinion.getCheckStatus().toString())));
			TaskOpinionEntity oldEntity = get(taskOpinion.getId());
			MyBeanUtils.copyBeanNotNull2Bean(taskOpinion, oldEntity);
			this.taskOpinionDao.updateTaskOpinion(oldEntity);
		} catch (Exception e) {
			logger.error("审批意见更新失败");
			throw new BusinessException("审批意见更新失败");
		}
		logger.info("审批意见更新成功");
	}

	@Override
	public TaskOpinionEntity get(String id) throws BusinessException {
		TaskOpinionEntity TaskOpinion=null;
		try {
			TaskOpinion=this.taskOpinionDao.getTaskOpinion(id);
		} catch (Exception e) {
			logger.error("审批意见获取失败");
			throw new BusinessException("审批意见获取失败");
		}
		logger.info("审批意见获取成功");
		return TaskOpinion;
	}

	@Override
	public List<TaskOpinionEntity> queryList() throws BusinessException {
		List<TaskOpinionEntity> TaskOpinionList=new ArrayList<TaskOpinionEntity>();
		try {
			TaskOpinionList=this.taskOpinionDao.queryTaskOpinionList();
		} catch (Exception e) {
			logger.error("审批意见获取列表失败");
			throw new BusinessException("审批意见获取列表失败");
		}
		logger.info("审批意见获取列表成功");
		return TaskOpinionList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskOpinionDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("审批意见获取分页列表失败");
			throw new BusinessException("审批意见获取分页列表失败");
		}
		logger.info("审批意见获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskOpinionEntity.class, param, propertyName);
	}
	
	
	public TaskOpinionEntity getByTaskId(String taskId){
		List<String> orderProperty=new ArrayList<String>();
		orderProperty.add("createTime");
		List<TaskOpinionEntity> list=this.taskOpinionDao.findByPropertyisOrder(TaskOpinionEntity.class, "taskId", taskId, false, orderProperty);
		//List<TaskOpinionEntity> list=this.taskOpinionDao.findByProperty(TaskOpinionEntity.class, "taskId", taskId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public TaskOpinionEntity getOpinionByTaskId(String taskId,String userId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("taskId", taskId);
		param.put("exeUserId", userId);
		List<TaskOpinionEntity> list=this.taskOpinionDao.findByPropertys(TaskOpinionEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<TaskOpinionEntity> getByActInstIdTaskKey(String actInstId,
			String nodeId) {
		// TODO Auto-generated method stub
		//添加参数
		Map<String,String> param=new HashMap<String,String>();
		param.put("actInstId", actInstId);
		param.put("taskKey", nodeId);
		//按时间降序
		List<String> orders=new ArrayList<String>();
		orders.add("endTime");
		//返回查询结果
		return this.taskOpinionDao.findByPropertysisOrder(TaskOpinionEntity.class, param, false, orders);
	}

	@Override
	public TaskOpinionEntity getLatestTaskOpinion(String actInstId,
			String nodeId) {
		// TODO Auto-generated method stub
		List<TaskOpinionEntity> list=getByActInstIdTaskKey(actInstId,nodeId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public TaskOpinionEntity getLatestUserOpinion(String instanceId,String curUserId){
		// TODO Auto-generated method stub
		//添加参数
		Map<String,String> param=new HashMap<String,String>();
		param.put("actInstId", instanceId);
		param.put("exeUserId", curUserId);
		//按时间降序
		List<String> orders=new ArrayList<String>();
		orders.add("endTime");
		//返回查询结果
		List<TaskOpinionEntity> list= this.taskOpinionDao.findByPropertysisOrder(TaskOpinionEntity.class, param, false, orders);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<TaskOpinionEntity> getByActInstIdTaskKeyStatus(
			String actInstId, String nodeId, Integer checkStatus) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInstId", actInstId);
		param.put("taskKey", nodeId);
		param.put("checkStatus", checkStatus);
		return this.taskOpinionDao.findByObjectPropertys(TaskOpinionEntity.class, param);
	}
	
	public void setTaskOpinionDao(TaskOpinionDao taskOpinionDao) {
		this.taskOpinionDao = taskOpinionDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.taskOpinionDao.executeHql("delete from TaskOpinionEntity t where t.actDefId='"+actDefId+"'");
	}

	@Override
	public List<TaskOpinionEntity> getByActInstId(String actInstId) {
		// TODO Auto-generated method stub
		//Map<String,String> param=new HashMap<String,String>();
		//param.put("actInstId", actInstId);
		List<String> order=new ArrayList<String>();
		order.add("updateTime");
		return this.taskOpinionDao.findByPropertyisOrder(TaskOpinionEntity.class, "actInstId", actInstId, true, order);
		//return this.taskOpinionDao.findByPropertyOrder(TaskOpinionEntity.class, param, true, "updateTime");
	}

	@Override
	public List<TaskOpinionEntity> getCheckOpinionByInstId(String actInstId) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInstId", actInstId);
		param.put("checkStatus", -1);
		return this.taskOpinionDao.findByObjectPropertys(TaskOpinionEntity.class, param);
	}

	@Override
	public List<TaskOpinionEntity> getApprovedList(String actInstId){
		return taskOpinionDao.findHql("from TaskOpinionEntity where actInstId=? and checkStatus!=-1",actInstId);
	}

	
}
