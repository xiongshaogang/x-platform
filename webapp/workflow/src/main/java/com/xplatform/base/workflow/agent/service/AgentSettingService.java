package com.xplatform.base.workflow.agent.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.agent.entity.AgentFlowEntity;
import com.xplatform.base.workflow.agent.entity.AgentSettingEntity;

/**
 * 
 * description : 流程代理设置管理service
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
public interface AgentSettingService {
	
	/**
	 * 新增流程代理设置
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param AgentSetting
	 * @return
	 */
	public String save(AgentSettingEntity AgentSetting) throws BusinessException;
	
	/**
	 * 删除流程代理设置
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除流程代理设置
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新流程代理设置
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param AgentSetting
	 * @return
	 */
	public void update(AgentSettingEntity AgentSetting) throws BusinessException;
	
	/**
	 * 查询一条流程代理设置记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public AgentSettingEntity get(String id);
	
	/**
	 * 查询流程代理设置列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<AgentSettingEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate流程代理设置分页列表
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

	public void deleteAgentFlowEntity(AgentSettingEntity agentSetting);

	public void saveFlow(AgentFlowEntity agentFlowEntity);

	public List<AgentFlowEntity> queryFlowList(AgentSettingEntity agentSetting);
	
	public AgentSettingEntity getValidAgentSetting(String flowKey,String userId,Date currentDate);
	
	public Map<String,Object> getAgent(DelegateTask delegateTask, String userId) ;
}
