package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * 哪些人可以看到模版
 * @author sony
 *
 */
@Entity
@Table(name = "t_app_form_user_share", schema = "")
public class AppFormUserShareEntity extends OperationEntity{ 
	private String formId;//表单模版id
	private String userId;//用户id
	private String name;//用户id
	private String type; //(user个人，org机构或部门，role角色)
	
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
	
	@Column(name = "type", length = 10)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "name", length = 200)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
