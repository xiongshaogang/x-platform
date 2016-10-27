package com.xplatform.base.workflow.node.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.workflow.core.bpmn20.entity.CallActivity;
import com.xplatform.base.workflow.core.bpmn20.entity.FlowElement;
import com.xplatform.base.workflow.core.bpmn20.entity.Process;
import com.xplatform.base.workflow.core.bpmn20.entity.UserTask;
import com.xplatform.base.workflow.core.bpmn20.entity.ht.BPMN20HtExtConst;
import com.xplatform.base.workflow.core.bpmn20.util.BPMN20Util;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.node.dao.NodeSetDao;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;

@Service("nodeSetService")
public class NodeSetServiceImpl implements NodeSetService {
	private static final Logger logger = Logger.getLogger(NodeSetServiceImpl.class);
	@Resource
	private NodeSetDao nodeSetDao;
	/**
	 * 新增或修改流程节点
	 * @param actFlowDefXml
	 * @param bpmDefinition
	 * @param isAdd
	 * @throws Exception
	 */
	public void saveOrUpdateNodeSet(String actFlowDefXml,DefinitionEntity definition, boolean isAdd) throws BusinessException {
		String defId = definition.getId();
		try {
			List<Process> processes = BPMN20Util.getProcess(actFlowDefXml);
			if (processes.size() == 0) {
				return;
			}

			Class[] classes = { UserTask.class, CallActivity.class };

			List<FlowElement> flowElements = BPMN20Util.getFlowElementByType((Process) processes.get(0), true, classes);
			if (isAdd) {
				for (FlowElement flowElement : flowElements){
					addNodeSet(definition, flowElement);
				}
			} else {
				Map<String,String> param=new HashMap<String,String>();
				param.put("defId", defId);
				param.put("setType", "0");//普通节点
				List<NodeSetEntity> nodeSetList=this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
				//先删除所有的节点
				deleteNodeSetList(nodeSetList, flowElements);
				//保存所有的节点
				updateNodeSetList(definition, nodeSetList, flowElements);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("更新节点失败");
		}
		
	}
	
	public void addNodeSet(DefinitionEntity definition, FlowElement flowNode)
			throws Exception {
		String defId = definition.getId();
		String actDefId = definition.getActId();
		Integer nodeOrder = Integer.valueOf(0);
		List extensions = BPMN20Util.getFlowElementExtension(flowNode,BPMN20HtExtConst._Order_QNAME);
		if (BeanUtils.isNotEmpty(extensions)) {
			nodeOrder = (Integer) extensions.get(0);
		}
		NodeSetEntity nodeSet = new NodeSetEntity();
		nodeSet.setFormType("-1");
		nodeSet.setActDefId(actDefId);
		nodeSet.setDefId(defId);
		nodeSet.setNodeId(flowNode.getId());
		nodeSet.setNodeName(flowNode.getName());
		nodeSet.setNodeOrder(nodeOrder);
		this.nodeSetDao.addNodeSet(nodeSet);
	}
	
	/**
	 * 修改时删除不要的节点
	 * @author xiehs
	 * @createtime 2014年10月21日 上午11:01:17
	 * @Decription
	 *
	 * @param nodeSetList
	 * @param flowElements
	 */
	public void deleteNodeSetList(List<NodeSetEntity> nodeSetList, List<FlowElement> flowElements){
		if(nodeSetList!=null && nodeSetList.size()>0){
			for(NodeSetEntity nodeSet:nodeSetList){
				String nodeId = nodeSet.getNodeId();
				boolean inflag = false;//该节点要不要删除的机制
				for (FlowElement flowNode : flowElements) {
					if (flowNode.getId().equals(nodeId)) {//修改的流程还有原来的节点，那么不删除
						inflag = true;
						break;
					}
				}
				if (!inflag) {//修改的流程没有了原来的节点，那么这个节点就需要删除
					this.nodeSetDao.deleteNodeSet(nodeSet.getId());
				}
			}
		}
	}

	public void updateNodeSetList(DefinitionEntity definition, List<NodeSetEntity> nodeSetList, List<FlowElement> flowElements)throws BusinessException{
		Map<String,NodeSetEntity> oldSetMap=new HashMap<String,NodeSetEntity>();
		if(nodeSetList!=null && nodeSetList.size()>0){
			for(NodeSetEntity nodeSet:nodeSetList){
				oldSetMap.put(nodeSet.getNodeId(), nodeSet);
			}
		}
		try {
			for (FlowElement flowElement : flowElements){
				if (oldSetMap.containsKey(flowElement.getId())) {//如果包含节点，那么就更新
					Integer nodeOrder = Integer.valueOf(0);
					List extensions = BPMN20Util.getFlowElementExtension(flowElement, BPMN20HtExtConst._Order_QNAME);
					if (BeanUtils.isNotEmpty(extensions)) {
						nodeOrder = (Integer) extensions.get(0);
					}
					NodeSetEntity bpmNodeSet = (NodeSetEntity) oldSetMap.get(flowElement.getId());
					bpmNodeSet.setNodeName(flowElement.getName());
					bpmNodeSet.setNodeOrder(nodeOrder);
					this.nodeSetDao.updateNodeSet(bpmNodeSet);
				} else {//如果老的节点没有，那么加入
					addNodeSet(definition, flowElement);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("更新节点失败");
		}
	}
	
	@Override
	public String save(NodeSetEntity nodeSet) throws BusinessException {
		String pk="";
		try {
			if(StringUtil.isNotEmpty(nodeSet.getId())){//任务节点一般是更新
				this.update(nodeSet);
			}else{//全局节点如果存在，那么先删除，如果不存在直接新增
				String setType=nodeSet.getSetType();
				String formType=nodeSet.getFormType();
				NodeSetEntity nodeSetEntity =null;
				if(NodeSetEntity.SetType_GloabalForm.equals(setType)){
					nodeSetEntity=getBySetType1(nodeSet.getDefId(), NodeSetEntity.SetType_GloabalForm);
				}else if(NodeSetEntity.SetType_BPMForm.equals(setType)){
					nodeSetEntity=getBySetType1(nodeSet.getDefId(), NodeSetEntity.SetType_BPMForm);
				}
				
				if (nodeSetEntity != null) {
					delete(nodeSetEntity.getId());
				}
				if(!"-1".equals(formType)){
					pk=this.nodeSetDao.addNodeSet(nodeSet);
				}
			}
		} catch (Exception e) {
			logger.error("资源保存失败");
			throw new BusinessException("资源保存失败");
		}
		logger.info("资源保存成功");
		return pk;
	}
	
	public List<NodeSetEntity> queryByPorperty(String porperty,Object value){
		return this.nodeSetDao.findByProperty(NodeSetEntity.class,porperty, value);
	}

	/**
	 * 
	 * 重写方法: getByOther|描述:获取非节点表单得到的节点集合（全局，实例，开始）
	 * 
	 * @param defId
	 * @return
	 */
	@Override
	public List<NodeSetEntity> getByOther(String defId)
			throws BusinessException {
		// TODO Auto-generated method stub
		return this.nodeSetDao.findHql("from NodeSetEntity t where t.defId=? and setType>=1",defId);
	}
	
	/**
	 * 
	 * 通过流程定义id和表单类型查询节点设置
	 * 
	 */
	public NodeSetEntity getBySetType(String defId,String fromType){
		Map<String,String> param=new HashMap<String,String>();
		param.put("defId", defId);
		param.put("formType", fromType);
		NodeSetEntity nodeSet=null;
		List<NodeSetEntity> list= this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if(list!=null && list.size()>0){
			nodeSet=list.get(0);
		}
		return nodeSet;
	}
	
	public NodeSetEntity getBySetType1(String defId,String setType){
		Map<String,String> param=new HashMap<String,String>();
		param.put("defId", defId);
		param.put("setType", setType);
		NodeSetEntity nodeSet=null;
		this.nodeSetDao.getSession().clear();
		List<NodeSetEntity> list= this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if(list!=null && list.size()>0){
			nodeSet=list.get(0);
		}
		return nodeSet;
	}
	
	public List<NodeSetEntity> getTaskNodeList(String defId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("defId", defId);
		param.put("setType", "0");
		return this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
	}
	
	public List<NodeSetEntity> getTaskNodeListByActDefId(String actDefId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("setType", NodeSetEntity.SetType_TaskNode);
		return this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
	}
	
	/**
	 * 
	 * 重写方法: getMapByDefId|描述:通过流程定义id获取所有的节点map
	 * 
	 * @param defId
	 * @return
	 */
	public Map<String, NodeSetEntity> getMapByDefId(String defId){
	    Map<String,NodeSetEntity> map = new HashMap<String,NodeSetEntity>();
	    List<NodeSetEntity> list = queryByPorperty("defId",defId);
	    for (NodeSetEntity bpmNodeSet : list) {
	      map.put(bpmNodeSet.getNodeId(), bpmNodeSet);
	    }
	    return map;
   }
	
	public NodeSetEntity getNodeSetByActDefIdNodeId(String actDefId,String nodeId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		NodeSetEntity nodeSet=null;
		List<NodeSetEntity> list= this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if(list!=null && list.size()>0){
			nodeSet=list.get(0);
		}
		return nodeSet;
	}
	
	public NodeSetEntity getNodeSetByDefIdNodeId(String defId,String nodeId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("defId", defId);
		param.put("nodeId", nodeId);
		NodeSetEntity nodeSet=null;
		List<NodeSetEntity> list= this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if(list!=null && list.size()>0){
			nodeSet=list.get(0);
		}
		return nodeSet;
	}
	
	@Override
	public void update(NodeSetEntity nodeset) throws BusinessException {
		try {
			NodeSetEntity oldEntity = getNodeSetByActDefIdNodeId(nodeset.getActDefId(),nodeset.getNodeId());
			MyBeanUtils.copyBeanNotNull2Bean(nodeset, oldEntity);
			this.nodeSetDao.updateNodeSet(oldEntity);
		} catch (Exception e) {
			logger.error("节点通知类型设置失败");
			throw new BusinessException("节点通知类型设置失败");
		}
		logger.info("节点通知类型设置成功");
	}
	
	@Override
	public void deleteByDefId(String defId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeSetDao.executeHql("delete from NodeSetEntity t where t.defId='"+defId+"'");
	}
	@Override
	public void delete(String id) throws BusinessException {
		this.nodeSetDao.deleteEntityById(NodeSetEntity.class, id);
	}

	@Override
	public NodeSetEntity getByActDefIdNodeId(String actDefId,
			String nodeId) {
		// TODO Auto-generated method stub
		NodeSetEntity nodeSet=this.getNodeSetByActDefIdNodeId(actDefId, nodeId);
		if(nodeSet==null){//全局设置
			List<NodeSetEntity> nodeSetList=this.nodeSetDao.findByProperty(NodeSetEntity.class, "setType","2");
			if(nodeSetList!=null && nodeSetList.size()>0){
				nodeSet=nodeSetList.get(0);
			}
		}
		return nodeSet;
	}

	@Override
	public NodeSetEntity getGlobalByActDefId(String actDefId) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("actDefId", actDefId);
		param.put("setType", NodeSetEntity.SetType_GloabalForm);
		List<NodeSetEntity> nodeSetList = this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if (nodeSetList != null && nodeSetList.size() > 0) {
			return nodeSetList.get(0);
		}
		return null;
	}
	@Override
	public NodeSetEntity getBpmFormByActDefId(String actDefId) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("actDefId", actDefId);
		param.put("setType", NodeSetEntity.SetType_BPMForm);
		List<NodeSetEntity> nodeSetList = this.nodeSetDao.findByPropertys(NodeSetEntity.class, param);
		if (nodeSetList != null && nodeSetList.size() > 0) {
			return nodeSetList.get(0);
		}
		return null;
	}
}
