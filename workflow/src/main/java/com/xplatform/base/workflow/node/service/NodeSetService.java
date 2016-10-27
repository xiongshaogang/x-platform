package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;

public interface NodeSetService {
	
	public void saveOrUpdateNodeSet(String actFlowDefXml,DefinitionEntity definition, boolean isAdd) throws BusinessException;

	/**
	 * 新增资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param Definition
	 * @return
	 */
	public String save(NodeSetEntity nodeSet) throws BusinessException;
	
	public List<NodeSetEntity> getByOther(String defId) throws BusinessException;
	
	public List<NodeSetEntity> queryByPorperty(String porperty,Object value);
	
	public NodeSetEntity getBySetType(String defId,String fromType);
	
	public NodeSetEntity getBySetType1(String defId,String setType);
	
	public List<NodeSetEntity> getTaskNodeList(String defId);
	
	public List<NodeSetEntity> getTaskNodeListByActDefId(String actDefId);
	
	public Map<String, NodeSetEntity> getMapByDefId(String defId);
	
	public NodeSetEntity getNodeSetByActDefIdNodeId(String actDefId,String nodeId);
	
	public NodeSetEntity getNodeSetByDefIdNodeId(String defId,String nodeId);
	
	/**
	 * 更新节点设置
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(NodeSetEntity nodeSet) throws BusinessException;
	
	public void deleteByDefId(String defId)throws BusinessException;
	
	public void delete(String id) throws BusinessException ;
	
	public NodeSetEntity getByActDefIdNodeId(String actDefId,String nodeId);
	/**
	 * 获得全局表单设置
	 * @author xiehs
	 * @createtime 2014年10月7日 上午9:50:05
	 * @Decription
	 *
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public NodeSetEntity getGlobalByActDefId(String actDefId);
	/**
	 * 获得业务综合表单设置
	 * @author xiehs
	 * @createtime 2014年10月7日 上午9:50:05
	 * @Decription
	 *
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public NodeSetEntity getBpmFormByActDefId(String actDefId);
}
