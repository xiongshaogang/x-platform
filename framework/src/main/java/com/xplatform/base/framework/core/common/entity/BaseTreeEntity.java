package com.xplatform.base.framework.core.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseTreeEntity extends OperationEntity {
	private Integer level;//层次
	private String treeIndex;//层次索引
	private String isLeaf;//是否叶子节点
	private String iconCls;//树节点图形
	private String checked;//是否选中
	private String available;//是否可操作
	@Column(name = "level", nullable = true, length = 4)
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Column(name = "treeIndex", nullable = true, length = 320)
	public String getTreeIndex() {
		return treeIndex;
	}
	public void setTreeIndex(String treeIndex) {
		this.treeIndex = treeIndex;
	}
	
	@Column(name = "isLeaf",columnDefinition="char", nullable = true, length = 1)
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	@Column(name = "iconCls", nullable = true, length = 64)
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column(name = "checked",columnDefinition="char", nullable = true, length = 1)
	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	@Column(name = "available", nullable = true, length = 5)
	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}
	
	
}
