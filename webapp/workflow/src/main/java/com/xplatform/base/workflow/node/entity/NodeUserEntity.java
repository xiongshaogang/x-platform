package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :已改造为工作流选择多类型人员的通用存储表,通过funcType区分
 *
 * @version 1.1
 * @author xiehs
 * @createtime : 2014年7月31日 下午2:27:01
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月31日 下午2:27:01
 * xiaqiang		2014年12月3日 下午2:27:01        改造成工作流选人的通用表
 */
@Entity
@Table(name = "t_flow_node_user")
public class NodeUserEntity extends OperationEntity {
	public static final String FUNC_NODE_USER = "nodeUser";// 节点用户选择
	public static final String FUNC_PRE_NOTICE_USER = "preNoticeUser";// 节点任务开始抄送
	public static final String FUNC_LAST_NOTICE_USER = "lastNoticeUser";// 节点任务完成抄送
	public static final String FUNC_END_CC_USER = "endCCUser";// 流程结束抄送
	
	private String defId;//流程定义
	private String nodeId;//节点id
	private String assignIds;//分配的id集合
	private String assignNames;//候选名称
	private String assignType;//分配类型
	private String assignTypeName;//分配类型
	private String countType;//计算类型
	private String countTypeName;//计算类型名称
	//功能类型,因为工作流选人构造成了一个综合功能,办结抄送选人、节点选执行人等等都在此表,用此字段标识是哪个功能用到
	//现有功能标识(扩充请在注释上继续添加): nodeUser-节点执行人;preNoticeUser-前置通知抄送人;lastNoticeUser-后置通知抄送人;endCCUser-办结抄送人
	private String funcType;

	@Column(name = "def_id", nullable = true, length = 1000)
	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	@Column(name = "node_id", nullable = true, length = 1000)
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "assign_ids", nullable = true, length = 1000)
	public String getAssignIds() {
		return assignIds;
	}

	public void setAssignIds(String assignIds) {
		this.assignIds = assignIds;
	}

	@Column(name = "assign_names", nullable = true, length = 1000)
	public String getAssignNames() {
		return assignNames;
	}

	public void setAssignNames(String assignNames) {
		this.assignNames = assignNames;
	}

	@Column(name = "assign_type", nullable = true, length = 50)
	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	@Column(name = "count_type", nullable = true, length = 5)
	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	@Column(name = "assing_type_name", nullable = true, length = 100)
	public String getAssignTypeName() {
		return assignTypeName;
	}

	public void setAssignTypeName(String assignTypeName) {
		this.assignTypeName = assignTypeName;
	}

	@Column(name = "count_type_name", nullable = true, length = 30)
	public String getCountTypeName() {
		return countTypeName;
	}

	public void setCountTypeName(String countTypeName) {
		this.countTypeName = countTypeName;
	}

	@Column(name = "func_type", nullable = true, length = 30)
	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

}
