package com.xplatform.base.workflow.node.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.dao.NodeUserConditionDao;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
/**
 * 
 * description :节点用户条件dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("NodeUserConditionDao")
public class NodeUserConditionDaoImpl extends CommonDao implements NodeUserConditionDao {

	@Override
	public String addNodeUserCondition(NodeUserConditionEntity NodeUserCondition) {
		// TODO Auto-generated method stub
		return (String) this.save(NodeUserCondition);
	}

	@Override
	public void deleteNodeUserCondition(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(NodeUserConditionEntity.class, id);
	}

	@Override
	public void updateNodeUserCondition(NodeUserConditionEntity NodeUserCondition) {
		// TODO Auto-generated method stub
		this.updateEntitie(NodeUserCondition);
	}

	@Override
	public NodeUserConditionEntity getNodeUserCondition(String id) {
		// TODO Auto-generated method stub
		return (NodeUserConditionEntity) this.get(NodeUserConditionEntity.class, id);
	}

	@Override
	public List<NodeUserConditionEntity> queryNodeUserConditionList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from NodeUserConditionEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
