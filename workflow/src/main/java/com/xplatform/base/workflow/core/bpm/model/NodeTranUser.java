package com.xplatform.base.workflow.core.bpm.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.xplatform.base.workflow.node.model.NodeUserMap;

public class NodeTranUser {
	private String nodeName;//结点名字
	private String nodeId;//结点ID
	Set<NodeUserMap> nodeUserMapSet = new LinkedHashSet<NodeUserMap>();//结点用户集合

	Map<String, String> nextPathMap = new HashMap<String, String>();//下一个路径

	public Map<String, String> getNextPathMap() {
		return this.nextPathMap;
	}

	public void setNextPathMap(Map<String, String> nextPathMap) {
		this.nextPathMap = nextPathMap;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public NodeTranUser() {
	}

	public NodeTranUser(String nodeId, String nodeName,
			Set<NodeUserMap> nodeUserMapSet) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.nodeUserMapSet = nodeUserMapSet;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Set<NodeUserMap> getNodeUserMapSet() {
		return this.nodeUserMapSet;
	}

	public void setNodeUserMapSet(Set<NodeUserMap> nodeUserMapSet) {
		this.nodeUserMapSet = nodeUserMapSet;
	}
}