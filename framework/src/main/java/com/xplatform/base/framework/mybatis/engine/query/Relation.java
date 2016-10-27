package com.xplatform.base.framework.mybatis.engine.query;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;

/**
 * 
 * <STRONG>查询关系接口</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:43:23 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:43:23 PM
 *</pre>
 */
public interface Relation {
	
	/**
	 * 
	 * <pre>
	 * 设置查询关系
	 * </pre>
	 * @param criterionMap
	 */
	void criterions(Map<String,List<Criterion>> criterionMap);
	
	/**
	 * 
	 * <pre>
	 * 获得查询关系
	 * </pre>
	 * @return
	 */
	public Map<String,List<Criterion>> getCriterions();
}
