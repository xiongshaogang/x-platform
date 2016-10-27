package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;

public interface NodeUserConditionService {
	/**
	 * 新增节点用户条件
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param NodeUserCondition
	 * @return
	 */
	public String save(NodeUserConditionEntity NodeUserCondition) throws BusinessException;
	
	/**
	 * 删除节点用户条件
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除节点用户条件
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新节点用户条件
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param NodeUserCondition
	 * @return
	 */
	public void update(NodeUserConditionEntity NodeUserCondition) throws BusinessException;
	
	/**
	 * 查询一条节点用户条件记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public NodeUserConditionEntity get(String id);
	
	/**
	 * 查询节点用户条件列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<NodeUserConditionEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate节点用户条件分页列表
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
	
	public List<NodeUserConditionEntity> getBySetId(String nodeSetId);
	
}
