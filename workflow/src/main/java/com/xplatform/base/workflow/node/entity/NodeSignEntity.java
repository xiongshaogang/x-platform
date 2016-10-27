package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :会签节点和特权
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:03:46
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:03:46
 *
 */
@Entity
@Table(name="t_flow_node_sign")
public class NodeSignEntity extends OperationEntity {
	public static final String VOTE_TYPE_PERCENT = "percent";//百分比投票

	public static final String VOTE_TYPE_ABSOLUTE = "absolute";//绝对票数

	public static final String FLOW_MODE_DIRECT = "direct";//达到投票比例就结束

	public static final String FLOW_MODE_WAITALL = "complete";//等到所有的人投票完才结束

	
	//会签基本信息
	private String nodeId;//节点id
	private String actDefId;//acitiviti定义id
	private String decideType;//决策类型（通过pass,反对reject）
	private Float voteAgreeAmount;//投票通过数
	private Float voteRefuseAmount;//投票拒绝数
	private Float voteReconsideAmount;//投票拒绝数
	private String voteType;//投票类型（绝对票数，百分比）
	private String completeType;//完成会签方式（满足条件结束会签，所有人投完票再结束会签）
	
	@Column(name = "node_id", nullable = true, length = 32)
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column(name = "act_id", nullable = true, length = 32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name = "decide_type", nullable = true, length = 5)
	public String getDecideType() {
		return decideType;
	}
	public void setDecideType(String decideType) {
		this.decideType = decideType;
	}
	
	@Column(name = "vote_agree_amount", nullable = true, length = 32)
	public Float getVoteAgreeAmount() {
		return voteAgreeAmount;
	}
	public void setVoteAgreeAmount(Float voteAgreeAmount) {
		this.voteAgreeAmount = voteAgreeAmount;
	}
	
	@Column(name = "vote_refuse_amount", nullable = true, length = 32)
	public Float getVoteRefuseAmount() {
		return voteRefuseAmount;
	}
	public void setVoteRefuseAmount(Float voteRefuseAmount) {
		this.voteRefuseAmount = voteRefuseAmount;
	}
	
	@Column(name = "vote_reconside_amount", nullable = true, length = 32)
	public Float getVoteReconsideAmount() {
		return voteReconsideAmount;
	}
	public void setVoteReconsideAmount(Float voteReconsideAmount) {
		this.voteReconsideAmount = voteReconsideAmount;
	}
	@Column(name = "vote_type", nullable = true, length = 10)
	public String getVoteType() {
		return voteType;
	}
	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}
	
	@Column(name = "complete_type", nullable = true, length = 10)
	public String getCompleteType() {
		return completeType;
	}
	public void setCompleteType(String completeType) {
		this.completeType = completeType;
	}
}
