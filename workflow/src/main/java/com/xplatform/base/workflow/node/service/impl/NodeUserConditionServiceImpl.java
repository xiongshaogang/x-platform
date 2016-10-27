package com.xplatform.base.workflow.node.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xplatform.base.workflow.node.dao.NodeUserConditionDao;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
import com.xplatform.base.workflow.node.mybatis.dao.NodeUserMybatisDao;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;
import com.xplatform.base.workflow.node.service.NodeUserConditionService;
/**
 * 
 * description :节点用户条件service实现
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
@Service("NodeUserConditionService")
public class NodeUserConditionServiceImpl implements NodeUserConditionService {
	private static final Logger logger = Logger.getLogger(NodeUserConditionServiceImpl.class);
	@Resource
	private NodeUserConditionDao nodeUserConditionDao;
	
	private NodeUserMybatisDao nodeUserConditionMybatisDao;
	
	@Resource
	private BaseService baseService;
	
	@Override
	@Action(moduleCode="NodeUserConditionManager",description="节点用户条件新增",detail="节点用户条件${name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(NodeUserConditionEntity NodeUserCondition) throws BusinessException {
		String pk="";
		try {
			pk=this.nodeUserConditionDao.addNodeUserCondition(NodeUserCondition);
		} catch (Exception e) {
			logger.error("节点用户条件保存失败");
			throw new BusinessException("节点用户条件保存失败");
		}
		logger.info("节点用户条件保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="NodeUserConditionManager",description="节点用户条件删除",detail="节点用户条件${name}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			this.nodeUserConditionDao.deleteNodeUserCondition(id);
		} catch (Exception e) {
			logger.error("节点用户条件删除失败");
			throw new BusinessException("节点用户条件删除失败");
		}
		logger.info("节点用户条件删除成功");
	}

	@Override
	@Action(moduleCode="NodeUserConditionManager",description="节点用户条件批量删除",detail="节点用户条件${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("节点用户条件批量删除成功");
	}

	@Override
	@Action(moduleCode="NodeUserConditionManager",description="节点用户条件修改",detail="节点用户条件${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(NodeUserConditionEntity NodeUserCondition) throws BusinessException {
		try {
			NodeUserConditionEntity oldEntity = get(NodeUserCondition.getId());
			MyBeanUtils.copyBeanNotNull2Bean(NodeUserCondition, oldEntity);
			this.nodeUserConditionDao.updateNodeUserCondition(oldEntity);
		} catch (Exception e) {
			logger.error("节点用户条件更新失败");
			throw new BusinessException("节点用户条件更新失败");
		}
		logger.info("节点用户条件更新成功");
	}

	@Override
	public NodeUserConditionEntity get(String id){
		NodeUserConditionEntity NodeUserCondition=null;
		try {
			NodeUserCondition=this.nodeUserConditionDao.getNodeUserCondition(id);
		} catch (Exception e) {
			logger.error("节点用户条件获取失败");
			//throw new BusinessException("节点用户条件获取失败");
		}
		logger.info("节点用户条件获取成功");
		return NodeUserCondition;
	}

	@Override
	public List<NodeUserConditionEntity> queryList() throws BusinessException {
		List<NodeUserConditionEntity> NodeUserConditionList=new ArrayList<NodeUserConditionEntity>();
		try {
			NodeUserConditionList=this.nodeUserConditionDao.queryNodeUserConditionList();
		} catch (Exception e) {
			logger.error("节点用户条件获取列表失败");
			throw new BusinessException("节点用户条件获取列表失败");
		}
		logger.info("节点用户条件获取列表成功");
		return NodeUserConditionList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		try {
			this.nodeUserConditionDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			logger.error("节点用户条件获取分页列表失败");
			throw new BusinessException("节点用户条件获取分页列表失败");
		}
		logger.info("节点用户条件获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(NodeUserConditionEntity.class, param, propertyName);
	}
	
	/**
	 * 根据节点设置id得到节点用户条件
	 * @author xiehs
	 * @createtime 2014年8月4日 下午4:35:27
	 * @Decription
	 *
	 * @param nodeSetId
	 * @return
	 */
	public List<NodeUserConditionEntity> getBySetId(String nodeSetId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("setId", nodeSetId);
		return this.nodeUserConditionDao.findByPropertys(NodeUserConditionEntity.class, param);
	}
	
	
	public void setNodeUserConditionDao(NodeUserConditionDao nodeUserConditionDao) {
		this.nodeUserConditionDao = nodeUserConditionDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Autowired
	public void setNodeUserConditionMybatisDao(NodeUserMybatisDao nodeUserConditionMybatisDao) {
		this.nodeUserConditionMybatisDao = nodeUserConditionMybatisDao;
	}

}
