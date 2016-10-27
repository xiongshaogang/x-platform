package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * 哪些人可以看到模版的数据
 * @author sony
 *
 */
@Entity
@Table(name = "t_app_form_user_data", schema = "")
public class AppFormUserData extends OperationEntity{ 
	private String formId;//表单模版id
	private String userId;//
	
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
	
}
