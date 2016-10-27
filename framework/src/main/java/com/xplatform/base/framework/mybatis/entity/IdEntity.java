package com.xplatform.base.framework.mybatis.entity;

import java.io.Serializable;

/**
 * 统一定义id的entity基类.
 * 
 * 主键维护基类
 * 
 * @author xiehs
 */
public class IdEntity implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * MyBatis实体主键
	 */
	protected String id;
	
	int revision;

	String value;
	
	/**
	 * 获取实体主键值
	 * @return 主键值
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置实体主键值
	 * @param id 主键值
	 */
	public void setId(String id) {
		this.id = id;
	}

	public int getRevision() {
		return revision;
	}

	public String getValue() {
		return value;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getRevisionNext() {
		return revision + 1;
	}
}
