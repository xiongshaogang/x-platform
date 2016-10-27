package com.xplatform.base.workflow.node.mybatis.vo;

public class NodeUserVo {
	private String id;//节点用户id
	private String setId;//code
	private String defId;//备注
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private String assignIds;//分配的id集合
	private String assignNames;//候选名称
	private String assignType;//分配类型
	private String assignTypeName;//分配类型
	private String countType;//计算类型
	private String countTypeName;//计算类型名称
	private String flag;//是否分配了用户
	//功能类型,因为工作流选人构造成了一个综合功能,办结抄送选人、节点选执行人等等都在此表,用此字段标识是哪个功能用到
	//现有功能标识(扩充请在注释上继续添加): nodeUser-节点执行人;preNoticeUser-前置通知抄送人;lastNoticeUser-后置通知抄送人;endCCUser-办结抄送人
	private String funcType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getAssignIds() {
		return assignIds;
	}

	public void setAssignIds(String assignIds) {
		this.assignIds = assignIds;
	}

	public String getAssignNames() {
		return assignNames;
	}

	public void setAssignNames(String assignNames) {
		this.assignNames = assignNames;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getAssignTypeName() {
		return assignTypeName;
	}

	public void setAssignTypeName(String assignTypeName) {
		this.assignTypeName = assignTypeName;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getCountTypeName() {
		return countTypeName;
	}

	public void setCountTypeName(String countTypeName) {
		this.countTypeName = countTypeName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

}
