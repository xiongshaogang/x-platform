package com.xplatform.base.workflow.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.task.dao.TaskSignDataDao;
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;
import com.xplatform.base.workflow.task.mybatis.dao.TaskSignDataMybatisDao;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskSignDataService;
/**
 * 
 * description :任务会签投票service实现
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
@Service("taskSignDataService")
public class TaskSignDataServiceImpl implements TaskSignDataService {
	private static final Logger logger = Logger.getLogger(TaskSignDataServiceImpl.class);
	@Resource
	private TaskSignDataDao taskSignDataDao;
	
	@Autowired
	private TaskSignDataMybatisDao taskSignDataMybatisDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private FlowService flowService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private SysUserService sysUserService;

	@Override
	@Action(moduleCode="TaskSignDataManager",description="任务会签投票新增",detail="任务会签投票${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(TaskSignDataEntity TaskSignData) throws BusinessException {
		String pk="";
		try {
			pk=this.taskSignDataDao.addTaskSignData(TaskSignData);
		} catch (Exception e) {
			logger.error("任务会签投票保存失败");
			throw new BusinessException("任务会签投票保存失败");
		}
		logger.info("任务会签投票保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="TaskSignDataManager",description="任务会签投票删除",detail="任务会签投票${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.taskSignDataDao.deleteTaskSignData(id);
		} catch (Exception e) {
			logger.error("任务会签投票删除失败");
			throw new BusinessException("任务会签投票删除失败");
		}
		logger.info("任务会签投票删除成功");
	}

	@Override
	@Action(moduleCode="TaskSignDataManager",description="任务会签投票批量删除",detail="任务会签投票${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("任务会签投票批量删除成功");
	}

	@Override
	@Action(moduleCode="TaskSignDataManager",description="任务会签投票修改",detail="任务会签投票${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(TaskSignDataEntity TaskSignData) throws BusinessException {
		try {
			TaskSignDataEntity oldEntity = get(TaskSignData.getId());
			MyBeanUtils.copyBeanNotNull2Bean(TaskSignData, oldEntity);
			this.taskSignDataDao.updateTaskSignData(oldEntity);
		} catch (Exception e) {
			logger.error("任务会签投票更新失败");
			throw new BusinessException("任务会签投票更新失败");
		}
		logger.info("任务会签投票更新成功");
	}

	@Override
	public TaskSignDataEntity get(String id) {
		TaskSignDataEntity TaskSignData=null;
		try {
			TaskSignData=this.taskSignDataDao.getTaskSignData(id);
		} catch (Exception e) {
			logger.error("任务会签投票获取失败");
			//throw new BusinessException("任务会签投票获取失败");
		}
		logger.info("任务会签投票获取成功");
		return TaskSignData;
	}

	@Override
	public List<TaskSignDataEntity> queryList() throws BusinessException {
		List<TaskSignDataEntity> TaskSignDataList=new ArrayList<TaskSignDataEntity>();
		try {
			TaskSignDataList=this.taskSignDataDao.queryTaskSignDataList();
		} catch (Exception e) {
			logger.error("任务会签投票获取列表失败");
			throw new BusinessException("任务会签投票获取列表失败");
		}
		logger.info("任务会签投票获取列表成功");
		return TaskSignDataList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.taskSignDataDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("任务会签投票获取分页列表失败");
			throw new BusinessException("任务会签投票获取分页列表失败");
		}
		logger.info("任务会签投票获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(TaskSignDataEntity.class, param, propertyName);
	}
	
	public void setTaskSignDataDao(TaskSignDataDao taskSignDataDao) {
		this.taskSignDataDao = taskSignDataDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public Integer getAgreeVoteCount(String actInstId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInstId", actInstId);
		param.put("nodeId", nodeId);
		Integer count= this.taskSignDataMybatisDao.getAgreeVoteCount(param);
		if(count==null){
			return 0;
		}else{
			return count;
		}
	}

	@Override
	public Integer getRefuseVoteCount(String actInstId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInstId", actInstId);
		param.put("nodeId", nodeId);
		Integer count= this.taskSignDataMybatisDao.getRefuseVoteCount(param);
		if(count==null){
			return 0;
		}else{
			return count;
		}
	}
	
	@Override
	public Integer getReconsideVoteCount(String actInstId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInstId", actInstId);
		param.put("nodeId", nodeId);
		Integer count= this.taskSignDataMybatisDao.getAbortVoteCount(param);
		if(count==null){
			return 0;
		}else{
			return count;
		}
	}

	@Override
	public void batchUpdateCompleted(String actInstId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInstId", actInstId);
		param.put("nodeId", nodeId);
		this.taskSignDataMybatisDao.updateCompleted(param);
	}

	@Override
	public void signVoteTask(String taskId, String content, String isAgree) throws BusinessException{
		// TODO Auto-generated method stub
		UserEntity sysUser = ClientUtil.getUserEntity();
		String userId="";
		String fullName="";
		if (BeanUtils.isNotEmpty(sysUser)) {
			userId = sysUser.getId();
			fullName = sysUser.getName();
		}
		TaskSignDataEntity taskSignData = this.getByTaskId(taskId);
		if (taskSignData != null) {
			taskSignData.setIsCompleted("1");
			taskSignData.setIsAgree(isAgree);
			taskSignData.setContent(content);
			taskSignData.setVoteTime(new Date());
			taskSignData.setVoteUserId(userId);
			taskSignData.setVoteUserName(fullName);
			update(taskSignData);
		}
	}

	@Override
	public TaskSignDataEntity getByTaskId(String taskId) {
		// TODO Auto-generated method stub
		List<TaskSignDataEntity> list=this.taskSignDataDao.findByProperty(TaskSignDataEntity.class, "taskId", taskId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.taskSignDataDao.executeHql("delete from TaskSignDataEntity t where t.actDefId='"+actDefId+"'");
	}

	@Override
	public List<TaskSignDataEntity> getByNodeAndInstanceId(String instanceId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInsId", instanceId);
		param.put("nodeId", nodeId);
		Integer batch=this.getMaxBatch(instanceId,nodeId);
		if(batch==0){
			batch=1;
		}
		param.put("batch", batch);
		return this.taskSignDataDao.findByObjectPropertys(TaskSignDataEntity.class, param);
	}
	
	@Override
	public List<TaskSignDataEntity> getByNodeAndInstanceId(String instanceId,String nodeId, String isCompleted){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInsId", instanceId);
		param.put("nodeId", nodeId);
		Integer batch=this.getMaxBatch(instanceId,nodeId);
		if(batch==0){
			batch=1;
		}
		param.put("batch", batch);
		param.put("isCompleted", isCompleted);
		return this.taskSignDataDao.findByObjectPropertys(TaskSignDataEntity.class, param);
	}

	private List<String> getCanAddUsers(List<String> curUserList,
			String[] addUsers) {
		List users = new ArrayList();
		for (String userId : addUsers) {
			if (!curUserList.contains(userId)) {
				users.add(userId);
			}
		}
		return users;
	}
	
	private void addSignData(String taskId, String nodeId, String nodeName,
			String instanceId, Integer signNums, String userId, Integer batch)  throws BusinessException {

		TaskSignDataEntity newSignData = new TaskSignDataEntity();
		newSignData.setTaskId(taskId);

		newSignData.setNodeId(nodeId);
		newSignData.setNodeName(nodeName);
		newSignData.setTaskId(taskId);
		newSignData.setSignNums(signNums);
		newSignData.setIsCompleted(TaskSignDataEntity.NOT_COMPLETED.toString());
		newSignData.setIsAgree(null);
		newSignData.setBatch(batch.intValue());
		newSignData.setContent(null);
		newSignData.setVoteTime(null);
		newSignData.setVoteUserId(userId);

		UserEntity sysUser = this.sysUserService.getUserById(userId);
		newSignData.setVoteUserName(sysUser.getName());

		this.save(newSignData);
	}
	
	@Override
	public void addSign(String signUserIds, String taskId)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(signUserIds))
			return;
		try {
		
			TaskEntity taskEntity = this.flowService.getTask(taskId);
			ExecutionEntity executionEntity = this.flowService.getExecution(taskEntity.getExecutionId());
	
			String processInstanceId = executionEntity.getProcessInstanceId();
	
			ProcessDefinitionEntity proDefEntity = this.flowService.getProcessDefinitionByProcessInanceId(processInstanceId);
	
			ActivityImpl actImpl = proDefEntity.findActivity(executionEntity.getActivityId());
	
			String multiInstance = (String) actImpl.getProperty("multiInstance");
	
			if (multiInstance == null){
				return;
			}
			
			//获取未完成的任务数
			Map<String,String> params=new HashMap<String,String>();
		    params.put("actInstId", processInstanceId);
		    params.put("nodeId", executionEntity.getActivityId());
		    params.put("isCompleted", TaskSignDataEntity.NOT_COMPLETED.toString());
		    Integer maxSignNums =this.taskSignDataMybatisDao.getMaxSignNums(params);
		    if(maxSignNums==null){
		    	maxSignNums=0;
		    }
			this.logger.debug("multiInstance:" + multiInstance);
	
			Integer signNums = Integer.valueOf(maxSignNums.intValue() == 0 ? 1: maxSignNums.intValue());
	
			List uidlist = new ArrayList();
			
			List<TaskSignDataEntity> existTaskSignDatas = getByNodeAndInstanceId(processInstanceId, taskEntity.getTaskDefinitionKey(), "0");
			Integer curBatch = Integer.valueOf(1);
			for (TaskSignDataEntity taskSignData : existTaskSignDatas) {
				curBatch = Integer.valueOf(taskSignData.getBatch());
				uidlist.add(taskSignData.getVoteUserId());
			}
	
			String[] uIds = signUserIds.split("[,]");
	
			List<String> addUsers = getCanAddUsers(uidlist, uIds);
	
			int userAmount = addUsers.size();
	
			Integer nrOfInstances = (Integer) this.runtimeService.getVariable(
					executionEntity.getId(), "nrOfInstances");
			if (nrOfInstances != null) {
				this.runtimeService.setVariable(executionEntity.getId(),
						"nrOfInstances", Integer.valueOf(nrOfInstances.intValue()
								+ userAmount));
			}
	
			if ("sequential".equals(multiInstance)) {//串行加签
				String nodeId = executionEntity.getActivityId();
				String varName = nodeId + "_" + "signUsers";
				String exeId = executionEntity.getId();
				List<TaskExecutor> addList = new ArrayList<TaskExecutor>();
				for (int i = 0; i < userAmount; i++) {
					String userId = addUsers.get(i);
					int sn = signNums.intValue() + 1;
					addSignData("", nodeId, taskEntity.getName(),
							processInstanceId, Integer.valueOf(sn), userId,
							curBatch);
					addList.add(TaskExecutor.getTaskUser(userId.toString(), ""));
				}
				List<TaskExecutor> list = (List<TaskExecutor>) this.runtimeService.getVariable(exeId, varName);
				list.addAll(addList);
				this.runtimeService.setVariable(executionEntity.getId(), varName,list);
			} else {//并行加签
				Integer loopCounter = (Integer) this.runtimeService.getVariable(
						executionEntity.getId(), "loopCounter");
	
				Integer nrOfActiveInstances = (Integer) this.runtimeService
						.getVariable(executionEntity.getId(), "nrOfActiveInstances");
				this.runtimeService.setVariable(executionEntity.getId(),
						"nrOfActiveInstances", Integer.valueOf(nrOfActiveInstances
								.intValue()
								+ userAmount));
				for (int i = 0; i < userAmount; i++) {
					String userId =  addUsers.get(i);
					ProcessTask newProcessTask = this.flowService.newTask(taskId,(String) addUsers.get(i));
					String executionId = newProcessTask.getExecutionId();
	
					this.runtimeService.setVariableLocal(executionId,"loopCounter", Integer.valueOf(loopCounter.intValue()+ i + 1));
					this.runtimeService.setVariableLocal(executionId, "assignee",TaskExecutor.getTaskUser(userId.toString(), ""));
	
					int sn = signNums.intValue() + 1;
					addSignData(newProcessTask.getId(), executionEntity
							.getActivityId(), taskEntity.getName(),
							processInstanceId, Integer.valueOf(sn), userId,
							curBatch);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("加签失败");
		}
		/*ProcessRun processRun = this.processRunService
				.getByActInstanceId(new Long(processInstanceId));
		String memo = "用户在任务[" + taskEntity.getName() + "]执行了补签操作。";
		this.bpmRunLogService.addRunLog(processRun.getRunId(),
				BpmRunLog.OPERATOR_TYPE_SIGN, memo);*/
	}

	@Override
	public Integer getMaxBatch(String processInstanceId, String nodeId) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInstId", processInstanceId);
		param.put("nodeId", nodeId);
		Integer sum= this.taskSignDataMybatisDao.getMaxBatch(param);
		if(sum==null){
			return 0;
		}else {
			return sum;
		}
	}

	@Override
	public TaskSignDataEntity getUserTaskSign(String processInstanceId,
			String nodeId, String executorId) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("actInsId", processInstanceId);
		param.put("nodeId", nodeId);
		Integer batch=this.getMaxBatch(processInstanceId,nodeId);
		if(batch==0){
			batch=1;
		}
		param.put("batch", batch);
		param.put("isCompleted", "0");
		param.put("voteUserId", executorId);
	    List<TaskSignDataEntity> list=this.taskSignDataDao.findByObjectPropertys(TaskSignDataEntity.class, param);
	    if(list!=null && list.size()>0){
	    	return list.get(0);
	    }
	    return null;
	}
}
