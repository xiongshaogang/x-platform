package com.xplatform.base.workflow.core.bpm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.Dom4jUtil;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.threadlocal.TaskThreadService;

public class NodeCache {
	private static final Log logger = LogFactory.getLog(NodeCache.class);

	private static Map<String, Map<String, FlowNode>> actNodesMap = new HashMap();

	public void add(String actDefId, Map<String, FlowNode> map) {
		actNodesMap.put(actDefId, map);
	}

	public static synchronized Map<String, FlowNode> getByActDefId(
			String actDefId) {
		/*if (actNodesMap.containsKey(actDefId)) {
			return (Map) actNodesMap.get(actDefId);
		}*/
		Map map = readFromXml(actDefId);
		actNodesMap.put(actDefId, map);
		return map;
	}

	public static FlowNode getStartNode(String actDefId) {
		getByActDefId(actDefId);
		Map nodeMap = (Map) actNodesMap.get(actDefId);
		return getStartNode(nodeMap);
	}

	public static FlowNode getStartNode(Map<String, FlowNode> nodeMap) {
		for (FlowNode flowNode : nodeMap.values()) {
			if ("startEvent".equals(flowNode.getNodeType())) {
				FlowNode parentNode = flowNode.getParentNode();
				if ((parentNode == null)
						|| ("callActivity".equals(parentNode.getNodeType()))) {
					return flowNode;
				}
			}
		}
		return null;
	}

	private static List<FlowNode> getFirstNode(String actDefId) {
		FlowNode startNode = getStartNode(actDefId);
		if (startNode == null)
			return new ArrayList();
		return startNode.getNextFlowNodes();
	}

	public static FlowNode getFirstNodeId(String actDefId) throws Exception {
		FlowNode startNode = getStartNode(actDefId);
		if (startNode == null)
			return null;
		List list = startNode.getNextFlowNodes();
		if (list.size() > 1) {
			throw new Exception("流程定义:" + actDefId + ",起始节点后只能设置一个节点!");
		}
		if (list.size() == 0) {
			throw new Exception("流程定义:" + actDefId + ",起始节点没有后续节点，请检查流程图设置!");
		}
		return (FlowNode) list.get(0);
	}

	public static boolean isFirstNode(String actDefId, String nodeId) {
		List<FlowNode> list = getFirstNode(actDefId);
		for (FlowNode flowNode : list) {
			if (nodeId.equals(flowNode.getNodeId()))
				return true;
		}
		return false;
	}

	public static boolean isSignTaskNode(String actDefId, String nodeId) {
		getByActDefId(actDefId);
		Map nodeMap = (Map) actNodesMap.get(actDefId);
		FlowNode flowNode = (FlowNode) nodeMap.get(nodeId);

		return (flowNode.getIsMultiInstance().booleanValue())
				&& (flowNode.getNodeType().equals("userTask"));
	}

	public static List<FlowNode> getEndNode(String actDefId) {
		getByActDefId(actDefId);
		Map nodeMap = (Map) actNodesMap.get(actDefId);
		return getEndNode(nodeMap);
	}

	public static FlowNode getNodeByActNodeId(String actDefId, String nodeId) {
		getByActDefId(actDefId);
		Map nodeMap = (Map) actNodesMap.get(actDefId);
		return (FlowNode) nodeMap.get(nodeId);
	}

	private static List<FlowNode> getEndNode(Map<String, FlowNode> nodeMap) {
		List flowNodeList = new ArrayList();
		for (FlowNode flowNode : nodeMap.values()) {
			if ((!"endEvent".equals(flowNode.getNodeType()))
					|| (!BeanUtils.isEmpty(flowNode.getNextFlowNodes())))
				continue;
			flowNodeList.add(flowNode);
		}

		return flowNodeList;
	}

	public static boolean hasExternalSubprocess(String actDefId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = (Map) actNodesMap.get(actDefId);
		for (FlowNode flowNode : nodeMap.values()) {
			if ("callActivity".equals(flowNode.getNodeType())) {
				return true;
			}
		}
		return false;
	}

