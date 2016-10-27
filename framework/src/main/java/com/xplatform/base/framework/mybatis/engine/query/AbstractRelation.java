/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.query;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;

/**  
 * <STRONG>类描述</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 10:41:08 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                        修改时间                        修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 10:41:08 AM
 *</pre>  
 */
public class AbstractRelation implements Relation {

	protected  Map<String, List<Criterion>> criterionMap;
	public void criterions(Map<String, List<Criterion>> criterionMap) {
		if(criterionMap == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		
		this.criterionMap = criterionMap;
	}

	public Map<String, List<Criterion>> getCriterions() {
		return criterionMap;
	}
}
