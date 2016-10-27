package com.xplatform.base.workflow.node.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.dao.NodeScriptDao;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
/**
 * 
 * description :节点时间脚本dao实现
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
@Repository("nodeScriptDao")
public class NodeScriptDaoImpl extends CommonDao implements NodeScriptDao {

	@Override
	public String addNodeScript(NodeScriptEntity NodeScript) {
		// TODO Auto-generated method stub
		return (String) this.save(NodeScript);
	}

	@Override
	public void deleteNodeScript(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(NodeScriptEntity.class, id);
	}

	@Override
	public void updateNodeScript(NodeScriptEntity NodeScript) {
		// TODO Auto-generated method stub
		this.updateEntitie(NodeScript);
	}

	@Override
	public NodeScriptEntity getNodeScript(String id) {
		// TODO Auto-generated method stub
		return (NodeScriptEntity) this.get(NodeScriptEntity.class, id);
	}

	@Override
	public List<NodeScriptEntity> queryNodeScriptList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from NodeScriptEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeScriptEntity> queryListByActDefIdAndNodeId(String actDefId,
			String nodeId) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("nodeId", nodeId);
		param.put("actDefId", actDefId);
		return this.findByPropertys(NodeScriptEntity.class, param);
	}

}
