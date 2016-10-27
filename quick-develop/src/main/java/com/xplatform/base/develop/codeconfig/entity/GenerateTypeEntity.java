package com.xplatform.base.develop.codeconfig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * @Title: Entity
 * @Description: 页面模型种类表
 * @author onlineGenerator
 * @date 2014-05-12 20:09:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_dev_generate_type")
@SuppressWarnings("serial")
public class GenerateTypeEntity extends OperationEntity implements
		java.io.Serializable {
	/** 模型名称 */
	private java.lang.String name;
	/** 父节点ID */
	private java.lang.String parentid;
	/** 排序字段 */
	private java.lang.Integer sort;
	/** 树路径 */
	private String treepath;
	/** 树层级 */
	private java.lang.Integer level;
	/** 是否叶子节点 */
	private java.lang.Integer isleaf;

	public GenerateTypeEntity() {
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 模型名称
	 */
	@Column(name = "NAME", nullable = false, length = 100)
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 模型名称
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 父节点ID
	 */
	@Column(name = "PARENTID", nullable = true, length = 100)
	public java.lang.String getParentid() {
		return this.parentid;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 父节点ID
	 */
	public void setParentid(java.lang.String parentid) {
		this.parentid = parentid;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 排序字段
	 */
	@Column(name = "SORT", nullable = true)
	public java.lang.Integer getSort() {
		return this.sort;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer 排序字段
	 */
	public void setSort(java.lang.Integer sort) {
		this.sort = sort;
	}

	/**
	 * 方法: 取得javax.xml.soap.Text
	 *
	 * @return: javax.xml.soap.Text 树路径
	 */
	@Column(name = "TREEPATH", nullable = true)
	public String getTreepath() {
		return this.treepath;
	}

	/**
	 * 方法: 设置javax.xml.soap.Text
	 *
	 * @param: javax.xml.soap.Text 树路径
	 */
	public void setTreepath(String treepath) {
		this.treepath = treepath;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 树层级
	 */
	@Column(name = "LEVEL", nullable = true)
	public java.lang.Integer getLevel() {
		return this.level;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer 树层级
	 */
	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 是否叶子节点
	 */
	@Column(name = "ISLEAF", nullable = true)
	public java.lang.Integer getIsleaf() {
		return this.isleaf;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer 是否叶子节点
	 */
	public void setIsleaf(java.lang.Integer isleaf) {
		this.isleaf = isleaf;
	}

}
