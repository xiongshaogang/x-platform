package com.xplatform.base.workflow.task.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.task.entity.TaskExeEntity;

/**
 * 
 * description : 任务转办代理管理service
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
public interface TaskExeService {
	
	/**
	 * 新增任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param TaskExe
	 * @return
	 */
	public String save(TaskExeEntity TaskExe) throws BusinessException;
	
	/**
	 * 删除任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param TaskExe
	 * @return
	 */
	public void update(TaskExeEntity TaskExe) throws BusinessException;
	
	/**
	 * 查询一条任务转办代理记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public TaskExeEntity get(String id);
	
	/**
	 * 查询任务转办代理列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<TaskExeEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate任务转办代理分页列表
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
	 * 获取任务的转办代理集合
	 * @author xiehs
	 * @createtime 2014年10月6日 上午11:14:08
	 * @Decription
	 *
	 * @param taskId
	 * @return
	 */
	public List<TaskExeEntity> getByTaskId(String taskId);
	
	/**
	 * 完成代理
	 * @author xiehs
	 * @createtime 2014年9月25日 上午10:06:56
	 * @Decription
	 *
	 * @param taskId
	 */
	public void complete(String taskId);
	
	/**
	 * 判断是否为转办代理
	 * @author xiehs
	 * @createtime 2014年10月6日 上午11:13:54
	 * @Decription
	 *
	 * @param taskEnt
	 * @param definition
	 * @return
	 */
	public boolean isAssigneeTask(TaskEntity taskEnt,DefinitionEntity definition,String userId);
	
	/**
	 * 取消转办代理
	 * @author xiehs
	 * @createtime 2014年10月6日 上午11:13:43
	 * @Decription
	 *
	 * @param taskId
	 */
	public void cancel(String taskId);
	
	public void saveAssignee(TaskExeEntity TaskExe,UserEntity user) throws BusinessException;
	/**
	 * 取消专版代理
	 * @author xiehs
	 * @createtime 2014年10月28日 下午3:20:42
	 * @Decription
	 *
	 * @param bpmTaskExe
	 * @param sysUser
	 * @param opinion
	 * @param informType
	 * @return
	 * @throws BusinessException
	 */
	public ProcessInstanceEntity cancel(TaskExeEntity bpmTaskExe, UserEntity sysUser,String opinion, String informType) throws BusinessException;
	/**
	 * 批量取消转办代理
	 * @author xiehs
	 * @createtime 2014年10月28日 下午3:20:57
	 * @Decription
	 *
	 * @param ids
	 * @param opinion
	 * @param informType
	 * @param sysUser
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskExeEntity> cancelBat(String ids, String opinion,String informType, UserEntity sysUser) throws BusinessException; 
}
