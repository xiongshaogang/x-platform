/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.vo;


/**  
 * <STRONG>类描述</STRONG> : CMS表单流转实体类 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-20 上午11:50:32<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                      修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-20 上午11:50:32
 *</pre>  
 */
public class resultMsg {
	/** 
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 表单类型key
     */
	private String formKey;
	/**
	 * 表单类型Id
	 */
	private String formId;
	/**
	 * 表单类型name
	 */
	private String formName;
	/**
     * 表单流转类型
     */
	private String redirectType;
	/**
     * 表单流转url
     */
	private String url;
	/**
     * 表单流转名称
     */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	public resultMsg() {
		super();
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getRedirectType() {
		return redirectType;
	}
	public void setRedirectType(String redirectType) {
		this.redirectType = redirectType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
}
