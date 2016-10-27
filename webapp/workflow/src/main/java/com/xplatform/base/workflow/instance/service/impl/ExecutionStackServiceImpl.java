package com.xplatform.base.workflow.instance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.instance.dao.ExecutionStackDao;
import com.xplatform.base.workflow.instance.entity.ExecutionStackEntity;
import com.xplatform.base.workflow.instance.service.ExecutionStackService;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;
import com.xplatform.base.workflow.threadlocal.TaskUserAssignService;

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
@Service("executionStackService")
public class ExecutionStackServiceImpl implements ExecutionStackService {
	private static final Logger logger = Logger.getLogger(ExecutionStackServiceImpl.class);
	@Resource
	private ExecutionStackDao executionStackDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private TaskUserAssignService taskUserAssignService;
	
	@Resource
	private TaskService taskService;

	@Resource
	private FlowService flowService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private TaskOpinionService taskOpinionService;
	
	@Resource
	private TaskNodeStatusService taskNodeStatusService;

	@Override
	@Action(moduleCode="ExecutionStackManager",description="流程实例新增",detail="流程实例${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ExecutionStackEntity ExecutionStack) throws BusinessException {
		String pk="";
		try {
			pk=this.executionStackDao.addExecutionStack(ExecutionStack);
		} catch (Exception e) {
			logger.error("流程实例保存失败");
			throw new BusinessException("流程实例保存失败");
		}
		logger.info("流程实例保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="ExecutionStackManager",description="流程实例删除",detail="流程实例${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.executionStackDao.deleteExecutionStack(id);
		} catch (Exception e) {
			logger.error("流程实例删除失败");
			throw new BusinessException("流程实例删除失败");
		}
		logger.info("流程实例删除成功");
	}

	@Override
	@Action(moduleCode="ExecutionStackManager",description="流程实例批量删除",detail="流程实例${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
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
	@Action(moduleCode="ExecutionStackManager",description="流程实例修改",detail="流程实例${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(ExecutionStackEntity ExecutionStack) throws BusinessException {
		try {
			ExecutionStackEntity oldEntity = get(ExecutionStack.getId());
			MyBeanUtils.copyBeanNotNull2Bean(ExecutionStack, oldEntity);
			this.executionStackDao.updateExecutionStack(oldEntity);
		} catch (Exception e) {
			logger.error("流程实例更新失败");
			throw new BusinessException("流程实例更新失败");
		}
		logger.info("流程实例更新成功");
	}

	@Override
	public ExecutionStackEntity get(String id){
		ExecutionStackEntity ExecutionStack=null;
		try {
			ExecutionStack=this.executionStackDao.getExecutionStack(id);
		} catch (Exception e) {
			logger.error("流程实例获取失败");
			//throw new BusinessException("流程实例获取失败");
		}
		logger.info("流程实例获取成功");
		return ExecutionStack;
	}

	@Override
	public List<ExecutionStackEntity> queryList() throws BusinessException {
		List<ExecutionStackEntity> ExecutionStackList=new ArrayList<ExecutionStackEntity>();
		try {
			ExecutionStackList=this.executionStackDao.queryExecutionStackList();
		} catch (Exception e) {
			logger.error("流程实例获取列表失败");
			throw new BusinessException("流程实例获取列表失败");
		}
		logger.info("流程实例获取列表成功");
		return ExecutionStackList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.executionStackDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("流程实例获取分页列表失败");
			throw new BusinessException("流程实例获取分页列表失败");
		}
		logger.info("流程实例获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(ExecutionStackEntity.class, param, propertyName);
	}
	
	public ExecutionStackEntity getLastestStack(String actInstId,String parentNodeId,String taskToken){
		if (StringUtil.isNotEmpty(taskToken)) {
			List<ExecutionStackEntity> list = getByActInstIdNodeIdToken(actInstId, parentNodeId, taskToken);
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
	    }
		return getLastestStack(actInstId, parentNodeId);
	}
	
	private List<ExecutionStackEntity> getByActInstIdNodeIdToken(String actInstId,String parentNodeId, String taskToken){
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInsId", actInstId);
		param.put("nodeId", parentNodeId);
		param.put("taskToken", taskToken);
		List<String> orders=new ArrayList<String>();
		orders.add("createTime");
		return this.executionStackDao.findByPropertysisOrder(ExecutionStackEntity.class, param, false, orders);
	}
	
	@Override
	public void initStack(String actInstId, List<Task> taskList)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (BeanUtils.isEmpty(taskList))
			return;
		//组装数据
		Map<String,ExecutionStackEntity> nodeIdStackMap = new HashMap<String,ExecutionStackEntity>();
		for (Task task : taskList) {
			String nodeId = task.getTaskDefinitionKey();
			if (!nodeIdStackMap.containsKey(nodeId)) {
				ExecutionStackEntity stack = new ExecutionStackEntity();
				stack.setActInsId(actInstId);
				stack.setActDefId(task.getProcessDefinitionId());
				stack.setAssignees(task.getAssignee());
				stack.setDepth(Integer.valueOf(1));
				stack.setParentId("-1");
				stack.setNodeId(nodeId);
				stack.setNodeName(task.getName());
				stack.setTaskIds(task.getId());
				nodeIdStackMap.put(nodeId, stack);
			} else {
				ExecutionStackEntity stack = (ExecutionStackEntity) nodeIdStackMap.get(nodeId);
				stack.setIsMutiTask(ExecutionStackEntity.MULTI_TASK);
				stack.setAssignees(stack.getAssignees() + ","+ task.getAssignee());
				stack.setTaskIds(stack.getTaskIds() + "," + task.getId());
			}
		}
		//保存数据
		for(Map.Entry<String, ExecutionStackEntity> entry:nodeIdStackMap.entrySet()){
			ExecutionStackEntity executionStack=entry.getValue();
			String pk=this.save(executionStack);
			executionStack.setNodePath(pk);//设置路径
			executionStack.setStatus("N");//初始化任务，没有完成的状态
			this.update(executionStack);
		}
	}

	@Override
	public void pop(ExecutionStackEntity parentStack, boolean isRecover,
			Integer isBack) throws BusinessException {
		// TODO Auto-generated method stub
		String instanceId = parentStack.getActInsId();
		String actDefId=parentStack.getActDefId();

		List<ExecutionStackEntity> subChilds = this.getByParentId(parentStack.getId());

		UserEntity curUser = ClientUtil.getUserEntity();
		if (BeanUtils.isEmpty(subChilds))
			return;

		ExecutionStackEntity executionStack = subChilds.get(0);
		String prevNodeId = executionStack.getNodeId();
		Short status = TaskOpinionEntity.STATUS_REJECT;

		if (isBack.intValue() == 1) {
			TaskOpinionEntity taskOpinion = this.taskOpinionService.getLatestTaskOpinion(instanceId, prevNodeId);
			if (taskOpinion != null) {
				taskOpinion.setExeUserId(curUser.getId());
				taskOpinion.setExeUserName(curUser.getName());
				taskOpinion.setEndTime(new Date());
				taskOpinion.setDurTime(DateUtils.getTime(DateUtils.getTime(taskOpinion.getCreateTime(), taskOpinion.getEndTime())));
				if (isRecover) {
					status = TaskOpinionEntity.STATUS_RECOVER;
				}
				taskOpinion.setCheckStatus(status.intValue());
				this.taskOpinionService.update(taskOpinion);
			}

			this.taskNodeStatusService.saveOrUpdte(actDefId, instanceId,prevNodeId, status.toString());
		}

		this.delete(executionStack.getId());
	}

	@Override
	public ExecutionStackEntity backPrepared(ProcessCmd processCmd,
			TaskEntity taskEntity, String taskToken) throws BusinessException {
		// TODO Auto-generated method stub
		ExecutionStackEntity parentStack = null;
		String instanceId = taskEntity.getProcessInstanceId();
		String actDefId = taskEntity.getProcessDefinitionId();
		String nodeId = taskEntity.getTaskDefinitionKey();//当前的任务节点

		if (processCmd.getStackId() != null) {
			parentStack = get(processCmd.getStackId());
		} else if (StringUtil.isEmpty(processCmd.getDestTask())) {
			ExecutionStackEntity executionStack = getLastestStack(instanceId, nodeId,taskToken);
			if ((executionStack != null)&& (StringUtil.isNotEmpty(executionStack.getParentId()))) {
				parentStack = get(executionStack.getParentId());//获取父节点
				while (nodeId.equals(parentStack.getNodeId())) {//这段代码的目的是
					parentStack = get(parentStack.getParentId());
				}
			}

		}

		if (parentStack != null) {
			processCmd.setDestTask(parentStack.getNodeId());
			//是不是会签节点
			boolean rtn = NodeCache.isSignTaskNode(actDefId, parentStack.getNodeId());
			//不是会签节点，那么在节点上添加人
			if (!rtn) {
				String assignee = parentStack.getAssignees();
				String[] aryAssignee = assignee.split(",");
				List<TaskExecutor> list = new ArrayList<TaskExecutor>();
				for (String userId : aryAssignee) {
					UserEntity user=sysUserService.getUserById(userId);
					list.add(TaskExecutor.getTaskUser(userId, user.getName()));
				}
				this.taskUserAssignService.addNodeUser(parentStack.getNodeId(),list);
			}
		}
		return parentStack;
	}

	@Override
	public void pushNewTasks(String actInstId, String destNodeId,
			List<Task> newTasks, String oldTaskToken) throws BusinessException {
		// TODO Auto-generated method stub
		if (newTasks.size() == 0)
			return;

		ExecutionStackEntity curExeNode = this.getLastestStack(actInstId,destNodeId, oldTaskToken);

		ProcessDefinitionEntity processDef = null;

		Map<String,ExecutionStackEntity> nodeIdStackMap = new HashMap<String,ExecutionStackEntity>();
		int i = 0;

		boolean isIssued = false;
		Set<Task> nodeSet = new HashSet<Task>(newTasks);
		if (nodeSet.size() < newTasks.size()) {
			isIssued = true;
		}

		for (Task task : newTasks) {
			i++;
			TaskEntity taskEntity = (TaskEntity) task;
			try {
				taskEntity = (TaskEntity) this.taskService.createTaskQuery().taskId(task.getId()).singleResult();
			} catch (Exception ex) {
				this.logger.warn("ex:" + ex.getMessage());
			}
			if (taskEntity == null)
				continue;
			String nodeId = taskEntity.getTaskDefinitionKey();

			if (processDef == null) {
				processDef = this.flowService.getProcessDefinitionByProcessInanceId(taskEntity.getProcessInstanceId());
			}

			ActivityImpl taskAct = processDef.findActivity(nodeId);

			if (taskAct == null)
				continue;
			String multiInstance = (String) taskAct.getProperty("multiInstance");
			ExecutionStackEntity stack = (ExecutionStackEntity) nodeIdStackMap.get(nodeId);

			if ((StringUtil.isNotEmpty(multiInstance)) && (stack != null)) {
				stack.setIsMutiTask(ExecutionStackEntity.MULTI_TASK);
				stack.setAssignees(stack.getAssignees() + ","
						+ task.getAssignee());
				stack.setTaskIds(stack.getTaskIds() + "," + task.getId());
				this.update(stack);
			} else {
				stack = new ExecutionStackEntity();
				stack.setActInsId(taskEntity.getProcessInstanceId());
				stack.setAssignees(taskEntity.getAssignee());
				stack.setActDefId(taskEntity.getProcessDefinitionId());
				if (curExeNode == null) {
					stack.setDepth(Integer.valueOf(1));
					stack.setParentId("-1");
				} else {
					stack.setDepth(Integer.valueOf(curExeNode.getDepth() == null ? 1: curExeNode.getDepth().intValue() + 1));
					stack.setParentId(curExeNode.getId());
				}

				stack.setNodeId(nodeId);
				stack.setNodeName(taskEntity.getName());
				stack.setTaskIds(taskEntity.getId());

				String taskToken = (String) this.taskService.getVariableLocal(taskEntity.getId(), TaskForkEntity.TAKEN_VAR_NAME);
				if (taskToken != null) {
					stack.setTaskToken(taskToken);
				} else if ((stack != null) && (isIssued)) {
					String token = "T_" + i;
					this.taskService.setVariableLocal(taskEntity.getId(),TaskForkEntity.TAKEN_VAR_NAME, token);
					stack.setTaskToken(token);
				}
				String pk=this.save(stack);
				if (curExeNode == null) {
					stack.setNodePath(pk);//设置路径
				} else {
					stack.setNodePath(curExeNode.getNodePath() +","+pk);//设置路径
				}
				stack.setStatus("N");//初始化任务，没有完成的状态
				this.update(stack);
				nodeIdStackMap.put(nodeId, stack);
			}
		}
	}

	@Override
	public void addStack(String actInstId, String destNodeId,
			String oldTaskToken) throws BusinessException {
		// TODO Auto-generated method stub
		List<Task> taskList = TaskThreadService.getNewTasks();
		if (taskList != null){
			pushNewTasks(actInstId, destNodeId, taskList, oldTaskToken);
		}
	}

	@Override
	public Integer delSubChilds(String stackId, String nodePath)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExecutionStackEntity> getByParentId(String parentId)
			throws BusinessException {
		// TODO Auto-generated method stub
		return this.executionStackDao.findByProperty(ExecutionStackEntity.class, "parentId", parentId);
	}

	@Override
	public List<ExecutionStackEntity> getByParentIdAndEndTimeNotNull(
			String parentId) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExecutionStackEntity> getByActInstIdNodeId(String actInstId,
			String nodeId) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String,String> param=new HashMap<String,String>();
		param.put("nodeId", nodeId);
		param.put("actInsId", actInstId);
		return this.executionStackDao.findByPropertys(ExecutionStackEntity.class,param);
	}

	@Override
	public void genSiblingTask(ExecutionStackEntity parentStack,
			ProcessTask copyTaskEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExecutionStackEntity getLastestStack(String actInstId, String parentNodeId) {
		// TODO Auto-generated method stub
		Map<String,String> param =new HashMap<String,String>();
		param.put("actInsId", actInstId);
		param.put("nodeId", parentNodeId);
		List<String> orders=new ArrayList<String>();
		orders.add("createTime");
		List<ExecutionStackEntity> list= this.executionStackDao.findByPropertysisOrder(ExecutionStackEntity.class, param, false, orders);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	public void setExecutionStackDao(ExecutionStackDao executionStackDao) {
		this.executionStackDao = executionStackDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.executionStackDao.executeHql("delete from ExecutionStackEntity t where t.actDefId='"+actDefId+"'");
	}
}
