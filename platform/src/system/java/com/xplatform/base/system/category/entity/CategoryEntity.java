package com.xplatform.base.system.category.entity;

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

import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;
import com.xplatform.base.system.type.entity.TypeRoleEntity;

@Entity
@Table(name = "t_sys_category")
public class CategoryEntity extends BaseTreeEntity {

	private String code;
	private String name;
	private String sysType;
	private String remark;
	private CategoryEntity parent;// 父节点
	private List<CategoryEntity> children = new ArrayList<CategoryEntity>(); //子节点

	List<TypeRoleEntity> typeRoleList = new ArrayList<TypeRoleEntity>();

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
	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
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
	public CategoryEntity getParent() {
		return parent;
	}

	public void setParent(CategoryEntity parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "parent")
	public List<CategoryEntity> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryEntity> children) {
		this.children = children;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
	public List<TypeRoleEntity> getTypeRoleList() {
		return typeRoleList;
	}

	public void setTypeRoleList(List<TypeRoleEntity> typeRoleList) {
		this.typeRoleList = typeRoleList;
	}

}
