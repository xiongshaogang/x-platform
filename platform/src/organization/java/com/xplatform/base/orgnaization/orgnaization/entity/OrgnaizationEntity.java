package com.xplatform.base.orgnaization.orgnaization.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;

/**
 * 
 * description :  组织机构表(机构，部门，岗位)
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月9日 上午11:45:25
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年12月9日 上午11:45:25
 *
 */
@Entity
@Table(name="t_org_orgnaization")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "parent","children","userOrgEntity","orgRole" })
public class OrgnaizationEntity extends BaseTreeEntity implements Serializable{
	
	private static final long serialVersionUID = 412312785691099718L;
	private String name;        // 机构名称
	private String code;        //机构代码
	private String shortName;    // 机构简称
	private String enName ;      //  英文名
	private String type;         //  机构类型（机构org,部门dept,岗位job）
	private String telephone;     // 电话
	private String address;       //  地址
	private String email;         // 邮箱
	private String fax;          //  传真
	private String post;          // 邮编
	private String description;   // 备注
	private OrgnaizationEntity parent;     // 父节点
	private List<OrgnaizationEntity> children =new ArrayList<OrgnaizationEntity>(); //子节点
	private List<OrgRoleEntity> orgRole;   //岗位角色
	private List<UserOrgEntity> userOrgEntity;//员工机构岗位
	private String managerUserId;//负责人Id
	private String managerUserName;//负责人姓名
	private String leaderUserId;//分管领导Id
	private String leaderUserName;//分管领导姓名
	private Boolean isManage;//分管领导姓名
	private String rootTypeId;//根文件夹目录Id
	private String logo;//根文件夹目录Id
	
	public static final String DEFAULT_ID = "-2";// 组织管理员
	public static final String DEFAULT_NAME = "个人事务";// 普通会员
	
	@Column(name="name",nullable=false,length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="code",nullable=false,length=50)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="shortName",nullable=true,length=50)
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@Column(name="enName",nullable=true,length=50)
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	
	@Column(name="type",nullable=false,length=50)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="telephone",nullable=true,length=20)
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Column(name="address",nullable=true,length=1000)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="email",nullable=true,length=50)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="fax",nullable=true,length=50)
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Column(name="post",nullable=true,length=20)
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	
	
	@Column(name="description",nullable=true,length=1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "parentId")
	@ForeignKey(name = "null")
	public OrgnaizationEntity getParent() {
		return parent;
	}
	public void setParent(OrgnaizationEntity parent) {
		this.parent = parent;
	}
	
	@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "org", fetch = FetchType.LAZY)
	public List<UserOrgEntity> getUserOrgEntity() {
		return userOrgEntity;
	}
	public void setUserOrgEntity(List<UserOrgEntity> userOrgEntity) {
		this.userOrgEntity = userOrgEntity;
	}
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "parent")
	public List<OrgnaizationEntity> getChildren() {
		return children;
	}
	public void setChildren(List<OrgnaizationEntity> children) {
		this.children = children;
	}
	
	@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "org", fetch = FetchType.LAZY)
	public List<OrgRoleEntity> getOrgRole() {
		return orgRole;
	}
	public void setOrgRole(List<OrgRoleEntity> orgRole) {
		this.orgRole = orgRole;
	}

	@Column(name = "managerUserId", length = 3200)
	public String getManagerUserId() {
		return managerUserId;
	}

	public void setManagerUserId(String managerUserId) {
		this.managerUserId = managerUserId;
	}

	@Column(name = "managerUserName", length = 3200)
	public String getManagerUserName() {
		return managerUserName;
	}

	public void setManagerUserName(String managerUserName) {
		this.managerUserName = managerUserName;
	}

	@Column(name = "leaderUserId", length = 32)
	public String getLeaderUserId() {
		return leaderUserId;
	}

	public void setLeaderUserId(String leaderUserId) {
		this.leaderUserId = leaderUserId;
	}

	@Column(name = "leaderUserName", length = 60)
	public String getLeaderUserName() {
		return leaderUserName;
	}

	public void setLeaderUserName(String leaderUserName) {
		this.leaderUserName = leaderUserName;
	}

	@Column(name = "logo", length = 32)
	public String getLogo() {
		return logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@Column(name = "rootTypeId", length = 32)
	public String getRootTypeId() {
		return rootTypeId;
	}
	public void setRootTypeId(String rootTypeId) {
		this.rootTypeId = rootTypeId;
	}
	@Transient
	public Boolean getIsManage() {
		return isManage;
	}
	public void setIsManage(Boolean isManage) {
		this.isManage = isManage;
	}
}
