package com.xplatform.base.workflow.node.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.dao.NodeSetDao;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;

@Repository("nodeSetDao")
public class NodeSetDaoImpl extends CommonDao implements NodeSetDao {

	@Override
	public String addNodeSet(NodeSetEntity NodeSet) {
		return (String) this.save(NodeSet);
	}

	@Override
	public void deleteNodeSet(String id) {
		this.deleteEntityById(NodeSetEntity.class, id);
	}

	@Override
	public void updateNodeSet(NodeSetEntity NodeSet) {
		this.updateEntitie(NodeSet);
	}

	@Override
	public NodeSetEntity getNodeSet(String id) {
		return (NodeSetEntity) this.get(NodeSetEntity.class, id);
	}

	@Override
	public List<NodeSetEntity> queryNodeSetList() {
		return this.findByQueryString("from NodeSetEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		this.getDataGridReturn(cq, false);
	}

	

}
