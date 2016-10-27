package com.xplatform.base.workflow.approve.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.approve.entity.ApproveItemEntity;

/**
 * 
 * description : 审批常用语管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface ApproveItemService {
	
	/**
	 * 新增审批常用语
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param ApproveItem
	 * @return
	 */
	public String save(ApproveItemEntity ApproveItem) throws BusinessException;
	
	/**
	 * 删除审批常用语
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除审批常用语
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新审批常用语
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param ApproveItem
	 * @return
	 */
	public void update(ApproveItemEntity ApproveItem) throws BusinessException;
	
	/**
	 * 查询一条审批常用语记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public ApproveItemEntity get(String id) throws BusinessException;
	
	/**
	 * 查询审批常用语列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<ApproveItemEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate审批常用语分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	/**
	 * 获取流程任务节点的审批常用语
	 * @author xiehs
	 * @createtime 2014年7月24日 上午10:03:46
	 * @Decription
	 *
	 * @param actDefId
	 * @param nodeId
	 * @param isGrobal
	 * @return
	 */
	public ApproveItemEntity getTaskApproval(String actDefId,String nodeId,String isGrobal);
	
	/**
	 * 获取流程全局的审批常用语
	 * @author xiehs
	 * @createtime 2014年7月24日 上午10:03:59
	 * @Decription
	 *
	 * @param actDefId
	 * @param isGrobal
	 * @return
	 */
	public ApproveItemEntity getFlowApproval(String actDefId,String isGrobal);

	public List<ApproveItemEntity> getByActDefId(String actDefId);
	
	public List<String> getApprovalByActDefId(String actDefId, String nodeId);
}
