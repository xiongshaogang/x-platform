/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.vo;

/**  
 * <STRONG>类描述</STRONG> : 主实体信息值对象 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-10-25 下午01:41:20<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                      修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-10-25 下午01:41:20
 *</pre>  
 */
public class VoManinEntity {
	/**
     * id
     */
    private String id;
    /**
     * key
     */
    private String key;
    /**
     * version
     */
    private String version;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
