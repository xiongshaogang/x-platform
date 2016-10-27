package com.xplatform.base.workflow.instance.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程表单运行实例
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月14日 上午10:28:09
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月14日 上午10:28:09
 *
 */
@Entity
@Table(name="t_flow_instance_form")
public class ProcessInstFormEntity extends OperationEntity {
	private String formId;//表单id
	private String formKey;//表单key
	private String formType;//表单类型（全局，节点）
	private String formUrl;//表单url
	private String actInstId;//acitiviti流程实例id
	private String instId;//流程实例扩展id
	private String actDefId;//acitiviti定义id
	private String actNodeId;//节点id
	
	@Column(name="form_id",nullable=true,length=32)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@Column(name="form_key",nullable=true,length=32)
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	
	@Column(name="form_type",nullable=true,length=32)
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	
	@Column(name="form_url",nullable=true,length=100)
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
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
	
	@Column(name="act_node_id",nullable=true,length=32)
	public String getActNodeId() {
		return actNodeId;
	}
	public void setActNodeId(String actNodeId) {
		this.actNodeId = actNodeId;
	}
	
	
}
