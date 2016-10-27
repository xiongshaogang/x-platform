package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;

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
public interface TaskNodeStatusService {
	
	/**
	 * 新增流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param ProcessInsStatus
	 * @return
	 */
	public String save(TaskNodeStatusEntity ProcessInsStatus) throws BusinessException;
	
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
	 * @param ProcessInsStatus
	 * @return
	 */
	public void update(TaskNodeStatusEntity ProcessInsStatus) throws BusinessException;
	
	/**
	 * 查询一条流程实例记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskNodeStatusEntity get(String id);
	
	/**
	 * 查询流程实例列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskNodeStatusEntity> queryList() throws BusinessException;
	
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
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	/**
	 * 通过流程实例id和节点id查询任务状态
	 * @author xiehs
	 * @createtime 2014年8月28日 上午10:54:48
	 * @Decription
	 *
	 * @param processInstanceId
	 * @param nodeId
	 * @return
	 */
	public TaskNodeStatusEntity getByInstNodeId(String processInstanceId,String nodeId);
	
	/**
	 * 新增或修改任务状态
	 * @author xiehs
	 * @createtime 2014年8月28日 上午10:54:48
	 * @Decription
	 *
	 * @param processInstanceId
	 * @param nodeId
	 * @return
	 */
	public void saveOrUpdte(String actDefId,String actInstanceId,String nodeId,String status);
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
	
	List<TaskNodeStatusEntity>  getByActInstanceId(String instanceId);
	
	public abstract Map<String, String> getStatusByInstanceId(String instanceId);
}
