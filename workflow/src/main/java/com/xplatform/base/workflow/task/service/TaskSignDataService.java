package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;

/**
 * 
 * description : 任务会签投票管理service
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
public interface TaskSignDataService {
	
	/**
	 * 新增任务会签投票
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param TaskSignData
	 * @return
	 */
	public String save(TaskSignDataEntity TaskSignData) throws BusinessException;
	
	/**
	 * 删除任务会签投票
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除任务会签投票
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新任务会签投票
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param TaskSignData
	 * @return
	 */
	public void update(TaskSignDataEntity TaskSignData) throws BusinessException;
	
	/**
	 * 查询一条任务会签投票记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskSignDataEntity get(String id);
	
	/**
	 * 查询任务会签投票列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskSignDataEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate任务会签投票分页列表
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
	 * 投票同意的票数统计
	 * @author xiehs
	 * @createtime 2014年9月23日 下午5:49:34
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getAgreeVoteCount(String actInstId, String nodeId);
	
	/**
	 * 投票再议的票数统计
	 * @author xiehs
	 * @createtime 2014年11月20日 下午5:24:31
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getReconsideVoteCount(String actInstId, String nodeId);

	/**
	 * 投票反对的票数统计
	 * @author xiehs
	 * @createtime 2014年9月23日 下午5:49:41
	 * @Decription
	 *
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getRefuseVoteCount(String actInstId, String nodeId);
	
	public void batchUpdateCompleted(String actInstId,String nodeId);
	/**
	 * 会签任务投票
	 * @author xiehs
	 * @createtime 2014年9月29日 下午5:23:32
	 * @Decription
	 *
	 * @param taskId
	 * @param content
	 * @param isAgree
	 */
	public void signVoteTask(String taskId, String content, String isAgree)  throws BusinessException;
	
	/**
	 * 获取一个任务的会签投票
	 * @author xiehs
	 * @createtime 2014年9月29日 下午5:32:08
	 * @Decription
	 *
	 * @param taskId
	 * @return
	 */
	public TaskSignDataEntity getByTaskId(String taskId);
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
	
	/**
	 * 获取最大批次的会签数据
	 * @author xiehs
	 * @createtime 2014年10月5日 上午10:48:27
	 * @Decription
	 *
	 * @param instanceId
	 * @param nodeId
	 */
	public List<TaskSignDataEntity> getByNodeAndInstanceId(String instanceId,String nodeId);
	
	public List<TaskSignDataEntity> getByNodeAndInstanceId(String instanceId,String nodeId, String isCompleted);
	/**
	 * 处理加签的逻辑
	 * @author xiehs
	 * @createtime 2014年10月6日 下午5:03:05
	 * @Decription
	 *
	 * @param signUserIds
	 * @param taskId
	 * @throws BusinessException
	 */
	public void addSign(String signUserIds,String taskId)throws BusinessException;
	
	public Integer getMaxBatch(String processInstanceId,String nodeId);
	
	public TaskSignDataEntity getUserTaskSign(String processInstanceId,String nodeId,String executorId);
}
