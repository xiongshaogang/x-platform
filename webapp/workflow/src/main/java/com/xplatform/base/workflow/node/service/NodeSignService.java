package com.xplatform.base.workflow.node.service;

import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
import com.xplatform.base.workflow.node.entity.NodeSignPrivilegeEntity;


public interface NodeSignService {

	/** 根据 nodeId,actDefId 取得会签节点信息*/
	NodeSignEntity getNodeSignByNodeIdAndActDefId(String nodeId, String actDefId);

	/** 取得该会签节点特权规则信息list*/
	List<NodeSignPrivilegeEntity> getNodeSignPrivilege(NodeSignEntity nodeSign);

	/** 更新信息*/
	public void update(NodeSignEntity NodeSign) throws BusinessException;
	
	/** 保存信息*/
	public String save(NodeSignEntity NodeSign) throws BusinessException;

	/** 根据会签节点和规则实体删除该节点下所有的特权规则信息*/
	void deleteNodeSignPrivilegeById(String id);

	/** 保存特权规则信息*/
	void savePrivilege(NodeSignPrivilegeEntity nspe);
	
	/** 根据流程定义id和节点id获取会签节点*/
	public NodeSignEntity getByDefIdAndNodeId(String actDefId, String nodeId);
	
	/** 检查是否具有一票否决权*/
	public boolean checkNodeSignPrivilege(String defId,String nodeId,String privilegeType,String userId,String actInstId);
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