	public static void clear(String actDefId) {
		actNodesMap.remove(actDefId);
	}

	private static Map<String, FlowNode> readFromXml(String actDefId) {
		String xml="";
		DefinitionService definitionService=ApplicationContextUtil.getBean("definitionService");
		Map<String,String> param=new HashMap<String,String>();
		param.put("actId", actDefId);
		List<DefinitionEntity> definitionList=definitionService.findByPropertitys(param);
		if(definitionList!=null && definitionList.size()>0){
			if(TaskThreadService.getProcessCmd()!=null){
	        	Integer isStartAssign=TaskThreadService.getProcessCmd().getVariables().get("isStartAssign")==null?0:Integer.parseInt(TaskThreadService.getProcessCmd().getVariables().get("isStartAssign").toString());
	            if(isStartAssign==1){
	            	String businessKey=TaskThreadService.getProcessCmd().getBusinessKey();
	            	Map<String,Object> dep=definitionService.getDeployXml(null,businessKey);
	        		String defXml= (String)dep.get("defXml");
	                try {
						xml = BpmUtil.transform(PinyinUtil.getPinYinHeadChar(definitionList.get(0).getName()), definitionList.get(0).getName(), defXml);
					}catch (Exception e) {
						e.printStackTrace();
					}
	            }else{
        			String deployId = definitionList.get(0).getActDeployId();
        			FlowService flowService=ApplicationContextUtil.getBean("flowService");
        			xml = flowService.getDefXmlByDeployId(deployId);
	            }
	        }else{
    			String deployId = definitionList.get(0).getActDeployId();
    			FlowService flowService=ApplicationContextUtil.getBean("flowService");
    			xml = flowService.getDefXmlByDeployId(deployId);
	        }
		}
		
		return parseXml(xml, null);
	}

	private static String getXmlByDefKey(String actDefkey) {
		DefinitionService definitionService=ApplicationContextUtil.getBean("definitionService");
		Map<String,String> param=new HashMap<String,String>();
		param.put("actKey", actDefkey);
		List<DefinitionEntity> definitionList=definitionService.findByPropertitys(param);
		String xml="";
		if(definitionList!=null && definitionList.size()>0){
			String deployId = definitionList.get(0).getActDeployId();
			FlowService flowService=ApplicationContextUtil.getBean("flowService");
			xml = flowService.getDefXmlByDeployId(deployId);
		}
		return xml;
	}

