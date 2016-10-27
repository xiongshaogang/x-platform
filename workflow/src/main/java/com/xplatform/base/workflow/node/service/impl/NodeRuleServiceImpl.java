package com.xplatform.base.workflow.node.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.workflow.node.dao.NodeRuleDao;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeRuleService;

/**
 * 
 * description :节点跳转规则service实现
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
@Service("nodeRuleService")
public class NodeRuleServiceImpl implements NodeRuleService {
	private static final Logger logger = Logger.getLogger(NodeRuleServiceImpl.class);
	@Resource
	private NodeRuleDao nodeRuleDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private GroovyScriptEngine scriptEngine;

	@Override
	@Action(moduleCode="NodeRuleManager",description="节点跳转规则新增",detail="节点跳转规则${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(NodeRuleEntity NodeRule) throws BusinessException {
		String pk="";
		try {
			pk=this.nodeRuleDao.addNodeRule(NodeRule);
		} catch (Exception e) {
			logger.error("节点跳转规则保存失败");
			throw new BusinessException("节点跳转规则保存失败");
		}
		logger.info("节点跳转规则保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="NodeRuleManager",description="节点跳转规则删除",detail="节点跳转规则${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.nodeRuleDao.deleteNodeRule(id);
		} catch (Exception e) {
			logger.error("节点跳转规则删除失败");
			throw new BusinessException("节点跳转规则删除失败");
		}
		logger.info("节点跳转规则删除成功");
	}

	@Override
	@Action(moduleCode="NodeRuleManager",description="节点跳转规则批量删除",detail="节点跳转规则${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("节点跳转规则批量删除成功");
	}

	@Override
	@Action(moduleCode="NodeRuleManager",description="节点跳转规则修改",detail="节点跳转规则${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(NodeRuleEntity NodeRule) throws BusinessException {
		try {
			NodeRuleEntity oldEntity = get(NodeRule.getId());
			MyBeanUtils.copyBeanNotNull2Bean(NodeRule, oldEntity);
			this.nodeRuleDao.updateNodeRule(oldEntity);
		} catch (Exception e) {
			logger.error("节点跳转规则更新失败");
			throw new BusinessException("节点跳转规则更新失败");
		}
		logger.info("节点跳转规则更新成功");
	}

	@Override
	public NodeRuleEntity get(String id){
		NodeRuleEntity NodeRule=null;
		try {
			NodeRule=this.nodeRuleDao.getNodeRule(id);
		} catch (Exception e) {
			logger.error("节点跳转规则获取失败");
			//throw new BusinessException("节点跳转规则获取失败");
		}
		logger.info("节点跳转规则获取成功");
		return NodeRule;
	}

	@Override
	public List<NodeRuleEntity> queryList() throws BusinessException {
		List<NodeRuleEntity> NodeRuleList=new ArrayList<NodeRuleEntity>();
		try {
			NodeRuleList=this.nodeRuleDao.queryNodeRuleList();
		} catch (Exception e) {
			logger.error("节点跳转规则获取列表失败");
			throw new BusinessException("节点跳转规则获取列表失败");
		}
		logger.info("节点跳转规则获取列表成功");
		return NodeRuleList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.nodeRuleDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("节点跳转规则获取分页列表失败");
			throw new BusinessException("节点跳转规则获取分页列表失败");
		}
		logger.info("节点跳转规则获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(NodeRuleEntity.class, param, propertyName);
	}
	
	public void setNodeRuleDao(NodeRuleDao nodeRuleDao) {
		this.nodeRuleDao = nodeRuleDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public List<NodeRuleEntity> queryListByNodeIdAndActDefId(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<NodeRuleEntity> nodeRuleEntityList=new ArrayList<NodeRuleEntity>();
		try {
			nodeRuleEntityList=this.nodeRuleDao.queryListByNodeIdAndActDefId(param);
		} catch (Exception e) {
			logger.error("节点规则数据获取失败");
			//throw new BusinessException("节点规则数据获取失败");
		}
		return nodeRuleEntityList;
	}

	@Override
	public List<NodeRuleEntity> getByActDefId(String actDefId) {
		// TODO Auto-generated method stub
		return this.nodeRuleDao.findByProperty(NodeRuleEntity.class, "actDefId", actDefId);
	}

	@Override
	public String evaluate(ExecutionEntity execution) {
		// TODO Auto-generated method stub
		String activityId = execution.getActivityId();
		String actDefId = execution.getProcessDefinitionId();
		Map<String,Object> param= new HashMap<String,Object>(); 
		param.put("nodeId", activityId);
		param.put("actDefId", actDefId);
		List<NodeRuleEntity> ruleList = this.queryListByNodeIdAndActDefId(param);

		Map<String,Object> vars = new HashMap<String,Object>();
		vars.putAll(execution.getVariables());

		if (BeanUtils.isEmpty(ruleList)) {
			return "";
		}
		//计算规则
		for (NodeRuleEntity nodeRule : ruleList) {
			try {
				Boolean rtn = Boolean.valueOf(this.scriptEngine.executeBoolean(nodeRule.getCondition(), vars));
				if (rtn != null) {
					if (rtn.booleanValue()) {
						this.logger.debug("the last rule decision is "+ nodeRule.getTargetNodeId());
						return nodeRule.getTargetNodeId();
					}
				} else {
					this.logger.debug("条件表达式返回为空，请使用return 语句返回!");
				}

			} catch (Exception ex) {
				this.logger.debug("error message: " + ex.getMessage());
			}
		}
		return "";
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeRuleDao.executeHql("delete from NodeRuleEntity t where t.actDefId='"+actDefId+"'");
	}
}
