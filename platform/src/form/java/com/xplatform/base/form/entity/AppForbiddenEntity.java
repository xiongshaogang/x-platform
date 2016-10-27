package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 哪些人不看那些模板
 * @author lixt
 */
@Entity
@Table(name = "t_app_form_forbidden", schema = "")
public class AppForbiddenEntity extends OperationEntity{

	private String formId;//表单模版id
	private String userId;//用户id，
	private String orgId;//机构id
	private String formCode;//表单模版id
	
	@Column(name = "formId", length = 32)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@Column(name = "userId", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "orgId", length = 32)
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "formCode", length = 100)
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	
}
