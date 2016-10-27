package com.xplatform.base.orgnaization.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_org_user_verifycode")
public class UserVerifycodeEntity extends OperationEntity{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phone;//手机号码
	private String loginName;//登陆名
	private String verifyCode;//验证码
	private String moduleFlag;//模块
	
	@Column(name = "phone", length = 11)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "loginName", length = 100)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name = "verifyCode", length = 100)
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	@Column(name = "moduleFlag", length = 100)
	public String getModuleFlag() {
		return moduleFlag;
	}
	public void setModuleFlag(String moduleFlag) {
		this.moduleFlag = moduleFlag;
	}
	 
}
