package com.xplatform.base.workflow.instance.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程业务运行实例
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年10月23日 上午10:21:42
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年10月23日 上午10:21:42
 *
 */
@Entity
@Table(name="t_flow_instance_business")
public class ProcessInstBusinessEntity extends OperationEntity {
	private String actInstId;//acitiviti流程实例id
	private String instId;//流程实例扩展id
	private String actDefId;//acitiviti定义id
	private String businessKey;//业务id
	private String businessName;//业务名称
	
	@Column(name="business_key",nullable=true,length=100)
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	@Column(name="business_name",nullable=true,length=300)
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="inst_id",nullable=true,length=32)
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	
	
	
}
