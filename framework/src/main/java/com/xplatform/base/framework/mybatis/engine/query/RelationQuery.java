/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.query;

import java.util.List;

import com.xplatform.base.framework.mybatis.engine.Query;

/**  
 * <STRONG>条件查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 3, 2012 9:21:39 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                        修改时间                        修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 3, 2012 9:21:39 AM
 *</pre>  
 */
public interface RelationQuery<T extends Query<T, U> , U> extends FieldQuery<T, U> {
	
	RelationQuery<T,U> relations(List<Relation> relationList);
	
}
