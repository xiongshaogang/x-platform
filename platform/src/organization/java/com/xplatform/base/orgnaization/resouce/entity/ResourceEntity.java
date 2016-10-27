package com.xplatform.base.orgnaization.resouce.entity;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;

@Entity
@Table(name="t_org_resource")
public class ResourceEntity extends OperationEntity {
	
	private String name;        // 机构名称
	private String code;        //机构代码
	private String url;         //模块url
	private String optType;     //操作数据字典
	private String optCode;     //操作code
	private String filterType;   //过滤的类型（common表示公共，user表示需要控制）
	private int orderby;         //排序号
	private String isInterceptor ; //是否拦截权限
	private String description;   // 备注
	private ModuleEntity module;     // 模块
	
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
	
	@Column(name="url",nullable=false,length=50)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="orderby",nullable=false,length=20)
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}
	
	
	@Column(name="optType",nullable=true,length=32)
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	@Column(name="optCode",nullable=true,length=100)
	public String getOptCode() {
		return optCode;
	}
	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
	
	@Column(name="filterType",nullable=true,length=20)
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	@Column(name="isInterceptor",columnDefinition="char",nullable=false,length=1)
	public String getIsInterceptor() {
		return isInterceptor;
	}
	public void setIsInterceptor(String isInterceptor) {
		this.isInterceptor = isInterceptor;
	}
	
	@Column(name="description",nullable=false,length=1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "moduleId")
	public ModuleEntity getModule() {
		return module;
	}
	public void setModule(ModuleEntity module) {
		this.module = module;
	}
	
}
