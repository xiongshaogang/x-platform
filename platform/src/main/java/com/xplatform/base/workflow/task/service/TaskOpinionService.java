package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;

/**
 * 
 * description : 审批意见service
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
public interface TaskOpinionService {
	
	/**
	 * 新增审批意见
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param TaskOpinion
	 * @return
	 */
	public String save(TaskOpinionEntity TaskOpinion) throws BusinessException;
	
	/**
	 * 删除审批意见
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除审批意见
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新审批意见
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param TaskOpinion
	 * @return
	 */
	public void update(TaskOpinionEntity TaskOpinion) throws BusinessException;
	
	/**
	 * 查询一条审批意见记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskOpinionEntity get(String id) throws BusinessException;
	
	/**
	 * 查询审批意见列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskOpinionEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate审批意见分页列表
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
	 * 通过任务id查询审批意见
	 * @author xiehs
	 * @createtime 2014年9月1日 上午11:03:17
	 * @Decription
	 *
	 * @param taskId
	 * @return
	 */
	public TaskOpinionEntity getByTaskId(String taskId);
	
	public TaskOpinionEntity getOpinionByTaskId(String taskId,String userId);
	
	/**
	 * 获取某个节点的所有审批意见
	 * @author xiehs
	 * @createtime 2014年9月11日 下午7:54:37
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public List<TaskOpinionEntity> getByActInstIdTaskKey(String actInstId,String nodeId);
	
	/**
	 * 获取某个节点最近的一次审批意见
	 * @author xiehs
	 * @createtime 2014年9月11日 下午7:54:45
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public TaskOpinionEntity getLatestTaskOpinion(String actInstId,String nodeId);
	/**
	 * 获取某个用户最近的一次审批意见
	 * @author xiehs
	 * @createtime 2014年9月11日 下午7:54:45
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public TaskOpinionEntity getLatestUserOpinion(String instanceId,String curUserId);
	
	public List<TaskOpinionEntity> getByActInstIdTaskKeyStatus(String actInstId,String nodeId,Integer checkStatus);
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
	
	public List<TaskOpinionEntity> getByActInstId(String actInstId);
	
	/**
	 * 审核中的任务
	 * @author xiehs
	 * @createtime 2014年10月9日 上午11:54:46
	 * @Decription
	 *
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinionEntity> getCheckOpinionByInstId(String actInstId);
	
	public List<TaskOpinionEntity> getApprovedList(String actInstId);
}
