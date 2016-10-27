package com.xplatform.base.workflow.core.facade.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.agent.service.AgentSettingService;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.bpm.util.BpmConst;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstCptoService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;
import com.xplatform.base.workflow.threadlocal.TaskUserAssignService;

/**
 * 
 * description :任务创建事件，主要是分配人员
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年9月2日 下午3:28:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年9月2日 下午3:28:57
 *
 */
public class TaskCreateListener extends BaseTaskListener {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;

	private TaskOpinionService taskOpinionService =ApplicationContextUtil.getBean("taskOpinionService");
	private TaskUserAssignService taskUserAssignService = ApplicationContextUtil.getBean("taskUserAssignService");
	private TaskNodeStatusService taskNodeStatusService = ApplicationContextUtil.getBean("taskNodeStatusService");
	private AgentSettingService agentSettingService = ApplicationContextUtil.getBean("agentSettingService");
	private SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
	private ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
	private DefinitionService definitionService = ApplicationContextUtil.getBean("definitionService");
	private ProcessInstCptoService processInstCptoService = ApplicationContextUtil.getBean("processInstCptoService");
	private FlowInstanceUserService flowInstanceUserService = ApplicationContextUtil.getBean("flowInstanceUserService");
	
	@Override
	protected void execute(DelegateTask delegateTask, String actDefId,String nodeId) {
		// TODO Auto-generated method stub
		delegateTask.setDescription(TaskOpinionEntity.STATUS_CHECKING.toString());
		

		String token = TaskThreadService.getToken();
		if (token != null) {
			delegateTask.setVariableLocal(TaskForkEntity.TAKEN_VAR_NAME, token);
		}

		TaskThreadService.addTask((TaskEntity) delegateTask);

		addOpinion(token, delegateTask); //添加任务状态

		String actInstanceId = delegateTask.getProcessInstanceId();

		this.taskNodeStatusService.saveOrUpdte(actDefId, actInstanceId, nodeId, TaskOpinionEntity.STATUS_CHECKING.toString());//流程状态
		//抄送信息
		copyMessage(nodeId,delegateTask);
		//nextProcess中设置,跳转的结点跟结点的执行人
		Map<String,List<TaskExecutor>> nodeUserMap = this.taskUserAssignService.getNodeUserMap();
		boolean isHandForkTask = handlerForkTask(actDefId, nodeId, nodeUserMap,delegateTask);//添加动态任务，主要用来流程的分发与汇总
		if (isHandForkTask){
			return;
		}
		//子流程
		boolean isSubProcess = handSubProcessUser(delegateTask);
		if (isSubProcess){
			return;
		}
		//多实例外部调用子流程
		boolean isHandExtUser = handExtSubProcessUser(delegateTask);
		if (isHandExtUser){
			return;
		}
		//跳转的结点跟结点的执行人
		if ((nodeUserMap != null) && (nodeUserMap.get(nodeId) != null)) {
			List executorIds = (List) nodeUserMap.get(nodeId);
			assignUser(delegateTask, executorIds);
			return;
		}
		//动态选择任务的执行人在nextProcess中设置
		List<TaskExecutor> executorUsers = this.taskUserAssignService.getExecutors(); 

		if (BeanUtils.isNotEmpty(executorUsers)) {
			assignUser(delegateTask, executorUsers);
			return;
		}

		//那么执行节点配置的时候指定的计算候选人
		handAssignUserFromDb(actDefId, nodeId, delegateTask);
	}
		
	
	private boolean handSubProcessUser(DelegateTask delegateTask) {
		FlowNode flowNode = (FlowNode) NodeCache.getByActDefId(delegateTask.getProcessDefinitionId()).get(delegateTask.getTaskDefinitionKey());
		boolean isMultipleNode = flowNode.getIsMultiInstance().booleanValue();
		if (!isMultipleNode){//单实例子流程不做处理
			return false;
		}

		TaskExecutor taskExecutor = (TaskExecutor) delegateTask.getVariable("assignee");
		
		if (taskExecutor != null) {
			//分配用户
			assignUser(delegateTask, taskExecutor);
			//完成的子流程实例
			int completeInstance = ((Integer) delegateTask.getVariable("nrOfCompletedInstances")).intValue();
			//总的子流程实例
			int nrOfInstances = ((Integer) delegateTask.getVariable("nrOfInstances")).intValue();
			//子流程结束
			if (completeInstance == nrOfInstances) {
				delegateTask.removeVariable("subAssignIds");
			}
		}
		return true;
	}

