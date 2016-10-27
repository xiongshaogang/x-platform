package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.entity.TaskDueStateEntity;

/**
 * 
 * description : 任务催办状态管理service
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
public interface TaskDueStateService {

	/**
	 * 新增任务催办状态
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param TaskDueState
	 * @return
	 */
	public String save(TaskDueStateEntity TaskDueState) throws BusinessException;

	/**
	 * 删除任务催办状态
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;

	/**
	 * 批量删除任务催办状态
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;

	/**
	 * 更新任务催办状态
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param TaskDueState
	 * @return
	 */
	public void update(TaskDueStateEntity TaskDueState) throws BusinessException;

	/**
	 * 查询一条任务催办状态记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskDueStateEntity get(String id);

	/**
	 * 查询任务催办状态列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskDueStateEntity> queryList() throws BusinessException;

	/**
	 * hibernate任务催办状态分页列表
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
	public boolean isUnique(Map<String, String> param, String propertyName);

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
	 * @author xiaqiang
	 * @createtime 2014年10月30日 下午3:00:43
	 * @Decription 找到催办已经发送的消息数量
	 *
	 * @param map 传入taskId与userId与remindType
	 * @return
	 * @throws BusinessException
	 */
	public Integer getAmountByUserTaskId(String taskId, String userId, String type, String taskDueId)
			throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月30日 下午3:01:03
	 * @Decription 找到催办已经发送的消息数量
	 *
	 * @param map 传入taskId与remindType
	 * @return
	 * @throws BusinessException
	 */
	public Integer getAmountByTaskId(String taskId, String type, String taskDueId) throws BusinessException;
}