	private static Map<String, FlowNode> parseXml(String xml,
			FlowNode parentNode) {
		xml = xml.replace(
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document document = Dom4jUtil.loadXml(xml);
		Element el = document.getRootElement();
		Map map = new HashMap();
		Element processEl = (Element) el.selectSingleNode("./process");
		Iterator its = processEl.elementIterator();

		while (its.hasNext()) {
			Element nodeEl = (Element) its.next();
			String nodeType = nodeEl.getName();

			String nodeId = nodeEl.attributeValue("id");
			String nodeName = nodeEl.attributeValue("name");

			boolean isMultiInstance = nodeEl
					.selectSingleNode("./multiInstanceLoopCharacteristics") != null;

			if (("startEvent".equals(nodeType))
					|| ("userTask".equals(nodeType))
					|| ("parallelGateway".equals(nodeType))
					|| ("inclusiveGateway".equals(nodeType))
					|| ("exclusiveGateway".equals(nodeType))
					|| ("endEvent".equals(nodeType))
					|| ("serviceTask".equals(nodeType))) {
				FlowNode flowNode = new FlowNode(nodeId, nodeName, nodeType,
						isMultiInstance);
				if ("startEvent".equalsIgnoreCase(nodeType)) {
					flowNode.setParentNode(parentNode);
				}
				map.put(nodeId, flowNode);
			} else if ("subProcess".equals(nodeType)) {
				FlowNode subProcessNode = new FlowNode(nodeId, nodeName,
						nodeType, new ArrayList(), isMultiInstance);

				map.put(nodeId, subProcessNode);

				List<Element> subElements = nodeEl.elements();
				for (Element subEl : subElements) {
					String subNodeType = subEl.getName();
					if ((!"startEvent".equals(subNodeType))
							&& (!"userTask".equals(subNodeType))
							&& (!"parallelGateway".equals(subNodeType))
							&& (!"inclusiveGateway".equals(subNodeType))
							&& (!"exclusiveGateway".equals(subNodeType))
							&& (!"endEvent".equals(subNodeType))
							&& (!"serviceTask".equals(subNodeType)))
						continue;
					String subNodeName = subEl.attributeValue("name");
					String subNodeId = subEl.attributeValue("id");
					FlowNode flowNode = new FlowNode(subNodeId, subNodeName,
							subNodeType, false, subProcessNode);
					subProcessNode.getSubFlowNodes().add(flowNode);
					map.put(subNodeId, flowNode);
				}

			} else if ("callActivity".equals(nodeType)) {
				String flowKey = nodeEl.attributeValue("calledElement");

				String subProcessXml = getXmlByDefKey(flowKey);

				FlowNode flowNode = new FlowNode(nodeId, nodeName, nodeType,
						isMultiInstance);
				Map subChildNodes = parseXml(subProcessXml, flowNode);

				flowNode.setAttribute("subFlowKey", flowKey);
				map.put(nodeId, flowNode);

				flowNode.setSubProcessNodes(subChildNodes);
			}

		}

		List seqFlowList = document
				.selectNodes("/definitions/process//sequenceFlow");
		for (int i = 0; i < seqFlowList.size(); i++) {
			Element seqFlow = (Element) seqFlowList.get(i);
			String sourceRef = seqFlow.attributeValue("sourceRef");
			String targetRef = seqFlow.attributeValue("targetRef");

			FlowNode sourceFlowNode = (FlowNode) map.get(sourceRef);
			FlowNode targetFlowNode = (FlowNode) map.get(targetRef);
			if ((sourceFlowNode != null) && (targetFlowNode != null)) {
				logger.debug("sourceRef:" + sourceFlowNode.getNodeName()
						+ " targetRef:" + targetFlowNode.getNodeName());
				if (targetFlowNode.getParentNode() != null) {
					logger.debug("parentNode:"
							+ targetFlowNode.getParentNode().getNodeName());
				}

				if (("startEvent".equals(sourceFlowNode.getNodeType()))
						&& (sourceFlowNode.getParentNode() != null)) {
					sourceFlowNode.setFirstNode(true);
					sourceFlowNode.getParentNode().setSubFirstNode(
							sourceFlowNode);

					if ((targetFlowNode.getParentNode() != null)
							&& (targetFlowNode.getParentNode()
									.getIsMultiInstance().booleanValue())) {
						targetFlowNode
								.setIsMultiInstance(Boolean.valueOf(true));
					}
				}
				sourceFlowNode.getNextFlowNodes().add(targetFlowNode);
				targetFlowNode.getPreFlowNodes().add(sourceFlowNode);
			}
		}
		return map;
	}

	public static Set<String> parseXmlBySubKey(String xml) {
		xml = xml.replace(
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document document = Dom4jUtil.loadXml(xml);
		Element el = document.getRootElement();
		Element processEl = (Element) el.selectSingleNode("./process");
		if (BeanUtils.isEmpty(processEl))
			return null;
		Iterator its = processEl.elementIterator();
		Set keySet = new HashSet();
		while (its.hasNext()) {
			Element nodeEl = (Element) its.next();
			String nodeType = nodeEl.getName();

			if ("callActivity".equals(nodeType)) {
				String flowKey = nodeEl.attributeValue("calledElement");
				keySet.add(flowKey);

				String subProcessXml = getXmlByDefKey(flowKey);

				Set<String> kSet = parseXmlBySubKey(subProcessXml);
				for (String key : kSet) {
					keySet.add(key);
				}
			}
		}
		return keySet;
	}
}