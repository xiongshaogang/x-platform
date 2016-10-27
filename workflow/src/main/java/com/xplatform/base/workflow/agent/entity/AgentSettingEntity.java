package com.xplatform.base.workflow.agent.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_flow_agent_setting")
public class AgentSettingEntity extends OperationEntity {
	
	public static String AUTHTYPE_GENERAL = "0";
	public static String AUTHTYPE_PARTIAL = "1";
	public static String AUTHTYPE_CONDITION = "2";
	
	private String name;//代理名称
	private String code;//代理code
	private String authId;//授权id
	private String authName;//授权人姓名
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private String status;//状态
	private String type;//类型，1.全部代理 2.部分代理 3.条件代理
	private String agentId; //代理人id
	private String agentName; //代理人姓名
	private String description; //描述
	private String selfReceive;//授权人自己是否可以处理任务  是:Y,否:N
	private String conExp;//条件表达式
	
	@Column(name = "auth_id", nullable = true, length = 32)
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	
	@Column(name = "auth_name", nullable = true, length = 100)
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	
	@Column(name = "start_time", nullable = true)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "end_time", nullable = true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "status", columnDefinition="char",nullable = true, length = 1)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "type", columnDefinition="char", nullable = true, length = 1)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "agent_id", nullable = true, length = 32)
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	@Column(name = "agent_name", nullable = true, length = 100)
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "code", nullable = true, length = 50)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "con_exp", nullable = true, length = 2000)
	public String getConExp() {
		return conExp;
	}
	public void setConExp(String conExp) {
		this.conExp = conExp;
	}
	
	@Column(name = "self_receive", nullable = true, length = 5)
	public String getSelfReceive() {
		return selfReceive;
	}
	public void setSelfReceive(String selfReceive) {
		this.selfReceive = selfReceive;
	}
	
}
