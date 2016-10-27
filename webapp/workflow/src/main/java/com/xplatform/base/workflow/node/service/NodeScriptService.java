package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;

public interface NodeScriptService {
	public NodeScriptEntity getScriptByType(String nodeId,String actDefId, String scriptType);
	
	/**
	 * 新增节点跳转规则
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param NodeScript
	 * @return
	 */
	public String save(NodeScriptEntity NodeScript) throws BusinessException;
	
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
	 * @param NodeScript
	 * @return
	 */
	public void update(NodeScriptEntity NodeScript) throws BusinessException;
	
	/**
	 * 查询一条节点跳转规则记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public NodeScriptEntity get(String id);
	
	/**
	 * 查询节点跳转规则列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<NodeScriptEntity> queryList() throws BusinessException;
	
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

	public void deleteByActDefIdAndNodeId(String nodeId, String actDefId);

	public List<NodeScriptEntity> queryListByActDefIdAndNodeId(String actDefId,
			String nodeId);

	public List<NodeScriptEntity> getByNodeScriptId(String actDefId);
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
