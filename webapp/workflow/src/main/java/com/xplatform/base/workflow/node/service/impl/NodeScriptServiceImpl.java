package com.xplatform.base.workflow.node.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.node.dao.NodeScriptDao;
import com.xplatform.base.workflow.node.entity.NodeScriptEntity;
import com.xplatform.base.workflow.node.service.NodeScriptService;
/**
 * 
 * description :节点时间脚本service实现
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
@Service("nodeScriptService")
public class NodeScriptServiceImpl implements NodeScriptService {
	private static final Logger logger = Logger.getLogger(NodeScriptServiceImpl.class);
	@Resource
	private NodeScriptDao nodeScriptDao;
	
	@Resource
	private BaseService baseService;
	
	@Override
	public NodeScriptEntity getScriptByType(String nodeId, String actDefId,
			String scriptType) {
		// TODO Auto-generated method stub
		Map<String,String> param = new HashMap<String,String>();
		param.put("nodeId", nodeId);
		param.put("actDefId", actDefId);
		param.put("type", scriptType);
		List<NodeScriptEntity> list=this.nodeScriptDao.findByPropertys(NodeScriptEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Action(moduleCode="NodeScriptManager",description="节点时间脚本新增",detail="节点时间脚本${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(NodeScriptEntity NodeScript) throws BusinessException {
		String pk="";
		try {
			pk=this.nodeScriptDao.addNodeScript(NodeScript);
		} catch (Exception e) {
			logger.error("节点时间脚本保存失败");
			throw new BusinessException("节点时间脚本保存失败");
		}
		logger.info("节点时间脚本保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="NodeScriptManager",description="节点时间脚本删除",detail="节点时间脚本${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.nodeScriptDao.deleteNodeScript(id);
		} catch (Exception e) {
			logger.error("节点时间脚本删除失败");
			throw new BusinessException("节点时间脚本删除失败");
		}
		logger.info("节点时间脚本删除成功");
	}

	@Override
	@Action(moduleCode="NodeScriptManager",description="节点时间脚本批量删除",detail="节点时间脚本${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("节点时间脚本批量删除成功");
	}

	@Override
	@Action(moduleCode="NodeScriptManager",description="节点时间脚本修改",detail="节点时间脚本${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(NodeScriptEntity NodeScript) throws BusinessException {
		try {
			NodeScriptEntity oldEntity = get(NodeScript.getId());
			MyBeanUtils.copyBeanNotNull2Bean(NodeScript, oldEntity);
			this.nodeScriptDao.updateNodeScript(oldEntity);
		} catch (Exception e) {
			logger.error("节点时间脚本更新失败");
			throw new BusinessException("节点时间脚本更新失败");
		}
		logger.info("节点时间脚本更新成功");
	}

	@Override
	public NodeScriptEntity get(String id){
		NodeScriptEntity NodeScript=null;
		try {
			NodeScript=this.nodeScriptDao.getNodeScript(id);
		} catch (Exception e) {
			logger.error("节点时间脚本获取失败");
			//throw new BusinessException("节点时间脚本获取失败");
		}
		logger.info("节点时间脚本获取成功");
		return NodeScript;
	}

	@Override
	public List<NodeScriptEntity> queryList() throws BusinessException {
		List<NodeScriptEntity> NodeScriptList=new ArrayList<NodeScriptEntity>();
		try {
			NodeScriptList=this.nodeScriptDao.queryNodeScriptList();
		} catch (Exception e) {
			logger.error("节点时间脚本获取列表失败");
			throw new BusinessException("节点时间脚本获取列表失败");
		}
		logger.info("节点时间脚本获取列表成功");
		return NodeScriptList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.nodeScriptDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("节点时间脚本获取分页列表失败");
			throw new BusinessException("节点时间脚本获取分页列表失败");
		}
		logger.info("节点时间脚本获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(NodeScriptEntity.class, param, propertyName);
	}
	
	public void setNodeScriptDao(NodeScriptDao nodeScriptDao) {
		this.nodeScriptDao = nodeScriptDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void deleteByActDefIdAndNodeId(String nodeId, String actDefId) {
		// TODO Auto-generated method stub
		this.nodeScriptDao.executeSql("delete from t_flow_node_script where node_id = ? and act_id = ?", new Object[]{nodeId,actDefId});
	}

	@Override
	public List<NodeScriptEntity> queryListByActDefIdAndNodeId(String actDefId,
			String nodeId) {
		List<NodeScriptEntity> NodeScriptList=new ArrayList<NodeScriptEntity>();
		try {
			NodeScriptList=this.nodeScriptDao.queryListByActDefIdAndNodeId(actDefId,nodeId);
		} catch (Exception e) {
			logger.error("事件脚本获取失败");
			//throw new BusinessException("事件脚本获取失败");
		}
		return NodeScriptList;
	}

	@Override
	public List<NodeScriptEntity> getByNodeScriptId(String actDefId) {
		// TODO Auto-generated method stub
		return this.nodeScriptDao.findByProperty(NodeScriptEntity.class, "actDefId", actDefId);
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeScriptDao.executeHql("delete from NodeScriptEntity t where t.actDefId='"+actDefId+"'");
	}

}
