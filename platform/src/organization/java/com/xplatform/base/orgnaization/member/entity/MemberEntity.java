package com.xplatform.base.orgnaization.member.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.poi.excel.annotation.Excel;
import com.xplatform.base.poi.excel.annotation.ExcelTarget;

/**   
 * @Title: Entity
 * @Description: 会员信息
 * @author onlineGenerator
 * @date 2014-05-21 14:29:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_org_member", schema = "")
@SuppressWarnings("serial")
@ExcelTarget("memberEntity")
public class MemberEntity extends OperationEntity implements java.io.Serializable {
	/**编码*/
	@Excel(name="编号",orderNum="1")
	private String code;
	/**姓名*/
	@Excel(name="会员姓名",orderNum="2")
	private String name;
	/**用户名*/
	private String loginName;
	/**密码*/
	private String loginPassword;
	/**用户类型*/
	private String userType;
	/**企业编码*/
	private String companyId;
	/**电子邮箱*/
	private String email;
	/**出生日期*/
	private Date birthday;
	/**性别*/
	private String sex;
	/**地址*/
	private String address;
	/**邮编*/
	private String post;
	/**电话号码*/
	private String telephone;
	/**手机号码*/
	private String mobile;
	/**QQ号码*/
	private String qq;
	/**MSN*/
	private String msn;
	/**国籍*/
	private String nationallity;
	/**证件类型*/
	private String certificateType;
	/**证件号码*/
	private String certificateNum;
	/**单位名称*/
	private String unitName;
	/**启用状态*/
	private String available;
	/**是否激活*/
	private String isActived;
	/**激活日期*/
	private java.util.Date activedDate;
	/**传真*/
	private String fax;
	/**备注*/
	private String note;
	/**删除标识*/
	private String flag;
	
	/**
	 *方法: 取得String
	 *@return: String  编码
	 */
	@Column(name ="CODE",nullable=true,length=36)
	public String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置String
	 *@param: String  编码
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 *方法: 取得String
	 *@return: String  姓名
	 */
	@Column(name ="NAME",nullable=true,length=100)
	public String getName(){
		return this.name;
	}

	/**
	 *方法: 设置String
	 *@param: String  姓名
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 *方法: 取得String
	 *@return: String  用户名
	 */
	@Column(name ="LOGIN_NAME",nullable=true,length=64)
	public String getLoginName(){
		return this.loginName;
	}

	/**
	 *方法: 设置String
	 *@param: String  用户名
	 */
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
	/**
	 *方法: 取得String
	 *@return: String  密码
	 */
	@Column(name ="LOGIN_PASSWORD",nullable=true,length=64)
	public String getLoginPassword(){
		return this.loginPassword;
	}

	/**
	 *方法: 设置String
	 *@param: String  密码
	 */
	public void setLoginPassword(String loginPassword){
		this.loginPassword = loginPassword;
	}
	/**
	 *方法: 取得String
	 *@return: String  用户类型
	 */
	@Column(name ="USER_TYPE",nullable=true,length=10)
	public String getUserType(){
		return this.userType;
	}

	/**
	 *方法: 设置String
	 *@param: String  用户类型
	 */
	public void setUserType(String userType){
		this.userType = userType;
	}
	/**
	 *方法: 取得String
	 *@return: String  企业编码
	 */
	@Column(name ="COMPANY_ID",nullable=true,length=64)
	public String getCompanyId(){
		return this.companyId;
	}

	/**
	 *方法: 设置String
	 *@param: String  企业编码
	 */
	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}
	/**
	 *方法: 取得String
	 *@return: String  电子邮箱
	 */
	@Column(name ="EMAIL",nullable=true,length=100)
	public String getEmail(){
		return this.email;
	}

	/**
	 *方法: 设置String
	 *@param: String  电子邮箱
	 */
	public void setEmail(String email){
		this.email = email;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  出生日期
	 */
	@Column(name ="BIRTHDAY",nullable=true)
	public java.util.Date getBirthday(){
		return this.birthday;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  出生日期
	 */
	public void setBirthday(java.util.Date birthday){
		this.birthday = birthday;
	}
	/**
	 *方法: 取得String
	 *@return: String  性别
	 */
	@Column(name ="SEX",nullable=true,length=10)
	public String getSex(){
		return this.sex;
	}

	/**
	 *方法: 设置String
	 *@param: String  性别
	 */
	public void setSex(String sex){
		this.sex = sex;
	}
	/**
	 *方法: 取得String
	 *@return: String  地址
	 */
	@Column(name ="ADDRESS",nullable=true,length=200)
	public String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置String
	 *@param: String  地址
	 */
	public void setAddress(String address){
		this.address = address;
	}
	/**
	 *方法: 取得String
	 *@return: String  邮编
	 */
	@Column(name ="POST",nullable=true,length=6)
	public String getPost(){
		return this.post;
	}

	/**
	 *方法: 设置String
	 *@param: String  邮编
	 */
	public void setPost(String post){
		this.post = post;
	}
	/**
	 *方法: 取得String
	 *@return: String  电话号码
	 */
	@Column(name ="TELEPHONE",nullable=true,length=32)
	public String getTelephone(){
		return this.telephone;
	}

	/**
	 *方法: 设置String
	 *@param: String  电话号码
	 */
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}
	/**
	 *方法: 取得String
	 *@return: String  手机号码
	 */
	@Column(name ="MOBILE",nullable=true,length=32)
	public String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置String
	 *@param: String  手机号码
	 */
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得String
	 *@return: String  QQ号码
	 */
	@Column(name ="QQ",nullable=true,length=64)
	public String getQq(){
		return this.qq;
	}

	/**
	 *方法: 设置String
	 *@param: String  QQ号码
	 */
	public void setQq(String qq){
		this.qq = qq;
	}
	/**
	 *方法: 取得String
	 *@return: String  MSN
	 */
	@Column(name ="MSN",nullable=true,length=64)
	public String getMsn(){
		return this.msn;
	}

	/**
	 *方法: 设置String
	 *@param: String  MSN
	 */
	public void setMsn(String msn){
		this.msn = msn;
	}
	/**
	 *方法: 取得String
	 *@return: String  国籍
	 */
	@Column(name ="NATIONALLITY",nullable=true,length=100)
	public String getNationallity(){
		return this.nationallity;
	}

	/**
	 *方法: 设置String
	 *@param: String  国籍
	 */
	public void setNationallity(String nationallity){
		this.nationallity = nationallity;
	}
	/**
	 *方法: 取得String
	 *@return: String  证件类型
	 */
	@Column(name ="CERTIFICATE_TYPE",nullable=true,length=64)
	public String getCertificateType(){
		return this.certificateType;
	}

	/**
	 *方法: 设置String
	 *@param: String  证件类型
	 */
	public void setCertificateType(String certificateType){
		this.certificateType = certificateType;
	}
	/**
	 *方法: 取得String
	 *@return: String  证件号码
	 */
	@Column(name ="CERTIFICATE_NUM",nullable=true,length=64)
	public String getCertificateNum(){
		return this.certificateNum;
	}

	/**
	 *方法: 设置String
	 *@param: String  证件号码
	 */
	public void setCertificateNum(String certificateNum){
		this.certificateNum = certificateNum;
	}
	/**
	 *方法: 取得String
	 *@return: String  单位名称
	 */
	@Column(name ="UNIT_NAME",nullable=true,length=100)
	public String getUnitName(){
		return this.unitName;
	}

	/**
	 *方法: 设置String
	 *@param: String  单位名称
	 */
	public void setUnitName(String unitName){
		this.unitName = unitName;
	}
	/**
	 *方法: 取得String
	 *@return: String  启用状态
	 */
	@Column(name ="AVAILABLE",nullable=true,length=10)
	public String getAvailable(){
		return this.available;
	}

	/**
	 *方法: 设置String
	 *@param: String  启用状态
	 */
	public void setAvailable(String available){
		this.available = available;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否激活
	 */
	@Column(name ="IS_ACTIVED",nullable=true,length=10)
	public String getIsActived(){
		return this.isActived;
	}

	/**
	 *方法: 设置String
	 *@param: String  是否激活
	 */
	public void setIsActived(String isActived){
		this.isActived = isActived;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  激活日期
	 */
	@Column(name ="ACTIVED_DATE",nullable=true)
	public java.util.Date getActivedDate(){
		return this.activedDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  激活日期
	 */
	public void setActivedDate(java.util.Date activedDate){
		this.activedDate = activedDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  传真
	 */
	@Column(name ="FAX",nullable=true,length=32)
	public String getFax(){
		return this.fax;
	}

	/**
	 *方法: 设置String
	 *@param: String  传真
	 */
	public void setFax(String fax){
		this.fax = fax;
	}
	/**
	 *方法: 取得String
	 *@return: String  备注
	 */
	@Column(name ="NOTE",nullable=true,length=200)
	public String getNote(){
		return this.note;
	}

	/**
	 *方法: 设置String
	 *@param: String  备注
	 */
	public void setNote(String note){
		this.note = note;
	}
	/**
	 *方法: 取得String
	 *@return: String  删除标识
	 */
	@Column(name ="FLAG",nullable=true,length=10)
	public String getFlag(){
		return this.flag;
	}

	/**
	 *方法: 设置String
	 *@param: String  删除标识
	 */
	public void setFlag(String flag){
		this.flag = flag;
	}
}
