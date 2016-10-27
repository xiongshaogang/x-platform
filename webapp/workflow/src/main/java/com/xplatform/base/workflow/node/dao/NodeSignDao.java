package com.xplatform.base.workflow.node.dao;


import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;

/**
 * 
 * description : 会签规则设置dao
 *
 * @version 1.0
 * @author binyong
 * 
 */
public interface NodeSignDao extends ICommonDao{

	void updateNodeSign(NodeSignEntity nodeSignEntity);

	String addNodeSign(NodeSignEntity nodeSign);
	
}
