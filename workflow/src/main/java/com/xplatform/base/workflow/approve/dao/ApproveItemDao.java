package com.xplatform.base.workflow.approve.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;

/**
 * 
 * description : 审批常用语管理dao
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:08:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:08:58
 *
 */
public interface ApproveItemDao extends ICommonDao{
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:38
	 * @Decription 新增
	 *
	 * @param ApproveItem
	 * @return
	 */
	public String addApproveItem(ApproveItemEntity ApproveItem);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:06
	 * @Decription 删除
	 * @param id
	 * @return
	 */
	public void deleteApproveItem(String id);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:56
	 * @Decription 修改
	 * @param ApproveItem
	 * @return
	 */
	public void updateApproveItem (ApproveItemEntity ApproveItem);
	
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:08:19
	 * @Decription 通过id查询单条记录
	 * @param id
	 * @return
	 */
	public ApproveItemEntity getApproveItem(String id);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:48
	 * @Decription 查询所有的记录
	 * @return
	 */
	public List<ApproveItemEntity> queryApproveItemList();
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:58:12
	 * @Decription hibernate分页查询
	 * @param cq
	 * @param b
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
	
}
