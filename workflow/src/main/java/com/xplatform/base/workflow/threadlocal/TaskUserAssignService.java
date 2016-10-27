package com.xplatform.base.workflow.threadlocal;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;










import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.script.IScript;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.service.NodeUserService;
/**
 * 
 * description :多实例任务动态用户分配，用threadlocal来管理
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月12日 下午2:21:03
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月12日 下午2:21:03
 *
 */
@Service
public class TaskUserAssignService implements IScript {
	private Logger logger = LoggerFactory.getLogger(TaskUserAssignService.class);
	//任务节点对应的id和执行人集合
	private static ThreadLocal<Map<String, List<TaskExecutor>>> nodeUserMapLocal = new ThreadLocal<Map<String, List<TaskExecutor>>>();

	private static ThreadLocal<List<TaskExecutor>> formUsers = new ThreadLocal<List<TaskExecutor>>();
	//任务节点的执行人
	private static ThreadLocal<List<TaskExecutor>> taskExecutors = new ThreadLocal<List<TaskExecutor>>();

	@Resource
	private NodeUserService nodeUserService;

	@Resource
	private DefinitionService definitionService;

	public void setNodeUser(Map<String, List<TaskExecutor>> map) {
		nodeUserMapLocal.set(map);
	}

	/**
	 * @Decription 增加节点和对应的执行人
	 * @author xiehs
	 * @createtime 2014年8月12日 下午2:17:52
	 * @param nodeId
	 * @param executors
	 */
	public void addNodeUser(String nodeId, List<TaskExecutor> executors) {
		if (BeanUtils.isEmpty(executors))
			return;

		Map<String, List<TaskExecutor>> nodeUserMap = (Map<String, List<TaskExecutor>>) nodeUserMapLocal.get();
		if (nodeUserMap == null){
			nodeUserMap = new HashMap<String, List<TaskExecutor>>();
		}
		nodeUserMap.remove(nodeId);
		nodeUserMap.put(nodeId, executors);
		nodeUserMapLocal.set(nodeUserMap);
	}

	public void addNodeUser(String nodeId, String userIds) {
		if (StringUtil.isEmpty(userIds))
			return;
		List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
		addNodeUser(nodeId, executorList);
	}

	public void addNodeUser(String[] aryNodeId, String[] aryUserIds) {
		if (BeanUtils.isEmpty(aryUserIds))
			return;
		Map<String, List<TaskExecutor>> nodeUserMap = (Map<String, List<TaskExecutor>>) nodeUserMapLocal.get();
		if (nodeUserMap == null){
			nodeUserMap = new HashMap<String, List<TaskExecutor>>();
		}
		for (int i = 0; i < aryNodeId.length; i++) {
			String nodeId = aryNodeId[i];
			String userIds = aryUserIds[i];
			if (userIds == null)
				continue;
			List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
			nodeUserMap.put(nodeId, executorList);
		}
		nodeUserMapLocal.set(nodeUserMap);
	}

	public Map<String, List<TaskExecutor>> getNodeUserMap() {
		return (Map<String, List<TaskExecutor>>) nodeUserMapLocal.get();
	}

	public void clearNodeUserMap() {
		nodeUserMapLocal.remove();
	}
	
