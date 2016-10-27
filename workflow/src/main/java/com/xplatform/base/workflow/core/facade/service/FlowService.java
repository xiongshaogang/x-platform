package com.xplatform.base.workflow.core.facade.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeTranUser;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;

public interface FlowService {
	

	
	
	public ProcessInstance startFlowById(String proessDefId,
			Map<String, Object> variables) ;

	public ProcessInstance startFlowById(String porcessDefId,
			String businessKey, Map<String, Object> variables) ;

	/**
	 * 根据流程key开始生成流程实例
	 * @param processDefKey
	 * @param businessKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startFlowByKey(String processDefKey,
			String businessKey, Map<String, Object> variables) ;

	public void activeProcessDefinition(String actDefId) throws BusinessException;
	
	public void stopProcessDefinition(String actDefId) throws BusinessException;
	/**
	 * 发布流程
	 * @param name
	 * @param xml
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Deployment deploy(String name, String xml)throws UnsupportedEncodingException ;

	
	
	public void wirteDefXml(String deployId, String defXml);
	
	/**
	 * 通过deployId得到流程定义
	 * @author xiehs
	 * @createtime 2014年7月7日 下午6:13:34
	 * @Decription
	 * @param deployId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByDeployId(String deployId);
	
	/**
	 * 通过流程实例获取流程定义
	 * @author xiehs
	 * @createtime 2014年8月11日 下午6:24:59
	 * @Decription
	 *
	 * @param processInstanceId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByProcessInanceId(String processInstanceId);
		
	public String getDefXmlByProcessDefinitionId(String processDefinitionId);
	public String getDefXmlByProcessProcessInanceId(String processInstanceId);
	public String getDefXmlByDeployId(String deployId);
	public String getDefXmlByProcessTaskId(String taskId);
	
	public Map<String, String> getExecuteNodesMap(String actDefId,boolean includeSubProcess);
	
	/**
	 * 保存节点的条件
	 * @author xiehs
	 * @createtime 2014年8月1日 上午11:53:00
	 * @Decription
	 *
	 * @param defId
	 * @param forkNode
	 * @param map
	 * @param canChoicePathNodeId
	 * @throws IOException
	 */
	public void saveCondition(String defId, String forkNode,Map<String, String> map, String canChoicePathNodeId)throws IOException;

	/**
	 * 根据任务id获取任务实体
	 * @author xiehs
	 * @createtime 2014年8月19日 下午3:55:30
	 * @Decription
	 *
	 * @param taskId
	 * @return
	 */
	public TaskEntity getTask(String taskId);
	
	/**
	 * 通过流程实例获取任务集合
	 * @author xiehs
	 * @createtime 2014年10月4日 下午5:47:33
	 * @Decription
	 *
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessTask> getTasks(String processInstanceId);
	
	/**
	 * 寻找流程实例的第一个任务结点
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:54:51
	 * @Decription
	 *
	 * @param actInstanceId
	 * @return
	 */
	public ProcessTask getFirstNodeTask(String actInstanceId);
	
	/**
	 * 设置流程变量
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:59:22
	 * @Decription
	 *
	 * @param taskId
	 * @param variableName
	 * @param varVal
	 */
	public void setTaskVariable(String taskId, String variableName,Object varVal);
	
	/**
	 * 删除会签的变量
	 * @author xiehs
	 * @createtime 2014年9月24日 上午9:56:25
	 * @Decription
	 *
	 * @param executionId
	 */
	public void delLoopAssigneeVars(String executionId);
	
	/**
	 * 通过流程实例删除流程
	 * @param instanceId
	 * @param memo
	 * @param userId
	 * @return
	 */
	public ProcessInstanceEntity delProcessByInstanceId(String instanceId, String memo,String userId);
	
	public ExecutionEntity getExecution(String executionId);
	
	public void onlyCompleteTask(String taskId);
	
	public void transTo(String taskId, String toNode)throws BusinessException;
	
	public boolean isSignTask(TaskEntity taskEntity);
	
	public Page<ProcessTask> getMyTasks(Page<ProcessTask> page);
	
	public Page<ProcessTask>  getTasks(Page<ProcessTask> page);
	
	public boolean getIsAllowBackByTask(TaskEntity task);

	public boolean getIsAllowBackByTask(ProcessTask task) ;
	
	public ProcessInstanceEntity endProcessByInstanceId(String instanceId, String nodeId,
			String memo,String userId,String taskId) throws BusinessException ;
	
	/**
	 * 判断节点是否具有驳回权限
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean getIsAllowBackByTask(String actDefId, String nodeId) ;
	
	public ProcessTask newTask(String taskId,String userId);
	
	public Map<String, Map<String, String>> getJumpNodes(String taskId);
	
	public boolean getCanChoicePath(String actDefId, String taskId);
	
	public List<NodeTranUser> getStartNodeUserMap(String actDefId,String startUserId, Map<String, Object> vars) throws Exception;
	
	public List<NodeTranUser> getNodeTaskUserMap(String taskId, String preUserId,boolean canChoicePath) ;
}
