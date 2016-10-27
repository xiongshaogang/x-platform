package com.xplatform.base.workflow.core.facade.caculateuser.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculate;
import com.xplatform.base.workflow.core.facade.caculateuser.NodeUserCalculateSelector;

public class NodeUserCalculateSelectorImpl implements NodeUserCalculateSelector {
	
	private Map<String, NodeUserCalculate> nodeUserCalculate = new LinkedHashMap<String, NodeUserCalculate>();

	public Map<String, NodeUserCalculate> getnodeUserCalculate() {
		return this.nodeUserCalculate;
	}

	public List<NodeUserCalculate> getBpmNodeUserCalculationList() {
		List<NodeUserCalculate> list = new ArrayList<NodeUserCalculate>();
		Map<String, NodeUserCalculate> map = this.nodeUserCalculate;
		for (Map.Entry<String,NodeUserCalculate> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}
	
	public NodeUserCalculate getByKey(String key) {
		return (NodeUserCalculate) this.nodeUserCalculate.get(key);
	}

	public void setNodeUserCalculate(Map<String, NodeUserCalculate> nodeUserCalculate) {
		this.nodeUserCalculate = nodeUserCalculate;
	}

	
}
