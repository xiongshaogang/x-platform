package com.xplatform.base.workflow.node.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.workflow.node.dao.NodeSignDao;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
/**
 * 
 * description :会签规则设置dao实现
 *
 * @version 1.0
 * @author binyong
 *
 */
@Repository("nodeSignDao")
public class NodeSignDaoImpl extends CommonDao implements NodeSignDao {

	@Override
	public void updateNodeSign(NodeSignEntity nodeSignEntity) {
		// TODO Auto-generated method stub
		this.updateEntitie(nodeSignEntity);
	}

	@Override
	public String addNodeSign(NodeSignEntity nodeSign) {
		// TODO Auto-generated method stub
		return (String) this.save(nodeSign);
	}


}
