/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.vo;

/**
 * 
 * <STRONG>类描述</STRONG> : 数据传输值对象 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-31 上午10:12:30<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-31 上午10:12:30
 *</pre>
 */
public class DataTransferVo {
	/**
	 * 数据来源表
	 */
	private String srcTableName;
	/**
	 * 数据接收表
	 */
	private String destTalbeName;
	/**
	 * sql查询的字段拼接字符串
	 */
	private String selectColumnsStr;
	/**
	 * sql插入字段拼接字符串
	 */
	private String insertColumnsStr;
	/**
	 * 数据记录原始id
	 */
	private String oldId;
	/**
	 * 数据记录新id
	 */
	private String newId;
	public DataTransferVo() {
		super();
	}

	public DataTransferVo(String srcTableName, String destTalbeName,
			String selectColumnsStr, String insertColumnsStr, String oldId,
			String newId) {
		super();
		this.srcTableName = srcTableName;
		this.destTalbeName = destTalbeName;
		this.selectColumnsStr = selectColumnsStr;
		this.insertColumnsStr = insertColumnsStr;
		this.oldId = oldId;
		this.newId = newId;
	}

	public String getSrcTableName() {
		return srcTableName;
	}
	public void setSrcTableName(String srcTableName) {
		this.srcTableName = srcTableName;
	}
	public String getDestTalbeName() {
		return destTalbeName;
	}
	public void setDestTalbeName(String destTalbeName) {
		this.destTalbeName = destTalbeName;
	}

	public String getSelectColumnsStr() {
		return selectColumnsStr;
	}

	public void setSelectColumnsStr(String selectColumnsStr) {
		this.selectColumnsStr = selectColumnsStr;
	}

	public String getInsertColumnsStr() {
		return insertColumnsStr;
	}

	public void setInsertColumnsStr(String insertColumnsStr) {
		this.insertColumnsStr = insertColumnsStr;
	}

	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public String getNewId() {
		return newId;
	}
	public void setNewId(String newId) {
		this.newId = newId;
	}
}
