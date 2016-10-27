package com.xplatform.base.orgnaization.member.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;

import javax.xml.soap.Text;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 会员信息
 * @author onlineGenerator
 * @date 2014-05-21 14:29:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_member", schema = "")
@SuppressWarnings("serial")
public class TOrgUser extends OperationEntity implements java.io.Serializable {
	
	/**编码*/
	private java.lang.String code;
	/**用户名*/
	private java.lang.String userName;
	/**密码*/
	private java.lang.String password;
	/**邮件地址*/
	private java.lang.String email;
	/**用户类型*/
	private java.lang.String userType;
	/**用户类型ID*/
	private java.lang.String userTypeId;
	/**删除标识*/
	private java.lang.String flag;
	/**是否激活*/
	private java.lang.String active;
	/**是否锁定*/
	private java.lang.String locked;
	/**登录错误时间*/
	private java.lang.Integer loginErrorTimes;
	
	@Column(name ="CODE",nullable=true,length=50)
	public java.lang.String getCode() {
		return code;
	}
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	@Column(name ="USER_NAME",nullable=true,length=50)
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	@Column(name ="PASSWORD",nullable=true,length=50)
	public java.lang.String getPassword() {
		return password;
	}
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	@Column(name ="EMAIL",nullable=true,length=100)
	public java.lang.String getEmail() {
		return email;
	}
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	@Column(name ="USER_TYPE",nullable=true,length=20)
	public java.lang.String getUserType() {
		return userType;
	}
	public void setUserType(java.lang.String userType) {
		this.userType = userType;
	}
	@Column(name ="USER_TYPE_ID",nullable=true,length=32)
	public java.lang.String getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(java.lang.String userTypeId) {
		this.userTypeId = userTypeId;
	}
	@Column(name ="FLAG",nullable=true,length=1)
	public java.lang.String getFlag() {
		return flag;
	}
	public void setFlag(java.lang.String flag) {
		this.flag = flag;
	}
	@Column(name ="ACTIVE",nullable=true,length=1)
	public java.lang.String getActive() {
		return active;
	}
	public void setActive(java.lang.String active) {
		this.active = active;
	}
	@Column(name ="LOCKED",nullable=true,length=1)
	public java.lang.String getLocked() {
		return locked;
	}
	public void setLocked(java.lang.String locked) {
		this.locked = locked;
	}
	@Column(name ="LOGIN_ERROR_TIMES",nullable=true,length=11)
	public java.lang.Integer getLoginErrorTimes() {
		return loginErrorTimes;
	}
	public void setLoginErrorTimes(java.lang.Integer loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}
	
	
	
}
