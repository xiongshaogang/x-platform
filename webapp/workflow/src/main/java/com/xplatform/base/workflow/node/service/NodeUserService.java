package com.xplatform.base.workflow.node.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;

public interface NodeUserService {
	
	/**
	 * 新增节点用户
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param NodeUser
	 * @return
	 */
	public String save(NodeUserEntity NodeUser) throws BusinessException;
	
	/**
	 * 删除节点用户
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;
	
	/**
	 * 批量删除节点用户
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新节点用户
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param NodeUser
	 * @return
	 */
	public void update(NodeUserEntity NodeUser) throws BusinessException;
	
	/**
	 * 查询一条节点用户记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public NodeUserEntity get(String id);
	
	/**
	 * 查询节点用户列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<NodeUserEntity> queryList() throws BusinessException;
	
	/**
	 * hibernate节点用户分页列表
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
	 * @author xiaqiang
	 * @createtime 2014年12月3日 下午4:50:39
	 * @Decription 通过流程定义id与funcType功能用途去查询用户配置记录
	 *
	 * @param map
	 * @return
	 */
	public List<NodeUserVo> queryNodeUserListByDefIdAndType(Map<String, String> map);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年12月3日 下午4:52:19
	 * @Decription 通过流程定义id,节点id,funcType功能用户去查询用户配置记录
	 *
	 * @param map
	 * @return
	 */
	public List<NodeUserVo> queryNodeUserListByCondition(Map<String, String> map);
	
	public List<TaskExecutor> getExecutors(String actDefId,String actInstId,String nodeId,String startUserId,Map<String, Object> vars,String funcType);
	/**
	 * 删除流程定义的所有记录
	 * @author xiehs
	 * @createtime 2014年10月2日 上午12:00:47
	 * @Decription
	 *
	 * @param actDefId
	 * @throws BusinessException
	 */
	public void deleteByDefId(String defId) throws BusinessException;
}
