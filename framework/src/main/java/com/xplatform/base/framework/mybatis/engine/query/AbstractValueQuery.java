/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.query;

import java.util.Map;

/**  
 * <STRONG>抽象值查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 13, 2012 5:07:45 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                        修改时间                        修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 13, 2012 5:07:45 PM
 *</pre>  
 */
public class AbstractValueQuery<T extends ValueQuery<T, U>, U> extends AbstractRelationQuery<T, U> implements ValueQuery <T,U>{

	/**
	 * 查询值MAP
	 */
	protected Map<String, String> queryMap;
	
	/**
	 * 设置查询MAP
	 */
	public ValueQuery<T, U> values(Map<String, String> queryMap) {
		this.queryMap = queryMap;
		return this;
	}
	
	@Override
	protected void build() {
		
	}
}