	/** 
     * 获取多实例内部子流程的执行用户集合 
     * @param execution 
     * @return 
     * @throws Execption 
    */  
	public List<TaskExecutor> getMultipleUser(ActivityExecution execution)
			throws Exception {
		String nodeId = execution.getActivity().getId();
		ExecutionEntity executionEnt = (ExecutionEntity) execution;

		String multiInstance = (String) executionEnt.getActivity().getProperty(
				"multiInstance");
		String varName = nodeId + "_" + "subAssignIds";

		if ("sequential".equals(multiInstance)) {
			List list = (List) execution.getVariable(varName);
			if (list != null)
				return list;
		}

		Map nodeMap = NodeCache.getByActDefId(executionEnt
				.getProcessDefinitionId());
		FlowNode subProcessNode = (FlowNode) nodeMap.get(nodeId);
		FlowNode firstNode = subProcessNode.getSubFirstNode();//子流程的第一个结点，开始节点
		FlowNode secondeNode = (FlowNode) firstNode.getNextFlowNodes().get(0);//子流程第二个节点

		//动态设置的节点用户
		List<TaskExecutor> userList = getExecutors();

		if ((BeanUtils.isEmpty(userList)) && (nodeUserMapLocal.get() != null)) {
			userList = (List) ((Map) nodeUserMapLocal.get()).get(secondeNode
					.getNodeId());
		}
		
		//如果没有设置
		if (BeanUtils.isEmpty(userList)) {
			String actInstId = execution.getProcessInstanceId();
			Map variables = execution.getVariables();
			String startUserId = variables.get("startUser").toString();
			String actDefId = execution.getProcessDefinitionId();
			//String preTaskUser = ClientUtil.getSessionUserName().getId();
			//根据第二个节点设置的人员实例化
			userList = this.nodeUserService.getExecutors(actDefId,actInstId, secondeNode.getNodeId(), startUserId, variables,NodeUserEntity.FUNC_NODE_USER);
			userList=changeUser(userList);
		}

		if (BeanUtils.isEmpty(userList)) {
			//MessageUtil.addMsg("请设置子流程:[" + secondeNode.getNodeName() + "]的人员!");
		}
		this.logger.debug("userList size:" + userList.size());

		if ((BeanUtils.isNotEmpty(userList))
				&& ("sequential".equals(multiInstance))) {
			executionEnt.setVariable(varName, userList);
		}

		return userList;
	}

	/** 
     * 获取多实体外部子流程的执行用户集合 
     * @param execution 
     * @return 
     * @throws Execption 
    */ 
	public List<TaskExecutor> getExtSubProcessMultipleUser(
			ActivityExecution execution) throws Exception {
		String nodeId = execution.getActivity().getId();
		String nodeName = execution.getCurrentActivityName();
		ExecutionEntity executionEnt = (ExecutionEntity) execution;

		String multiInstance = (String) executionEnt.getActivity().getProperty("multiInstance");

		String varName = executionEnt.getActivityId() + "_" + "subExtAssignIds";

		if ("sequential".equals(multiInstance)) {
			List userIds = (List) executionEnt.getParent().getVariable(varName);
			if (userIds != null)
				return userIds;
		} else {
			List userIds = (List) execution.getVariable(varName);
			if (userIds != null)
				return userIds;

		}

		Map nodeMap = NodeCache.getByActDefId(executionEnt
				.getProcessDefinitionId());

		FlowNode subProcessNode = (FlowNode) nodeMap.get(nodeId);

		String flowKey = subProcessNode.getAttribute("subFlowKey");

		Map subProcessNodesMap = subProcessNode.getSubProcessNodes();

		FlowNode startNode = NodeCache.getStartNode(subProcessNodesMap);

		FlowNode secodeNode = (FlowNode) startNode.getNextFlowNodes().get(0);

		List<TaskExecutor> userList = getExecutors();

		if (BeanUtils.isEmpty(userList)) {
			Map map = getNodeUserMap();
			if (map != null) {
				userList = (List) map.get(secodeNode.getNodeId());
			}
		}

		if (BeanUtils.isEmpty(userList)) {
			String actInstId = execution.getProcessInstanceId();

			Map<String,Object> variables = execution.getVariables();
			String startUserId = variables.get("startUser").toString();
			DefinitionEntity definition = this.definitionService.getMainDefByActDefKey(flowKey);
			String actDefId = definition.getActId();

			userList = this.nodeUserService.getExecutors(actDefId,actInstId, secodeNode.getNodeId(), startUserId, variables,NodeUserEntity.FUNC_NODE_USER);
			userList=changeUser(userList);
		}
		
		if ((multiInstance != null) && (BeanUtils.isEmpty(userList))) {
			//MessageUtil.addMsg("请设置子流程:[" + nodeName + "]的人员!");
		}

		this.logger.debug("userList size:" + userList.size());

		executionEnt.setVariable(varName, userList);

		return userList;
	}

