package com.xplatform.base.workflow.instance.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.mybatis.vo.InstanceVo;

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
public interface ProcessInstanceService {
	
	/**
	 * 新增流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param ProcessInstance
	 * @return
	 */
	public String save(ProcessInstanceEntity ProcessInstance) throws BusinessException;
	
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
	 * @param ProcessInstance
	 * @return
	 */
	public void update(ProcessInstanceEntity ProcessInstance) throws BusinessException;
	
	/**
	 * 查询一条流程实例记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ProcessInstanceEntity get(String id);
	
	/**
	 * 查询流程实例列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ProcessInstanceEntity> queryList() throws BusinessException;
	
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
	 * 启动流程
	 * @author xiehs
	 * @createtime 2014年8月26日 上午11:44:17
	 * @Decription
	 *
	 * @param processCmd
	 */
	public void startProcess(ProcessCmd processCmd) throws BusinessException;
	
	/**
	 * 通过流程实例id获取流程实例扩展
	 * @author xiehs
	 * @createtime 2014年8月28日 下午4:50:57
	 * @Decription
	 *
	 * @param actInstId
	 * @return
	 */
	public ProcessInstanceEntity getByActInstanceId(String actInstId);
	
	/**
	 * 往下流转
	 * @author xiehs
	 * @createtime 2014年9月25日 上午9:13:45
	 * @Decription
	 *
	 * @param processCmd
	 * @return
	 * @throws BusinessException
	 */
	public ProcessInstanceEntity nextProcess(ProcessCmd processCmd) throws BusinessException;
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
	 * 判断一个人是否有审批任务的权限
	 * @author xiehs
	 * @createtime 2014年10月4日 下午5:57:16
	 * @Decription
	 *
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public boolean getHasRightsByTask(String taskId, String userId);
	/**
	 * 撤销
	 * @author xiehs
	 * @createtime 2014年10月6日 上午10:49:12
	 * @Decription
	 *
	 * @param runId
	 * @param informType
	 * @param memo
	 */
	public void executeRecover(String runId,String informType,String memo)throws BusinessException;
	
	/**
	 * 追回
	 * @author xiehs
	 * @createtime 2014年10月6日 上午10:49:18
	 * @Decription
	 *
	 * @param runId
	 * @param informType
	 * @param memo
	 */
	public void executeRedo(String runId,String informType,String memo)throws BusinessException;
	
	public Page<InstanceVo> queryRequestInstanceList(Page<InstanceVo> page);
	
	public Page<InstanceVo> queryCompleteInstanceList(Page<InstanceVo> page);
	
	public void batchComplte(String taskIds,String opinion,String voteAgree)throws BusinessException;
	
	public void executeDivertProcess(ProcessInstHistoryEntity hisInst,List<String> targetUserIds, UserEntity currUser, String informType,String suggestion) throws BusinessException ;
	
	public List<ProcessInstanceEntity> getProcessByParentBusinessKey(String businessKey);
}
