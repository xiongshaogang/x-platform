package com.xplatform.base.workflow.approve.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.approve.dao.ApproveItemDao;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;
/**
 * 
 * description :审批常用语dao实现
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
@Repository("approveItemDao")
public class ApproveItemImpl extends CommonDao implements ApproveItemDao {

	@Override
	public String addApproveItem(ApproveItemEntity ApproveItem) {
		// TODO Auto-generated method stub
		return (String) this.save(ApproveItem);
	}

	@Override
	public void deleteApproveItem(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ApproveItemEntity.class, id);
	}

	@Override
	public void updateApproveItem(ApproveItemEntity ApproveItem) {
		// TODO Auto-generated method stub
		this.updateEntitie(ApproveItem);
	}

	@Override
	public ApproveItemEntity getApproveItem(String id) {
		// TODO Auto-generated method stub
		return (ApproveItemEntity) this.get(ApproveItemEntity.class, id);
	}

	@Override
	public List<ApproveItemEntity> queryApproveItemList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ApproveItemEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
