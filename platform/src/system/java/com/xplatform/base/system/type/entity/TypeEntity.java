package com.xplatform.base.system.type.entity;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;

@Entity
@Table(name = "t_sys_type")
public class TypeEntity extends BaseTreeEntity {

	private String code;
	private String name;
	private String type;//所属类型(personal-个人,work-工作,org-公司)
	private String remark;
	private TypeEntity parent;// 父节点
	private String orgId;// 机构Id
	private Integer isPublic;//是否公共区(用于区别公司栏目下的公共文件夹与个人)
	private List<TypeEntity> children = new ArrayList<TypeEntity>(); // 子节点

	List<TypeRoleEntity> typeRoleList = new ArrayList<TypeRoleEntity>();

	public TypeEntity() {
	}
	
	public TypeEntity(String id) {
		super.setId(id);
	}
	
	@Column(name = "code", length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type", length = 64)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "remark", length = 1000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "parentId")
	@ForeignKey(name = "null")
	public TypeEntity getParent() {
		return parent;
	}

	public void setParent(TypeEntity parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "parent")
	public List<TypeEntity> getChildren() {
		return children;
	}

	public void setChildren(List<TypeEntity> children) {
		this.children = children;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
	public List<TypeRoleEntity> getTypeRoleList() {
		return typeRoleList;
	}

	public void setTypeRoleList(List<TypeRoleEntity> typeRoleList) {
		this.typeRoleList = typeRoleList;
	}

	@Column(name = "orgId", length = 32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "isPublic")
	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	
}
