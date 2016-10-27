package com.xplatform.base.workflow.node.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.workflow.node.dao.NodeMessageDao;
import com.xplatform.base.workflow.node.entity.NodeMessageEntity;
import com.xplatform.base.workflow.node.service.NodeMessageService;

@Service("nodeMessageService")
public class NodeMessageServiceImpl implements NodeMessageService {
	
	@Resource
	private NodeMessageDao nodeMessageDao;
	

	public void setNodeMessageDao(NodeMessageDao nodeMessageDao) {
		this.nodeMessageDao = nodeMessageDao;
	}


	@Override
	public NodeMessageEntity getEntity(Map<String, String> params) {
		// TODO Auto-generated method stub
		List<NodeMessageEntity> list =  this.nodeMessageDao.findByPropertys(NodeMessageEntity.class, params);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public void save(NodeMessageEntity n) {
		// TODO Auto-generated method stub
		this.nodeMessageDao.save(n);
	}


	@Override
	public void update(NodeMessageEntity nme) {
		// TODO Auto-generated method stub
		this.nodeMessageDao.updateEntitie(nme);
	}


	@Override
	public List<NodeMessageEntity> getNodeMessageList(Map<String, String> params) {
		// TODO Auto-generated method stub
		return this.nodeMessageDao.findByPropertys(NodeMessageEntity.class, params);
	}


	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeMessageDao.executeHql("delete from NodeMessageEntity t where t.actDefId='"+actDefId+"'");
	}

}