	private boolean handExtSubProcessUser(DelegateTask delegateTask) {
		ExecutionEntity executionEnt = (ExecutionEntity) delegateTask.getExecution();

		if (executionEnt.getSuperExecution() == null)
			return false;
		if (!BpmUtil.isMuiltiExcetion(executionEnt.getSuperExecution()))
			return false;
		String actDefId = executionEnt.getSuperExecution().getProcessDefinitionId();
		Map mapParent = NodeCache.getByActDefId(actDefId);

		String parentNodeId = executionEnt.getSuperExecution().getActivityId();
		String curentNodeId = executionEnt.getActivityId();

		FlowNode parentFlowNode = (FlowNode) mapParent.get(parentNodeId);
		Map subNodeMap = parentFlowNode.getSubProcessNodes();
		FlowNode startNode = NodeCache.getStartNode(subNodeMap);

		if (startNode.getNextFlowNodes().size() == 1) {
			FlowNode nextNode = (FlowNode) startNode.getNextFlowNodes().get(0);
			if (nextNode.getNodeId().equals(curentNodeId)) {
				TaskExecutor taskExecutor = (TaskExecutor) executionEnt.getSuperExecution().getVariable("assignee");
				if (taskExecutor != null) {
					assignUser(delegateTask, taskExecutor);
				}
				return true;
			}
			return false;
		}
		this.logger.debug("多实例外部调用子流程起始节点后只能跟一个任务节点");
		return false;
	}
	
	private boolean handlerForkTask(String actDefId, String nodeId,Map<String, List<TaskExecutor>> nodeUserMap,DelegateTask delegateTask) {
		return false;
	}
	
