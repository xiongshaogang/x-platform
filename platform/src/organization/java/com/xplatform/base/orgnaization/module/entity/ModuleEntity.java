package com.xplatform.base.orgnaization.module.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;
import com.xplatform.base.orgnaization.role.entity.RoleModuleEntity;

@Entity
@Table(name = "t_org_module")
public class ModuleEntity extends BaseTreeEntity {

	private String name; // 机构名称
	private String code; //机构代码
	private String url; //模块url
	private Integer orderby; //排序号
	private String isIframe; //是否iframe
	private String isInterceptor; //是否拦截权限
	private String description; // 备注
	private String status; //日志开关
	private String phoneShow;//手机是否查看
	private ModuleEntity parent; // 父节点
	private String subsystem; //子系统(来自数据字典 subsystem)
	private String subSystemName; //子系统(来自数据字典 subsystem)
	private List<ModuleEntity> children = new ArrayList<ModuleEntity>(); //子节点
	private List<RoleModuleEntity> roleModuleList = new ArrayList<RoleModuleEntity>();
	  

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = false, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "url", nullable = true, length = 50)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "orderby", nullable = true, length = 20)
	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	@Column(name = "isIframe", columnDefinition = "char", nullable = true, length = 1)
	public String getIsIframe() {
		return isIframe;
	}

	public void setIsIframe(String isIframe) {
		this.isIframe = isIframe;
	}

	@Column(name = "isInterceptor", columnDefinition = "char", nullable = true, length = 1)
	public String getIsInterceptor() {
		return isInterceptor;
	}

	public void setIsInterceptor(String isInterceptor) {
		this.isInterceptor = isInterceptor;
	}

	@Column(name = "phoneShow", columnDefinition = "char", nullable = true, length = 1)
	public String getPhoneShow() {
		return phoneShow;
	}

	public void setPhoneShow(String phoneShow) {
		this.phoneShow = phoneShow;
	}

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "status", nullable = true, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "parentId")
	@ForeignKey(name="null")
	public ModuleEntity getParent() {
		return parent;
	}

	public void setParent(ModuleEntity parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public List<ModuleEntity> getChildren() {
		return children;
	}

	public void setChildren(List<ModuleEntity> children) {
		this.children = children;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	public List<RoleModuleEntity> getRoleModuleList() {
		return roleModuleList;
	}

	public void setRoleModuleList(List<RoleModuleEntity> roleModuleList) {
		this.roleModuleList = roleModuleList;
	}

	@Column(name = "subSystem", nullable = true, length = 50)
	public String getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}
	
	@Column(name = "subSystemName", nullable = true, length = 50)
	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}

}
