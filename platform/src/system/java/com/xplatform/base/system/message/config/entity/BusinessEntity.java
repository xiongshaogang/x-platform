package com.xplatform.base.system.message.config.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name="t_sys_message_business",schema="")
@SuppressWarnings("serial")
public class BusinessEntity extends OperationEntity implements Serializable{
	
	private String businessType;//业务类型
    private Date businesstime;//业务时间
    private String businessPlace;//业务地点
    private String businessPerson;//业务人员
    private String businessObj;//业务对象
    private String reference; //内部通告文号
    private String companyId;// 公司
    private String companyName;// 公司
    private String fromId;//发送邮件Id
    
    @Column(name ="business_type",nullable=true,length=20)
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	@Column(name ="business_time",nullable=true,length=50)
	public Date getBusinesstime() {
		return businesstime;
	}
	public void setBusinesstime(Date businesstime) {
		this.businesstime = businesstime;
	}
	
	@Column(name ="business_place",nullable=true,length=200)
	public String getBusinessPlace() {
		return businessPlace;
	}
	public void setBusinessPlace(String businessPlace) {
		this.businessPlace = businessPlace;
	}
	
	@Column(name ="business_person",nullable=true,length=200)
	public String getBusinessPerson() {
		return businessPerson;
	}
	public void setBusinessPerson(String businessPerson) {
		this.businessPerson = businessPerson;
	}
	
	@Column(name ="business_obj",nullable=true,length=200)
	public String getBusinessObj() {
		return businessObj;
	}
	public void setBusinessObj(String businessObj) {
		this.businessObj = businessObj;
	}
	
	@Column(name ="from_id",nullable=true,length=32)
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	
	@Column(name ="reference",nullable=true,length=200)
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Column(name ="company_id",nullable=true,length=200)
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	@Column(name ="company_name",nullable=true,length=200)
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
}