	private void addOpinion(String token, DelegateTask delegateTask) {
		TaskOpinionEntity taskOpinion = new TaskOpinionEntity(delegateTask);
		taskOpinion.setTaskToken(token);
		ProcessCmd cmd=TaskThreadService.getProcessCmd();
		if(StringUtil.isNotEmpty(cmd.getBackType())){
			taskOpinion.setBackType(cmd.getBackType());
			taskOpinion.setBackTaskId(cmd.getTaskId());
			taskOpinion.setBackNodeId(cmd.getNodeId());
			TaskOpinionEntity preTaskOpinionthis=taskOpinionService.getLatestTaskOpinion(delegateTask.getProcessInstanceId(), delegateTask.getTaskDefinitionKey());
			if(preTaskOpinionthis!=null && preTaskOpinionthis.getParentNodeId()!=null){
				taskOpinion.setParentNodeId(preTaskOpinionthis.getParentNodeId());
			}
		}else{
			if(cmd.isBack().intValue() == 0){
				taskOpinion.setParentNodeId(cmd.getParentNodeId());
			}else{
				TaskOpinionEntity preTaskOpinionthis=taskOpinionService.getLatestTaskOpinion(delegateTask.getProcessInstanceId(), delegateTask.getTaskDefinitionKey());
				if(preTaskOpinionthis!=null && preTaskOpinionthis.getParentNodeId()!=null){
					taskOpinion.setParentNodeId(preTaskOpinionthis.getParentNodeId());
				}
			}
			
		}
		NodeSetService nodeSetService = ApplicationContextUtil.getBean("nodeSetService");
		NodeSetEntity bpmNodeSet = nodeSetService.getByActDefIdNodeId(delegateTask.getProcessDefinitionId(), cmd.getNodeId());
		//节点表单设置为空，取全局的表单设置
		if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
			NodeSetEntity globalNodeSet = nodeSetService.getGlobalByActDefId(delegateTask.getProcessDefinitionId());
			bpmNodeSet.setFormId(globalNodeSet.getFormId());
		}
		taskOpinion.setFormId(bpmNodeSet.getFormId());
		try {
			this.taskOpinionService.save(taskOpinion);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//从节点配置的用户中计算用户
	private void handAssignUserFromDb(String actDefId, String nodeId,DelegateTask delegateTask) {
		Integer isStartAssign=TaskThreadService.getProcessCmd().getVariables().get("isStartAssign")==null?0:Integer.parseInt(TaskThreadService.getProcessCmd().getVariables().get("isStartAssign").toString());
        if(isStartAssign==1){
        	String businessKey=TaskThreadService.getProcessCmd().getBusinessKey();
        	try {
        		FlowInstanceUserEntity user=flowInstanceUserService.queryFIUListByStatus(businessKey,delegateTask.getTaskDefinitionKey());
        		if(user!=null){
        			List<TaskExecutor> users=new ArrayList<TaskExecutor>();
            		TaskExecutor execute=new TaskExecutor(user.getType(), user.getUserId(), user.getUserName());
            		users.add(execute);
            		assignUser(delegateTask, users);
        		}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
        	String actInstId = delegateTask.getProcessInstanceId();
    		NodeUserService nodeUserService=ApplicationContextUtil.getBean("nodeUserService");
    		Map<String,Object> vars = delegateTask.getVariables();
    		String startUserId = (String) vars.get("startUser");
    		if ((StringUtil.isEmpty(startUserId))&& (vars.containsKey("innerPassVars"))) {
    			Map<String,Object> localVars =  (Map<String, Object>) vars.get("innerPassVars");
    			startUserId = (String) localVars.get("startUser");
    		}
    		List<TaskExecutor> users= nodeUserService.getExecutors(actDefId, actInstId, nodeId,startUserId, vars,NodeUserEntity.FUNC_NODE_USER);
    		assignUser(delegateTask, users);
        }
		
		
	}
	
	private void assignUser(DelegateTask delegateTask, TaskExecutor taskExecutor) {
		if ("user".equals(taskExecutor.getType())) {
			delegateTask.setOwner(taskExecutor.getExecuteId());

			String sysUserId = taskExecutor.getExecuteId();
			UserEntity sysUser = null;
			String selfReceive="";
			if (isAllowAgent()) {
				Map<String,Object> agent= this.agentSettingService.getAgent(delegateTask,sysUserId);
				if(agent!=null){
					sysUser=(UserEntity)agent.get("sysUser");
					selfReceive=(String)agent.get("selfReceive");
				}
			}
			//是否代理
			if (sysUser != null && !StringUtil.equals("Y", selfReceive)) {//代理自己不接收任务
				delegateTask.setAssignee(sysUser.getId());
				delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
				delegateTask.setOwner(taskExecutor.getExecuteId());
			} else if(sysUser != null && StringUtil.equals("Y", selfReceive)){//代理自己接收任务
				//delegateTask.setAssignee(sysUser.getId());
				delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
				delegateTask.setOwner(taskExecutor.getExecuteId());
				delegateTask.addCandidateUser(sysUserId);
				delegateTask.addCandidateUser(taskExecutor.getExecuteId());
			}else {//非代理
				delegateTask.setAssignee(taskExecutor.getExecuteId());
			}
			
			TaskOpinionEntity taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());
			UserEntity exeUser = this.sysUserService.getUserById(sysUserId);
			taskOpinion.setExeUserId(exeUser.getId());
			taskOpinion.setExeUserName(exeUser.getUserName());
			try {
				this.taskOpinionService.update(taskOpinion);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			delegateTask.setAssignee("0");
			delegateTask.setOwner("0");
			List<TaskExecutor> userList = getByTaskExecutor(taskExecutor);
			for (TaskExecutor ex : userList){
				if (ex.getType().equals("user")) {
					delegateTask.addCandidateUser(ex.getExecuteId());
				} else {
					delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
				}
			}
		}
	}
	
	private void assignUser(DelegateTask delegateTask,List<TaskExecutor> executors) {
		if (BeanUtils.isEmpty(executors)) {
			String msg = "节点:" + delegateTask.getName() + ",没有设置执行人";
			return;
		}

		if (executors.size() == 1) {
			TaskExecutor taskExecutor = (TaskExecutor) executors.get(0);
			TaskOpinionEntity taskOpinion;
			if ("user".equals(taskExecutor.getType())) {
				String sysUserId = taskExecutor.getExecuteId();
				UserEntity sysUser = null;
				String selfReceive="";
				if (isAllowAgent()) {
					Map<String,Object> agent= this.agentSettingService.getAgent(delegateTask,sysUserId);
					if(agent!=null && agent.size()>0){
						sysUser=(UserEntity)agent.get("sysUser");
						selfReceive=(String)agent.get("selfReceive");
					}
				}
				if (sysUser != null && !StringUtil.equals("Y", selfReceive)) {//代理自己不接收任务
					delegateTask.setAssignee(sysUser.getId());
					delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
					delegateTask.setOwner(taskExecutor.getExecuteId());
				} else if(sysUser != null && StringUtil.equals("Y", selfReceive)){//代理自己接收任务
					delegateTask.setDescription(TaskOpinionEntity.STATUS_AGENT.toString());
					delegateTask.setOwner(taskExecutor.getExecuteId());
					delegateTask.setAssignee("0");
					delegateTask.addCandidateUser(sysUserId);
					delegateTask.addCandidateUser(sysUser.getId());
				}else {//非代理
					delegateTask.setAssignee(taskExecutor.getExecuteId());
				}

				taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());
				sysUser = this.sysUserService.getUserById(sysUserId);
				taskOpinion.setExeUserId(sysUser.getId());
				taskOpinion.setExeUserName(sysUser.getUserName());

				try {
					this.taskOpinionService.update(taskOpinion);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				delegateTask.setAssignee("0");//这个必须设置
				delegateTask.setOwner("0");
				List<TaskExecutor> list = getByTaskExecutor(taskExecutor);
				for (TaskExecutor ex : list) {
					if (ex.getType().equals("user")) {
						delegateTask.addCandidateUser(ex.getExecuteId());
					} else{
						delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
					}
				}
			}
		//多个执行人，那么就设置为候选人	
		} else {
			delegateTask.setAssignee("0");//这个必须设置
			delegateTask.setOwner("0");

			Set<TaskExecutor> set = getByTaskExecutors(executors);
			if (BeanUtils.isEmpty(set)) {
				String msg = "没有设置人员,请检查人员配置!";
				return ;
				//throw new BusinessException(msg);
			}
			for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();) {
				TaskExecutor ex = it.next();
				if (ex.getType().equals("user")) {
					delegateTask.addCandidateUser(ex.getExecuteId());
				} else{
					delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
				}
			}
		}
	}
	
	public void copyMessage(String nodeId, DelegateTask delegateTask){
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Map<String,Object> var=delegateTask.getVariables();
		var.put("nodeId", delegateTask.getTaskDefinitionKey());
		var.put("nodeName", delegateTask.getName());
		var.put("taskId", delegateTask.getId());
		var.put("exeType", "create");
		ProcessInstanceEntity processRun=processInstanceService.getByActInstanceId(delegateTask.getProcessInstanceId());
		DefinitionEntity definition=definitionService.getByActDefId(delegateTask.getProcessDefinitionId());
		try {
			this.processInstCptoService.handlerCopyTask(processRun, var, cmd.getCurrentUserId().toString(), definition,NodeUserEntity.FUNC_PRE_NOTICE_USER);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected String getScriptType() {
		return BpmConst.PreScript;
	}
}