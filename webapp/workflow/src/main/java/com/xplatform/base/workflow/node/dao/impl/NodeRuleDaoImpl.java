package com.xplatform.base.workflow.node.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.dao.NodeRuleDao;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;
/**
 * 
 * description :节点跳转规则dao实现
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
@Repository("nodeRuleDao")
public class NodeRuleDaoImpl extends CommonDao implements NodeRuleDao {

	@Override
	public String addNodeRule(NodeRuleEntity NodeRule) {
		// TODO Auto-generated method stub
		return (String) this.save(NodeRule);
	}

	@Override
	public void deleteNodeRule(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(NodeRuleEntity.class, id);
	}

	@Override
	public void updateNodeRule(NodeRuleEntity NodeRule) {
		// TODO Auto-generated method stub
		this.updateEntitie(NodeRule);
	}

	@Override
	public NodeRuleEntity getNodeRule(String id) {
		// TODO Auto-generated method stub
		return (NodeRuleEntity) this.get(NodeRuleEntity.class, id);
	}

	@Override
	public List<NodeRuleEntity> queryNodeRuleList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from NodeRuleEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	@Override
	public List<NodeRuleEntity> queryListByNodeIdAndActDefId(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.findByPropertys(NodeRuleEntity.class, param);
	}

}
