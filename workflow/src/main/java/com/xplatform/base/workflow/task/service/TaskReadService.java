package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.task.entity.TaskReadEntity;

/**
 * 
 * description : 任务已读管理service
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
public interface TaskReadService {
	
	/**
	 * 新增任务已读
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param TaskRead
	 * @return
	 */
	public String save(TaskReadEntity TaskRead) throws BusinessException;
	
	/**
	 * 删除任务已读
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除任务已读
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新任务已读
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param TaskRead
	 * @return
	 */
	public void update(TaskReadEntity TaskRead) throws BusinessException;
	
	/**
	 * 查询一条任务已读记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskReadEntity get(String id);
	
	/**
	 * 查询任务已读列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskReadEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate任务已读分页列表
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
	 * 获取流程实例中已读的任务列表
	 * @author xiehs
	 * @createtime 2014年10月6日 上午11:08:03
	 * @Decription
	 *
	 * @param instActId
	 * @param taskId
	 * @return
	 */
	public List<TaskReadEntity> getTaskRead(String instActId, String taskId);
	
}
