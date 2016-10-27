package com.xplatform.base.workflow.instance.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.instance.entity.ExecutionStackEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;

/**
 * 
 * description : 流程实例管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface ExecutionStackService {
	
	/**
	 * 新增流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param ExecutionStack
	 * @return
	 */
	public String save(ExecutionStackEntity ExecutionStack) throws BusinessException;
	
	/**
	 * 删除流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param ExecutionStack
	 * @return
	 */
	public void update(ExecutionStackEntity ExecutionStack) throws BusinessException;
	
	/**
	 * 查询一条流程实例记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ExecutionStackEntity get(String id);
	
	/**
	 * 查询流程实例列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ExecutionStackEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate流程实例分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName)throws BusinessException;
	//初始化
	public void initStack(String actInstId, List<Task> taskList)throws BusinessException;
	//删除
	public void pop(ExecutionStackEntity parentStack, boolean isRecover,Integer isBack)throws BusinessException;
	//组装回退数据
	public ExecutionStackEntity backPrepared(ProcessCmd processCmd,TaskEntity taskEntity, String taskToken)throws BusinessException;
	//新的任务进栈
	public void pushNewTasks(String actInstId, String destNodeId,List<Task> newTasks, String oldTaskToken)throws BusinessException;
	//进栈
	public void addStack(String actInstId, String destNodeId,String oldTaskToken)throws BusinessException;
	//删除子节点
	public Integer delSubChilds(String stackId, String nodePath)throws BusinessException;
	//得到父节点
	public List<ExecutionStackEntity> getByParentId(String parentId)throws BusinessException;
	//得到父节点
	public List<ExecutionStackEntity> getByParentIdAndEndTimeNotNull(String parentId)throws BusinessException;
	//根据流程实例和节点获取栈节点
	public List<ExecutionStackEntity> getByActInstIdNodeId(String actInstId,String nodeId)throws BusinessException;
	//
	public void genSiblingTask(ExecutionStackEntity parentStack,ProcessTask copyTaskEntity) ;
	//获取最近的任务节点
	public ExecutionStackEntity getLastestStack(String actInstId, String nodeId)throws BusinessException;
	//获取最近的任务节点
	public ExecutionStackEntity getLastestStack(String instanceId,String nodeId,String token)throws BusinessException;
	/**
	 * 删除流程定义的所有记录
	 * @author xiehs
	 * @createtime 2014年10月2日 上午12:00:47
	 * @Decription
	 *
	 * @param actDefId
	 * @throws BusinessException
	 */
	public void deleteByActDefId(String actDefId) throws BusinessException;
}
