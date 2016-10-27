package com.xplatform.base.orgnaization.user.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_org_user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "userOrgEntity" })
public class UserEntity extends OperationEntity implements Serializable {
	private static final long serialVersionUID = 3007995206419032595L;
	private String name; // 员工姓名
	private String code; // 员工代码
	private String sex; // 性别
	private Date birthday; // 出身日期
	private String cardType; // 证件类型
	private String cardNo; // 证件号码
	private Date enterDate; // 入职日期
	private Date regularDate; // 转正日期
	private Date leaveDate; // 离职日期
	private String jobState; // 工作状态
	private String qq; // QQ
	private String telephone; // 家庭电话
	private String phone; // 个人手机号码
	private String address; // 家庭地址
	private String email; // 私人邮箱
	private String post; // 家庭邮编
	private String politicsStatus; // 政治面貌
	private String maritalStatus; // 婚姻状态
	private String searchKey; //昵称关键字(用昵称,手机,名称全拼,名称简拼)
	private String sortKey;  //昵称拼音首字母
	// private String portrait80;
	// //个人头像80X80缩略图(存储的是系统请求,带上附件id,例如:attachController.do?getPlainImage&aId=feefwefwef123)
	private String portrait; // 个人头像230X230原图(存储的是系统请求,带上附件id,例如:attachController.do?getPlainImage&aId=feefwefwef123)
	private List<UserOrgEntity> userOrgEntity;// 员工机构岗位

	private String orgId; // 当前所在机构ID
	private String orgName; //当前所在机构名称
	
	private String orgIds;
	private String orgNames;      

	private String currentOrgId;
	private String currentOrgName;
	private String currentOrgType;
	private String currentUserOrgId;
	/* 登陆信息 */
	private String userName;// 名称
	private String password;// 密码
	private String verifyCode;// app端注册手机验证码
	private Integer loginErrorTimes;// 登陆错误次数
	private String flag;// 0已删除 1正在使用 2未激活 3锁定
	private Date verifySendTime;// 手机验证码发送时间
	private Date registerTime;// 成功注册时间
	
	private String rootTypeId;//个人根文件夹Id

	@Column(name = "name", nullable = true, length = 50)
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

	@Column(name = "sex", nullable = true, length = 50)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "birthday", nullable = true)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "cardType", nullable = true, length = 20)
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@Column(name = "cardNo", nullable = true, length = 50)
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "enterDate", nullable = true)
	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	@Column(name = "retularDate", nullable = true)
	public Date getRegularDate() {
		return regularDate;
	}

	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}

	@Column(name = "leaveDate", nullable = true)
	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	@Column(name = "jobSate", nullable = true, length = 20)
	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	@Column(name = "qq", nullable = true, length = 50)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "telephone", nullable = true, length = 20)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "phone", nullable = true, length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "address", nullable = true, length = 1000)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "email", nullable = true, length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "post", nullable = true, length = 50)
	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	@Column(name = "politicsStatus", nullable = true, length = 20)
	public String getPoliticsStatus() {
		return politicsStatus;
	}

	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}

	@Column(name = "maritalStatus", nullable = true, length = 20)
	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@Column(name = "portrait", length = 32)
	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "user", fetch = FetchType.LAZY)
	public List<UserOrgEntity> getUserOrgEntity() {
		return userOrgEntity;
	}

	public void setUserOrgEntity(List<UserOrgEntity> userOrgEntity) {
		this.userOrgEntity = userOrgEntity;
	}

	@Column(name = "orgId", length = 320)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "orgName", length = 1000)
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "userName", length = 60)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "password", length = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "loginErrorTimes", length = 4)
	public Integer getLoginErrorTimes() {
		return loginErrorTimes;
	}

	public void setLoginErrorTimes(Integer loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	@Column(name = "flag", columnDefinition = "char", length = 1)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "verifyCode", length = 10)
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@Column(name = "verifySendTime")
	public Date getVerifySendTime() {
		return verifySendTime;
	}

	public void setVerifySendTime(Date verifySendTime) {
		this.verifySendTime = verifySendTime;
	}

	@Column(name = "registerTime")
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Transient
	public String getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(String currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	@Transient
	public String getCurrentOrgName() {
		return currentOrgName;
	}

	public void setCurrentOrgName(String currentOrgName) {
		this.currentOrgName = currentOrgName;
	}

	@Transient
	public String getCurrentOrgType() {
		return currentOrgType;
	}

	public void setCurrentOrgType(String currentOrgType) {
		this.currentOrgType = currentOrgType;
	}

	@Transient
	public String getCurrentUserOrgId() {
		return currentUserOrgId;
	}

	public void setCurrentUserOrgId(String currentUserOrgId) {
		this.currentUserOrgId = currentUserOrgId;
	}

	@Column(name = "orgIds", length = 320)
	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	@Column(name = "orgNames", length = 1000)
	public String getOrgNames() {
		return orgNames;
	}

	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}

	@Column(name = "searchKey", length = 500)
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	@Column(name = "sortKey", length = 6)
	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	@Column(name = "rootTypeId", length = 32)
	public String getRootTypeId() {
		return rootTypeId;
	}

	public void setRootTypeId(String rootTypeId) {
		this.rootTypeId = rootTypeId;
	}


}
