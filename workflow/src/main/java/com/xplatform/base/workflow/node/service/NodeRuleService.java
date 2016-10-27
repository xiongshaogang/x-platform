package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;

/**
 * 
 * description : 节点跳转规则管理service
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
public interface NodeRuleService {
	
	/**
	 * 新增节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param NodeRule
	 * @return
	 */
	public String save(NodeRuleEntity NodeRule) throws BusinessException;
	
	/**
	 * 删除节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param NodeRule
	 * @return
	 */
	public void update(NodeRuleEntity NodeRule) throws BusinessException;
	
	/**
	 * 查询一条节点跳转规则记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public NodeRuleEntity get(String id);
	
	/**
	 * 查询节点跳转规则列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<NodeRuleEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate节点跳转规则分页列表
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

	public List<NodeRuleEntity> queryListByNodeIdAndActDefId(Map<String, Object> param);

	public List<NodeRuleEntity> getByActDefId(String actDefId);
	
	/**
	 * 根据规则计算节点跳转规则
	 * @author xiehs
	 * @createtime 2014年9月24日 下午4:11:23
	 * @Decription
	 *
	 * @param execution
	 * @return 目标节点
	 */
	public String evaluate(ExecutionEntity execution);
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