	/** 
     * 获取普通会签流程的执行用户集合 
     * @param execution 
     * @return 
     * @throws Execption 
    */ 
	public List<TaskExecutor> getSignUser(ActivityExecution execution)
			throws Exception {
		String nodeId = execution.getActivity().getId();
		String nodeName = (String) execution.getActivity().getProperty("name");
		String multiInstance = (String) execution.getActivity().getProperty(
				"multiInstance");

		List userIds = null;

		String varName = nodeId + "_" + "signUsers";

		//串行
		if ("sequential".equals(multiInstance)) {
			userIds = (List) execution.getVariable(varName);
			if (userIds != null) {
				return userIds;
			}
		}

		Map nodeUserMap = (Map) nodeUserMapLocal.get();

		if ((nodeUserMap != null)
				&& (BeanUtils.isNotEmpty(nodeUserMap.get(nodeId)))) {
			userIds = (List) nodeUserMap.get(nodeId);
			saveExecutorVar(execution, userIds);
			return userIds;
		}

		userIds = getExecutors();

		if (BeanUtils.isNotEmpty(userIds)) {
			saveExecutorVar(execution, userIds);
			addNodeUser(nodeId, userIds);
			return userIds;
		}

		ExecutionEntity ent = (ExecutionEntity) execution;
		String actDefId = ent.getProcessDefinitionId();

		String actInstId = execution.getProcessInstanceId();
		Map<String,Object> variables = execution.getVariables();
		String startUserId = variables.get("startUser").toString();
		//数据库配置计算人员
		List<TaskExecutor> list = this.nodeUserService.getExecutors(actDefId,actInstId, nodeId, startUserId, variables,NodeUserEntity.FUNC_NODE_USER);
		list=changeUser(list);
		if (BeanUtils.isEmpty(list)) {
			//MessageUtil.addMsg("请设置会签节点:[" + nodeName + "]的人员!");
		}
		if (BeanUtils.isNotEmpty(list)) {
			saveExecutorVar(execution, list);
		}
		addNodeUser(nodeId, list);
		return list;
	}

	private void saveExecutorVar(ActivityExecution execution,
			List<TaskExecutor> userIds) {
		String multiInstance = (String) execution.getActivity().getProperty(
				"multiInstance");
		if ("sequential".equals(multiInstance)) {
			String nodeId = execution.getActivity().getId();
			String varName = nodeId + "_" + "signUsers";
			execution.setVariable(varName, userIds);
		}
	}
	
	private List<TaskExecutor> changeUser(List<TaskExecutor> list){
		SysUserService sysUserService=ApplicationContextUtil.getBean("sysUserService");
		Map<String,TaskExecutor> userMapList=new HashMap<String,TaskExecutor>();
		List<TaskExecutor> exeList=new ArrayList<TaskExecutor>();
		if(list!=null && list.size()>0){
			for(TaskExecutor ex:list){
				List<UserEntity> userList=new ArrayList<UserEntity>();
				if("role".equals(ex.getType())){
					userList=sysUserService.getUserListByRoleId(ex.getExecuteId());
				}else if("pos".equals(ex.getType())){
					userList=sysUserService.getUserByCurrentOrgIds(ex.getExecuteId());
				}else if("org".equals(ex.getType())){
					userList=sysUserService.getUserByCurrentOrgIds(ex.getExecuteId());
				}else{
					userMapList.put(ex.getExecuteId(), ex);
				}
				if(userList!=null && userList.size()>0){
					for(UserEntity user:userList){
						userMapList.put(user.getId(), TaskExecutor.getTaskUser(user.getId(), user.getName()));
					}
				}
			}
		}
		for(Map.Entry<String, TaskExecutor> entry:userMapList.entrySet()){
			exeList.add(entry.getValue());
		}
		return exeList;
	}

	public void setExecutors(String users) {
		if (StringUtil.isEmpty(users)) {
			return;
		}
		String[] aryUsers = users.split(",");
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		for (String userId : aryUsers) {
			TaskExecutor executor = new TaskExecutor(userId);
			list.add(executor);
		}
		taskExecutors.set(list);
	}

	public void setExecutors(List<TaskExecutor> users) {
		taskExecutors.set(users);
	}

	public List<TaskExecutor> getExecutors() {
		return (List<TaskExecutor>) taskExecutors.get();
	}

	public void clearExecutors() {
		taskExecutors.remove();
	}

	public static void clearAll() {
		formUsers.remove();
		taskExecutors.remove();
		nodeUserMapLocal.remove();
	}
}