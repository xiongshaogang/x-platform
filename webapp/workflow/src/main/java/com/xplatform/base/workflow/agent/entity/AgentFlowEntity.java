package com.xplatform.base.workflow.agent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_flow_agent_flow")
public class AgentFlowEntity extends OperationEntity {
	private AgentSettingEntity setting;
	private String flowId;
	private String flowCode;
	private String flowName;
	private Integer flowVersion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "setting_id")
	@ForeignKey(name="null")
	public AgentSettingEntity getSetting() {
		return setting;
	}
	public void setSetting(AgentSettingEntity setting) {
		this.setting = setting;
	}
	
	@Column(name = "flow_id", nullable = true, length = 32)
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
	@Column(name = "flow_code", nullable = true, length = 100)
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	
	@Column(name = "flow_name", nullable = true, length = 100)
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	
	@Column(name = "flow_version", nullable = true, length = 4)
	public Integer getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}
	
}
