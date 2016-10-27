package com.xplatform.base.workflow.node.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.dao.NodeUserDao;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
/**
 * 
 * description :节点用户dao实现
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
@Repository("nodeUserDao")
public class NodeUserDaoImpl extends CommonDao implements NodeUserDao {

	@Override
	public String addNodeUser(NodeUserEntity NodeUser) {
		// TODO Auto-generated method stub
		return (String) this.save(NodeUser);
	}

	@Override
	public void deleteNodeUser(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(NodeUserEntity.class, id);
	}

	@Override
	public void updateNodeUser(NodeUserEntity NodeUser) {
		// TODO Auto-generated method stub
		this.updateEntitie(NodeUser);
	}

	@Override
	public NodeUserEntity getNodeUser(String id) {
		// TODO Auto-generated method stub
		return (NodeUserEntity) this.get(NodeUserEntity.class, id);
	}

	@Override
	public List<NodeUserEntity> queryNodeUserList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from NodeUserEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
