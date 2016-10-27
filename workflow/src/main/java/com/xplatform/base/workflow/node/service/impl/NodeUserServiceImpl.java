package com.xplatform.base.workflow.node.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculateSelector;
import com.xplatform.base.workflow.core.facade.model.CalcVars;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.node.dao.NodeUserDao;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;
import com.xplatform.base.workflow.node.mybatis.dao.NodeUserMybatisDao;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;
import com.xplatform.base.workflow.node.service.NodeUserService;
/**
 * 
 * description :节点用户service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("nodeUserService")
public class NodeUserServiceImpl implements NodeUserService {
	private static final Logger logger = Logger.getLogger(NodeUserServiceImpl.class);
	@Resource
	private NodeUserDao nodeUserDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private DefinitionService definitionService;
	
	@Resource
	private NodeUserCalculateSelector nodeUserCalculateSelector;
	
	@Autowired
	private NodeUserMybatisDao nodeUserMybatisDao;
	
	@Override
	@Action(moduleCode="NodeUserManager",description="节点用户新增",detail="节点用户${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(NodeUserEntity NodeUser) throws BusinessException {
		String pk="";
		try {
			pk=this.nodeUserDao.addNodeUser(NodeUser);
		} catch (Exception e) {
			logger.error("节点用户保存失败");
			throw new BusinessException("节点用户保存失败");
		}
		logger.info("节点用户保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="NodeUserManager",description="节点用户删除",detail="节点用户${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.nodeUserDao.deleteNodeUser(id);
		} catch (Exception e) {
			logger.error("节点用户删除失败");
			throw new BusinessException("节点用户删除失败");
		}
		logger.info("节点用户删除成功");
	}

	@Override
	@Action(moduleCode="NodeUserManager",description="节点用户批量删除",detail="节点用户${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("节点用户批量删除成功");
	}

	@Override
	@Action(moduleCode="NodeUserManager",description="节点用户修改",detail="节点用户${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(NodeUserEntity NodeUser) throws BusinessException {
		try {
			NodeUserEntity oldEntity = get(NodeUser.getId());
			MyBeanUtils.copyBeanNotNull2Bean(NodeUser, oldEntity);
			this.nodeUserDao.updateNodeUser(oldEntity);
		} catch (Exception e) {
			logger.error("节点用户更新失败");
			throw new BusinessException("节点用户更新失败");
		}
		logger.info("节点用户更新成功");
	}

	@Override
	public NodeUserEntity get(String id){
		NodeUserEntity NodeUser=null;
		try {
			NodeUser=this.nodeUserDao.getNodeUser(id);
		} catch (Exception e) {
			logger.error("节点用户获取失败");
			//throw new BusinessException("节点用户获取失败");
		}
		logger.info("节点用户获取成功");
		return NodeUser;
	}

	@Override
	public List<NodeUserEntity> queryList() throws BusinessException {
		List<NodeUserEntity> NodeUserList=new ArrayList<NodeUserEntity>();
		try {
			NodeUserList=this.nodeUserDao.queryNodeUserList();
		} catch (Exception e) {
			logger.error("节点用户获取列表失败");
			throw new BusinessException("节点用户获取列表失败");
		}
		logger.info("节点用户获取列表成功");
		return NodeUserList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.nodeUserDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("节点用户获取分页列表失败");
			throw new BusinessException("节点用户获取分页列表失败");
		}
		logger.info("节点用户获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(NodeUserEntity.class, param, propertyName);
	}
	
	public List<NodeUserVo> queryNodeUserListByDefIdAndType(Map<String, String> map){
		return this.nodeUserMybatisDao.queryNodeUserListByDefIdAndType(map);
	}
	
	public List<NodeUserVo> queryNodeUserListByCondition(Map<String, String> map){
		return this.nodeUserMybatisDao.queryNodeUserListByCondition(map);
	}
	
	public List<TaskExecutor> getExecutors(String actDefId,String actInstId,String nodeId,String startUserId,Map<String, Object> vars,String funcType) {
		DefinitionEntity definition=definitionService.getByActDefId(actDefId);
		if(definition==null){ return null;}
		Map<String,String> param=new HashMap<String,String>();
		param.put("defId", definition.getId());
		if(StringUtil.isNotEmpty(nodeId)){
			param.put("nodeId", nodeId);
		}
		param.put("funcType", funcType);
		List<NodeUserEntity> nodeUsers = this.nodeUserDao.findByPropertys(NodeUserEntity.class, param);
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		Set<TaskExecutor> userIdSet = new HashSet<TaskExecutor>();
		for (NodeUserEntity nodeUser : nodeUsers) {
			Set<TaskExecutor> uIdSet = null;
			//获取用户
			uIdSet = getByNodeUser(nodeUser, startUserId, actInstId, vars);
			if (userIdSet.size() == 0) {
				userIdSet = uIdSet;
			} else {
				//计算与或非
				userIdSet = computeUserSet(nodeUser.getCountType(), userIdSet, uIdSet);
			}
		}
		list.addAll(userIdSet);
		return list;
	}
	
	private Set<TaskExecutor> getByNodeUser(NodeUserEntity nodeUser,
			String startUserId, String actInstId,
			Map<String, Object> vars) {
		CalcVars params = new CalcVars(startUserId, null,actInstId, vars);
		NodeUserCalculate calculation = this.nodeUserCalculateSelector.getByKey(nodeUser.getAssignType());
		List<TaskExecutor> list= calculation.getTaskExecutor(nodeUser, params);
		if(list!=null && list.size()>0){
			return new HashSet<TaskExecutor>(list);
		}
		return null;
	}
	
	//与或非计算  不断的累计到userIdSet
	public Set<TaskExecutor> computeUserSet(String computeType, Set<TaskExecutor> userIdSet, Set<TaskExecutor> newUserSet) {
		if (newUserSet == null){
			return userIdSet;
		}
		if (StringUtil.equals(computeType, "and")){//与
			userIdSet.addAll(newUserSet);
		}else if (StringUtil.equals(computeType, "or")) {//或
			Set<TaskExecutor> orLastSet = new LinkedHashSet<TaskExecutor>();
			for(TaskExecutor taskExecutor:userIdSet){
				if (newUserSet.contains(taskExecutor)) {
					orLastSet.add(taskExecutor);
				}
			}
			return orLastSet;
		}else if(StringUtil.equals(computeType, "not")){//非
			for(TaskExecutor taskExecutor:userIdSet){
				if (newUserSet.contains(taskExecutor)) {
					userIdSet.remove(taskExecutor);
				}
			}
		}else{//默认是与
			userIdSet.addAll(newUserSet);
		}
		return userIdSet;
	}
	
	public void setNodeUserDao(NodeUserDao nodeUserDao) {
		this.nodeUserDao = nodeUserDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByDefId(String defId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeUserDao.executeHql("delete from NodeUserEntity t where t.defId='"+defId+"'");
	}

}
